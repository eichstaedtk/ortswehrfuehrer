package de.eichstaedt.ortswehrfuehrer.application.uebung;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.eichstaedt.ortswehrfuehrer.domain.Ausbildung;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Bewertungspunkt;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Einsatzauftrag;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.FunktionsBesetzung;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.GuardrailPruefbericht;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Lageentwicklung;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Schwierigkeitsgrad;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Sicherheitshinweis;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.SzenarioParameter;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeEinheit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Katalog von taktischen Fachvorlagen und Generierungsregeln nach FwDV 3 und UVV,
 * geladen aus einer externen JSON-Konfigurationsdatei.
 */
@ApplicationScoped
public class SzenarioKatalog {

  private static final Logger log = LoggerFactory.getLogger(SzenarioKatalog.class);
  public static final String STANDARD_VORLAGEN_RESSOURCE = "/szenarien-katalog.json";

  public static record VorlagenPaket(
      String titel,
      String alarmstichwort,
      String ausgangslage,
      String schadenslage,
      List<Einsatzauftrag> auftraege,
      List<Lageentwicklung> lageentwicklungen,
      List<Sicherheitshinweis> sicherheitshinweise,
      List<Bewertungspunkt> bewertungspunkte
  ) {

  }

  public static record EinsatzauftragVorlage(
      String einheit,
      String auftrag,
      String mittel,
      String ziel,
      String weg,
      boolean nurGruppe
  ) {

    public Einsatzauftrag zuEinsatzauftrag() {
      return new Einsatzauftrag(einheit, auftrag, mittel, ziel, weg);
    }
  }

  public static record LageentwicklungVorlage(
      int nachMinuten,
      String ereignis,
      String erwarteteMassnahme,
      String schwierigkeitsgrad
  ) {

    public boolean passtZu(Schwierigkeitsgrad grad) {
      if (schwierigkeitsgrad == null || schwierigkeitsgrad.isBlank() || "ALLE".equalsIgnoreCase(
          schwierigkeitsgrad)) {
        return true;
      }
      if ("ANSPRUCHSVOLL".equalsIgnoreCase(schwierigkeitsgrad)) {
        return grad == Schwierigkeitsgrad.ANSPRUCHSVOLL;
      }
      if ("STANDARD".equalsIgnoreCase(schwierigkeitsgrad) || "BASIS".equalsIgnoreCase(
          schwierigkeitsgrad)) {
        return grad != Schwierigkeitsgrad.ANSPRUCHSVOLL;
      }
      return true;
    }

    public Lageentwicklung zuLageentwicklung() {
      return new Lageentwicklung(nachMinuten, ereignis, erwarteteMassnahme);
    }
  }

  public static record SzenarioVorlage(
      String id,
      String art,
      String titel,
      String alarmstichwort,
      String ausgangslage,
      String schadenslage,
      List<EinsatzauftragVorlage> auftraege,
      List<LageentwicklungVorlage> lageentwicklungen,
      List<Sicherheitshinweis> sicherheitshinweise,
      List<Bewertungspunkt> bewertungspunkte
  ) {

    public VorlagenPaket zuVorlagenPaket(
        TaktischeEinheit einheit,
        String ort,
        String fahrzeugNamen,
        Schwierigkeitsgrad grad
    ) {
      String aufbereiteterTitel = ersetzePlatzhalter(titel, ort, fahrzeugNamen);
      String aufbereiteteAusgangslage = ersetzePlatzhalter(ausgangslage, ort, fahrzeugNamen);
      String aufbereiteteSchadenslage = ersetzePlatzhalter(schadenslage, ort, fahrzeugNamen);

      boolean istGruppe = einheit != null && einheit.istGruppe();

      List<Einsatzauftrag> aufbereiteteAuftraege = auftraege != null
          ? auftraege.stream()
          .filter(a -> !a.nurGruppe() || istGruppe)
          .map(EinsatzauftragVorlage::zuEinsatzauftrag)
          .toList()
          : List.of();

      List<Lageentwicklung> aufbereiteteLage = lageentwicklungen != null
          ? lageentwicklungen.stream()
          .filter(l -> l.passtZu(grad))
          .map(LageentwicklungVorlage::zuLageentwicklung)
          .toList()
          : List.of();

      List<Sicherheitshinweis> hinweise =
          sicherheitshinweise != null ? sicherheitshinweise : List.of();
      List<Bewertungspunkt> punkte = bewertungspunkte != null ? bewertungspunkte : List.of();

      return new VorlagenPaket(
          aufbereiteterTitel,
          alarmstichwort != null ? alarmstichwort : "",
          aufbereiteteAusgangslage,
          aufbereiteteSchadenslage,
          aufbereiteteAuftraege,
          aufbereiteteLage,
          hinweise,
          punkte
      );
    }

    private static String ersetzePlatzhalter(String text, String ort, String fahrzeugNamen) {
      if (text == null) {
        return "";
      }
      String result = text;
      if (result.contains("{ort}")) {
        result = result.replace("{ort}", ort != null ? ort : "");
      }
      if (result.contains("{fahrzeuge}")) {
        result = result.replace("{fahrzeuge}", fahrzeugNamen != null ? fahrzeugNamen : "");
      }
      return result;
    }
  }

