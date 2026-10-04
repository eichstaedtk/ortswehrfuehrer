package de.eichstaedt.ortswehrfuehrer.adapter.web;

import de.eichstaedt.ortswehrfuehrer.application.WehrApplicationService;
import de.eichstaedt.ortswehrfuehrer.domain.Adresse;
import de.eichstaedt.ortswehrfuehrer.domain.Kamerad;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for managing Kameraden via web form submissions.
 */
@Path("/kameraden")
public class KameradController {

  private static final Logger log = LoggerFactory.getLogger(KameradController.class);

  private final WehrApplicationService wehrService;

  @Inject
  public KameradController(WehrApplicationService wehrService) {
    this.wehrService = wehrService;
  }

  public KameradController() {
    this(new WehrApplicationService());
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public Response kameradHinzufuegen(
      @FormParam("vorname") String vorname,
      @FormParam("nachname") String nachname,
      @FormParam("geburtsdatum") String geburtsdatumStr,
      @FormParam("strasse") String strasse,
      @FormParam("hausnummer") String hausnummer,
      @FormParam("postleitzahl") String postleitzahl,
      @FormParam("ort") String ort,
      @FormParam("telefonnummer") String telefonnummer,
      @FormParam("emailAdresse") String emailAdresse
  ) {
    if (vorname == null || vorname.isBlank() || nachname == null || nachname.isBlank()) {
      log.warn("Kamerad konnte nicht aufgenommen werden: Vor- oder Nachname fehlt (vorname='{}', nachname='{}').", vorname, nachname);
      return Response.seeOther(URI.create("/?fehler=name_fehlt#kameraden")).build();
    }

    LocalDate geburtsdatum = null;
    if (geburtsdatumStr != null && !geburtsdatumStr.isBlank()) {
      try {
        geburtsdatum = LocalDate.parse(geburtsdatumStr.trim());
      } catch (DateTimeParseException e) {
        log.warn("Geburtsdatum konnte nicht geparst werden: '{}'.", geburtsdatumStr, e);
      }
    }

    Adresse adresse = null;
    if ((strasse != null && !strasse.isBlank())
        || (hausnummer != null && !hausnummer.isBlank())
        || (postleitzahl != null && !postleitzahl.isBlank())
        || (ort != null && !ort.isBlank())) {
      adresse = new Adresse(
          strasse != null ? strasse.trim() : "",
          hausnummer != null ? hausnummer.trim() : "",
          postleitzahl != null ? postleitzahl.trim() : "",
          ort != null ? ort.trim() : ""
      );
    }

    Kamerad kamerad = Kamerad.builder()
        .vorname(vorname.trim())
        .nachname(nachname.trim())
        .geburtsdatum(geburtsdatum)
        .adresse(adresse)
        .telefonnummer(telefonnummer != null && !telefonnummer.isBlank() ? telefonnummer.trim() : null)
        .emailAdresse(emailAdresse != null && !emailAdresse.isBlank() ? emailAdresse.trim() : null)
        .build();

    wehrService.kameradHinzufuegen(kamerad);

    return Response.seeOther(URI.create("/?erfolg=kamerad_hinzugefuegt#kameraden")).build();
  }
}
