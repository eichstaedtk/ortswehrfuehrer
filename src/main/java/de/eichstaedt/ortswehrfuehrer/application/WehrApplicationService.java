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
import java.time.format.DateTimeParseException;
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

  public static final String JUGENDABTEILUNG = "Jugendabteilung";
  public static final String EINSATZABTEILUNG = "Einsatzabteilung";
  public static final String ALTERS_UND_EHRENABTEILUNG = "Alters- und Ehrenabteilung";
  public static final String UNBEKANNT = "Unbekannt";
  public static final String NICHT_ZUGEORDNET = "Nicht zugeordnet";

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

  public DashboardUebersicht getDashboardUebersicht() {
    Wehr wehr = getAktiveWehr();
    Set<Kamerad> kameraden = getAlleKameraden();
    List<Einsatzfahrzeug> fahrzeugeListe = getFahrzeugeDerAktivenWehr();
    int anzahlWehren = wehr != null ? 1 : 0;
    int anzahlGebaeude = wehr != null && wehr.getGebaeude() != null ? wehr.getGebaeude().size() : 0;
    return new DashboardUebersicht(
        anzahlWehren,
        kameraden.size(),
        fahrzeugeListe.size(),
        anzahlGebaeude,
        wehr,
        kameraden,
        fahrzeugeListe
    );
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

  public Einsatzfahrzeug fahrzeugHinzufuegen(String kennung, Fahrzeugtyp fahrzeugtyp, String bezeichnung) {
    if (kennung == null || kennung.isBlank()) {
      throw new IllegalArgumentException("Funkkennung fehlt.");
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
    return fahrzeugHinzufuegen(fahrzeug);
  }

  public Einsatzfahrzeug fahrzeugHinzufuegen(String kennung, String fahrzeugtypStr, String bezeichnung) {
    if (kennung == null || kennung.isBlank()) {
      throw new IllegalArgumentException("Funkkennung fehlt.");
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

    return fahrzeugHinzufuegen(fahrzeug);
  }

  public void fahrzeugEntfernen(String fahrzeugId) {
    if (fahrzeugId == null || fahrzeugId.isBlank()) {
      throw new IllegalArgumentException("Fahrzeug-ID darf nicht leer sein.");
    }
    if (aktiveWehr == null) {
      throw new IllegalStateException("Keine aktive Wehr vorhanden.");
    }
    String trimmedId = fahrzeugId.trim();
    aktiveWehr.fahrzeugEntfernen(trimmedId);
    fahrzeuge.remove(trimmedId);
    log.info("Fahrzeug mit ID '{}' erfolgreich aus Wehr '{}' entfernt.", trimmedId, aktiveWehr.getName());
  }

  public void fahrzeugEntfernen(Einsatzfahrzeug fahrzeug) {
    if (fahrzeug == null) {
      throw new IllegalArgumentException("Einsatzfahrzeug darf nicht null sein.");
    }
    fahrzeugEntfernen(fahrzeug.getId());
  }

  public void fahrzeugLoeschen(String fahrzeugId) {
    fahrzeugEntfernen(fahrzeugId);
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

  public void kameradEntfernen(String kameradId) {
    if (kameradId == null || kameradId.isBlank()) {
      throw new IllegalArgumentException("Kamerad-ID darf nicht leer sein.");
    }
    if (aktiveWehr == null) {
      throw new IllegalStateException("Keine aktive Wehr vorhanden.");
    }
    String trimmedId = kameradId.trim();
    aktiveWehr.kameradEntfernen(trimmedId);
    log.info("Kamerad mit ID '{}' erfolgreich aus Wehr '{}' entfernt.", trimmedId, aktiveWehr.getName());
  }

  public void kameradEntfernen(Kamerad kamerad) {
    if (kamerad == null) {
      throw new IllegalArgumentException("Kamerad darf nicht null sein.");
    }
    kameradEntfernen(kamerad.getId());
  }

  public Kamerad kameradHinzufuegen(
      String vorname,
      String nachname,
      LocalDate geburtsdatum,
      Adresse adresse,
      String telefonnummer,
      String emailAdresse
  ) {
    if (vorname == null || vorname.isBlank() || nachname == null || nachname.isBlank()) {
      throw new IllegalArgumentException("Vor- und Nachname sind Pflichtangaben.");
    }
    Kamerad kamerad = Kamerad.builder()
        .vorname(vorname.trim())
        .nachname(nachname.trim())
        .geburtsdatum(geburtsdatum)
        .adresse(adresse)
        .telefonnummer(telefonnummer != null && !telefonnummer.isBlank() ? telefonnummer.trim() : null)
        .emailAdresse(emailAdresse != null && !emailAdresse.isBlank() ? emailAdresse.trim() : null)
        .build();
    return kameradHinzufuegen(kamerad);
  }

  public Kamerad kameradHinzufuegen(
      String vorname,
      String nachname,
      String geburtsdatumStr,
      String strasse,
      String hausnummer,
      String postleitzahl,
      String ort,
      String telefonnummer,
      String emailAdresse
  ) {
    if (vorname == null || vorname.isBlank() || nachname == null || nachname.isBlank()) {
      throw new IllegalArgumentException("Vor- oder Nachname fehlt.");
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

    return kameradHinzufuegen(kamerad);
  }

  public String ermittleAbteilungName(Kamerad kamerad) {
    if (aktiveWehr == null || kamerad == null) {
      return UNBEKANNT;
    }
    if (aktiveWehr.getJugendabteilung() != null
        && aktiveWehr.getJugendabteilung().getKameraden().contains(kamerad)) {
      return JUGENDABTEILUNG;
    }
    if (aktiveWehr.getEinsatzabteilung() != null
        && aktiveWehr.getEinsatzabteilung().getKameraden().contains(kamerad)) {
      return EINSATZABTEILUNG;
    }
    if (aktiveWehr.getAltersUndEhrenabteilung() != null
        && aktiveWehr.getAltersUndEhrenabteilung().getKameraden().contains(kamerad)) {
      return ALTERS_UND_EHRENABTEILUNG;
    }
    return NICHT_ZUGEORDNET;
  }
}
