package de.eichstaedt.ortswehrfuehrer.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeCredentials;
import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeErgebnis;
import de.eichstaedt.ortswehrfuehrer.domain.Benutzer;
import de.eichstaedt.ortswehrfuehrer.domain.Rolle;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnmeldungApplicationServiceTest {

    private AnmeldungApplicationService service;
    private LokalerAuthenticationProvider lokalerProvider;

    @BeforeEach
    void setUp() {
        lokalerProvider = new LokalerAuthenticationProvider();
        service = new AnmeldungApplicationService(lokalerProvider);
    }

    @Test
    void testAnmeldungErfolgreich() {
        AnmeldeErgebnis ergebnis = service.anmelden("owf", "owf123");

        assertTrue(ergebnis.erfolgreich());
        assertNotNull(ergebnis.benutzer());
        assertEquals("owf", ergebnis.benutzer().benutzername());
    }

    @Test
    void testAnmeldungFehlgeschlagenUngueltigeDaten() {
        AnmeldeErgebnis ergebnis = service.anmelden("owf", "falschesPasswort");

        assertFalse(ergebnis.erfolgreich());
        assertEquals("Ungültige Anmeldedaten.", ergebnis.fehlermeldung());
    }

    @Test
    void testAnmeldungFehlgeschlagenLeereFelder() {
        AnmeldeErgebnis ergebnis = service.anmelden("", "");

        assertFalse(ergebnis.erfolgreich());
        assertEquals("Bitte Benutzername und Passwort angeben.", ergebnis.fehlermeldung());
    }

    @Test
    void testAnmeldungProviderNichtVerfuegbar() {
        lokalerProvider.setVerfuegbar(false);

        AnmeldeErgebnis ergebnis = service.anmelden("owf", "owf123");

        assertFalse(ergebnis.erfolgreich());
        assertFalse(ergebnis.serviceVerfuegbar());
        assertEquals("Authentifizierungsdienst steht aktuell nicht zur Verfügung", ergebnis.fehlermeldung());
    }

    @Test
    void testAnmeldungOhneKonfiguriertenProvider() {
        service.setAuthenticationProvider(null);

        AnmeldeErgebnis ergebnis = service.anmelden("owf", "owf123");

        assertFalse(ergebnis.erfolgreich());
        assertFalse(ergebnis.serviceVerfuegbar());
        assertEquals("Authentifizierungsdienst steht aktuell nicht zur Verfügung", ergebnis.fehlermeldung());
    }

    @Test
    void testAustauschbarerAuthenticationProvider() {
        OAuth2AuthenticationProvider oAuth2Provider = new OAuth2AuthenticationProvider();
        service.setAuthenticationProvider(oAuth2Provider);

        assertEquals(oAuth2Provider, service.getAuthenticationProvider());

        AnmeldeErgebnis ergebnis = service.anmelden("oauth2-user", "secret");
        assertTrue(ergebnis.erfolgreich());
        assertEquals("oauth2-user", ergebnis.benutzer().benutzername());
    }

    @Test
    void testSitzungsVerwaltung() {
        Benutzer benutzer = new Benutzer("owf", "Ortswehrführer", Set.of(Rolle.ORTSWEHRFUEHRER));

        String sitzungsId = service.erstelleSitzung(benutzer);
        assertNotNull(sitzungsId);
        assertFalse(sitzungsId.isBlank());

        assertTrue(service.istAngemeldet(sitzungsId));
        assertEquals(benutzer, service.getAngemeldetenBenutzer(sitzungsId));

        service.abmelden(sitzungsId);
        assertFalse(service.istAngemeldet(sitzungsId));
        assertNull(service.getAngemeldetenBenutzer(sitzungsId));
    }

    @Test
    void testSitzungsVerwaltungMitUngueltigenParametern() {
        assertThrows(IllegalArgumentException.class, () -> service.erstelleSitzung(null));
        assertFalse(service.istAngemeldet(null));
        assertFalse(service.istAngemeldet(""));
        assertNull(service.getAngemeldetenBenutzer(null));
        assertNull(service.getAngemeldetenBenutzer(""));
    }
}
