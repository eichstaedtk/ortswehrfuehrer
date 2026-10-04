package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class AbmessungenTest {

  @Test
  void testAbmessungenInstanziierungUndGetter() {
    Abmessungen abmessungen = new Abmessungen(7.3, 2.5, 3.3);

    assertEquals(7.3, abmessungen.laengeMax());
    assertEquals(2.5, abmessungen.breiteMax());
    assertEquals(3.3, abmessungen.hoeheMax());
    assertEquals("7,3 × 2,5 × 3,3", abmessungen.formatiert());
    assertEquals("7,3 × 2,5 × 3,3", abmessungen.toString());
  }

  @Test
  void testAbmessungenMitDezimalstellen() {
    Abmessungen abmessungen = new Abmessungen(6.3, 2.35, 2.9);
    assertEquals("6,3 × 2,35 × 2,9", abmessungen.formatiert());

    Abmessungen tlf4000 = new Abmessungen(10.0, 2.55, 3.3);
    assertEquals("10,0 × 2,55 × 3,3", tlf4000.formatiert());
  }

  @Test
  void testAbmessungenNullWerte() {
    Abmessungen abmessungen = new Abmessungen(null, null, null);
    assertEquals("n. v.", abmessungen.formatiert());
    assertEquals("n. v.", abmessungen.toString());

    Abmessungen teilNull = new Abmessungen(6.0, null, 2.6);
    assertEquals("n. v.", teilNull.formatiert());
  }

  @Test
  void testEqualsUndHashCode() {
    Abmessungen a1 = new Abmessungen(6.0, 2.3, 2.6);
    Abmessungen a2 = new Abmessungen(6.0, 2.3, 2.6);
    Abmessungen a3 = new Abmessungen(7.3, 2.5, 3.3);

    assertEquals(a1, a2);
    assertEquals(a1.hashCode(), a2.hashCode());
    assertNotEquals(a1, a3);
    assertNotNull(a1);
    assertFalse(a1.equals(null));
  }
}
