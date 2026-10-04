package de.eichstaedt.ortswehrfuehrer.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeCredentials;
import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeErgebnis;
import de.eichstaedt.ortswehrfuehrer.domain.Benutzer;
import de.eichstaedt.ortswehrfuehrer.domain.Rolle;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LokalerAuthenticationProviderTest {

    private LokalerAuthenticationProvider provider;

    @BeforeEach
    void setUp() {
        provider = new LokalerAuthenticationProvider();
    }

    @Test
    void testErfolgreicheAnmeldungOrtswehrfuehrer() {
        AnmeldeErgebnis ergebnis = provider.authentifizieren(new AnmeldeCredentials("owf", "owf123"));

        assertTrue(ergebnis.erfolgreich());
        assertNotNull(ergebnis.benutzer());
        assertEquals("owf", ergebnis.benutzer().benutzername());
        assertTrue(ergebnis.benutzer().rollen().contains(Rolle.ORTSWEHRFUEHRER));
    }

    @Test
    void testErfolgreicheAnmeldungKamerad() {
        AnmeldeErgebnis ergebnis = provider.authentifizieren(new AnmeldeCredentials("kamerad", "kamerad123"));

        assertTrue(ergebnis.erfolgreich());
        assertNotNull(ergebnis.benutzer());
        assertEquals("kamerad", ergebnis.benutzer().benutzername());
        assertTrue(ergebnis.benutzer().rollen().contains(Rolle.KAMERAD));
    }

    @Test
    void testErfolgreicheAnmeldungAdministrator() {
        AnmeldeErgebnis ergebnis = provider.authentifizieren(new AnmeldeCredentials("admin", "admin123"));

        assertTrue(ergebnis.erfolgreich());
        assertNotNull(ergebnis.benutzer());
        assertEquals("admin", ergebnis.benutzer().benutzername());
        assertTrue(ergebnis.benutzer().rollen().contains(Rolle.ADMINISTRATOR));
    }

    @Test
    void testFehlgeschlageneAnmeldungFalschesPasswort() {
        AnmeldeErgebnis ergebnis = provider.authentifizieren(new AnmeldeCredentials("owf", "falschesPasswort"));

        assertFalse(ergebnis.erfolgreich());
        assertEquals("Ungültige Anmeldedaten.", ergebnis.fehlermeldung());
    }

    @Test
    void testFehlgeschlageneAnmeldungUnbekannterBenutzer() {
        AnmeldeErgebnis ergebnis = provider.authentifizieren(new AnmeldeCredentials("unbekannt", "passwort123"));

        assertFalse(ergebnis.erfolgreich());
        assertEquals("Ungültige Anmeldedaten.", ergebnis.fehlermeldung());
    }

    @Test
    void testLeereCredentialsLiefertFehler() {
        AnmeldeErgebnis ergebnis1 = provider.authentifizieren(new AnmeldeCredentials("", "pass"));
        AnmeldeErgebnis ergebnis2 = provider.authentifizieren(new AnmeldeCredentials("user", ""));
        AnmeldeErgebnis ergebnis3 = provider.authentifizieren(null);

        assertFalse(ergebnis1.erfolgreich());
        assertFalse(ergebnis2.erfolgreich());
        assertFalse(ergebnis3.erfolgreich());
        assertEquals("Bitte Benutzername und Passwort angeben.", ergebnis1.fehlermeldung());
    }

    @Test
    void testDienstNichtVerfuegbar() {
        provider.setVerfuegbar(false);
        assertFalse(provider.isVerfuegbar());

        AnmeldeErgebnis ergebnis = provider.authentifizieren(new AnmeldeCredentials("owf", "owf123"));
        assertFalse(ergebnis.erfolgreich());
        assertFalse(ergebnis.serviceVerfuegbar());
        assertEquals("Authentifizierungsdienst steht aktuell nicht zur Verfügung", ergebnis.fehlermeldung());
    }

    @Test
    void testRegistriereNeuenBenutzer() {
        provider.registriereBenutzer(
                new Benutzer("neu", "Neuer Kamerad", Set.of(Rolle.KAMERAD)),
                "neu123"
        );

        AnmeldeErgebnis ergebnis = provider.authentifizieren(new AnmeldeCredentials("neu", "neu123"));
        assertTrue(ergebnis.erfolgreich());
        assertEquals("Neuer Kamerad", ergebnis.benutzer().anzeigeName());
    }

    @Test
    void testProviderTyp() {
        assertEquals("LOKAL", provider.getProviderTyp());
    }
}
