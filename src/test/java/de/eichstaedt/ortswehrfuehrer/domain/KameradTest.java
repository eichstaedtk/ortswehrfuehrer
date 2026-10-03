package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class KameradTest {

  @Test
  void testNoArgsConstructorAndSetters() {
    Kamerad kamerad = new Kamerad();
    assertNull(kamerad.getId());
    assertNull(kamerad.getVorname());
    assertNull(kamerad.getNachname());

    kamerad.setId("custom-id-123");
    kamerad.setVorname("Max");
    kamerad.setNachname("Mustermann");

    assertEquals("custom-id-123", kamerad.getId());
    assertEquals("Max", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
  }

  @Test
  void testConstructorWithVornameAndNachname() {
    Kamerad kamerad = new Kamerad("Max", "Mustermann");

    assertNotNull(kamerad.getId());
    assertDoesNotThrow(() -> UUID.fromString(kamerad.getId()));
    assertEquals("Max", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
  }

  @Test
  void testAllArgsConstructor() {
    String id = UUID.randomUUID().toString();
    Kamerad kamerad = new Kamerad(id, "Erika", "Mustermann");

    assertEquals(id, kamerad.getId());
    assertEquals("Erika", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
  }
}
