package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
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
    assertTrue(abteilung.getKameraden().contains(kamerad));
  }

  @Test
  void testSetKameraden() {
    Abteilung abteilung = new Abteilung("Einsatzabteilung");
    Kamerad kamerad1 = Kamerad.builder().vorname("Max").nachname("Mustermann").build();
    Kamerad kamerad2 = Kamerad.builder().vorname("Erika").nachname("Musterfrau").build();

    abteilung.setKameraden(Set.of(kamerad1, kamerad2));

    assertEquals(2, abteilung.getKameraden().size());
    assertTrue(abteilung.getKameraden().contains(kamerad1));
    assertTrue(abteilung.getKameraden().contains(kamerad2));
  }

  @Test
  void testKeineDoppeltenKameradenInAbteilung() {
    Abteilung abteilung = new Abteilung("Einsatzabteilung");
    Kamerad kamerad = Kamerad.builder()
        .id("kamerad-1")
        .vorname("Max")
        .nachname("Mustermann")
        .build();

    abteilung.kameradHinzufuegen(kamerad);
    abteilung.kameradHinzufuegen(kamerad);

    assertEquals(1, abteilung.getKameraden().size());
    assertTrue(abteilung.getKameraden().contains(kamerad));
  }

  @Test
  void testKameradEntfernenMitInstanzUndId() {
    Abteilung abteilung = new Abteilung("Einsatzabteilung");
    Kamerad k1 = Kamerad.builder().id("kam-1").vorname("Max").nachname("Mustermann").build();
    Kamerad k2 = Kamerad.builder().id("kam-2").vorname("Erika").nachname("Musterfrau").build();

    abteilung.kameradHinzufuegen(k1);
    abteilung.kameradHinzufuegen(k2);
    assertEquals(2, abteilung.getKameraden().size());

    // Entfernen mit Instanz
    Abteilung res1 = abteilung.kameradEntfernen(k1);
    assertSame(abteilung, res1);
    assertEquals(1, abteilung.getKameraden().size());
    assertFalse(abteilung.getKameraden().contains(k1));
    assertTrue(abteilung.getKameraden().contains(k2));

    // Entfernen mit ID
    Abteilung res2 = abteilung.kameradEntfernen("kam-2");
    assertSame(abteilung, res2);
    assertTrue(abteilung.getKameraden().isEmpty());

    // Null- / Blank-Handling
    assertDoesNotThrow(() -> abteilung.kameradEntfernen((Kamerad) null));
    assertDoesNotThrow(() -> abteilung.kameradEntfernen((String) null));
    assertDoesNotThrow(() -> abteilung.kameradEntfernen("   "));
  }

  @Test
  void testEqualsUndHashCode() {
    Abteilung a1 = new Abteilung("abt-1", "Einsatzabteilung");
    Abteilung a2 = new Abteilung("abt-1", "Jugendabteilung");
    Abteilung a3 = new Abteilung("abt-2", "Einsatzabteilung");

    assertEquals(a1, a2);
    assertTrue(a1.equals(a1));
    assertEquals(a1.hashCode(), a2.hashCode());
    assertFalse(a1.equals(a3));
    assertNotNull(a1);
    assertFalse(a1.equals(null));
    assertFalse(a1.equals("other"));
  }
}
