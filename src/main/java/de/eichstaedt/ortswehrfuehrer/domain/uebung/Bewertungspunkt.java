package de.eichstaedt.ortswehrfuehrer.domain.uebung;

import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Bewertungs- und Beobachtungskriterium für den Übungsleiter.
 */
@ValueObject
public record Bewertungspunkt(
    String kategorie,
    String kriterien,
    int punkte
) {

  public Bewertungspunkt {
    kategorie = kategorie != null ? kategorie.trim() : "Taktik";
    kriterien = kriterien != null ? kriterien.trim() : "";
    if (punkte <= 0) {
      punkte = 1;
    }
  }
}