  private static record KatalogContainer(List<SzenarioVorlage> vorlagen) {

  }

  private final Map<String, SzenarioVorlage> vorlagen = new LinkedHashMap<>();

  public SzenarioKatalog() {
    this(new ObjectMapper());
  }

  @Inject
  public SzenarioKatalog(ObjectMapper objectMapper) {
    this(objectMapper, STANDARD_VORLAGEN_RESSOURCE);
  }

  public SzenarioKatalog(ObjectMapper objectMapper, String ressourcenPfad) {
    ladeVorlagen(objectMapper != null ? objectMapper : new ObjectMapper(), ressourcenPfad);
  }

  private void ladeVorlagen(ObjectMapper objectMapper, String ressourcenPfad) {
    ObjectMapper mapper = objectMapper.copy()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    InputStream is = getResourceAsStream(ressourcenPfad);
    if (is == null) {
      log.error("Szenario-Vorlagendatei konnte nicht im Classpath gefunden werden: {}",
          ressourcenPfad);
      throw new IllegalStateException("Szenario-Vorlagendatei nicht gefunden: " + ressourcenPfad);
    }

    try (is) {
      KatalogContainer container = mapper.readValue(is, KatalogContainer.class);
      if (container != null && container.vorlagen() != null) {
        for (SzenarioVorlage vorlage : container.vorlagen()) {
          if (vorlage.id() != null && !vorlage.id().isBlank()) {
            vorlagen.put(vorlage.id(), vorlage);
          }
        }
      }
      log.info("{} Szenariovorlagen erfolgreich aus {} geladen.", vorlagen.size(), ressourcenPfad);
    } catch (IOException e) {
      log.error("Fehler beim Einlesen der Szenariovorlagen aus {}: {}", ressourcenPfad,
          e.getMessage(), e);
      throw new IllegalStateException(
          "Fehler beim Laden der Szenariovorlagen aus " + ressourcenPfad, e);
    }
  }

  private InputStream getResourceAsStream(String pfad) {
    if (pfad == null || pfad.isBlank()) {
      return null;
    }
    InputStream is = getClass().getResourceAsStream(pfad);
    if (is == null && !pfad.startsWith("/")) {
      is = getClass().getResourceAsStream("/" + pfad);
    }
    if (is == null) {
      String ohneSlash = pfad.startsWith("/") ? pfad.substring(1) : pfad;
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      if (cl != null) {
        is = cl.getResourceAsStream(ohneSlash);
      }
      if (is == null) {
        is = getClass().getClassLoader().getResourceAsStream(ohneSlash);
      }
    }
    return is;
  }

