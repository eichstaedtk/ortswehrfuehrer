package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WehrFactoryTest {

  private WehrFactory factory;

  @BeforeEach
  void setUp() {
    factory = new WehrFactory();
  }

  @Test
  void testErzeugeNeueWehr() {
    Wehr wehr = factory.erzeugeNeueWehr();

    assertNotNull(wehr);
    assertNotNull(wehr.getId());
    assertDoesNotThrow(() -> UUID.fromString(wehr.getId()));
    assertNull(wehr.getName());
    assertNull(wehr.getGruendungsdatum());
    assertNotNull(wehr.getGebaeude());
    assertTrue(wehr.getGebaeude().isEmpty());
    assertNotNull(wehr.getFahrzeugIds());
    assertTrue(wehr.getFahrzeugIds().isEmpty());
    assertNotNull(wehr.getKameraden());
    assertTrue(wehr.getKameraden().isEmpty());
  }

  @Test
  void testErzeugeWehrMitName() {
    String name = "Freiwillige Feuerwehr Göttlin";
    Wehr wehr = factory.erzeugeWehr(name);

    assertNotNull(wehr);
    assertNotNull(wehr.getId());
    assertDoesNotThrow(() -> UUID.fromString(wehr.getId()));
    assertEquals(name, wehr.getName());
    assertNull(wehr.getGruendungsdatum());
  }

  @Test
  void testErzeugeWehrMitNameUndGruendungsdatum() {
    String name = "Freiwillige Feuerwehr Göttlin";
    LocalDate gruendung = LocalDate.of(1924, 5, 1);
    Wehr wehr = factory.erzeugeWehr(name, gruendung);

    assertNotNull(wehr);
    assertNotNull(wehr.getId());
    assertDoesNotThrow(() -> UUID.fromString(wehr.getId()));
    assertEquals(name, wehr.getName());
    assertEquals(gruendung, wehr.getGruendungsdatum());
  }

  @Test
  void testRekonstruiereWehrMitId() {
    String id = "wehr-12345";
    String name = "Freiwillige Feuerwehr Rathenow";
    LocalDate gruendung = LocalDate.of(1880, 10, 15);

    Wehr wehr = factory.rekonstruiereWehr(id, name, gruendung);

    assertNotNull(wehr);
    assertEquals(id, wehr.getId());
    assertEquals(name, wehr.getName());
    assertEquals(gruendung, wehr.getGruendungsdatum());
  }

  @Test
  void testRekonstruiereWehrMitNullOderLeererIdGeneriertNeueId() {
    Wehr wehrNullId = factory.rekonstruiereWehr(null, "Feuerwehr A", null);
    assertNotNull(wehrNullId.getId());
    assertDoesNotThrow(() -> UUID.fromString(wehrNullId.getId()));

    Wehr wehrBlankId = factory.rekonstruiereWehr("   ", "Feuerwehr B", null);
    assertNotNull(wehrBlankId.getId());
    assertDoesNotThrow(() -> UUID.fromString(wehrBlankId.getId()));
  }

  @Test
  void testErzeugeWehrMitVollstaendigerAusstattung() {
    String name = "Freiwillige Feuerwehr Göttlin";
    LocalDate gruendung = LocalDate.of(1924, 5, 1);

    Gebaeude geraetehaus = new Gebaeude("Gerätehaus Göttlin",
        new Adresse("Hauptstraße", "12a", "14712", "Göttlin"));

    String fahrzeugId1 = "fz-tsf-01";
    String fahrzeugId2 = "fz-tlf-02";

    Kamerad kamerad = Kamerad.builder()
        .vorname("Max")
        .nachname("Mustermann")
        .geburtsdatum(LocalDate.of(1990, 5, 20))
        .build();

    Wehr wehr = factory.erzeugeWehr(
        name,
        gruendung,
        List.of(geraetehaus),
        Set.of(fahrzeugId1, fahrzeugId2),
        List.of(kamerad)
    );

    assertNotNull(wehr);
    assertEquals(name, wehr.getName());
    assertEquals(gruendung, wehr.getGruendungsdatum());
    assertEquals(1, wehr.getGebaeude().size());
    assertEquals(geraetehaus, wehr.getGebaeude().get(0));
    assertEquals(2, wehr.getFahrzeugIds().size());
    assertTrue(wehr.getFahrzeugIds().contains(fahrzeugId1));
    assertTrue(wehr.getFahrzeugIds().contains(fahrzeugId2));
    assertEquals(1, wehr.getKameraden().size());
    assertTrue(wehr.getKameraden().contains(kamerad));
  }

  @Test
  void testErzeugeWehrMitNullKollektionen() {
    Wehr wehr = factory.erzeugeWehr(
        "Feuerwehr Test",
        LocalDate.of(2000, 1, 1),
        null,
        null,
        null
    );

    assertNotNull(wehr);
    assertEquals("Feuerwehr Test", wehr.getName());
    assertTrue(wehr.getGebaeude().isEmpty());
    assertTrue(wehr.getFahrzeugIds().isEmpty());
    assertTrue(wehr.getKameraden().isEmpty());
  }
}
