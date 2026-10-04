package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AdresseTest {

  @Test
  void testRecordComponents() {
    Adresse adresse = new Adresse("Dorfstraße", "1", "14712", "Stechow");

    assertEquals("Dorfstraße", adresse.strasse());
    assertEquals("1", adresse.hausnummer());
    assertEquals("14712", adresse.postleitzahl());
    assertEquals("Stechow", adresse.ort());
  }

  @Test
  void testEqualsAndHashCode() {
    Adresse adresse1 = new Adresse("Hauptstraße", "12a", "14712", "Göttlin");
    Adresse adresse2 = new Adresse("Hauptstraße", "12a", "14712", "Göttlin");
    Adresse adresse3 = new Adresse("Dorfstraße", "1", "14712", "Stechow");

    assertEquals(adresse1, adresse2);
    assertEquals(adresse1.hashCode(), adresse2.hashCode());
    assertNotEquals(adresse1, adresse3);
    assertNotNull(adresse1);
    assertFalse(adresse1.equals(null));
    assertFalse(adresse1.equals(new Object()));
  }

  @Test
  void testToString() {
    Adresse adresse = new Adresse("Hauptstraße", "12a", "14712", "Göttlin");
    String toString = adresse.toString();

    assertNotNull(toString);
    assertTrue(toString.contains("Hauptstraße"));
    assertTrue(toString.contains("12a"));
    assertTrue(toString.contains("14712"));
    assertTrue(toString.contains("Göttlin"));
  }
}
