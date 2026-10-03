package de.eichstaedt.ortswehrfuehrer.domain;

/**
 * Created by konrad.eichstaedt@gmx.de on 03.10.26.
 * <p>
 * This Enum represents the type of a fire department vehicle (Fahrzeugtyp) according to DIN 14530.
 */
public enum Fahrzeugtyp {
  KLF("Teil 24", "L I", "3,0–4,75 t", "Staffel", "500 l", "n. v.", "PFPN 10-1000", new Abmessungen(6.0, 2.3, 2.6), "n. v. (üblicherweise Straße, Kat. 1)", "n. v.", "C1"),
  TSF("Teil 16", "L I", "3,0–4,75 t", "Staffel", "kein Tank", "keines", "PFPN 10-1000", new Abmessungen(6.0, 2.3, 2.6), "n. v. (üblicherweise Straße, Kat. 1)", "n. v.", "C1"),
  TSF_W("Teil 17", "L II", "4,75–7,5 t", "Staffel", "500 l", "n. v.", "PFPN 10-1000", new Abmessungen(6.3, 2.35, 2.9), "n. v. (üblicherweise Straße, Kat. 1)", "n. v.", "C1"),
  TSFW("Teil 17", "L II", "4,75–7,5 t", "Staffel", "500 l", "n. v.", "PFPN 10-1000", new Abmessungen(6.3, 2.35, 2.9), "n. v. (üblicherweise Straße, Kat. 1)", "n. v.", "C1"),
  MLF("Teil 25", "L II", "4,75–7,5 t", "Staffel", "n. v.", "n. v.", "n. v.", null, "n. v. (üblicherweise Straße, Kat. 1)", "n. v.", "C1"),
  LF10("Teil 5", "M II", "9,0–14,0 t", "Gruppe", "1.200 l", "120 l (6 × 20 l)", "FPN 10-1000", new Abmessungen(7.3, 2.5, 3.3), "Straße (Kat. 1) oder Gelände (Kat. 2)", "Straße oder Allrad", "C"),
  LF_10("Teil 5", "M II", "9,0–14,0 t", "Gruppe", "1.200 l", "120 l (6 × 20 l)", "FPN 10-1000", new Abmessungen(7.3, 2.5, 3.3), "Straße (Kat. 1) oder Gelände (Kat. 2)", "Straße oder Allrad", "C"),
  HLF10("Teil 26", "M II", "9,0–14,0 t", "Gruppe", "1.000 l", "120 l (6 × 20 l)", "FPN 10-1000", new Abmessungen(7.3, 2.5, 3.3), "Straße (Kat. 1) oder Gelände (Kat. 2)", "Straße oder Allrad", "C"),
  HLF_10("Teil 26", "M II", "9,0–14,0 t", "Gruppe", "1.000 l", "120 l (6 × 20 l)", "FPN 10-1000", new Abmessungen(7.3, 2.5, 3.3), "Straße (Kat. 1) oder Gelände (Kat. 2)", "Straße oder Allrad", "C"),
  LF20("Teil 11", "M III", "14,0–16,0 t", "Gruppe", "2.000 l", "120 l (6 × 20 l)", "FPN 10-2000", new Abmessungen(8.6, 2.5, 3.3), "Straße (Kat. 1) oder Gelände (Kat. 2)", "Straße oder Allrad", "C"),
  LF_20("Teil 11", "M III", "14,0–16,0 t", "Gruppe", "2.000 l", "120 l (6 × 20 l)", "FPN 10-2000", new Abmessungen(8.6, 2.5, 3.3), "Straße (Kat. 1) oder Gelände (Kat. 2)", "Straße oder Allrad", "C"),
  HLF20("Teil 27", "M III", "14,0–16,0 t", "Gruppe", "1.600 l", "120 l (6 × 20 l)", "FPN 10-2000", new Abmessungen(8.6, 2.5, 3.3), "Straße (Kat. 1) oder Gelände (Kat. 2)", "Straße oder Allrad", "C"),
  HLF_20("Teil 27", "M III", "14,0–16,0 t", "Gruppe", "1.600 l", "120 l (6 × 20 l)", "FPN 10-2000", new Abmessungen(8.6, 2.5, 3.3), "Straße (Kat. 1) oder Gelände (Kat. 2)", "Straße oder Allrad", "C"),
  LF20_KATS("Teil 8", "M III", "14,0–16,0 t", "Gruppe", "1.000 l", "120 l (6 × 20 l)", "FPN 10-2000", new Abmessungen(7.3, 2.5, 3.3), "Gelände (Kat. 2)", "Allrad", "C"),
  LF20KATS("Teil 8", "M III", "14,0–16,0 t", "Gruppe", "1.000 l", "120 l (6 × 20 l)", "FPN 10-2000", new Abmessungen(7.3, 2.5, 3.3), "Gelände (Kat. 2)", "Allrad", "C"),
  TLF2000("Teil 18", "M II", "9,0–14,0 t", "Trupp", "2.000 l", "n. v.", "FPN 10-1000", new Abmessungen(6.3, 2.3, 3.1), "Gelände (Kat. 2)", "Allrad", "C"),
  TLF_2000("Teil 18", "M II", "9,0–14,0 t", "Trupp", "2.000 l", "n. v.", "FPN 10-1000", new Abmessungen(6.3, 2.3, 3.1), "Gelände (Kat. 2)", "Allrad", "C"),
  TLF3000("Teil 22", "M II", "9,0–14,0 t", "Trupp", "3.000 l", "120 l", "FPN 10-2000", new Abmessungen(7.5, 2.5, 3.3), "Gelände (Kat. 2)", "Allrad", "C"),
  TLF_3000("Teil 22", "M II", "9,0–14,0 t", "Trupp", "3.000 l", "120 l", "FPN 10-2000", new Abmessungen(7.5, 2.5, 3.3), "Gelände (Kat. 2)", "Allrad", "C"),
  TLF4000("Teil 21", "M III", "14,0–16,0 t", "Trupp", "4.000 l", "mind. 500 l (fest eingebaut)", "FPN 10-2000", new Abmessungen(10.0, 2.55, 3.3), "Gelände (Kat. 2)", "Allrad", "C"),
  TLF_4000("Teil 21", "M III", "14,0–16,0 t", "Trupp", "4.000 l", "mind. 500 l (fest eingebaut)", "FPN 10-2000", new Abmessungen(10.0, 2.55, 3.3), "Gelände (Kat. 2)", "Allrad", "C"),
  LF1000("n. v.", "n. v.", "n. v.", "n. v.", "n. v.", "n. v.", "n. v.", null, "n. v.", "n. v.", "n. v.");

