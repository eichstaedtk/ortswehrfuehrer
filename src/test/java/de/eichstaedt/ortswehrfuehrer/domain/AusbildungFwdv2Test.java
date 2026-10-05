package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class AusbildungFwdv2Test {

  @Test
  void testTruppmannTeil1Attribute() {
    Ausbildung ausbildung = Ausbildung.TRUPPMANN_TEIL_1;
    assertEquals("2.1.1", ausbildung.getZiffer());
    assertEquals("Truppmann Teil 1 (Grundausbildungslehrgang)", ausbildung.getBezeichnung());
    assertEquals(70, ausbildung.getMindeststunden());
    assertEquals(Ausbildungskategorie.TRUPPAUSBILDUNG, ausbildung.getKategorie());
    assertEquals("FwDV 2 2.1.1", ausbildung.getFwdv2());
  }

  @Test
  void testTruppmannTeil2Attribute() {
    Ausbildung ausbildung = Ausbildung.TRUPPMANN_TEIL_2;
    assertEquals("2.1.2", ausbildung.getZiffer());
    assertEquals("Truppmann Teil 2", ausbildung.getBezeichnung());
    assertEquals(80, ausbildung.getMindeststunden());
    assertEquals(Ausbildungskategorie.TRUPPAUSBILDUNG, ausbildung.getKategorie());
    assertEquals("FwDV 2 2.1.2", ausbildung.getFwdv2());
  }

  @Test
  void testTruppfuehrerAttribute() {
    Ausbildung ausbildung = Ausbildung.TRUPPFUEHRER;
    assertEquals("2.2", ausbildung.getZiffer());
    assertEquals("Truppführer", ausbildung.getBezeichnung());
    assertEquals(35, ausbildung.getMindeststunden());
    assertEquals(Ausbildungskategorie.TRUPPAUSBILDUNG, ausbildung.getKategorie());
    assertEquals("FwDV 2 2.2", ausbildung.getFwdv2());
  }

  @Test
  void testTechnischeAusbildungAttribute() {
    assertEquals("3.1", Ausbildung.SPRECHFUNKER.getZiffer());
    assertEquals(16, Ausbildung.SPRECHFUNKER.getMindeststunden());
    assertEquals(Ausbildungskategorie.TECHNISCHE_AUSBILDUNG, Ausbildung.SPRECHFUNKER.getKategorie());

    assertEquals("3.2", Ausbildung.ATEMSCHUTZGERAETETRAEGER.getZiffer());
    assertEquals(25, Ausbildung.ATEMSCHUTZGERAETETRAEGER.getMindeststunden());
    assertEquals(Ausbildungskategorie.TECHNISCHE_AUSBILDUNG, Ausbildung.ATEMSCHUTZGERAETETRAEGER.getKategorie());

    assertEquals("3.3", Ausbildung.MASCHINIST.getZiffer());
    assertEquals(35, Ausbildung.MASCHINIST.getMindeststunden());
    assertEquals(Ausbildungskategorie.TECHNISCHE_AUSBILDUNG, Ausbildung.MASCHINIST.getKategorie());

    assertEquals("3.4", Ausbildung.TECHNISCHE_HILFELEISTUNG.getZiffer());
    assertEquals(35, Ausbildung.TECHNISCHE_HILFELEISTUNG.getMindeststunden());
    assertEquals(Ausbildungskategorie.TECHNISCHE_AUSBILDUNG, Ausbildung.TECHNISCHE_HILFELEISTUNG.getKategorie());

    assertEquals("3.5", Ausbildung.ABC_EINSATZ.getZiffer());
    assertEquals(70, Ausbildung.ABC_EINSATZ.getMindeststunden());
    assertEquals(Ausbildungskategorie.TECHNISCHE_AUSBILDUNG, Ausbildung.ABC_EINSATZ.getKategorie());

    assertEquals("3.6", Ausbildung.ABC_ERKUNDUNG.getZiffer());
    assertEquals(35, Ausbildung.ABC_ERKUNDUNG.getMindeststunden());
    assertEquals(Ausbildungskategorie.TECHNISCHE_AUSBILDUNG, Ausbildung.ABC_ERKUNDUNG.getKategorie());

    assertEquals("3.7", Ausbildung.ABC_DEKONTAMINATION_P_G.getZiffer());
    assertEquals(28, Ausbildung.ABC_DEKONTAMINATION_P_G.getMindeststunden());
    assertEquals(Ausbildungskategorie.TECHNISCHE_AUSBILDUNG, Ausbildung.ABC_DEKONTAMINATION_P_G.getKategorie());

    assertEquals("3.8", Ausbildung.GERAETEWART.getZiffer());
    assertEquals(35, Ausbildung.GERAETEWART.getMindeststunden());
    assertEquals(Ausbildungskategorie.TECHNISCHE_AUSBILDUNG, Ausbildung.GERAETEWART.getKategorie());

    assertEquals("3.9", Ausbildung.ATEMSCHUTZGERAETEWART.getZiffer());
    assertEquals(35, Ausbildung.ATEMSCHUTZGERAETEWART.getMindeststunden());
    assertEquals(Ausbildungskategorie.TECHNISCHE_AUSBILDUNG, Ausbildung.ATEMSCHUTZGERAETEWART.getKategorie());
  }

  @Test
  void testFuehrungsausbildungAttribute() {
    assertEquals("4.1", Ausbildung.GRUPPENFUEHRER.getZiffer());
    assertEquals(70, Ausbildung.GRUPPENFUEHRER.getMindeststunden());
    assertEquals(Ausbildungskategorie.FUEHRUNGSAUSBILDUNG, Ausbildung.GRUPPENFUEHRER.getKategorie());

    assertEquals("4.2", Ausbildung.ZUGFUEHRER.getZiffer());
    assertEquals(70, Ausbildung.ZUGFUEHRER.getMindeststunden());
    assertEquals(Ausbildungskategorie.FUEHRUNGSAUSBILDUNG, Ausbildung.ZUGFUEHRER.getKategorie());

    assertEquals("4.3", Ausbildung.VERBANDSFUEHRER.getZiffer());
    assertEquals(35, Ausbildung.VERBANDSFUEHRER.getMindeststunden());
    assertEquals(Ausbildungskategorie.FUEHRUNGSAUSBILDUNG, Ausbildung.VERBANDSFUEHRER.getKategorie());

    assertEquals("4.4", Ausbildung.EINFUEHRUNG_IN_DIE_STABSARBEIT.getZiffer());
    assertEquals(35, Ausbildung.EINFUEHRUNG_IN_DIE_STABSARBEIT.getMindeststunden());
    assertEquals(Ausbildungskategorie.FUEHRUNGSAUSBILDUNG, Ausbildung.EINFUEHRUNG_IN_DIE_STABSARBEIT.getKategorie());

    assertEquals("4.5", Ausbildung.FUEHREN_IM_ABC_EINSATZ.getZiffer());
    assertEquals(35, Ausbildung.FUEHREN_IM_ABC_EINSATZ.getMindeststunden());
    assertEquals(Ausbildungskategorie.FUEHRUNGSAUSBILDUNG, Ausbildung.FUEHREN_IM_ABC_EINSATZ.getKategorie());

    assertEquals("4.6", Ausbildung.LEITER_EINER_FEUERWEHR.getZiffer());
    assertEquals(35, Ausbildung.LEITER_EINER_FEUERWEHR.getMindeststunden());
    assertEquals(Ausbildungskategorie.FUEHRUNGSAUSBILDUNG, Ausbildung.LEITER_EINER_FEUERWEHR.getKategorie());

    assertEquals("4.7", Ausbildung.AUSBILDER_IN_DER_FEUERWEHR.getZiffer());
    assertEquals(35, Ausbildung.AUSBILDER_IN_DER_FEUERWEHR.getMindeststunden());
    assertEquals(Ausbildungskategorie.FUEHRUNGSAUSBILDUNG, Ausbildung.AUSBILDER_IN_DER_FEUERWEHR.getKategorie());
  }

  @Test
  void testFortbildungAttribute() {
    assertEquals("5", Ausbildung.FORTBILDUNG.getZiffer());
    assertEquals("Fortbildung", Ausbildung.FORTBILDUNG.getBezeichnung());
    assertEquals(0, Ausbildung.FORTBILDUNG.getMindeststunden());
    assertEquals(Ausbildungskategorie.FORTBILDUNG, Ausbildung.FORTBILDUNG.getKategorie());
  }

  @ParameterizedTest
  @EnumSource(Ausbildung.class)
  void testAllAusbildungenHaveNonBlankFields(Ausbildung ausbildung) {
    assertNotNull(ausbildung.getZiffer());
    assertFalse(ausbildung.getZiffer().isBlank());
    assertNotNull(ausbildung.getBezeichnung());
    assertFalse(ausbildung.getBezeichnung().isBlank());
    assertNotNull(ausbildung.getKategorie());
    assertTrue(ausbildung.getMindeststunden() >= 0);
    assertTrue(ausbildung.getFwdv2().startsWith("FwDV 2 "));
  }

  @Test
  void testFindeNachZiffer() {
    Optional<Ausbildung> result = Ausbildung.findeNachZiffer("3.2");
    assertTrue(result.isPresent());
    assertEquals(Ausbildung.ATEMSCHUTZGERAETETRAEGER, result.get());

    assertTrue(Ausbildung.findeNachZiffer("99.9").isEmpty());
    assertTrue(Ausbildung.findeNachZiffer(null).isEmpty());
    assertTrue(Ausbildung.findeNachZiffer("  ").isEmpty());
  }

  @Test
  void testFindeNachBezeichnung() {
    Optional<Ausbildung> result = Ausbildung.findeNachBezeichnung("Maschinist");
    assertTrue(result.isPresent());
    assertEquals(Ausbildung.MASCHINIST, result.get());

    assertTrue(Ausbildung.findeNachBezeichnung("Unbekannt").isEmpty());
    assertTrue(Ausbildung.findeNachBezeichnung(null).isEmpty());
  }

  @Test
  void testVon() {
    assertEquals(Optional.of(Ausbildung.GRUPPENFUEHRER), Ausbildung.von("GRUPPENFUEHRER"));
    assertEquals(Optional.of(Ausbildung.GRUPPENFUEHRER), Ausbildung.von("4.1"));
    assertEquals(Optional.of(Ausbildung.GRUPPENFUEHRER), Ausbildung.von("Gruppenführer"));
    assertEquals(Optional.empty(), Ausbildung.von("Ungueltig"));
    assertEquals(Optional.empty(), Ausbildung.von(null));
    assertEquals(Optional.empty(), Ausbildung.von(""));
  }

  @Test
  void testAusbildungenFuerKategorie() {
    List<Ausbildung> truppausbildungen = Ausbildung.ausbildungenFuerKategorie(Ausbildungskategorie.TRUPPAUSBILDUNG);
    assertEquals(3, truppausbildungen.size());
    assertTrue(truppausbildungen.contains(Ausbildung.TRUPPMANN_TEIL_1));
    assertTrue(truppausbildungen.contains(Ausbildung.TRUPPMANN_TEIL_2));
    assertTrue(truppausbildungen.contains(Ausbildung.TRUPPFUEHRER));

    List<Ausbildung> technische = Ausbildung.ausbildungenFuerKategorie(Ausbildungskategorie.TECHNISCHE_AUSBILDUNG);
    assertEquals(9, technische.size());

    List<Ausbildung> fuehrung = Ausbildung.ausbildungenFuerKategorie(Ausbildungskategorie.FUEHRUNGSAUSBILDUNG);
    assertEquals(7, fuehrung.size());

    List<Ausbildung> fortbildung = Ausbildung.ausbildungenFuerKategorie(Ausbildungskategorie.FORTBILDUNG);
    assertEquals(1, fortbildung.size());
    assertEquals(List.of(Ausbildung.FORTBILDUNG), fortbildung);

    assertTrue(Ausbildung.ausbildungenFuerKategorie(null).isEmpty());
  }
}
