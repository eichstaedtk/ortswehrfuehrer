package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class KameradTest {

  @Test
  void testNoArgsConstructorAndSetters() {
    Kamerad kamerad = new Kamerad();
    assertNull(kamerad.getId());
    assertNull(kamerad.getVorname());
    assertNull(kamerad.getNachname());
    assertNull(kamerad.getGeburtsdatum());
    assertNull(kamerad.getAdresse());

    LocalDate geburtsdatum = LocalDate.of(1990, 8, 15);
    Adresse adresse = new Adresse("Dorfstraße", "12", "14712", "Göttlin");

    kamerad.setId("custom-id-123");
    kamerad.setVorname("Max");
    kamerad.setNachname("Mustermann");
    kamerad.setGeburtsdatum(geburtsdatum);
    kamerad.setAdresse(adresse);

    assertEquals("custom-id-123", kamerad.getId());
    assertEquals("Max", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
    assertEquals(geburtsdatum, kamerad.getGeburtsdatum());
    assertEquals(adresse, kamerad.getAdresse());
  }

  @Test
  void testBuilderWithVornameAndNachname() {
    Kamerad kamerad = Kamerad.builder()
        .vorname("Max")
        .nachname("Mustermann")
        .build();

    assertNotNull(kamerad.getId());
    assertDoesNotThrow(() -> UUID.fromString(kamerad.getId()));
    assertEquals("Max", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
    assertNull(kamerad.getGeburtsdatum());
    assertNull(kamerad.getAdresse());
  }

  @Test
  void testBuilderWithAllFields() {
    String id = UUID.randomUUID().toString();
    LocalDate geburtsdatum = LocalDate.of(1992, 11, 4);
    Adresse adresse = new Adresse("Lindenweg", "7", "14712", "Göttlin");

    Kamerad kamerad = Kamerad.builder()
        .id(id)
        .vorname("Erika")
        .nachname("Musterfrau")
        .geburtsdatum(geburtsdatum)
        .adresse(adresse)
        .build();

    assertEquals(id, kamerad.getId());
    assertEquals("Erika", kamerad.getVorname());
    assertEquals("Musterfrau", kamerad.getNachname());
    assertEquals(geburtsdatum, kamerad.getGeburtsdatum());
    assertEquals(adresse, kamerad.getAdresse());
  }

  @Test
  void testBuilderWithGermanMethodAliases() {
    String id = UUID.randomUUID().toString();
    LocalDate geburtsdatum = LocalDate.of(1985, 3, 20);
    Adresse adresse = new Adresse("Hauptstraße", "5", "14712", "Stechow");

    Kamerad kamerad = Kamerad.builder()
        .mitId(id)
        .mitVorname("Max")
        .mitNachname("Mustermann")
        .mitGeburtsdatum(geburtsdatum)
        .mitAdresse(adresse)
        .build();

    assertEquals(id, kamerad.getId());
    assertEquals("Max", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
    assertEquals(geburtsdatum, kamerad.getGeburtsdatum());
    assertEquals(adresse, kamerad.getAdresse());
  }

  @Test
  void testBuilderGeneratesRandomIdWhenNotSet() {
    LocalDate geburtsdatum = LocalDate.of(1985, 3, 20);
    Adresse adresse = new Adresse("Hauptstraße", "5", "14712", "Stechow");
    Kamerad kamerad = Kamerad.builder()
        .vorname("Max")
        .nachname("Mustermann")
        .geburtsdatum(geburtsdatum)
        .adresse(adresse)
        .build();

    assertNotNull(kamerad.getId());
    assertDoesNotThrow(() -> UUID.fromString(kamerad.getId()));
    assertEquals("Max", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
    assertEquals(geburtsdatum, kamerad.getGeburtsdatum());
    assertEquals(adresse, kamerad.getAdresse());
  }
}
