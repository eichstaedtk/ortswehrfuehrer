package de.eichstaedt.ortswehrfuehrer.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Lehrgänge und Ausbildungen der Freiwilligen Feuerwehren nach Feuerwehr-Dienstvorschrift 2 (FwDV 2).
 */
public enum Ausbildung {
  // 2 Truppausbildung
  TRUPPMANN_TEIL_1("2.1.1", "Truppmann Teil 1 (Grundausbildungslehrgang)", 70, Ausbildungskategorie.TRUPPAUSBILDUNG),
  TRUPPMANN_TEIL_2("2.1.2", "Truppmann Teil 2", 80, Ausbildungskategorie.TRUPPAUSBILDUNG),
  TRUPPFUEHRER("2.2", "Truppführer", 35, Ausbildungskategorie.TRUPPAUSBILDUNG),

  // 3 Technische Ausbildung
  SPRECHFUNKER("3.1", "Sprechfunker", 16, Ausbildungskategorie.TECHNISCHE_AUSBILDUNG),
  ATEMSCHUTZGERAETETRAEGER("3.2", "Atemschutzgeräteträger", 25, Ausbildungskategorie.TECHNISCHE_AUSBILDUNG),
  MASCHINIST("3.3", "Maschinist", 35, Ausbildungskategorie.TECHNISCHE_AUSBILDUNG),
  TECHNISCHE_HILFELEISTUNG("3.4", "Technische Hilfeleistung", 35, Ausbildungskategorie.TECHNISCHE_AUSBILDUNG),
  ABC_EINSATZ("3.5", "ABC-Einsatz", 70, Ausbildungskategorie.TECHNISCHE_AUSBILDUNG),
  ABC_ERKUNDUNG("3.6", "ABC-Erkundung", 35, Ausbildungskategorie.TECHNISCHE_AUSBILDUNG),
  ABC_DEKONTAMINATION_P_G("3.7", "ABC-Dekontamination P/G", 28, Ausbildungskategorie.TECHNISCHE_AUSBILDUNG),
  GERAETEWART("3.8", "Gerätewart", 35, Ausbildungskategorie.TECHNISCHE_AUSBILDUNG),
  ATEMSCHUTZGERAETEWART("3.9", "Atemschutzgerätewart", 35, Ausbildungskategorie.TECHNISCHE_AUSBILDUNG),

  // 4 Führungsausbildung
  GRUPPENFUEHRER("4.1", "Gruppenführer", 70, Ausbildungskategorie.FUEHRUNGSAUSBILDUNG),
  ZUGFUEHRER("4.2", "Zugführer", 70, Ausbildungskategorie.FUEHRUNGSAUSBILDUNG),
  VERBANDSFUEHRER("4.3", "Verbandsführer", 35, Ausbildungskategorie.FUEHRUNGSAUSBILDUNG),
  EINFUEHRUNG_IN_DIE_STABSARBEIT("4.4", "Einführung in die Stabsarbeit", 35, Ausbildungskategorie.FUEHRUNGSAUSBILDUNG),
  FUEHREN_IM_ABC_EINSATZ("4.5", "Führen im ABC-Einsatz", 35, Ausbildungskategorie.FUEHRUNGSAUSBILDUNG),
  LEITER_EINER_FEUERWEHR("4.6", "Leiter einer Feuerwehr", 35, Ausbildungskategorie.FUEHRUNGSAUSBILDUNG),
  AUSBILDER_IN_DER_FEUERWEHR("4.7", "Ausbilder in der Feuerwehr", 35, Ausbildungskategorie.FUEHRUNGSAUSBILDUNG),

  // 5 Fortbildung
  FORTBILDUNG("5", "Fortbildung", 0, Ausbildungskategorie.FORTBILDUNG);

  private final String ziffer;
  private final String bezeichnung;
  private final int mindeststunden;
  private final Ausbildungskategorie kategorie;

  Ausbildung(String ziffer, String bezeichnung, int mindeststunden, Ausbildungskategorie kategorie) {
    this.ziffer = ziffer;
    this.bezeichnung = bezeichnung;
    this.mindeststunden = mindeststunden;
    this.kategorie = kategorie;
  }

  public String getZiffer() {
    return ziffer;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public int getMindeststunden() {
    return mindeststunden;
  }

  public Ausbildungskategorie getKategorie() {
    return kategorie;
  }

  public String getFwdv2() {
    return "FwDV 2 " + ziffer;
  }

  public static Optional<Ausbildung> findeNachZiffer(String ziffer) {
    if (ziffer == null || ziffer.isBlank()) {
      return Optional.empty();
    }
    return Arrays.stream(values())
        .filter(a -> a.ziffer.equalsIgnoreCase(ziffer.trim()))
        .findFirst();
  }

  public static Optional<Ausbildung> findeNachBezeichnung(String bezeichnung) {
    if (bezeichnung == null || bezeichnung.isBlank()) {
      return Optional.empty();
    }
    return Arrays.stream(values())
        .filter(a -> a.bezeichnung.equalsIgnoreCase(bezeichnung.trim()))
        .findFirst();
  }

  public static Optional<Ausbildung> von(String wert) {
    if (wert == null || wert.isBlank()) {
      return Optional.empty();
    }
    String trimmed = wert.trim();
    try {
      return Optional.of(Ausbildung.valueOf(trimmed));
    } catch (IllegalArgumentException ignored) {
    }
    return findeNachZiffer(trimmed)
        .or(() -> findeNachBezeichnung(trimmed));
  }

  public static List<Ausbildung> ausbildungenFuerKategorie(Ausbildungskategorie kategorie) {
    if (kategorie == null) {
      return List.of();
    }
    return Arrays.stream(values())
        .filter(a -> a.kategorie == kategorie)
        .toList();
  }
}
