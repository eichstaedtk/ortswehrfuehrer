package de.eichstaedt.ortswehrfuehrer.application;

import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeCredentials;
import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeErgebnis;
import de.eichstaedt.ortswehrfuehrer.domain.Benutzer;
import de.eichstaedt.ortswehrfuehrer.domain.Rolle;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Vorbereiteter OAuth2 Authentication Provider gemäß NFR-1 für zukünftige OIDC/OAuth2-Integrationen.
 */
@ApplicationScoped
public class OAuth2AuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2AuthenticationProvider.class);

    private boolean verfuegbar = true;

    @Override
    public AnmeldeErgebnis authentifizieren(AnmeldeCredentials credentials) {
        if (!verfuegbar) {
            LOGGER.warn("OAuth2-Authentifizierungsdienst ist nicht verfügbar.");
            return AnmeldeErgebnis.serviceNichtVerfuegbar("OAuth2-Authentifizierungsdienst steht aktuell nicht zur Verfügung");
        }

        if (credentials == null || !credentials.istGueltig()) {
            LOGGER.warn("OAuth2-Authentifizierung: Ungültige Anmeldedaten.");
            return AnmeldeErgebnis.fehlgeschlagen("Bitte Benutzername und Passwort angeben.");
        }

        // Beispielhafte Validierung für OAuth2/OIDC Token / Federation Simulation
        if ("oauth2-user".equalsIgnoreCase(credentials.benutzername().trim())) {
            LOGGER.info("Erfolgreiche OAuth2-Anmeldung für: {}", credentials.benutzername());
            return AnmeldeErgebnis.erfolgreich(new Benutzer(
                    credentials.benutzername().trim(),
                    "OAuth2 Benutzer",
                    Set.of(Rolle.KAMERAD)
            ));
        }

        LOGGER.warn("OAuth2-Authentifizierung fehlgeschlagen für: {}", credentials.benutzername());
        return AnmeldeErgebnis.fehlgeschlagen("OAuth2-Authentifizierung fehlgeschlagen: Benutzer nicht autorisiert.");
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
        return "OAUTH2";
    }
}
