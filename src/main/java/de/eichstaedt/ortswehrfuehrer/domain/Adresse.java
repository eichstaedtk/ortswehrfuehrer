package de.eichstaedt.ortswehrfuehrer.domain;

import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Created by konrad.eichstaedt@gmx.de on 02.10.26.
 * <p>
 * This Class represents an Address as Value Object
 */
@ValueObject
public record Adresse(String strasse, String hausnummer, String postleitzahl, String ort) {
}
