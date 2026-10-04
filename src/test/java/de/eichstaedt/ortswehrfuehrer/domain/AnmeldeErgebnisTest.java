package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

class AnmeldeErgebnisTest {

    @Test
    void testErfolgreichesErgebnis() {
        Benutzer benutzer = new Benutzer("owf", "Ortswehrführer", Set.of(Rolle.ORTSWEHRFUEHRER));
        AnmeldeErgebnis ergebnis = AnmeldeErgebnis.erfolgreich(benutzer);

        assertTrue(ergebnis.erfolgreich());
        assertTrue(ergebnis.serviceVerfuegbar());
        assertNull(ergebnis.fehlermeldung());
        assertNotNull(ergebnis.benutzer());
        assertEquals("owf", ergebnis.benutzer().benutzername());
    }

    @Test
    void testFehlgeschlagenesErgebnis() {
        AnmeldeErgebnis ergebnis = AnmeldeErgebnis.fehlgeschlagen("Ungültige Anmeldedaten");

        assertFalse(ergebnis.erfolgreich());
        assertTrue(ergebnis.serviceVerfuegbar());
        assertNull(ergebnis.benutzer());
        assertEquals("Ungültige Anmeldedaten", ergebnis.fehlermeldung());
    }

    @Test
    void testServiceNichtVerfuegbarErgebnis() {
        AnmeldeErgebnis ergebnis = AnmeldeErgebnis.serviceNichtVerfuegbar("Authentifizierungsdienst steht aktuell nicht zur Verfügung");

        assertFalse(ergebnis.erfolgreich());
        assertFalse(ergebnis.serviceVerfuegbar());
        assertNull(ergebnis.benutzer());
        assertEquals("Authentifizierungsdienst steht aktuell nicht zur Verfügung", ergebnis.fehlermeldung());
    }
}
