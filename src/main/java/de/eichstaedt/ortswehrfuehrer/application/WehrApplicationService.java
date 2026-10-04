package de.eichstaedt.ortswehrfuehrer.application;

import de.eichstaedt.ortswehrfuehrer.domain.Adresse;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Fahrzeugtyp;
import de.eichstaedt.ortswehrfuehrer.domain.Gebaeude;
import de.eichstaedt.ortswehrfuehrer.domain.Kamerad;
import de.eichstaedt.ortswehrfuehrer.domain.Wehr;
import de.eichstaedt.ortswehrfuehrer.domain.WehrFactory;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jmolecules.ddd.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application service orchestrating use cases for the {@link Wehr} aggregate root.
 */
@Service
@ApplicationScoped
public class WehrApplicationService {

  private static final Logger log = LoggerFactory.getLogger(WehrApplicationService.class);

  private final WehrFactory wehrFactory;
  private final Map<String, Einsatzfahrzeug> fahrzeuge = new ConcurrentHashMap<>();
  private Wehr aktiveWehr;

  public WehrApplicationService() {
    this(new WehrFactory());
  }

  public WehrApplicationService(WehrFactory wehrFactory) {
    this.wehrFactory = wehrFactory;
    initialisiereStandardWehr();
  }

  public final void initialisiereStandardWehr() {
    Einsatzfahrzeug tsfw = Einsatzfahrzeug.builder()
        .id("fz-tsfw-01")
        .bezeichnung("Tragkraftspritzenfahrzeug mit Wasser")
        .kennung("Florian Musterstadt 1/48-1")
        .fahrzeugtyp(Fahrzeugtyp.TSF_W)
        .build();

    Einsatzfahrzeug hlf10 = Einsatzfahrzeug.builder()
        .id("fz-hlf10-02")
        .bezeichnung("Hilfeleistungslöschgruppenfahrzeug 10")
        .kennung("Florian Musterstadt 1/43-1")
        .fahrzeugtyp(Fahrzeugtyp.HLF10)
        .build();

    this.fahrzeuge.clear();
    this.fahrzeuge.put(tsfw.getId(), tsfw);
    this.fahrzeuge.put(hlf10.getId(), hlf10);

    this.aktiveWehr = wehrFactory.erzeugeWehr(
        "Freiwillige Feuerwehr Musterstadt",
        LocalDate.of(1924, 5, 1),
        List.of(
            new Gebaeude("Gerätehaus Mitte",
                new Adresse("Hauptstraße", "1", "12345", "Musterstadt"))
        ),
        Set.of(tsfw.getId(), hlf10.getId()),
        List.of(
            Kamerad.builder()
                .vorname("Max")
                .nachname("Mustermann")
                .geburtsdatum(LocalDate.of(1995, 3, 15))
                .emailAdresse("max.mustermann@feuerwehr.de")
                .telefonnummer("0170 1234567")
                .adresse(new Adresse("Hauptstraße", "12", "12345", "Musterstadt"))
                .build(),
            Kamerad.builder()
                .vorname("Leon")
                .nachname("Schmidt")
                .geburtsdatum(LocalDate.of(2012, 8, 20))
                .adresse(new Adresse("Rosenweg", "4", "12345", "Musterstadt"))
                .build(),
            Kamerad.builder()
                .vorname("Hans")
                .nachname("Weber")
                .geburtsdatum(LocalDate.of(1950, 11, 2))
                .adresse(new Adresse("Am Wald", "8", "12345", "Musterstadt"))
                .build()
        )
    );
    log.info("Standard-Wehr '{}' erfolgreich initialisiert.", aktiveWehr.getName());
  }

  public Wehr getAktiveWehr() {
    return aktiveWehr;
  }

  public void setAktiveWehr(Wehr aktiveWehr) {
    this.aktiveWehr = aktiveWehr;
  }

  public Set<Kamerad> getAlleKameraden() {
    return aktiveWehr != null ? aktiveWehr.getKameraden() : Set.of();
  }

  public List<Einsatzfahrzeug> getAlleFahrzeuge() {
    return new ArrayList<>(fahrzeuge.values());
  }

  public List<Einsatzfahrzeug> getFahrzeugeDerAktivenWehr() {
    if (aktiveWehr == null || aktiveWehr.getFahrzeugIds() == null) {
      return List.of();
    }
    return aktiveWehr.getFahrzeugIds().stream()
        .map(id -> fahrzeuge.getOrDefault(
            id,
            Einsatzfahrzeug.builder().id(id).kennung(id).bezeichnung(id).build()
        ))
        .toList();
  }

  public Einsatzfahrzeug fahrzeugHinzufuegen(Einsatzfahrzeug fahrzeug) {
    if (fahrzeug == null) {
      throw new IllegalArgumentException("Einsatzfahrzeug darf nicht null sein.");
    }
    if (aktiveWehr == null) {
      throw new IllegalStateException("Keine aktive Wehr vorhanden.");
    }
    fahrzeuge.put(fahrzeug.getId(), fahrzeug);
    aktiveWehr.fahrzeugHinzufuegen(fahrzeug);
    log.info("Fahrzeug {} ({}) erfolgreich zur Wehr '{}' hinzugefügt.",
        fahrzeug.getKennung(), fahrzeug.getFahrzeugtyp(), aktiveWehr.getName());
    return fahrzeug;
  }

  public Kamerad kameradHinzufuegen(Kamerad kamerad) {
    if (kamerad == null) {
      throw new IllegalArgumentException("Kamerad darf nicht null sein.");
    }
    if (aktiveWehr == null) {
      throw new IllegalStateException("Keine aktive Wehr vorhanden.");
    }
    aktiveWehr.kameradHinzufuegen(kamerad);
    log.info("Kamerad {} {} erfolgreich zur Wehr '{}' hinzugefügt (Abteilung: {}).",
        kamerad.getVorname(), kamerad.getNachname(), aktiveWehr.getName(), ermittleAbteilungName(kamerad));
    return kamerad;
  }

  public Kamerad kameradHinzufuegen(
      String vorname,
      String nachname,
      LocalDate geburtsdatum,
      Adresse adresse,
      String telefonnummer,
      String emailAdresse
  ) {
    Kamerad kamerad = Kamerad.builder()
        .vorname(vorname)
        .nachname(nachname)
        .geburtsdatum(geburtsdatum)
        .adresse(adresse)
        .telefonnummer(telefonnummer)
        .emailAdresse(emailAdresse)
        .build();
    return kameradHinzufuegen(kamerad);
  }

  public String ermittleAbteilungName(Kamerad kamerad) {
    if (aktiveWehr == null || kamerad == null) {
      return "Unbekannt";
    }
    if (aktiveWehr.getJugendabteilung() != null
        && aktiveWehr.getJugendabteilung().getKameraden().contains(kamerad)) {
      return "Jugendabteilung";
    }
    if (aktiveWehr.getEinsatzabteilung() != null
        && aktiveWehr.getEinsatzabteilung().getKameraden().contains(kamerad)) {
      return "Einsatzabteilung";
    }
    if (aktiveWehr.getAltersUndEhrenabteilung() != null
        && aktiveWehr.getAltersUndEhrenabteilung().getKameraden().contains(kamerad)) {
      return "Alters- und Ehrenabteilung";
    }
    return "Nicht zugeordnet";
  }
}
