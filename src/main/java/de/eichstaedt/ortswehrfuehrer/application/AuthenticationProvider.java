package de.eichstaedt.ortswehrfuehrer.application;

import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeCredentials;
import de.eichstaedt.ortswehrfuehrer.domain.AnmeldeErgebnis;

/**
 * Port-Schnittstelle für austauschbare Authentifizierungsmechanismen (z. B. lokal, OAuth2).
 */
public interface AuthenticationProvider {

    AnmeldeErgebnis authentifizieren(AnmeldeCredentials credentials);

    boolean isVerfuegbar();

    String getProviderTyp();
}
