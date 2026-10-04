package de.eichstaedt.ortswehrfuehrer.domain;

import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Repräsentiert die eingegebenen Anmeldedaten.
 */
@ValueObject
public record AnmeldeCredentials(String benutzername, String passwort) {
    public boolean istGueltig() {
        return benutzername != null && !benutzername.isBlank() && passwort != null && !passwort.isBlank();
    }
}
