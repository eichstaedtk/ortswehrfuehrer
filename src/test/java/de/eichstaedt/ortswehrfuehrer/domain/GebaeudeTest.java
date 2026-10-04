package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class GebaeudeTest {

  @Test
  void testNoArgsConstructorAndSetters() {
    Gebaeude gebaeude = new Gebaeude();
    assertNull(gebaeude.getId());
    assertNull(gebaeude.getBezeichnung());
    assertNull(gebaeude.getAdresse());

    Adresse adresse = new Adresse("Hauptstraße", "12a", "14712", "Göttlin");
    gebaeude.setId("custom-id-123");
    gebaeude.setBezeichnung("Gerätehaus");
    gebaeude.setAdresse(adresse);

    assertEquals("custom-id-123", gebaeude.getId());
    assertEquals("Gerätehaus", gebaeude.getBezeichnung());
    assertEquals(adresse, gebaeude.getAdresse());
  }

  @Test
  void testConstructorWithBezeichnungAndAdresseGeneratesId() {
    Adresse adresse = new Adresse("Hauptstraße", "12a", "14712", "Göttlin");
    Gebaeude gebaeude = new Gebaeude("Gerätehaus Göttlin", adresse);

    assertNotNull(gebaeude.getId());
    assertDoesNotThrow(() -> UUID.fromString(gebaeude.getId()));
    assertEquals("Gerätehaus Göttlin", gebaeude.getBezeichnung());
    assertEquals(adresse, gebaeude.getAdresse());
  }

  @Test
  void testConstructorWithIdBezeichnungAndAdresse() {
    Adresse adresse = new Adresse("Dorfstraße", "1", "14712", "Stechow");
    Gebaeude gebaeude = new Gebaeude("id-999", "Feuerwehrhaus Stechow", adresse);

    assertEquals("id-999", gebaeude.getId());
    assertEquals("Feuerwehrhaus Stechow", gebaeude.getBezeichnung());
    assertEquals(adresse, gebaeude.getAdresse());
  }

  @Test
  void testEqualsUndHashCode() {
    Adresse adresse = new Adresse("Hauptstraße", "12a", "14712", "Göttlin");
    Gebaeude g1 = new Gebaeude("g-1", "Gerätehaus 1", adresse);
    Gebaeude g2 = new Gebaeude("g-1", "Gerätehaus 2", adresse);
    Gebaeude g3 = new Gebaeude("g-2", "Gerätehaus 1", adresse);

    assertEquals(g1, g2);
    assertTrue(g1.equals(g1));
    assertEquals(g1.hashCode(), g2.hashCode());
    assertFalse(g1.equals(g3));
    assertNotNull(g1);
    assertFalse(g1.equals(null));
    assertFalse(g1.equals("other"));
  }
}