  private final String dinNorm;
  private final String masseklasse;
  private final String zulassigeGesamtmasse;
  private final String besatzung;
  private final String loeschwasser;
  private final String schaummittel;
  private final String pumpe;
  private final Abmessungen abmessungen;
  private final String fahrgestellKategorie;
  private final String antrieb;
  private final String fuehrerschein;

  Fahrzeugtyp(
      String dinNorm,
      String masseklasse,
      String zulassigeGesamtmasse,
      String besatzung,
      String loeschwasser,
      String schaummittel,
      String pumpe,
      Abmessungen abmessungen,
      String fahrgestellKategorie,
      String antrieb,
      String fuehrerschein) {
    this.dinNorm = dinNorm;
    this.masseklasse = masseklasse;
    this.zulassigeGesamtmasse = zulassigeGesamtmasse;
    this.besatzung = besatzung;
    this.loeschwasser = loeschwasser;
    this.schaummittel = schaummittel;
    this.pumpe = pumpe;
    this.abmessungen = abmessungen;
    this.fahrgestellKategorie = fahrgestellKategorie;
    this.antrieb = antrieb;
    this.fuehrerschein = fuehrerschein;
  }

  public String getDinNorm() {
    return dinNorm;
  }

  public String getDin14530() {
    return "DIN 14530 " + dinNorm;
  }

  public String getMasseklasse() {
    return masseklasse;
  }

  public String getZulassigeGesamtmasse() {
    return zulassigeGesamtmasse;
  }

  public String getBesatzung() {
    return besatzung;
  }

  public int getBesatzungSollStaerke() {
    if ("Staffel".equalsIgnoreCase(besatzung)) {
      return 6;
    } else if ("Gruppe".equalsIgnoreCase(besatzung)) {
      return 9;
    } else if ("Trupp".equalsIgnoreCase(besatzung)) {
      return 3;
    }
    return 0;
  }

  public String getLoeschwasser() {
    return loeschwasser;
  }

  public String getSchaummittel() {
    return schaummittel;
  }

  public String getPumpe() {
    return pumpe;
  }

  public Abmessungen getAbmessungen() {
    return abmessungen;
  }

  public String getFahrgestellKategorie() {
    return fahrgestellKategorie;
  }

  public String getAntrieb() {
    return antrieb;
  }

  public String getFuehrerschein() {
    return fuehrerschein;
  }

  public String getFuehrerscheinklasse() {
    return fuehrerschein;
  }
}
