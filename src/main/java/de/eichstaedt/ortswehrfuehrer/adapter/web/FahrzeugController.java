package de.eichstaedt.ortswehrfuehrer.adapter.web;

import de.eichstaedt.ortswehrfuehrer.application.WehrApplicationService;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Fahrzeugtyp;
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
    if (kennung == null || kennung.isBlank()) {
      log.warn("Einsatzfahrzeug konnte nicht angelegt werden: Funkkennung fehlt.");
      return Response.seeOther(URI.create("/?fehler=fahrzeug_kennung_fehlt#fahrzeuge")).build();
    }

    Fahrzeugtyp fahrzeugtyp = null;
    if (fahrzeugtypStr != null && !fahrzeugtypStr.isBlank()) {
      try {
        fahrzeugtyp = Fahrzeugtyp.valueOf(fahrzeugtypStr.trim().toUpperCase());
      } catch (IllegalArgumentException e) {
        log.warn("Fahrzeugtyp konnte nicht zugeordnet werden: '{}'.", fahrzeugtypStr, e);
      }
    }

    String trimmedKennung = kennung.trim();
    String trimmedBezeichnung = (bezeichnung != null && !bezeichnung.isBlank())
        ? bezeichnung.trim()
        : (fahrzeugtyp != null ? fahrzeugtyp.name() : trimmedKennung);

    Einsatzfahrzeug fahrzeug = Einsatzfahrzeug.builder()
        .kennung(trimmedKennung)
        .fahrzeugtyp(fahrzeugtyp)
        .bezeichnung(trimmedBezeichnung)
        .build();

    wehrService.fahrzeugHinzufuegen(fahrzeug);

    return Response.seeOther(URI.create("/?erfolg=fahrzeug_hinzugefuegt#fahrzeuge")).build();
  }
}
