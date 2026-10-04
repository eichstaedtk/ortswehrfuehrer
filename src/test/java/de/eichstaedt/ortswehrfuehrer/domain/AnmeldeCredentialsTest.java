package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AnmeldeCredentialsTest {

    @Test
    void testIstGueltigBeiGueltigenDaten() {
        AnmeldeCredentials creds = new AnmeldeCredentials("benutzer", "geheim123");
        assertTrue(creds.istGueltig());
        assertEquals("benutzer", creds.benutzername());
        assertEquals("geheim123", creds.passwort());
    }

    @Test
    void testIstGueltigBeiUngueltigenDaten() {
        assertFalse(new AnmeldeCredentials(null, "pass").istGueltig());
        assertFalse(new AnmeldeCredentials("", "pass").istGueltig());
        assertFalse(new AnmeldeCredentials("  ", "pass").istGueltig());
        assertFalse(new AnmeldeCredentials("user", null).istGueltig());
        assertFalse(new AnmeldeCredentials("user", "").istGueltig());
        assertFalse(new AnmeldeCredentials("user", "   ").istGueltig());
    }

    @Test
    void testEqualsAndHashCode() {
        AnmeldeCredentials c1 = new AnmeldeCredentials("u1", "p1");
        AnmeldeCredentials c2 = new AnmeldeCredentials("u1", "p1");
        AnmeldeCredentials c3 = new AnmeldeCredentials("u2", "p2");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
    }
}
