package de.eichstaedt.ortswehrfuehrer.adapter.web;

import de.eichstaedt.ortswehrfuehrer.application.DashboardUebersicht;
import de.eichstaedt.ortswehrfuehrer.application.WehrApplicationService;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Kamerad;
import de.eichstaedt.ortswehrfuehrer.domain.Wehr;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for rendering the application's start page (dashboard) via Quarkus Qute (ADR-05).
 */
@Path("/")
public class IndexController {

  private static final Logger log = LoggerFactory.getLogger(IndexController.class);

  @CheckedTemplate(basePath = "")
  public static class Templates {

    public static native TemplateInstance index(
        int anzahlWehren,
        int anzahlKameraden,
        int anzahlFahrzeuge,
        int anzahlGebaeude,
        Wehr beispielWehr,
        Set<Kamerad> kameraden,
        List<Einsatzfahrzeug> fahrzeuge,
        String erfolg,
        String fehler
    );
  }

  private final WehrApplicationService wehrService;

  @Inject
  public IndexController(WehrApplicationService wehrService) {
    this.wehrService = wehrService;
  }

  public IndexController() {
    this(new WehrApplicationService());
  }

  public TemplateInstance index() {
    return index(null, null);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance index(
      @QueryParam("erfolg") String erfolg,
      @QueryParam("fehler") String fehler
  ) {
    DashboardUebersicht uebersicht = wehrService.getDashboardUebersicht();

    log.debug("Lade Startseite für Wehr '{}' mit {} Kameraden und {} Fahrzeugen (erfolg={}, fehler={}).",
        uebersicht.beispielWehr() != null ? uebersicht.beispielWehr().getName() : "keine",
        uebersicht.anzahlKameraden(), uebersicht.anzahlFahrzeuge(), erfolg, fehler);

    return Templates.index(
        uebersicht.anzahlWehren(),
        uebersicht.anzahlKameraden(),
        uebersicht.anzahlFahrzeuge(),
        uebersicht.anzahlGebaeude(),
        uebersicht.beispielWehr(),
        uebersicht.kameraden(),
        uebersicht.fahrzeuge(),
        erfolg,
        fehler
    );
  }
}
