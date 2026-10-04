package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class FeuerwehrfahrzeugDin14530Test {

  @Test
  void testKlfDin14530Attribute() {
    Fahrzeugtyp typ = Fahrzeugtyp.KLF;
    assertEquals("Teil 24", typ.getDinNorm());
    assertEquals("DIN 14530 Teil 24", typ.getDin14530());
    assertEquals("L I", typ.getMasseklasse());
    assertEquals("3,0–4,75 t", typ.getZulassigeGesamtmasse());
    assertEquals("Staffel", typ.getBesatzung());
    assertEquals(6, typ.getBesatzungSollStaerke());
    assertEquals("500 l", typ.getLoeschwasser());
    assertEquals("n. v.", typ.getSchaummittel());
    assertEquals("PFPN 10-1000", typ.getPumpe());
    assertEquals(new Abmessungen(6.0, 2.3, 2.6), typ.getAbmessungen());
    assertEquals("6,0 × 2,3 × 2,6", typ.getAbmessungen().formatiert());
    assertEquals("n. v. (üblicherweise Straße, Kat. 1)", typ.getFahrgestellKategorie());
    assertEquals("n. v.", typ.getAntrieb());
    assertEquals("C1", typ.getFuehrerschein());
    assertEquals("C1", typ.getFuehrerscheinklasse());
  }

  @Test
  void testTsfDin14530Attribute() {
    Fahrzeugtyp typ = Fahrzeugtyp.TSF;
    assertEquals("Teil 16", typ.getDinNorm());
    assertEquals("L I", typ.getMasseklasse());
    assertEquals("3,0–4,75 t", typ.getZulassigeGesamtmasse());
    assertEquals("Staffel", typ.getBesatzung());
    assertEquals(6, typ.getBesatzungSollStaerke());
    assertEquals("kein Tank", typ.getLoeschwasser());
    assertEquals("keines", typ.getSchaummittel());
    assertEquals("PFPN 10-1000", typ.getPumpe());
    assertEquals("6,0 × 2,3 × 2,6", typ.getAbmessungen().formatiert());
    assertEquals("C1", typ.getFuehrerschein());
  }

  @Test
  void testTsfWDin14530Attribute() {
    Fahrzeugtyp typ = Fahrzeugtyp.TSF_W;
    assertEquals("Teil 17", typ.getDinNorm());
    assertEquals("L II", typ.getMasseklasse());
    assertEquals("4,75–7,5 t", typ.getZulassigeGesamtmasse());
    assertEquals("Staffel", typ.getBesatzung());
    assertEquals(6, typ.getBesatzungSollStaerke());
    assertEquals("500 l", typ.getLoeschwasser());
    assertEquals("PFPN 10-1000", typ.getPumpe());
    assertEquals("6,3 × 2,35 × 2,9", typ.getAbmessungen().formatiert());
    assertEquals("C1", typ.getFuehrerschein());

    assertEquals(typ.getDinNorm(), Fahrzeugtyp.TSFW.getDinNorm());
  }

  @Test
  void testMlfDin14530Attribute() {
    Fahrzeugtyp typ = Fahrzeugtyp.MLF;
    assertEquals("Teil 25", typ.getDinNorm());
    assertEquals("L II", typ.getMasseklasse());
    assertEquals("4,75–7,5 t", typ.getZulassigeGesamtmasse());
    assertEquals("Staffel", typ.getBesatzung());
    assertEquals("n. v.", typ.getLoeschwasser());
    assertNull(typ.getAbmessungen());
    assertEquals("C1", typ.getFuehrerschein());
  }

  @Test
  void testLf10Din14530Attribute() {
    Fahrzeugtyp typ = Fahrzeugtyp.LF10;
    assertEquals("Teil 5", typ.getDinNorm());
    assertEquals("M II", typ.getMasseklasse());
    assertEquals("9,0–14,0 t", typ.getZulassigeGesamtmasse());
    assertEquals("Gruppe", typ.getBesatzung());
    assertEquals(9, typ.getBesatzungSollStaerke());
    assertEquals("1.200 l", typ.getLoeschwasser());
    assertEquals("120 l (6 × 20 l)", typ.getSchaummittel());
    assertEquals("FPN 10-1000", typ.getPumpe());
    assertEquals("7,3 × 2,5 × 3,3", typ.getAbmessungen().formatiert());
    assertEquals("Straße (Kat. 1) oder Gelände (Kat. 2)", typ.getFahrgestellKategorie());
    assertEquals("Straße oder Allrad", typ.getAntrieb());
    assertEquals("C", typ.getFuehrerschein());
  }

  @Test
  void testHlf10Din14530Attribute() {
    Fahrzeugtyp typ = Fahrzeugtyp.HLF10;
    assertEquals("Teil 26", typ.getDinNorm());
    assertEquals("M II", typ.getMasseklasse());
    assertEquals("9,0–14,0 t", typ.getZulassigeGesamtmasse());
    assertEquals("Gruppe", typ.getBesatzung());
    assertEquals(9, typ.getBesatzungSollStaerke());
    assertEquals("1.000 l", typ.getLoeschwasser());
    assertEquals("120 l (6 × 20 l)", typ.getSchaummittel());
    assertEquals("FPN 10-1000", typ.getPumpe());
    assertEquals("7,3 × 2,5 × 3,3", typ.getAbmessungen().formatiert());
    assertEquals("C", typ.getFuehrerschein());
  }

  @Test
  void testLf20Din14530Attribute() {
    Fahrzeugtyp typ = Fahrzeugtyp.LF20;
    assertEquals("Teil 11", typ.getDinNorm());
    assertEquals("M III", typ.getMasseklasse());
    assertEquals("14,0–16,0 t", typ.getZulassigeGesamtmasse());
    assertEquals("Gruppe", typ.getBesatzung());
    assertEquals(9, typ.getBesatzungSollStaerke());
    assertEquals("2.000 l", typ.getLoeschwasser());
    assertEquals("120 l (6 × 20 l)", typ.getSchaummittel());
    assertEquals("FPN 10-2000", typ.getPumpe());
    assertEquals("8,6 × 2,5 × 3,3", typ.getAbmessungen().formatiert());
    assertEquals("C", typ.getFuehrerschein());
  }

  @Test
  void testHlf20Din14530Attribute() {
    Fahrzeugtyp typ = Fahrzeugtyp.HLF20;
    assertEquals("Teil 27", typ.getDinNorm());
    assertEquals("M III", typ.getMasseklasse());
    assertEquals("14,0–16,0 t", typ.getZulassigeGesamtmasse());
    assertEquals("Gruppe", typ.getBesatzung());
    assertEquals(9, typ.getBesatzungSollStaerke());
    assertEquals("1.600 l", typ.getLoeschwasser());
    assertEquals("120 l (6 × 20 l)", typ.getSchaummittel());
    assertEquals("FPN 10-2000", typ.getPumpe());
    assertEquals("8,6 × 2,5 × 3,3", typ.getAbmessungen().formatiert());
    assertEquals("C", typ.getFuehrerschein());
  }

  @Test
  void testLf20KatSDin14530Attribute() {
    Fahrzeugtyp typ = Fahrzeugtyp.LF20_KATS;
    assertEquals("Teil 8", typ.getDinNorm());
    assertEquals("M III", typ.getMasseklasse());
    assertEquals("14,0–16,0 t", typ.getZulassigeGesamtmasse());
    assertEquals("Gruppe", typ.getBesatzung());
    assertEquals(9, typ.getBesatzungSollStaerke());
    assertEquals("1.000 l", typ.getLoeschwasser());
    assertEquals("120 l (6 × 20 l)", typ.getSchaummittel());
    assertEquals("FPN 10-2000", typ.getPumpe());
    assertEquals("7,3 × 2,5 × 3,3", typ.getAbmessungen().formatiert());
    assertEquals("Gelände (Kat. 2)", typ.getFahrgestellKategorie());
    assertEquals("Allrad", typ.getAntrieb());
    assertEquals("C", typ.getFuehrerschein());
  }

  @Test
  void testTlf2000Din14530Attribute() {
    Fahrzeugtyp typ = Fahrzeugtyp.TLF2000;
    assertEquals("Teil 18", typ.getDinNorm());
    assertEquals("M II", typ.getMasseklasse());
    assertEquals("9,0–14,0 t", typ.getZulassigeGesamtmasse());
    assertEquals("Trupp", typ.getBesatzung());
    assertEquals(3, typ.getBesatzungSollStaerke());
    assertEquals("2.000 l", typ.getLoeschwasser());
    assertEquals("FPN 10-1000", typ.getPumpe());
    assertEquals("6,3 × 2,3 × 3,1", typ.getAbmessungen().formatiert());
    assertEquals("Gelände (Kat. 2)", typ.getFahrgestellKategorie());
    assertEquals("Allrad", typ.getAntrieb());
    assertEquals("C", typ.getFuehrerschein());
  }

  @Test
  void testTlf3000Din14530Attribute() {
    Fahrzeugtyp typ = Fahrzeugtyp.TLF3000;
    assertEquals("Teil 22", typ.getDinNorm());
    assertEquals("M II", typ.getMasseklasse());
    assertEquals("9,0–14,0 t", typ.getZulassigeGesamtmasse());
    assertEquals("Trupp", typ.getBesatzung());
    assertEquals(3, typ.getBesatzungSollStaerke());
    assertEquals("3.000 l", typ.getLoeschwasser());
    assertEquals("120 l", typ.getSchaummittel());
    assertEquals("FPN 10-2000", typ.getPumpe());
    assertEquals("7,5 × 2,5 × 3,3", typ.getAbmessungen().formatiert());
    assertEquals("Gelände (Kat. 2)", typ.getFahrgestellKategorie());
    assertEquals("Allrad", typ.getAntrieb());
    assertEquals("C", typ.getFuehrerschein());
  }

  @Test
  void testTlf4000Din14530Attribute() {
    Fahrzeugtyp typ = Fahrzeugtyp.TLF4000;
    assertEquals("Teil 21", typ.getDinNorm());
    assertEquals("M III", typ.getMasseklasse());
    assertEquals("14,0–16,0 t", typ.getZulassigeGesamtmasse());
    assertEquals("Trupp", typ.getBesatzung());
    assertEquals(3, typ.getBesatzungSollStaerke());
    assertEquals("4.000 l", typ.getLoeschwasser());
    assertEquals("mind. 500 l (fest eingebaut)", typ.getSchaummittel());
    assertEquals("FPN 10-2000", typ.getPumpe());
    assertEquals("10,0 × 2,55 × 3,3", typ.getAbmessungen().formatiert());
    assertEquals("Gelände (Kat. 2)", typ.getFahrgestellKategorie());
    assertEquals("Allrad", typ.getAntrieb());
    assertEquals("C", typ.getFuehrerschein());
  }

  @ParameterizedTest
  @EnumSource(Fahrzeugtyp.class)
  void testAlleFahrzeugtypenHabenGueltigeAttribute(Fahrzeugtyp typ) {
    assertNotNull(typ.getDinNorm());
    assertNotNull(typ.getMasseklasse());
    assertNotNull(typ.getZulassigeGesamtmasse());
    assertNotNull(typ.getBesatzung());
    assertNotNull(typ.getLoeschwasser());
    assertNotNull(typ.getSchaummittel());
    assertNotNull(typ.getPumpe());
    assertNotNull(typ.getFahrgestellKategorie());
    assertNotNull(typ.getAntrieb());
    assertNotNull(typ.getFuehrerschein());
  }

  @Test
  void testEinsatzfahrzeugErbtAttributeAusFahrzeugtypUndErlaubtUeberschreiben() {
    Einsatzfahrzeug hlf10 = Einsatzfahrzeug.builder()
        .bezeichnung("Hilfeleistungslöschgruppenfahrzeug")
        .kennung("Florian Havelland 5/48/1")
        .fahrzeugtyp(Fahrzeugtyp.HLF10)
        .build();

    assertEquals("Teil 26", hlf10.getDinNorm());
    assertEquals("DIN 14530 Teil 26", hlf10.getDin14530());
    assertEquals("M II", hlf10.getMasseklasse());
    assertEquals("9,0–14,0 t", hlf10.getZulassigeGesamtmasse());
    assertEquals("Gruppe", hlf10.getBesatzung());
    assertEquals(9, hlf10.getBesatzungSollStaerke());
    assertEquals("1.000 l", hlf10.getLoeschwasser());
    assertEquals("120 l (6 × 20 l)", hlf10.getSchaummittel());
    assertEquals("FPN 10-1000", hlf10.getPumpe());
    assertEquals(new Abmessungen(7.3, 2.5, 3.3), hlf10.getAbmessungen());
    assertEquals("Straße (Kat. 1) oder Gelände (Kat. 2)", hlf10.getFahrgestellKategorie());
    assertEquals("Straße oder Allrad", hlf10.getAntrieb());
    assertEquals("C", hlf10.getFuehrerschein());
    assertEquals("C", hlf10.getFuehrerscheinklasse());

    // Praxisabweichung: LF 10 mit 1.200 l oder speziellem Schaummitteltank
    Einsatzfahrzeug sonderLf10 = Einsatzfahrzeug.builder()
        .bezeichnung("LF 10 mit Sonderausstattung")
        .kennung("Florian 1")
        .fahrzeugtyp(Fahrzeugtyp.LF10)
        .loeschwasser("1.600 l")
        .schaummittel("125 l fest")
        .abmessungen(7.5, 2.5, 3.4)
        .build();

    assertEquals("1.600 l", sonderLf10.getLoeschwasser());
    assertEquals("125 l fest", sonderLf10.getSchaummittel());
    assertEquals(new Abmessungen(7.5, 2.5, 3.4), sonderLf10.getAbmessungen());
    // Andere Attribute weiterhin aus dem Fahrzeugtyp:
    assertEquals("Teil 5", sonderLf10.getDinNorm());
    assertEquals("M II", sonderLf10.getMasseklasse());
    assertEquals("FPN 10-1000", sonderLf10.getPumpe());
  }
}
