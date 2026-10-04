package de.eichstaedt.ortswehrfuehrer.adapter.web;

import de.eichstaedt.ortswehrfuehrer.application.AnmeldungApplicationService;
import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeErgebnis;
import de.eichstaedt.ortswehrfuehrer.domain.Benutzer;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web-Controller für Authentifizierungsseiten, Login- und Logout-Aktionen (ADR-05).
 */
@Path("/")
public class AnmeldungController {

    private static final Logger log = LoggerFactory.getLogger(AnmeldungController.class);
    public static final String SITZUNG_COOKIE_NAME = "SITZUNG_ID";

    @CheckedTemplate(basePath = "")
    public static class Templates {
        public static native TemplateInstance anmeldung(String erfolg, String fehler, Benutzer benutzer);
    }

    @Inject
    AnmeldungApplicationService anmeldungService;

    @GET
    @Path("/anmeldung")
    @Produces(MediaType.TEXT_HTML)
    public Response anmeldeSeite(
            @QueryParam("erfolg") String erfolg,
            @QueryParam("fehler") String fehler,
            @CookieParam(SITZUNG_COOKIE_NAME) String sitzungsId
    ) {
        if (sitzungsId != null && anmeldungService.istAngemeldet(sitzungsId)) {
            log.debug("Benutzer bereits angemeldet, leite weiter zum Dashboard.");
            return Response.seeOther(URI.create("/")).build();
        }

        TemplateInstance instance = Templates.anmeldung(erfolg, fehler, null);
        return Response.ok(instance).build();
    }

    @POST
    @Path("/anmeldung")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response anmelden(
            @FormParam("benutzername") String benutzername,
            @FormParam("passwort") String passwort
    ) {
        if (benutzername == null || benutzername.isBlank() || passwort == null || passwort.isBlank()) {
            log.warn("Anmeldeversuch mit unvollständigen Daten abgelehnt.");
            return Response.seeOther(URI.create("/anmeldung?fehler=fehlende_eingabe")).build();
        }

        AnmeldeErgebnis ergebnis = anmeldungService.anmelden(benutzername, passwort);

        if (ergebnis.erfolgreich()) {
            String sitzungsId = anmeldungService.erstelleSitzung(ergebnis.benutzer());
            NewCookie cookie = new NewCookie.Builder(SITZUNG_COOKIE_NAME)
                    .value(sitzungsId)
                    .path("/")
                    .httpOnly(true)
                    .build();
            log.info("Benutzer '{}' erfolgreich angemeldet. Weiterleitung zu Startseite.", ergebnis.benutzer().benutzername());
            return Response.seeOther(URI.create("/")).cookie(cookie).build();
        }

        if (!ergebnis.serviceVerfuegbar()) {
            log.warn("Anmeldung fehlgeschlagen: Authentifizierungsdienst nicht verfügbar.");
            return Response.seeOther(URI.create("/anmeldung?fehler=service_nicht_verfuegbar")).build();
        }

        log.warn("Anmeldung fehlgeschlagen für Benutzername: {}", benutzername);
        return Response.seeOther(URI.create("/anmeldung?fehler=ungueltige_anmeldedaten")).build();
    }

    @GET
    @Path("/anmeldung/abbrechen")
    public Response abbrechenAnmeldung() {
        log.debug("Anmeldung abgebrochen, Weiterleitung zur sauberen Anmeldeseite.");
        return Response.seeOther(URI.create("/anmeldung")).build();
    }

    @GET
    @Path("/abbrechen")
    public Response abbrechen() {
        return abbrechenAnmeldung();
    }

    @POST
    @Path("/abmelden")
    public Response abmelden(@CookieParam(SITZUNG_COOKIE_NAME) String sitzungsId) {
        if (sitzungsId != null) {
            anmeldungService.abmelden(sitzungsId);
        }
        NewCookie loeschCookie = new NewCookie.Builder(SITZUNG_COOKIE_NAME)
                .value("")
                .path("/")
                .maxAge(0)
                .build();
        log.info("Benutzer abgemeldet. Weiterleitung zur Anmeldeseite.");
        return Response.seeOther(URI.create("/anmeldung?erfolg=abgemeldet")).cookie(loeschCookie).build();
    }

    @POST
    @Path("/anmeldung/abmelden")
    public Response abmeldenSubPath(@CookieParam(SITZUNG_COOKIE_NAME) String sitzungsId) {
        return abmelden(sitzungsId);
    }

    @GET
    @Path("/abmelden")
    public Response abmeldenGet(@CookieParam(SITZUNG_COOKIE_NAME) String sitzungsId) {
        return abmelden(sitzungsId);
    }
}
