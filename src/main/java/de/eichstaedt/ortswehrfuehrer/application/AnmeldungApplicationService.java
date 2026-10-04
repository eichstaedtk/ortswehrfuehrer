package de.eichstaedt.ortswehrfuehrer.application;

import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeCredentials;
import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeErgebnis;
import de.eichstaedt.ortswehrfuehrer.domain.Benutzer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.jmolecules.ddd.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application Service für die Authentifizierung, Sitzungsverwaltung und Koordination
 * der austauschbaren Authentication-Provider.
 */
@Service
@ApplicationScoped
public class AnmeldungApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnmeldungApplicationService.class);

    private final Map<String, Benutzer> sitzungsVerzeichnis = new ConcurrentHashMap<>();
    private AuthenticationProvider authenticationProvider;

    @Inject
    public AnmeldungApplicationService(LokalerAuthenticationProvider lokalerProvider) {
        this.authenticationProvider = lokalerProvider;
    }

    public AnmeldungApplicationService() {
        // Für CDI Proxies
    }

    public AnmeldeErgebnis anmelden(String benutzername, String passwort) {
        return anmelden(new AnmeldeCredentials(benutzername, passwort));
    }

    public AnmeldeErgebnis anmelden(AnmeldeCredentials credentials) {
        if (credentials == null || !credentials.istGueltig()) {
            LOGGER.warn("Anmeldeversuch abgelehnt: Leere Anmeldedaten.");
            return AnmeldeErgebnis.fehlgeschlagen("Bitte Benutzername und Passwort angeben.");
        }

        if (authenticationProvider == null) {
            LOGGER.error("Kein AuthenticationProvider konfiguriert.");
            return AnmeldeErgebnis.serviceNichtVerfuegbar("Authentifizierungsdienst steht aktuell nicht zur Verfügung");
        }

        if (!authenticationProvider.isVerfuegbar()) {
            LOGGER.warn("Authentifizierungsdienst ({}) ist aktuell nicht verfügbar.", authenticationProvider.getProviderTyp());
            return AnmeldeErgebnis.serviceNichtVerfuegbar("Authentifizierungsdienst steht aktuell nicht zur Verfügung");
        }

        return authenticationProvider.authentifizieren(credentials);
    }

    public String erstelleSitzung(Benutzer benutzer) {
        if (benutzer == null) {
            throw new IllegalArgumentException("Benutzer für Sitzung darf nicht null sein.");
        }
        String sitzungsId = UUID.randomUUID().toString();
        sitzungsVerzeichnis.put(sitzungsId, benutzer);
        LOGGER.info("Neue Sitzung erstellt: {} für Benutzer {}", sitzungsId, benutzer.benutzername());
        return sitzungsId;
    }

    public Benutzer getAngemeldetenBenutzer(String sitzungsId) {
        if (sitzungsId == null || sitzungsId.isBlank()) {
            return null;
        }
        return sitzungsVerzeichnis.get(sitzungsId);
    }

    public boolean istAngemeldet(String sitzungsId) {
        return getAngemeldetenBenutzer(sitzungsId) != null;
    }

    public void abmelden(String sitzungsId) {
        if (sitzungsId != null && !sitzungsId.isBlank()) {
            Benutzer entfernterBenutzer = sitzungsVerzeichnis.remove(sitzungsId);
            if (entfernterBenutzer != null) {
                LOGGER.info("Sitzung {} für Benutzer {} erfolgreich beendet.", sitzungsId, entfernterBenutzer.benutzername());
            }
        }
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
}
