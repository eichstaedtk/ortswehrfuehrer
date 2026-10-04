package de.eichstaedt.ortswehrfuehrer.domain;

import java.util.Set;
import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Repräsentiert einen Benutzer im Feuerwehrverwaltungssystem.
 */
@ValueObject
public record Benutzer(String benutzername, String anzeigeName, Set<Rolle> rollen) {
    public Benutzer {
        if (benutzername == null || benutzername.isBlank()) {
            throw new IllegalArgumentException("Benutzername darf nicht leer sein.");
        }
        if (anzeigeName == null || anzeigeName.isBlank()) {
            anzeigeName = benutzername;
        }
        rollen = (rollen == null) ? Set.of() : Set.copyOf(rollen);
    }
}
