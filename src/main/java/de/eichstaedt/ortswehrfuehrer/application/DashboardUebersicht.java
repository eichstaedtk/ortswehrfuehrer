package de.eichstaedt.ortswehrfuehrer.application;

import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Kamerad;
import de.eichstaedt.ortswehrfuehrer.domain.Wehr;
import java.util.List;
import java.util.Set;

/**
 * Data transfer object providing aggregated dashboard statistics and domain data.
 */
public record DashboardUebersicht(
    int anzahlWehren,
    int anzahlKameraden,
    int anzahlFahrzeuge,
    int anzahlGebaeude,
    Wehr beispielWehr,
    Set<Kamerad> kameraden,
    List<Einsatzfahrzeug> fahrzeuge
) {
}
