package de.eichstaedt.ortswehrfuehrer.domain.uebung;

/**
 * Tageszeit für die Rahmenbedingungen eines Übungsszenarios.
 */
public enum Tageszeit {
  TAG("Tag / Tageslicht"),
  DAEMMERUNG("Dämmerung / Eingeschränkte Sicht"),
  NACHT("Nacht / Dunkelheit (Ausleuchtung erforderlich)");

  private final String bezeichnung;

  Tageszeit(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }
}
