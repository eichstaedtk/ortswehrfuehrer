package de.eichstaedt.ortswehrfuehrer.adapter.web;

import de.eichstaedt.ortswehrfuehrer.application.WehrApplicationService;
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
import java.util.Set;

/**
 * Controller for rendering the application's start page (dashboard) via Quarkus Qute (ADR-05).
 */
@Path("/")
public class IndexWebResource {

  @CheckedTemplate(basePath = "")
  public static class Templates {

    public static native TemplateInstance index(
        int anzahlWehren,
        int anzahlKameraden,
        int anzahlFahrzeuge,
        int anzahlGebaeude,
        Wehr beispielWehr,
        Set<Kamerad> kameraden,
        String erfolg,
        String fehler
    );
  }

  private final WehrApplicationService wehrService;

  @Inject
  public IndexWebResource(WehrApplicationService wehrService) {
    this.wehrService = wehrService;
  }

  public IndexWebResource() {
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
    Wehr beispielWehr = wehrService.getAktiveWehr();
    Set<Kamerad> alleKameraden = wehrService.getAlleKameraden();

    return Templates.index(
        1,
        alleKameraden.size(),
        beispielWehr != null ? beispielWehr.getFahrzeugIds().size() : 0,
        beispielWehr != null ? beispielWehr.getGebaeude().size() : 0,
        beispielWehr,
        alleKameraden,
        erfolg,
        fehler
    );
  }
}