  public VorlagenPaket erstelleLoeschuebung(
      TaktischeEinheit einheit,
      List<FunktionsBesetzung> besetzungen,
      List<Einsatzfahrzeug> fahrzeuge,
      SzenarioParameter parameter,
      GuardrailPruefbericht guardrailBericht
  ) {
    long anzahlAgt = besetzungen != null ? besetzungen.stream()
        .filter(FunktionsBesetzung::istBesetzt)
        .filter(b -> b.hatQualifikation(Ausbildung.ATEMSCHUTZGERAETETRAEGER))
        .count() : 0;

    boolean darfInnenangriff = anzahlAgt >= 4;
    String ort = parameter != null && !parameter.ortObjekt().isBlank() ? parameter.ortObjekt()
        : "Wohngebäude Dorfstraße 14";
    Schwierigkeitsgrad grad =
        parameter != null ? parameter.schwierigkeit() : Schwierigkeitsgrad.MITTEL;

    String fahrzeugNamen = ermittleFahrzeugnamen(fahrzeuge, "Löschfahrzeug");

    var vorlagenLoeschangriff = new String[]{"LOESCHUEBUNG_INNENANGRIFF",
        "LOESCHUEBUNG_AUSSENANGRIFF", "LOESCHUEBUNG_WALD_VEGETATION",
        "LOESCHUEBUNG_LANDWIRTSCHAFT_WASSERFOERDERUNG", "LOESCHUEBUNG_KIRCHE_DENKMAL"};

    return erstelleAusVorlage(vorlagenLoeschangriff[(int) (Math.random() * 5)], einheit, ort,
        fahrzeugNamen, grad);
  }

  public VorlagenPaket erstelleTechnischeHilfeleistung(
      TaktischeEinheit einheit,
      List<FunktionsBesetzung> besetzungen,
      List<Einsatzfahrzeug> fahrzeuge,
      SzenarioParameter parameter,
      GuardrailPruefbericht guardrailBericht
  ) {
    boolean hatHydraulik = fahrzeuge != null && fahrzeuge.stream().anyMatch(
        f -> f.getFahrzeugtyp() != null && f.getFahrzeugtyp().name().toUpperCase()
            .startsWith("HLF"));
    String ort = parameter != null && !parameter.ortObjekt().isBlank() ? parameter.ortObjekt()
        : "Kreuzung B102 / Ortsausgang";
    Schwierigkeitsgrad grad =
        parameter != null ? parameter.schwierigkeit() : Schwierigkeitsgrad.MITTEL;
    String fahrzeugNamen = ermittleFahrzeugnamen(fahrzeuge, "Hilfeleistungsfahrzeug");

    var vorlagenTH = new String[]{"TH_VU_EINGEKLEMMT",
        "TH_BASISMASSNAHMEN", "TH_WASSERRETTUNG_HAVEL", "TH_GEFAHRGUT_KLEIN",
        "TH_TIERRETTUNG_LANDWIRTSCHAFT"};
    return erstelleAusVorlage(vorlagenTH[(int) (Math.random() * 5)], einheit, ort, fahrzeugNamen,
        grad);
  }

  public VorlagenPaket erstelleAusVorlage(
      String vorlageId,
      TaktischeEinheit einheit,
      String ort,
      String fahrzeugNamen,
      Schwierigkeitsgrad grad
  ) {
    SzenarioVorlage vorlage = vorlagen.get(vorlageId);
    if (vorlage == null) {
      throw new IllegalArgumentException("Keine Szenariovorlage gefunden für ID: " + vorlageId);
    }
    return vorlage.zuVorlagenPaket(einheit, ort, fahrzeugNamen, grad);
  }

  public Map<String, SzenarioVorlage> getVorlagen() {
    return Collections.unmodifiableMap(vorlagen);
  }

  public Optional<SzenarioVorlage> getVorlage(String id) {
    return Optional.ofNullable(vorlagen.get(id));
  }

  private String ermittleFahrzeugnamen(List<Einsatzfahrzeug> fahrzeuge, String fallback) {
    if (fahrzeuge == null || fahrzeuge.isEmpty()) {
      return fallback;
    }
    List<String> namen = fahrzeuge.stream()
        .map(f -> {
          if (f.getBezeichnung() != null && !f.getBezeichnung().isBlank()) {
            return f.getBezeichnung();
          }
          if (f.getFahrzeugtyp() != null) {
            return f.getFahrzeugtyp().name();
          }
          return "Einsatzfahrzeug";
        })
        .toList();
    return String.join(", ", namen);
  }
}
