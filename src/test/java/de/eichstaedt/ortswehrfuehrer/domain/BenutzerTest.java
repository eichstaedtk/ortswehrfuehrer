package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

class BenutzerTest {

    @Test
    void testBenutzerErstellungMitGueltigenDaten() {
        Benutzer benutzer = new Benutzer("owf", "Ortswehrführer Meier", Set.of(Rolle.ORTSWEHRFUEHRER));

        assertEquals("owf", benutzer.benutzername());
        assertEquals("Ortswehrführer Meier", benutzer.anzeigeName());
        assertTrue(benutzer.rollen().contains(Rolle.ORTSWEHRFUEHRER));
    }

    @Test
    void testBenutzerErstellungMitLeeremAnzeigeNameNutztBenutzername() {
        Benutzer benutzer = new Benutzer("admin", null, Set.of(Rolle.ADMINISTRATOR));

        assertEquals("admin", benutzer.benutzername());
        assertEquals("admin", benutzer.anzeigeName());
        assertTrue(benutzer.rollen().contains(Rolle.ADMINISTRATOR));
    }

    @Test
    void testBenutzerErstellungMitNullRollenErgibtLeeresSet() {
        Benutzer benutzer = new Benutzer("kamerad1", "Kamerad Müller", null);

        assertNotNull(benutzer.rollen());
        assertTrue(benutzer.rollen().isEmpty());
    }

    @Test
    void testBenutzerErstellungMitUngueltigemBenutzernameWirftException() {
        assertThrows(IllegalArgumentException.class, () -> new Benutzer(null, "Test", Set.of()));
        assertThrows(IllegalArgumentException.class, () -> new Benutzer("", "Test", Set.of()));
        assertThrows(IllegalArgumentException.class, () -> new Benutzer("   ", "Test", Set.of()));
    }

    @Test
    void testEqualsAndHashCode() {
        Benutzer b1 = new Benutzer("owf", "Meier", Set.of(Rolle.ORTSWEHRFUEHRER));
        Benutzer b2 = new Benutzer("owf", "Meier", Set.of(Rolle.ORTSWEHRFUEHRER));
        Benutzer b3 = new Benutzer("kamerad", "Schulze", Set.of(Rolle.KAMERAD));

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
        assertNotEquals(b1, b3);
        assertNotEquals(b1, null);
    }
}
