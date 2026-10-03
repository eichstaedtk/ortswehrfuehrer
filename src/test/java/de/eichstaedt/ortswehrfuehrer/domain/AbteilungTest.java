package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AbteilungTest {

  @Test
  void testNoArgsConstructorAndSetters() {
    Abteilung abteilung = new Abteilung();
    assertNotNull(abteilung.getId());
    assertDoesNotThrow(() -> UUID.fromString(abteilung.getId()));
    assertNull(abteilung.getBezeichnung());
    assertNotNull(abteilung.getKameraden());
    assertTrue(abteilung.getKameraden().isEmpty());

    abteilung.setId("custom-abteilung-id");
    abteilung.setBezeichnung("Einsatzabteilung");

    assertEquals("custom-abteilung-id", abteilung.getId());
    assertEquals("Einsatzabteilung", abteilung.getBezeichnung());
  }

  @Test
  void testConstructorWithBezeichnungGeneratesId() {
    Abteilung abteilung = new Abteilung("Jugendabteilung");

    assertNotNull(abteilung.getId());
    assertDoesNotThrow(() -> UUID.fromString(abteilung.getId()));
    assertEquals("Jugendabteilung", abteilung.getBezeichnung());
    assertNotNull(abteilung.getKameraden());
    assertTrue(abteilung.getKameraden().isEmpty());
  }

  @Test
  void testConstructorWithIdAndBezeichnung() {
    Abteilung abteilung = new Abteilung("custom-id", "Alters- und Ehrenabteilung");

    assertEquals("custom-id", abteilung.getId());
    assertEquals("Alters- und Ehrenabteilung", abteilung.getBezeichnung());
  }

  @Test
  void testKameradHinzufuegen() {
    Abteilung abteilung = new Abteilung("Einsatzabteilung");
    Kamerad kamerad = Kamerad.builder()
        .vorname("Max")
        .nachname("Mustermann")
        .build();

    Abteilung result = abteilung.kameradHinzufuegen(kamerad);

    assertSame(abteilung, result);
    assertEquals(1, abteilung.getKameraden().size());
    assertEquals(kamerad, abteilung.getKameraden().get(0));
  }

  @Test
  void testSetKameraden() {
    Abteilung abteilung = new Abteilung("Einsatzabteilung");
    Kamerad kamerad1 = Kamerad.builder().vorname("Max").nachname("Mustermann").build();
    Kamerad kamerad2 = Kamerad.builder().vorname("Erika").nachname("Musterfrau").build();

    abteilung.setKameraden(List.of(kamerad1, kamerad2));

    assertEquals(2, abteilung.getKameraden().size());
    assertEquals(kamerad1, abteilung.getKameraden().get(0));
    assertEquals(kamerad2, abteilung.getKameraden().get(1));
  }
}
