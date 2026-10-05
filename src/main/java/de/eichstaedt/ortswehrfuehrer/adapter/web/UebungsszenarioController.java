package de.eichstaedt.ortswehrfuehrer.adapter.web;

import de.eichstaedt.ortswehrfuehrer.application.AnmeldungApplicationService;
import de.eichstaedt.ortswehrfuehrer.application.WehrApplicationService;
import de.eichstaedt.ortswehrfuehrer.application.uebung.UebungsszenarioApplicationService;
import de.eichstaedt.ortswehrfuehrer.domain.Benutzer;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Kamerad;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.FunktionsBesetzung;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.GuardrailPruefbericht;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Schwierigkeitsgrad;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.SzenarioParameter;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Tageszeit;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeEinheit;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeFunktion;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsart;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsszenario;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller für den Übungsdienst und die Erstellung von Übungsszenarien (UC-001).
 * Enthält keine Geschäftslogik, delegiert an den Application Service.
 */
@Path("/uebungen")
public class UebungsszenarioController {

  private static final Logger log = LoggerFactory.getLogger(UebungsszenarioController.class);

  private final UebungsszenarioApplicationService uebungService;
  private final WehrApplicationService wehrService;
  private final AnmeldungApplicationService anmeldungService;

  @Inject
  @Location("uebungsszenario/liste")
  Template listeTemplate;

  @Inject
  @Location("uebungsszenario/wizard_schritt1")
  Template schritt1Template;

  @Inject
  @Location("uebungsszenario/wizard_schritt2")
  Template schritt2Template;

  @Inject
  @Location("uebungsszenario/wizard_schritt3")
  Template schritt3Template;

  @Inject
  @Location("uebungsszenario/vorschau")
  Template vorschauTemplate;

  @Inject
  @Location("uebungsszenario/export_pdf")
  Template exportPdfTemplate;

  @Inject
  public UebungsszenarioController(
      UebungsszenarioApplicationService uebungService,
      WehrApplicationService wehrService,
      AnmeldungApplicationService anmeldungService
  ) {
    this.uebungService = uebungService;
    this.wehrService = wehrService;
    this.anmeldungService = anmeldungService;
  }

