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
 * Controller for managing Einsatzfahrzeuge via web form submissions.
 */
@Path("/fahrzeuge")
public class FahrzeugController {

  private static final Logger log = LoggerFactory.getLogger(FahrzeugController.class);

  private final WehrApplicationService wehrService;

  @Inject
  public FahrzeugController(WehrApplicationService wehrService) {
    this.wehrService = wehrService;
  }

  public FahrzeugController() {
    this(new WehrApplicationService());
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public Response fahrzeugHinzufuegen(
      @FormParam("kennung") String kennung,
      @FormParam("fahrzeugtyp") String fahrzeugtypStr,
      @FormParam("bezeichnung") String bezeichnung
  ) {
    try {
      wehrService.fahrzeugHinzufuegen(kennung, fahrzeugtypStr, bezeichnung);
      return Response.seeOther(URI.create("/?erfolg=fahrzeug_hinzugefuegt#fahrzeuge")).build();
    } catch (IllegalArgumentException e) {
      log.warn("Einsatzfahrzeug konnte nicht angelegt werden: {}", e.getMessage());
      return Response.seeOther(URI.create("/?fehler=fahrzeug_kennung_fehlt#fahrzeuge")).build();
    }
  }
}
