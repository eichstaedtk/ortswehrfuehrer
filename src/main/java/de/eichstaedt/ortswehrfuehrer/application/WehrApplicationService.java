package de.eichstaedt.ortswehrfuehrer.application;

import de.eichstaedt.ortswehrfuehrer.domain.Adresse;
import de.eichstaedt.ortswehrfuehrer.domain.Ausbildung;
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
import java.util.LinkedHashSet;
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
        .id("tsf472")
        .bezeichnung("Tragkraftspritzenfahrzeug")
        .kennung("HVL/5/467/2")
        .fahrzeugtyp(Fahrzeugtyp.TSF)
        .build();

    Einsatzfahrzeug tlf1646 = Einsatzfahrzeug.builder()
        .id("fz-tlf1646-02")
        .bezeichnung("Tanklöschfahrzeug 14/46")
        .kennung("HVL/5/24/3")
        .fahrzeugtyp(Fahrzeugtyp.TLF4000)
        .build();

    this.fahrzeuge.clear();
    this.fahrzeuge.put(tsfw.getId(), tsfw);
    this.fahrzeuge.put(tlf1646.getId(), tlf1646);

    this.aktiveWehr = wehrFactory.erzeugeWehr(
        "Freiwillige Feuerwehr Göttlin",
        LocalDate.of(1924, 5, 1),
        List.of(
            new Gebaeude("Gerätehaus Göttliner Chaussee",
                new Adresse("Göttliner Chaussee", "10", "14712", "Rathenow"))
        ),
        Set.of(tsfw.getId(), tlf1646.getId()),
        List.of(
            Kamerad.builder()
                .vorname("Max")
                .nachname("Gens")
                .geburtsdatum(LocalDate.of(1995, 3, 15))
                .emailAdresse("max.mustermann@feuerwehr.de")
                .telefonnummer("0170 1234567")
                .adresse(new Adresse("Hauptstraße", "12", "12345", "Musterstadt"))
                .mitAusbildungen(List.of(
                    Ausbildung.TRUPPMANN_TEIL_1,
                    Ausbildung.TRUPPMANN_TEIL_2
                ))
                .build(),
            Kamerad.builder()
                .vorname("Apard")
                .nachname("Mezaros")
                .geburtsdatum(LocalDate.of(2012, 8, 20))
                .adresse(new Adresse("Rosenweg", "4", "12345", "Musterstadt"))
                .mitAusbildungen(List.of(
                    Ausbildung.TRUPPMANN_TEIL_1,
                    Ausbildung.TRUPPMANN_TEIL_2,
                    Ausbildung.SPRECHFUNKER,
                    Ausbildung.MASCHINIST,
                    Ausbildung.TRUPPFUEHRER
                ))
                .build(),
            Kamerad.builder()
                .vorname("Steffen")
                .nachname("Schröder")
                .geburtsdatum(LocalDate.of(1950, 11, 2))
                .adresse(new Adresse("Am Wald", "8", "12345", "Musterstadt"))
                .mitAusbildungen(List.of(
                    Ausbildung.TRUPPMANN_TEIL_1,
                    Ausbildung.TRUPPMANN_TEIL_2,
                    Ausbildung.SPRECHFUNKER,
                    Ausbildung.MASCHINIST,
                    Ausbildung.TRUPPFUEHRER
                ))
                .build(),
            Kamerad.builder()
                .vorname("Konrad")
                .nachname("Eichstädt")
                .geburtsdatum(LocalDate.of(1950, 11, 2))
                .adresse(new Adresse("Am Wald", "8", "12345", "Musterstadt"))
                .mitAusbildungen(List.of(
                    Ausbildung.TRUPPMANN_TEIL_1,
                    Ausbildung.TRUPPMANN_TEIL_2,
                    Ausbildung.SPRECHFUNKER,
                    Ausbildung.MASCHINIST,
                    Ausbildung.TRUPPFUEHRER,
                    Ausbildung.ATEMSCHUTZGERAETETRAEGER,
                    Ausbildung.GRUPPENFUEHRER
                ))
                .build(),
            Kamerad.builder()
                .vorname("Fabian")
                .nachname("Jörs")
                .geburtsdatum(LocalDate.of(1950, 11, 2))
                .adresse(new Adresse("Am Wald", "8", "12345", "Musterstadt"))
                .mitAusbildungen(List.of(
                    Ausbildung.TRUPPMANN_TEIL_1,
                    Ausbildung.TRUPPMANN_TEIL_2,
                    Ausbildung.SPRECHFUNKER,
                    Ausbildung.MASCHINIST,
                    Ausbildung.TRUPPFUEHRER,
                    Ausbildung.ATEMSCHUTZGERAETETRAEGER,
                    Ausbildung.GRUPPENFUEHRER
                ))
                .build(),
            Kamerad.builder()
                .vorname("Stefan Nickel")
                .nachname("Jörs")
                .geburtsdatum(LocalDate.of(1950, 11, 2))
                .adresse(new Adresse("Am Wald", "8", "12345", "Musterstadt"))
                .mitAusbildungen(List.of(
                    Ausbildung.TRUPPMANN_TEIL_1,
                    Ausbildung.TRUPPMANN_TEIL_2,
                    Ausbildung.SPRECHFUNKER,
                    Ausbildung.MASCHINIST,
                    Ausbildung.TRUPPFUEHRER,
                    Ausbildung.ATEMSCHUTZGERAETETRAEGER,
                    Ausbildung.GRUPPENFUEHRER
                ))
                .build(),
            Kamerad.builder()
                .vorname("Holger")
                .nachname("Schröder")
                .geburtsdatum(LocalDate.of(1950, 11, 2))
                .adresse(new Adresse("Am Wald", "8", "12345", "Musterstadt"))
                .mitAusbildungen(List.of(
                    Ausbildung.TRUPPMANN_TEIL_1,
                    Ausbildung.TRUPPMANN_TEIL_2,
                    Ausbildung.SPRECHFUNKER,
                    Ausbildung.MASCHINIST,
                    Ausbildung.TRUPPFUEHRER,
                    Ausbildung.ATEMSCHUTZGERAETETRAEGER,
                    Ausbildung.GRUPPENFUEHRER
                ))
                .build(),
            Kamerad.builder()
                .vorname("Lutz")
                .nachname("Hansich")
                .geburtsdatum(LocalDate.of(1950, 11, 2))
                .adresse(new Adresse("Am Wald", "8", "12345", "Musterstadt"))
                .mitAusbildungen(List.of(
                    Ausbildung.TRUPPMANN_TEIL_1,
                    Ausbildung.TRUPPMANN_TEIL_2,
                    Ausbildung.SPRECHFUNKER,
                    Ausbildung.MASCHINIST,
                    Ausbildung.TRUPPFUEHRER,
                    Ausbildung.ATEMSCHUTZGERAETETRAEGER,
                    Ausbildung.GRUPPENFUEHRER
                ))
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

  public Einsatzfahrzeug fahrzeugHinzufuegen(String kennung, Fahrzeugtyp fahrzeugtyp,
      String bezeichnung) {
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

  public Einsatzfahrzeug fahrzeugHinzufuegen(String kennung, String fahrzeugtypStr,
      String bezeichnung) {
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
    log.info("Fahrzeug mit ID '{}' erfolgreich aus Wehr '{}' entfernt.", trimmedId,
        aktiveWehr.getName());
  }

  public void fahrzeugEntfernen(Einsatzfahrzeug fahrzeug) {
    if (fahrzeug == null) {
      throw new IllegalArgumentException("Einsatzfahrzeug darf nicht null sein.");
    }
    fahrzeugEntfernen(fahrzeug.getId());
  }

  public Einsatzfahrzeug fahrzeugAendern(Einsatzfahrzeug fahrzeug) {
    if (fahrzeug == null || fahrzeug.getId() == null || fahrzeug.getId().isBlank()) {
      throw new IllegalArgumentException("Einsatzfahrzeug oder Fahrzeug-ID darf nicht leer sein.");
    }
    if (aktiveWehr == null) {
      throw new IllegalStateException("Keine aktive Wehr vorhanden.");
    }
    Einsatzfahrzeug existing = fahrzeuge.get(fahrzeug.getId());
    if (existing == null && (aktiveWehr.getFahrzeugIds() == null || !aktiveWehr.getFahrzeugIds()
        .contains(fahrzeug.getId()))) {
      throw new IllegalArgumentException(
          "Fahrzeug mit ID '" + fahrzeug.getId() + "' wurde nicht gefunden.");
    }
    fahrzeuge.put(fahrzeug.getId(), fahrzeug);
    aktiveWehr.fahrzeugHinzufuegen(fahrzeug.getId());
    log.info("Fahrzeug '{}' ({}) erfolgreich aktualisiert.", fahrzeug.getKennung(),
        fahrzeug.getId());
    return fahrzeug;
  }

  public Einsatzfahrzeug fahrzeugAendern(String fahrzeugId, String kennung, Fahrzeugtyp fahrzeugtyp,
      String bezeichnung) {
    if (fahrzeugId == null || fahrzeugId.isBlank()) {
      throw new IllegalArgumentException("Fahrzeug-ID darf nicht leer sein.");
    }
    if (kennung == null || kennung.isBlank()) {
      throw new IllegalArgumentException("Funkkennung darf nicht leer sein.");
    }
    if (aktiveWehr == null) {
      throw new IllegalStateException("Keine aktive Wehr vorhanden.");
    }
    String trimmedId = fahrzeugId.trim();
    Einsatzfahrzeug fahrzeug = fahrzeuge.get(trimmedId);
    if (fahrzeug == null) {
      if (aktiveWehr.getFahrzeugIds() != null && aktiveWehr.getFahrzeugIds().contains(trimmedId)) {
        fahrzeug = Einsatzfahrzeug.builder().id(trimmedId).kennung(trimmedId).build();
        fahrzeuge.put(trimmedId, fahrzeug);
      } else {
        throw new IllegalArgumentException(
            "Fahrzeug mit ID '" + trimmedId + "' wurde nicht gefunden.");
      }
    }
    String trimmedKennung = kennung.trim();
    String trimmedBezeichnung = (bezeichnung != null && !bezeichnung.isBlank())
        ? bezeichnung.trim()
        : (fahrzeugtyp != null ? fahrzeugtyp.name() : trimmedKennung);

    fahrzeug.fahrzeugAendern(trimmedBezeichnung, trimmedKennung, fahrzeugtyp);
    log.info("Fahrzeug '{}' ({}) erfolgreich aktualisiert.", fahrzeug.getKennung(),
        fahrzeug.getId());
    return fahrzeug;
  }

  public Einsatzfahrzeug fahrzeugAendern(String fahrzeugId, String kennung, String fahrzeugtypStr,
      String bezeichnung) {
    if (fahrzeugId == null || fahrzeugId.isBlank()) {
      throw new IllegalArgumentException("Fahrzeug-ID darf nicht leer sein.");
    }
    if (kennung == null || kennung.isBlank()) {
      throw new IllegalArgumentException("Funkkennung darf nicht leer sein.");
    }
    Fahrzeugtyp fahrzeugtyp = null;
    if (fahrzeugtypStr != null && !fahrzeugtypStr.isBlank()) {
      try {
        fahrzeugtyp = Fahrzeugtyp.valueOf(fahrzeugtypStr.trim().toUpperCase());
      } catch (IllegalArgumentException e) {
        log.warn("Fahrzeugtyp konnte nicht zugeordnet werden: '{}'.", fahrzeugtypStr, e);
      }
    }
    return fahrzeugAendern(fahrzeugId, kennung, fahrzeugtyp, bezeichnung);
  }

  public Einsatzfahrzeug getFahrzeug(String fahrzeugId) {
    if (fahrzeugId == null || fahrzeugId.isBlank()) {
      return null;
    }
    return fahrzeuge.get(fahrzeugId.trim());
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
        kamerad.getVorname(), kamerad.getNachname(), aktiveWehr.getName(),
        ermittleAbteilungName(kamerad));
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
    log.info("Kamerad mit ID '{}' erfolgreich aus Wehr '{}' entfernt.", trimmedId,
        aktiveWehr.getName());
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
    return kameradHinzufuegen(vorname, nachname, geburtsdatum, adresse, telefonnummer, emailAdresse,
        (Set<Ausbildung>) null);
  }

  public Kamerad kameradHinzufuegen(
      String vorname,
      String nachname,
      LocalDate geburtsdatum,
      Adresse adresse,
      String telefonnummer,
      String emailAdresse,
      Set<Ausbildung> ausbildungen
  ) {
    pruefeName(vorname, nachname);
    Kamerad kamerad = Kamerad.builder()
        .vorname(vorname.trim())
        .nachname(nachname.trim())
        .geburtsdatum(geburtsdatum)
        .adresse(adresse)
        .telefonnummer(trimToNull(telefonnummer))
        .emailAdresse(trimToNull(emailAdresse))
        .ausbildungen(ausbildungen)
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
    return kameradHinzufuegen(
        vorname,
        nachname,
        geburtsdatumStr,
        strasse,
        hausnummer,
        postleitzahl,
        ort,
        telefonnummer,
        emailAdresse,
        null
    );
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
      String emailAdresse,
      List<String> ausbildungen
  ) {
    pruefeName(vorname, nachname);
    return kameradHinzufuegen(
        vorname,
        nachname,
        parseGeburtsdatum(geburtsdatumStr),
        baueAdresse(strasse, hausnummer, postleitzahl, ort),
        telefonnummer,
        emailAdresse,
        parseAusbildungen(ausbildungen)
    );
  }

  public Kamerad kameradAendern(
      String kameradId,
      String vorname,
      String nachname,
      LocalDate geburtsdatum,
      Adresse adresse,
      String telefonnummer,
      String emailAdresse
  ) {
    if (kameradId == null || kameradId.isBlank()) {
      throw new IllegalArgumentException("Kamerad-ID darf nicht leer sein.");
    }
    Kamerad existing = getKamerad(kameradId);
    Set<Ausbildung> ausbildungen = existing != null ? existing.getAusbildungen() : null;
    return kameradAendern(kameradId, vorname, nachname, geburtsdatum, adresse, telefonnummer,
        emailAdresse, ausbildungen);
  }

  public Kamerad kameradAendern(
      String kameradId,
      String vorname,
      String nachname,
      LocalDate geburtsdatum,
      Adresse adresse,
      String telefonnummer,
      String emailAdresse,
      Set<Ausbildung> ausbildungen
  ) {
    if (kameradId == null || kameradId.isBlank()) {
      throw new IllegalArgumentException("Kamerad-ID darf nicht leer sein.");
    }
    pruefeName(vorname, nachname);
    if (aktiveWehr == null) {
      throw new IllegalStateException("Keine aktive Wehr vorhanden.");
    }
    String trimmedId = kameradId.trim();
    aktiveWehr.kameradAendern(
        trimmedId,
        vorname.trim(),
        nachname.trim(),
        geburtsdatum,
        adresse,
        trimToNull(telefonnummer),
        trimToNull(emailAdresse),
        ausbildungen
    );
    Kamerad kamerad = getKamerad(trimmedId);
    log.info("Kamerad {} {} ({}) erfolgreich geändert (Abteilung: {}, Ausbildungen: {}).",
        kamerad.getVorname(), kamerad.getNachname(), trimmedId, ermittleAbteilungName(kamerad),
        kamerad.getAusbildungen().size());
    return kamerad;
  }

  public Kamerad kameradAendern(
      String kameradId,
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
    return kameradAendern(
        kameradId,
        vorname,
        nachname,
        geburtsdatumStr,
        strasse,
        hausnummer,
        postleitzahl,
        ort,
        telefonnummer,
        emailAdresse,
        (List<String>) null
    );
  }

  public Kamerad kameradAendern(
      String kameradId,
      String vorname,
      String nachname,
      String geburtsdatumStr,
      String strasse,
      String hausnummer,
      String postleitzahl,
      String ort,
      String telefonnummer,
      String emailAdresse,
      List<String> ausbildungen
  ) {
    Kamerad existing = getKamerad(kameradId);
    Set<Ausbildung> ausbildungenSet = ausbildungen != null
        ? parseAusbildungen(ausbildungen)
        : (existing != null ? existing.getAusbildungen() : new LinkedHashSet<>());
    return kameradAendern(
        kameradId,
        vorname,
        nachname,
        parseGeburtsdatum(geburtsdatumStr),
        baueAdresse(strasse, hausnummer, postleitzahl, ort),
        telefonnummer,
        emailAdresse,
        ausbildungenSet
    );
  }

  public void ausbildungZuweisen(String kameradId, Ausbildung ausbildung) {
    if (kameradId == null || kameradId.isBlank()) {
      throw new IllegalArgumentException("Kamerad-ID darf nicht leer sein.");
    }
    if (ausbildung == null) {
      throw new IllegalArgumentException("Ausbildung darf nicht null sein.");
    }
    if (aktiveWehr == null) {
      throw new IllegalStateException("Keine aktive Wehr vorhanden.");
    }
    aktiveWehr.ausbildungZuweisen(kameradId.trim(), ausbildung);
    log.info("Ausbildung '{}' ({}) erfolgreich Kamerad '{}' zugewiesen.",
        ausbildung.getBezeichnung(), ausbildung.getZiffer(), kameradId);
  }

  public void ausbildungZuweisen(String kameradId, String ausbildungName) {
    Ausbildung.von(ausbildungName).ifPresent(a -> ausbildungZuweisen(kameradId, a));
  }

  public void ausbildungEntfernen(String kameradId, Ausbildung ausbildung) {
    if (kameradId == null || kameradId.isBlank()) {
      throw new IllegalArgumentException("Kamerad-ID darf nicht leer sein.");
    }
    if (ausbildung == null) {
      throw new IllegalArgumentException("Ausbildung darf nicht null sein.");
    }
    if (aktiveWehr == null) {
      throw new IllegalStateException("Keine aktive Wehr vorhanden.");
    }
    aktiveWehr.ausbildungEntfernen(kameradId.trim(), ausbildung);
    log.info("Ausbildung '{}' ({}) erfolgreich von Kamerad '{}' entfernt.",
        ausbildung.getBezeichnung(), ausbildung.getZiffer(), kameradId);
  }

  public void ausbildungEntfernen(String kameradId, String ausbildungName) {
    Ausbildung.von(ausbildungName).ifPresent(a -> ausbildungEntfernen(kameradId, a));
  }

  public void ausbildungenAktualisieren(String kameradId, Set<Ausbildung> ausbildungen) {
    Kamerad kamerad = getKamerad(kameradId);
    if (kamerad == null) {
      throw new IllegalArgumentException(
          "Kamerad mit ID '" + kameradId + "' wurde nicht gefunden.");
    }
    kamerad.setAusbildungen(ausbildungen);
  }

  public void ausbildungenAktualisieren(String kameradId, List<String> ausbildungenNamen) {
    ausbildungenAktualisieren(kameradId, parseAusbildungen(ausbildungenNamen));
  }

  public static Set<Ausbildung> parseAusbildungen(List<String> ausbildungenNamen) {
    if (ausbildungenNamen == null || ausbildungenNamen.isEmpty()) {
      return new LinkedHashSet<>();
    }
    Set<Ausbildung> result = new LinkedHashSet<>();
    for (String name : ausbildungenNamen) {
      if (name != null && !name.isBlank()) {
        Ausbildung.von(name).ifPresent(result::add);
      }
    }
    return result;
  }

  public Kamerad getKamerad(String kameradId) {
    if (aktiveWehr == null) {
      return null;
    }
    return aktiveWehr.findeKamerad(kameradId).orElse(null);
  }

  private static void pruefeName(String vorname, String nachname) {
    if (vorname == null || vorname.isBlank() || nachname == null || nachname.isBlank()) {
      throw new IllegalArgumentException("Vor- und Nachname sind Pflichtangaben.");
    }
  }

  private static LocalDate parseGeburtsdatum(String geburtsdatumStr) {
    if (geburtsdatumStr == null || geburtsdatumStr.isBlank()) {
      return null;
    }
    try {
      return LocalDate.parse(geburtsdatumStr.trim());
    } catch (DateTimeParseException e) {
      log.warn("Geburtsdatum konnte nicht geparst werden: '{}'.", geburtsdatumStr, e);
      return null;
    }
  }

  private static Adresse baueAdresse(String strasse, String hausnummer, String postleitzahl,
      String ort) {
    if (trimToNull(strasse) == null && trimToNull(hausnummer) == null
        && trimToNull(postleitzahl) == null && trimToNull(ort) == null) {
      return null;
    }
    return new Adresse(trimToEmpty(strasse), trimToEmpty(hausnummer), trimToEmpty(postleitzahl),
        trimToEmpty(ort));
  }

  private static String trimToNull(String wert) {
    return wert != null && !wert.isBlank() ? wert.trim() : null;
  }

  private static String trimToEmpty(String wert) {
    return wert != null ? wert.trim() : "";
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
