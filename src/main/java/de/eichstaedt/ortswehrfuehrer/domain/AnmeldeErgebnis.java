package de.eichstaedt.ortswehrfuehrer.domain;

import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Ergebnis eines Authentifizierungsversuchs.
 */
@ValueObject
public record AnmeldeErgebnis(
        boolean erfolgreich,
        Benutzer benutzer,
        String fehlermeldung,
        boolean serviceVerfuegbar
) {
    public static AnmeldeErgebnis erfolgreich(Benutzer benutzer) {
        return new AnmeldeErgebnis(true, benutzer, null, true);
    }

    public static AnmeldeErgebnis fehlgeschlagen(String fehlermeldung) {
        return new AnmeldeErgebnis(false, null, fehlermeldung, true);
    }

    public static AnmeldeErgebnis serviceNichtVerfuegbar(String fehlermeldung) {
        return new AnmeldeErgebnis(false, null, fehlermeldung, false);
    }
}
