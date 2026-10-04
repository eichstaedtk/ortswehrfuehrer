package de.eichstaedt.ortswehrfuehrer.application;

import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeCredentials;
import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeErgebnis;
import de.eichstaedt.ortswehrfuehrer.domain.Benutzer;
import de.eichstaedt.ortswehrfuehrer.domain.Rolle;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lokaler Authentication Provider für vorkonfigurierte oder lokal verwaltete Benutzerkonten.
 */
@ApplicationScoped
public class LokalerAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(LokalerAuthenticationProvider.class);

    private final Map<String, BenutzerEintrag> benutzerVerzeichnis = new ConcurrentHashMap<>();
    private boolean verfuegbar = true;

    private record BenutzerEintrag(Benutzer benutzer, String passwort) {}

    public LokalerAuthenticationProvider() {
        initialisiereStandardBenutzer();
    }

    private void initialisiereStandardBenutzer() {
        registriereBenutzer(
                new Benutzer("owf", "Ortswehrführer", Set.of(Rolle.ORTSWEHRFUEHRER)),
                "owf123"
        );
        registriereBenutzer(
                new Benutzer("kamerad", "Kamerad", Set.of(Rolle.KAMERAD)),
                "kamerad123"
        );
        registriereBenutzer(
                new Benutzer("admin", "Administrator", Set.of(Rolle.ADMINISTRATOR)),
                "admin123"
        );
    }

    public void registriereBenutzer(Benutzer benutzer, String passwort) {
        if (benutzer != null && passwort != null) {
            benutzerVerzeichnis.put(benutzer.benutzername().toLowerCase(), new BenutzerEintrag(benutzer, passwort));
        }
    }

    @Override
    public AnmeldeErgebnis authentifizieren(AnmeldeCredentials credentials) {
        if (!verfuegbar) {
            LOGGER.warn("Authentifizierungsanfrage abgewiesen: Lokaler Authentifizierungsdienst ist nicht verfügbar.");
            return AnmeldeErgebnis.serviceNichtVerfuegbar("Authentifizierungsdienst steht aktuell nicht zur Verfügung");
        }

        if (credentials == null || !credentials.istGueltig()) {
            LOGGER.warn("Authentifizierungsanfrage ungültig: Fehlende Pflichtfelder.");
            return AnmeldeErgebnis.fehlgeschlagen("Bitte Benutzername und Passwort angeben.");
        }

        String usernameKey = credentials.benutzername().trim().toLowerCase();
        BenutzerEintrag eintrag = benutzerVerzeichnis.get(usernameKey);

        if (eintrag != null && eintrag.passwort().equals(credentials.passwort())) {
            LOGGER.info("Erfolgreiche Anmeldung für Benutzer: {}", eintrag.benutzer().benutzername());
            return AnmeldeErgebnis.erfolgreich(eintrag.benutzer());
        }

        LOGGER.warn("Fehlgeschlagene Anmeldung für Benutzername: {}", credentials.benutzername());
        return AnmeldeErgebnis.fehlgeschlagen("Ungültige Anmeldedaten.");
    }

    @Override
    public boolean isVerfuegbar() {
        return verfuegbar;
    }

    public void setVerfuegbar(boolean verfuegbar) {
        this.verfuegbar = verfuegbar;
    }

    @Override
    public String getProviderTyp() {
        return "LOKAL";
    }
}
