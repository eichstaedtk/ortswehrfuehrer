package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EinsatzfahrzeugTest {

  @Test
  void testStandardKonstruktorUndSetter() {
    Einsatzfahrzeug fahrzeug = new Einsatzfahrzeug();
    assertNull(fahrzeug.getId());
    assertNull(fahrzeug.getBezeichnung());
    assertNull(fahrzeug.getKennung());
    assertNull(fahrzeug.getFahrzeugtyp());
    assertNull(fahrzeug.getFahrzeugTyp());
    assertNull(fahrzeug.getDinNorm());
    assertNull(fahrzeug.getDin14530());
    assertNull(fahrzeug.getMasseklasse());
    assertNull(fahrzeug.getZulassigeGesamtmasse());
    assertNull(fahrzeug.getBesatzung());
    assertEquals(0, fahrzeug.getBesatzungSollStaerke());
    assertNull(fahrzeug.getLoeschwasser());
    assertNull(fahrzeug.getSchaummittel());
    assertNull(fahrzeug.getPumpe());
    assertNull(fahrzeug.getAbmessungen());
    assertNull(fahrzeug.getFahrgestellKategorie());
    assertNull(fahrzeug.getAntrieb());
    assertNull(fahrzeug.getFuehrerschein());
    assertNull(fahrzeug.getFuehrerscheinklasse());

    fahrzeug.setId("fahrzeug-1");
    fahrzeug.setBezeichnung("TSF");
    fahrzeug.setKennung("HVL 5/47/2");
    fahrzeug.setFahrzeugtyp(Fahrzeugtyp.TSF);
    fahrzeug.setDinNorm("Teil 16");
    fahrzeug.setMasseklasse("L I");
    fahrzeug.setZulassigeGesamtmasse("3,0–4,75 t");
    fahrzeug.setBesatzung("Staffel");
    fahrzeug.setLoeschwasser("kein Tank");
    fahrzeug.setSchaummittel("keines");
    fahrzeug.setPumpe("PFPN 10-1000");
    fahrzeug.setAbmessungen(new Abmessungen(6.0, 2.3, 2.6));
    fahrzeug.setFahrgestellKategorie("Straße (Kat. 1)");
    fahrzeug.setAntrieb("Straße");
    fahrzeug.setFuehrerschein("C1");
    fahrzeug.setFuehrerscheinklasse("C1");

    assertEquals("fahrzeug-1", fahrzeug.getId());
    assertEquals("TSF", fahrzeug.getBezeichnung());
    assertEquals("HVL 5/47/2", fahrzeug.getKennung());
    assertEquals(Fahrzeugtyp.TSF, fahrzeug.getFahrzeugtyp());
    assertEquals(Fahrzeugtyp.TSF, fahrzeug.getFahrzeugTyp());
    assertEquals("Teil 16", fahrzeug.getDinNorm());
    assertEquals("DIN 14530 Teil 16", fahrzeug.getDin14530());
    assertEquals("L I", fahrzeug.getMasseklasse());
    assertEquals("3,0–4,75 t", fahrzeug.getZulassigeGesamtmasse());
    assertEquals("Staffel", fahrzeug.getBesatzung());
    assertEquals(6, fahrzeug.getBesatzungSollStaerke());
    assertEquals("kein Tank", fahrzeug.getLoeschwasser());
    assertEquals("keines", fahrzeug.getSchaummittel());
    assertEquals("PFPN 10-1000", fahrzeug.getPumpe());
    assertEquals(new Abmessungen(6.0, 2.3, 2.6), fahrzeug.getAbmessungen());
    assertEquals("Straße (Kat. 1)", fahrzeug.getFahrgestellKategorie());
    assertEquals("Straße", fahrzeug.getAntrieb());
    assertEquals("C1", fahrzeug.getFuehrerschein());
    assertEquals("C1", fahrzeug.getFuehrerscheinklasse());

    fahrzeug.setFahrzeugTyp(Fahrzeugtyp.HLF10);
    assertEquals(Fahrzeugtyp.HLF10, fahrzeug.getFahrzeugtyp());
  }

  @Test
  void testKonstruktorOhneIdGeneriertUUID() {
    Einsatzfahrzeug fahrzeug = new Einsatzfahrzeug("TSF", "HVL 5/47/2", Fahrzeugtyp.TSF);

    assertNotNull(fahrzeug.getId());
    assertEquals("TSF", fahrzeug.getBezeichnung());
    assertEquals("HVL 5/47/2", fahrzeug.getKennung());
    assertEquals(Fahrzeugtyp.TSF, fahrzeug.getFahrzeugtyp());
  }

  @Test
  void testKonstruktorMitId() {
    Einsatzfahrzeug fahrzeug = new Einsatzfahrzeug("f-123", "TLF 2000", "HVL 5/21/1", Fahrzeugtyp.TLF2000);

    assertEquals("f-123", fahrzeug.getId());
    assertEquals("TLF 2000", fahrzeug.getBezeichnung());
    assertEquals("HVL 5/21/1", fahrzeug.getKennung());
    assertEquals(Fahrzeugtyp.TLF2000, fahrzeug.getFahrzeugtyp());
  }

  @Test
  void testBuilderVollstaendig() {
    Einsatzfahrzeug fahrzeug = Einsatzfahrzeug.builder()
        .id("custom-id")
        .bezeichnung("HLF 10")
        .kennung("HVL 5/48/1")
        .fahrzeugtyp(Fahrzeugtyp.HLF10)
        .dinNorm("Teil 26")
        .masseklasse("M II")
        .zulassigeGesamtmasse("9,0–14,0 t")
        .besatzung("Gruppe")
        .loeschwasser("1.000 l")
        .schaummittel("120 l (6 × 20 l)")
        .pumpe("FPN 10-1000")
        .abmessungen(new Abmessungen(7.3, 2.5, 3.3))
        .fahrgestellKategorie("Gelände (Kat. 2)")
        .antrieb("Allrad")
        .fuehrerschein("C")
        .build();

    assertEquals("custom-id", fahrzeug.getId());
    assertEquals("HLF 10", fahrzeug.getBezeichnung());
    assertEquals("HVL 5/48/1", fahrzeug.getKennung());
    assertEquals(Fahrzeugtyp.HLF10, fahrzeug.getFahrzeugtyp());
    assertEquals("Teil 26", fahrzeug.getDinNorm());
    assertEquals("DIN 14530 Teil 26", fahrzeug.getDin14530());
    assertEquals("M II", fahrzeug.getMasseklasse());
    assertEquals("9,0–14,0 t", fahrzeug.getZulassigeGesamtmasse());
    assertEquals("Gruppe", fahrzeug.getBesatzung());
    assertEquals("1.000 l", fahrzeug.getLoeschwasser());
    assertEquals("120 l (6 × 20 l)", fahrzeug.getSchaummittel());
    assertEquals("FPN 10-1000", fahrzeug.getPumpe());
    assertEquals(new Abmessungen(7.3, 2.5, 3.3), fahrzeug.getAbmessungen());
    assertEquals("Gelände (Kat. 2)", fahrzeug.getFahrgestellKategorie());
    assertEquals("Allrad", fahrzeug.getAntrieb());
    assertEquals("C", fahrzeug.getFuehrerschein());
  }

  @Test
  void testBuilderMitDeutschenMethodenUndAutomatischerId() {
    Einsatzfahrzeug fahrzeug = Einsatzfahrzeug.builder()
        .mitId("id-42")
        .mitBezeichnung("LF 20")
        .mitKennung("HVL 5/42/1")
        .mitFahrzeugTyp(Fahrzeugtyp.LF20)
        .mitDinNorm("Teil 11")
        .mitDin14530("DIN 14530 Teil 11")
        .mitMasseklasse("M III")
        .mitZulassigeGesamtmasse("14,0–16,0 t")
        .mitGesamtmasse("14,0–16,0 t")
        .mitBesatzung("Gruppe")
        .mitLoeschwasser("2.000 l")
        .mitSchaummittel("120 l")
        .mitPumpe("FPN 10-2000")
        .mitAbmessungen(new Abmessungen(8.6, 2.5, 3.3))
        .mitFahrgestellKategorie("Straße (Kat. 1) oder Gelände (Kat. 2)")
        .mitAntrieb("Straße oder Allrad")
        .mitFuehrerschein("C")
        .mitFuehrerscheinklasse("C")
        .build();

    assertEquals("id-42", fahrzeug.getId());
    assertEquals("LF 20", fahrzeug.getBezeichnung());
    assertEquals("HVL 5/42/1", fahrzeug.getKennung());
    assertEquals(Fahrzeugtyp.LF20, fahrzeug.getFahrzeugtyp());
    assertEquals("DIN 14530 Teil 11", fahrzeug.getDinNorm());
    assertEquals("DIN 14530 Teil 11", fahrzeug.getDin14530());
    assertEquals("M III", fahrzeug.getMasseklasse());
    assertEquals("14,0–16,0 t", fahrzeug.getZulassigeGesamtmasse());
    assertEquals("Gruppe", fahrzeug.getBesatzung());
    assertEquals("2.000 l", fahrzeug.getLoeschwasser());
    assertEquals("120 l", fahrzeug.getSchaummittel());
    assertEquals("FPN 10-2000", fahrzeug.getPumpe());
    assertEquals(new Abmessungen(8.6, 2.5, 3.3), fahrzeug.getAbmessungen());
    assertEquals("Straße (Kat. 1) oder Gelände (Kat. 2)", fahrzeug.getFahrgestellKategorie());
    assertEquals("Straße oder Allrad", fahrzeug.getAntrieb());
    assertEquals("C", fahrzeug.getFuehrerschein());
    assertEquals("C", fahrzeug.getFuehrerscheinklasse());
  }

  @Test
  void testBuilderMitWeiterenAliasen() {
    Einsatzfahrzeug fahrzeug = Einsatzfahrzeug.builder()
        .fahrzeugTyp(Fahrzeugtyp.TLF2000)
        .din14530("Teil 18")
        .gesamtmasse("9,0–14,0 t")
        .abmessungen(6.3, 2.3, 3.1)
        .fuehrerscheinklasse("C")
        .build();

    assertEquals(Fahrzeugtyp.TLF2000, fahrzeug.getFahrzeugtyp());
    assertEquals("Teil 18", fahrzeug.getDinNorm());
    assertEquals("DIN 14530 Teil 18", fahrzeug.getDin14530());
    assertEquals("9,0–14,0 t", fahrzeug.getZulassigeGesamtmasse());
    assertEquals(new Abmessungen(6.3, 2.3, 3.1), fahrzeug.getAbmessungen());
    assertEquals("C", fahrzeug.getFuehrerscheinklasse());

    Einsatzfahrzeug fahrzeug2 = Einsatzfahrzeug.builder()
        .mitFahrzeugtyp(Fahrzeugtyp.TSF_W)
        .mitAbmessungen(6.3, 2.35, 2.9)
        .build();
    assertEquals(Fahrzeugtyp.TSF_W, fahrzeug2.getFahrzeugtyp());
    assertEquals(new Abmessungen(6.3, 2.35, 2.9), fahrzeug2.getAbmessungen());
  }

  @Test
  void testEqualsUndHashCode() {
    Einsatzfahrzeug f1 = new Einsatzfahrzeug("id-1", "TSF", "HVL 5/47/2", Fahrzeugtyp.TSF);
    Einsatzfahrzeug f2 = new Einsatzfahrzeug("id-1", "Anderer Name", "Andere Kennung", Fahrzeugtyp.HLF10);
    Einsatzfahrzeug f3 = new Einsatzfahrzeug("id-2", "TSF", "HVL 5/47/2", Fahrzeugtyp.TSF);

    assertEquals(f1, f2);
    assertTrue(f1.equals(f1));
    assertNotEquals(f1, f3);
    assertNotNull(f1);
    assertFalse(f1.equals(null));
    assertFalse(f1.equals("other-type"));
    assertEquals(f1.hashCode(), f2.hashCode());
  }

  @Test
  void testFahrzeugtypWerte() {
    assertEquals(Fahrzeugtyp.KLF, Fahrzeugtyp.valueOf("KLF"));
    assertEquals(Fahrzeugtyp.TSF, Fahrzeugtyp.valueOf("TSF"));
    assertEquals(Fahrzeugtyp.TSF_W, Fahrzeugtyp.valueOf("TSF_W"));
    assertEquals(Fahrzeugtyp.TSFW, Fahrzeugtyp.valueOf("TSFW"));
    assertEquals(Fahrzeugtyp.MLF, Fahrzeugtyp.valueOf("MLF"));
    assertEquals(Fahrzeugtyp.LF10, Fahrzeugtyp.valueOf("LF10"));
    assertEquals(Fahrzeugtyp.LF_10, Fahrzeugtyp.valueOf("LF_10"));
    assertEquals(Fahrzeugtyp.HLF10, Fahrzeugtyp.valueOf("HLF10"));
    assertEquals(Fahrzeugtyp.HLF_10, Fahrzeugtyp.valueOf("HLF_10"));
    assertEquals(Fahrzeugtyp.LF20, Fahrzeugtyp.valueOf("LF20"));
    assertEquals(Fahrzeugtyp.LF_20, Fahrzeugtyp.valueOf("LF_20"));
    assertEquals(Fahrzeugtyp.HLF20, Fahrzeugtyp.valueOf("HLF20"));
    assertEquals(Fahrzeugtyp.HLF_20, Fahrzeugtyp.valueOf("HLF_20"));
    assertEquals(Fahrzeugtyp.LF20_KATS, Fahrzeugtyp.valueOf("LF20_KATS"));
    assertEquals(Fahrzeugtyp.LF20KATS, Fahrzeugtyp.valueOf("LF20KATS"));
    assertEquals(Fahrzeugtyp.TLF2000, Fahrzeugtyp.valueOf("TLF2000"));
    assertEquals(Fahrzeugtyp.TLF_2000, Fahrzeugtyp.valueOf("TLF_2000"));
    assertEquals(Fahrzeugtyp.TLF3000, Fahrzeugtyp.valueOf("TLF3000"));
    assertEquals(Fahrzeugtyp.TLF_3000, Fahrzeugtyp.valueOf("TLF_3000"));
    assertEquals(Fahrzeugtyp.TLF4000, Fahrzeugtyp.valueOf("TLF4000"));
    assertEquals(Fahrzeugtyp.TLF_4000, Fahrzeugtyp.valueOf("TLF_4000"));
    assertEquals(Fahrzeugtyp.LF1000, Fahrzeugtyp.valueOf("LF1000"));
  }

  @Test
  void testFahrzeugAendern() {
    Einsatzfahrzeug fahrzeug = Einsatzfahrzeug.builder()
        .id("fz-1")
        .bezeichnung("Alte Bezeichnung")
        .kennung("Florian 1/48-1")
        .fahrzeugtyp(Fahrzeugtyp.TSF)
        .build();

    fahrzeug.fahrzeugAendern("HVL/5/47/1");
    assertEquals("HVL/5/47/1", fahrzeug.getKennung());

    fahrzeug.fahrzeugAendern("Neue Bezeichnung", "HVL/5/47/2", Fahrzeugtyp.TSF_W);
    assertEquals("Neue Bezeichnung", fahrzeug.getBezeichnung());
    assertEquals("HVL/5/47/2", fahrzeug.getKennung());
    assertEquals(Fahrzeugtyp.TSF_W, fahrzeug.getFahrzeugtyp());

    fahrzeug.aendern(null, null, null);
    assertEquals("Neue Bezeichnung", fahrzeug.getBezeichnung());
    assertEquals("HVL/5/47/2", fahrzeug.getKennung());
    assertEquals(Fahrzeugtyp.TSF_W, fahrzeug.getFahrzeugtyp());
  }
}
