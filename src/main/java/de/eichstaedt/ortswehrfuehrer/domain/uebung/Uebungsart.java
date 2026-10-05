package de.eichstaedt.ortswehrfuehrer.domain.uebung;

/**
 * Übungsart nach FwDV 3: Löscheinsatz oder Technische Hilfeleistung.
 */
public enum Uebungsart {
  LOESCHUEBUNG("Löschübung (Brandbekämpfung)"),
  TECHNISCHE_HILFELEISTUNG("Technische Hilfeleistung (TH)");

  private final String bezeichnung;

  Uebungsart(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }
}