  public UebungsszenarioController() {
    this(null, null, null);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance liste(
      @QueryParam("erfolg") String erfolg,
      @QueryParam("fehler") String fehler,
      @CookieParam(AnmeldungController.SITZUNG_COOKIE_NAME) String sitzungsId
  ) {
    List<Uebungsszenario> szenarien = uebungService != null ? uebungService.getAlleSzenarien() : List.of();
    Benutzer benutzer = ermittleBenutzer(sitzungsId);

    return listeTemplate
        .data("szenarien", szenarien)
        .data("erfolg", decodeErfolgsmeldung(erfolg))
        .data("fehler", decodeFehlermeldung(fehler))
        .data("benutzer", benutzer);
  }

  @GET
  @Path("/neu")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance wizardSchritt1(
      @CookieParam(AnmeldungController.SITZUNG_COOKIE_NAME) String sitzungsId
  ) {
    List<Einsatzfahrzeug> fahrzeuge = wehrService != null ? wehrService.getFahrzeugeDerAktivenWehr() : List.of();
    Benutzer benutzer = ermittleBenutzer(sitzungsId);

    return schritt1Template
        .data("verfuegbareFahrzeuge", fahrzeuge)
        .data("benutzer", benutzer);
  }

  @POST
  @Path("/start")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public Response wizardStart(
      @FormParam("art") String artStr,
      @FormParam("fahrzeugIds") List<String> fahrzeugIds,
      @FormParam("taktischeEinheit") String einheitStr,
      @CookieParam(AnmeldungController.SITZUNG_COOKIE_NAME) String sitzungsId
  ) {
    try {
      Uebungsart art = artStr != null ? Uebungsart.valueOf(artStr.trim().toUpperCase()) : Uebungsart.LOESCHUEBUNG;
      TaktischeEinheit einheit = einheitStr != null && !einheitStr.isBlank()
          ? TaktischeEinheit.valueOf(einheitStr.trim().toUpperCase())
          : null;
      Benutzer benutzer = ermittleBenutzer(sitzungsId);
      String ersteller = benutzer != null ? benutzer.anzeigeName() : "Ortswehrführer";

      Uebungsszenario szenario = uebungService.neuesSzenarioStarten(art, ersteller);
      Set<String> fahrzeugeSet = fahrzeugIds != null ? new HashSet<>(fahrzeugIds) : Set.of();
      uebungService.fahrzeugeZuweisen(szenario.getId(), fahrzeugeSet, einheit);

      return Response.seeOther(URI.create("/uebungen/" + szenario.getId() + "/besetzung")).build();
    } catch (Exception e) {
      log.warn("Fehler beim Starten des Übungsszenarios: {}", e.getMessage());
      return Response.seeOther(URI.create("/uebungen?fehler=start_fehlgeschlagen")).build();
    }
  }

  @GET
  @Path("/{id}/besetzung")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance wizardSchritt2(
      @PathParam("id") String id,
      @CookieParam(AnmeldungController.SITZUNG_COOKIE_NAME) String sitzungsId
  ) {
    Uebungsszenario szenario = uebungService.getSzenario(id);
    Set<Kamerad> kameraden = wehrService != null ? wehrService.getAlleKameraden() : Set.of();
    GuardrailPruefbericht bericht = uebungService.pruefeGuardrails(id);
    Benutzer benutzer = ermittleBenutzer(sitzungsId);

    return schritt2Template
        .data("szenario", szenario)
        .data("verfuegbareKameraden", kameraden)
        .data("bericht", bericht)
        .data("benutzer", benutzer);
  }

  @POST
  @Path("/{id}/besetzung")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public Response wizardBesetzungSpeichern(
      @PathParam("id") String id,
      @FormParam("funktionen") List<String> funktionen,
      @FormParam("kameradIds") List<String> kameradIds
  ) {
    try {
      List<FunktionsBesetzung> besetzungen = new ArrayList<>();
      if (funktionen != null && kameradIds != null) {
        for (int i = 0; i < funktionen.size(); i++) {
          TaktischeFunktion f = TaktischeFunktion.valueOf(funktionen.get(i).trim());
          String kId = i < kameradIds.size() ? kameradIds.get(i) : null;
          besetzungen.add(new FunktionsBesetzung(f, kId, null, null));
        }
      }
      uebungService.besetzungAktualisieren(id, besetzungen);
      return Response.seeOther(URI.create("/uebungen/" + id + "/parameter")).build();
    } catch (Exception e) {
      log.warn("Fehler beim Speichern der Besetzung für Szenario {}: {}", id, e.getMessage());
      return Response.seeOther(URI.create("/uebungen/" + id + "/besetzung?fehler=besetzung_fehlgeschlagen")).build();
    }
  }

  @GET
  @Path("/{id}/parameter")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance wizardSchritt3(
      @PathParam("id") String id,
      @CookieParam(AnmeldungController.SITZUNG_COOKIE_NAME) String sitzungsId
  ) {
    Uebungsszenario szenario = uebungService.getSzenario(id);
    Benutzer benutzer = ermittleBenutzer(sitzungsId);

    return schritt3Template
        .data("szenario", szenario)
        .data("benutzer", benutzer);
  }

  @POST
  @Path("/{id}/parameter")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public Response wizardParameterUndGenerieren(
      @PathParam("id") String id,
      @FormParam("dauerMinuten") Integer dauerMinuten,
      @FormParam("schwierigkeit") String schwierigkeitStr,
      @FormParam("tageszeit") String tageszeitStr,
      @FormParam("ortObjekt") String ortObjekt,
      @FormParam("lernziele") String lernzieleStr
  ) {
    try {
      int dauer = dauerMinuten != null && dauerMinuten > 0 ? dauerMinuten : 90;
      Schwierigkeitsgrad grad = schwierigkeitStr != null
          ? Schwierigkeitsgrad.valueOf(schwierigkeitStr.trim().toUpperCase())
          : Schwierigkeitsgrad.MITTEL;
      Tageszeit tageszeit = tageszeitStr != null
          ? Tageszeit.valueOf(tageszeitStr.trim().toUpperCase())
          : Tageszeit.TAG;
      List<String> lernziele = lernzieleStr != null && !lernzieleStr.isBlank()
          ? Arrays.stream(lernzieleStr.split(",")).map(String::trim).filter(s -> !s.isBlank()).toList()
          : List.of();

      SzenarioParameter parameter = new SzenarioParameter(dauer, grad, tageszeit, ortObjekt, lernziele);
      uebungService.parameterAktualisieren(id, parameter);
      uebungService.generiereSzenario(id);

      return Response.seeOther(URI.create("/uebungen/" + id + "?erfolg=szenario_generiert")).build();
    } catch (Exception e) {
      log.warn("Fehler bei Generierung des Szenarios {}: {}", id, e.getMessage());
      return Response.seeOther(URI.create("/uebungen/" + id + "/parameter?fehler=generierung_fehlgeschlagen")).build();
    }
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance vorschau(
      @PathParam("id") String id,
      @QueryParam("erfolg") String erfolg,
      @QueryParam("fehler") String fehler,
      @CookieParam(AnmeldungController.SITZUNG_COOKIE_NAME) String sitzungsId
  ) {
    Uebungsszenario szenario = uebungService.getSzenario(id);
    Benutzer benutzer = ermittleBenutzer(sitzungsId);

    return vorschauTemplate
        .data("szenario", szenario)
        .data("erfolg", decodeErfolgsmeldung(erfolg))
        .data("fehler", decodeFehlermeldung(fehler))
        .data("benutzer", benutzer);
  }

  @POST
  @Path("/{id}/ausgangslage")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public Response anpassenAusgangslage(
      @PathParam("id") String id,
      @FormParam("ausgangslage") String ausgangslage,
      @FormParam("schadenslage") String schadenslage
  ) {
    try {
      if (ausgangslage != null) {
        uebungService.anpassenAusgangslage(id, ausgangslage);
      }
      if (schadenslage != null) {
        uebungService.anpassenSchadenslage(id, schadenslage);
      }
      return Response.seeOther(URI.create("/uebungen/" + id + "?erfolg=lage_aktualisiert")).build();
    } catch (Exception e) {
      return Response.seeOther(URI.create("/uebungen/" + id + "?fehler=lage_aendern_fehlgeschlagen")).build();
    }
  }

  @POST
  @Path("/{id}/regenerieren/{abschnitt}")
  @Produces(MediaType.TEXT_HTML)
  public Response regeneriereAbschnitt(
      @PathParam("id") String id,
      @PathParam("abschnitt") String abschnitt
  ) {
    try {
      uebungService.regeneriereAbschnitt(id, abschnitt);
      return Response.seeOther(URI.create("/uebungen/" + id + "?erfolg=abschnitt_regeneriert")).build();
    } catch (Exception e) {
      return Response.seeOther(URI.create("/uebungen/" + id + "?fehler=regenerieren_fehlgeschlagen")).build();
    }
  }

  @POST
  @Path("/{id}/freigeben")
  @Produces(MediaType.TEXT_HTML)
  public Response freigeben(@PathParam("id") String id) {
    try {
      uebungService.freigeben(id);
      return Response.seeOther(URI.create("/uebungen/" + id + "?erfolg=szenario_freigegeben")).build();
    } catch (Exception e) {
      log.warn("Freigabe für Szenario {} fehlgeschlagen: {}", id, e.getMessage());
      return Response.seeOther(URI.create("/uebungen/" + id + "?fehler=freigabe_fehlgeschlagen")).build();
    }
  }

  @POST
  @Path("/{id}/archivieren")
  @Produces(MediaType.TEXT_HTML)
  public Response archivieren(@PathParam("id") String id) {
    try {
      uebungService.archivieren(id);
      return Response.seeOther(URI.create("/uebungen/" + id + "?erfolg=szenario_archiviert")).build();
    } catch (Exception e) {
      return Response.seeOther(URI.create("/uebungen/" + id + "?fehler=archivieren_fehlgeschlagen")).build();
    }
  }

  @POST
  @Path("/{id}/loeschen")
  @Produces(MediaType.TEXT_HTML)
  public Response loeschen(@PathParam("id") String id) {
    try {
      uebungService.loescheSzenario(id);
      return Response.seeOther(URI.create("/uebungen?erfolg=szenario_geloescht")).build();
    } catch (Exception e) {
      return Response.seeOther(URI.create("/uebungen?fehler=loeschen_fehlgeschlagen")).build();
    }
  }

  @GET
  @Path("/{id}/export/pdf")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance exportPdf(
      @PathParam("id") String id,
      @CookieParam(AnmeldungController.SITZUNG_COOKIE_NAME) String sitzungsId
  ) {
    Uebungsszenario szenario = uebungService.getSzenario(id);
    Benutzer benutzer = ermittleBenutzer(sitzungsId);

    return exportPdfTemplate
        .data("szenario", szenario)
        .data("benutzer", benutzer);
  }

  private Benutzer ermittleBenutzer(String sitzungsId) {
    return (anmeldungService != null && sitzungsId != null)
        ? anmeldungService.getAngemeldetenBenutzer(sitzungsId)
        : null;
  }

  private String decodeErfolgsmeldung(String code) {
    if (code == null) return null;
    return switch (code) {
      case "szenario_generiert" -> "Übungsszenario wurde erfolgreich generiert.";
      case "szenario_freigegeben" -> "Das Übungsszenario wurde erfolgreich freigegeben.";
      case "szenario_archiviert" -> "Das Übungsszenario wurde archiviert.";
      case "szenario_geloescht" -> "Das Übungsszenario wurde gelöscht.";
      case "lage_aktualisiert" -> "Ausgangs- und Schadenslage wurden aktualisiert.";
      case "abschnitt_regeneriert" -> "Der Abschnitt wurde erfolgreich neu generiert.";
      default -> code;
    };
  }

  private String decodeFehlermeldung(String code) {
    if (code == null) return null;
    return switch (code) {
      case "start_fehlgeschlagen" -> "Das Übungsszenario konnte nicht initialisiert werden.";
      case "besetzung_fehlgeschlagen" -> "Fehler beim Aktualisieren der Besetzungsmatrix.";
      case "generierung_fehlgeschlagen" -> "Die Szenariogenerierung ist fehlgeschlagen (bitte Guardrail-Prüfung beachten).";
      case "freigabe_fehlgeschlagen" -> "Die Freigabe ist fehlgeschlagen. Bitte prüfen Sie die Guardrail-Meldungen.";
      case "loeschen_fehlgeschlagen" -> "Das Szenario konnte nicht gelöscht werden.";
      default -> code;
    };
  }
}
