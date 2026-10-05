package de.eichstaedt.ortswehrfuehrer.domain.uebung;

/**
 * Taktische Funktionen nach Feuerwehr-Dienstvorschrift 3 (FwDV 3).
 */
public enum TaktischeFunktion {
  EINHEITSFUEHRER("Einheitsführer / Gruppenführer", "GF", true),
  MASCHINIST("Maschinist", "Ma", false),
  MELDER("Melder", "Me", false),
  ANGRIFFSTRUPPFUEHRER("Angriffstruppführer", "ATF", true),
  ANGRIFFSTRUPPMANN("Angriffstruppmann", "ATM", false),
  WASSERTRUPPFUEHRER("Wassertruppführer", "WTF", true),
  WASSERTRUPPMANN("Wassertruppmann", "WTM", false),
  SCHLAUCHTRUPPFUEHRER("Schlauchtruppführer", "STF", true),
  SCHLAUCHTRUPPMANN("Schlauchtruppmann", "STM", false);

  private final String bezeichnung;
  private final String kuerzel;
  private final boolean fuehrungsfunktion;

  TaktischeFunktion(String bezeichnung, String kuerzel, boolean fuehrungsfunktion) {
    this.bezeichnung = bezeichnung;
    this.kuerzel = kuerzel;
    this.fuehrungsfunktion = fuehrungsfunktion;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public String getKuerzel() {
    return kuerzel;
  }

  public boolean isFuehrungsfunktion() {
    return fuehrungsfunktion;
  }

  public boolean istAtemschutzRelevant() {
    return this == ANGRIFFSTRUPPFUEHRER || this == ANGRIFFSTRUPPMANN
        || this == WASSERTRUPPFUEHRER || this == WASSERTRUPPMANN;
  }

  public boolean istAngriffstrupp() {
    return this == ANGRIFFSTRUPPFUEHRER || this == ANGRIFFSTRUPPMANN;
  }

  public boolean istWassertrupp() {
    return this == WASSERTRUPPFUEHRER || this == WASSERTRUPPMANN;
  }

  public boolean istSchlauchtrupp() {
    return this == SCHLAUCHTRUPPFUEHRER || this == SCHLAUCHTRUPPMANN;
  }
}
