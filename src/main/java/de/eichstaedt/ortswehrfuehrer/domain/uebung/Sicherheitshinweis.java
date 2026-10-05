package de.eichstaedt.ortswehrfuehrer.domain.uebung;

import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Sicherheits- und UVV-Hinweis für den Übungsleiter und Teilnehmer.
 */
@ValueObject
public record Sicherheitshinweis(
    String vorschrift,
    String kategorie,
    String hinweisText,
    boolean kritisch
) {

  public Sicherheitshinweis {
    vorschrift = vorschrift != null ? vorschrift.trim() : "DGUV Vorschrift 49 (UVV Feuerwehr)";
    kategorie = kategorie != null ? kategorie.trim() : "Allgemein";
    hinweisText = hinweisText != null ? hinweisText.trim() : "";
  }
}
