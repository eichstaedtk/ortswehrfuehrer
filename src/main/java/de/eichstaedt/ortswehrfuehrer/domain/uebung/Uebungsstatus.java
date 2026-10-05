package de.eichstaedt.ortswehrfuehrer.domain.uebung;

/**
 * Status im Lebenszyklus eines Übungsszenarios.
 */
public enum Uebungsstatus {
  ENTWURF("Entwurf"),
  GENERIERT("Generiert"),
  FREIGEGEBEN("Freigegeben"),
  ARCHIVIERT("Archiviert");

  private final String bezeichnung;

  Uebungsstatus(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }
}
