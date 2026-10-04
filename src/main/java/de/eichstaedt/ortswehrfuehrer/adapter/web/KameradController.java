package de.eichstaedt.ortswehrfuehrer.adapter.web;

import de.eichstaedt.ortswehrfuehrer.application.WehrApplicationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
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
    try {
      wehrService.kameradHinzufuegen(
          vorname,
          nachname,
          geburtsdatumStr,
          strasse,
          hausnummer,
          postleitzahl,
          ort,
          telefonnummer,
          emailAdresse
      );
      return Response.seeOther(URI.create("/?erfolg=kamerad_hinzugefuegt#kameraden")).build();
    } catch (IllegalArgumentException e) {
      log.warn("Kamerad konnte nicht aufgenommen werden: {}", e.getMessage());
      return Response.seeOther(URI.create("/?fehler=name_fehlt#kameraden")).build();
    }
  }

  @POST
  @Path("/entfernen")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public Response kameradEntfernen(@FormParam("kameradId") String kameradId) {
    try {
      wehrService.kameradEntfernen(kameradId);
      return Response.seeOther(URI.create("/?erfolg=kamerad_entfernt#kameraden")).build();
    } catch (IllegalArgumentException | IllegalStateException e) {
      log.warn("Kamerad konnte nicht entfernt werden: {}", e.getMessage());
      return Response.seeOther(URI.create("/?fehler=kamerad_entfernen_fehlgeschlagen#kameraden")).build();
    }
  }
}
