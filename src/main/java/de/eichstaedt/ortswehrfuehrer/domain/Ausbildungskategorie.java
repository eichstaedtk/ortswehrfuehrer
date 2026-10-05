package de.eichstaedt.ortswehrfuehrer.domain;

/**
 * Kategorien von Ausbildungen und Lehrgängen nach Feuerwehr-Dienstvorschrift 2 (FwDV 2).
 */
public enum Ausbildungskategorie {
  TRUPPAUSBILDUNG("Truppausbildung"),
  TECHNISCHE_AUSBILDUNG("Technische Ausbildung"),
  FUEHRUNGSAUSBILDUNG("Führungsausbildung"),
  FORTBILDUNG("Fortbildung");

  private final String bezeichnung;

  Ausbildungskategorie(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }
}
