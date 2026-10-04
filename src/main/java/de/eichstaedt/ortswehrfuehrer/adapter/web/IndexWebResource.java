package de.eichstaedt.ortswehrfuehrer.adapter.web;

import de.eichstaedt.ortswehrfuehrer.domain.Adresse;
import de.eichstaedt.ortswehrfuehrer.domain.Gebaeude;
import de.eichstaedt.ortswehrfuehrer.domain.Kamerad;
import de.eichstaedt.ortswehrfuehrer.domain.Wehr;
import de.eichstaedt.ortswehrfuehrer.domain.WehrFactory;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;
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
        Wehr beispielWehr
    );
  }

  private final WehrFactory wehrFactory;

  public IndexWebResource() {
    this.wehrFactory = new WehrFactory();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance index() {
    Wehr beispielWehr = wehrFactory.erzeugeWehr(
        "Freiwillige Feuerwehr Musterstadt",
        LocalDate.of(1924, 5, 1),
        List.of(
            new Gebaeude("Gerätehaus Mitte",
                new Adresse("Hauptstraße", "1", "12345", "Musterstadt"))
        ),
        Set.of("fz-tsfw-01", "fz-hlf10-02"),
        List.of(
            Kamerad.builder()
                .vorname("Max")
                .nachname("Mustermann")
                .geburtsdatum(LocalDate.of(1995, 3, 15))
                .emailAdresse("max.mustermann@feuerwehr.de")
                .telefonnummer("0170 1234567")
                .build(),
            Kamerad.builder()
                .vorname("Leon")
                .nachname("Schmidt")
                .geburtsdatum(LocalDate.of(2012, 8, 20))
                .build(),
            Kamerad.builder()
                .vorname("Hans")
                .nachname("Weber")
                .geburtsdatum(LocalDate.of(1950, 11, 2))
                .build()
        )
    );

    return Templates.index(
        1,
        beispielWehr.getKameraden().size(),
        beispielWehr.getFahrzeugIds().size(),
        beispielWehr.getGebaeude().size(),
        beispielWehr
    );
  }
}
