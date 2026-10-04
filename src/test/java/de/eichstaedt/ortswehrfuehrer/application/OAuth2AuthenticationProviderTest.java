package de.eichstaedt.ortswehrfuehrer.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeCredentials;
import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeErgebnis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OAuth2AuthenticationProviderTest {

    private OAuth2AuthenticationProvider provider;

    @BeforeEach
    void setUp() {
        provider = new OAuth2AuthenticationProvider();
    }

    @Test
    void testErfolgreicheOAuth2Anmeldung() {
        AnmeldeErgebnis ergebnis = provider.authentifizieren(new AnmeldeCredentials("oauth2-user", "secret"));

        assertTrue(ergebnis.erfolgreich());
        assertNotNull(ergebnis.benutzer());
        assertEquals("oauth2-user", ergebnis.benutzer().benutzername());
    }

    @Test
    void testFehlgeschlageneOAuth2Anmeldung() {
        AnmeldeErgebnis ergebnis = provider.authentifizieren(new AnmeldeCredentials("unbekannt", "secret"));

        assertFalse(ergebnis.erfolgreich());
        assertTrue(ergebnis.fehlermeldung().contains("OAuth2-Authentifizierung fehlgeschlagen"));
    }

    @Test
    void testLeereCredentialsLiefertFehler() {
        AnmeldeErgebnis ergebnis = provider.authentifizieren(new AnmeldeCredentials("", "pass"));

        assertFalse(ergebnis.erfolgreich());
        assertEquals("Bitte Benutzername und Passwort angeben.", ergebnis.fehlermeldung());
    }

    @Test
    void testOAuth2DienstNichtVerfuegbar() {
        provider.setVerfuegbar(false);
        assertFalse(provider.isVerfuegbar());

        AnmeldeErgebnis ergebnis = provider.authentifizieren(new AnmeldeCredentials("oauth2-user", "secret"));
        assertFalse(ergebnis.erfolgreich());
        assertFalse(ergebnis.serviceVerfuegbar());
        assertEquals("OAuth2-Authentifizierungsdienst steht aktuell nicht zur Verfügung", ergebnis.fehlermeldung());
    }

    @Test
    void testProviderTyp() {
        assertEquals("OAUTH2", provider.getProviderTyp());
    }
}
