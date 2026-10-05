package de.eichstaedt.ortswehrfuehrer.domain.uebung;

/**
 * Schwierigkeitsgrad eines Übungsszenarios.
 */
public enum Schwierigkeitsgrad {
  LEICHT("Leicht (Standardablauf & Grundlagen)"),
  MITTEL("Mittel (Erweiterte Lage mit Hindernissen)"),
  ANSPRUCHSVOLL("Anspruchsvoll (Komplexe Lage & Dynamik)");

  private final String bezeichnung;

  Schwierigkeitsgrad(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }
}
