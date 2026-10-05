package de.eichstaedt.ortswehrfuehrer.domain.uebung;

import java.util.List;

/**
 * Taktische Einheiten nach Feuerwehr-Dienstvorschrift 3 (FwDV 3).
 */
public enum TaktischeEinheit {
  SELBSTSTAENDIGER_TRUPP("Selbstständiger Trupp", "0/1/2/3", 3, List.of(
      TaktischeFunktion.EINHEITSFUEHRER,
      TaktischeFunktion.MASCHINIST,
      TaktischeFunktion.MELDER
  )),
  STAFFEL("Staffel", "0/1/5/6", 6, List.of(
      TaktischeFunktion.EINHEITSFUEHRER,
      TaktischeFunktion.MASCHINIST,
      TaktischeFunktion.ANGRIFFSTRUPPFUEHRER,
      TaktischeFunktion.ANGRIFFSTRUPPMANN,
      TaktischeFunktion.WASSERTRUPPFUEHRER,
      TaktischeFunktion.WASSERTRUPPMANN
  )),
  GRUPPE("Gruppe", "0/1/8/9", 9, List.of(
      TaktischeFunktion.EINHEITSFUEHRER,
      TaktischeFunktion.MASCHINIST,
      TaktischeFunktion.MELDER,
      TaktischeFunktion.ANGRIFFSTRUPPFUEHRER,
      TaktischeFunktion.ANGRIFFSTRUPPMANN,
      TaktischeFunktion.WASSERTRUPPFUEHRER,
      TaktischeFunktion.WASSERTRUPPMANN,
      TaktischeFunktion.SCHLAUCHTRUPPFUEHRER,
      TaktischeFunktion.SCHLAUCHTRUPPMANN
  )),
  ZUG("Zug", "1/3/18/22", 22, List.of(
      TaktischeFunktion.EINHEITSFUEHRER
  ));

  private final String bezeichnung;
  private final String staerke;
  private final int sollStaerke;
  private final List<TaktischeFunktion> erforderlicheFunktionen;

  TaktischeEinheit(String bezeichnung, String staerke, int sollStaerke, List<TaktischeFunktion> erforderlicheFunktionen) {
    this.bezeichnung = bezeichnung;
    this.staerke = staerke;
    this.sollStaerke = sollStaerke;
    this.erforderlicheFunktionen = erforderlicheFunktionen;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public String getStaerke() {
    return staerke;
  }

  public int getSollStaerke() {
    return sollStaerke;
  }

  public List<TaktischeFunktion> getErforderlicheFunktionen() {
    return erforderlicheFunktionen;
  }

  public boolean istGruppe() {
    return this == GRUPPE;
  }

  public boolean istStaffel() {
    return this == STAFFEL;
  }

  public boolean istSelbststaendigerTrupp() {
    return this == SELBSTSTAENDIGER_TRUPP;
  }
}
