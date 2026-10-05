package de.eichstaedt.ortswehrfuehrer.domain.uebung;

import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Zeitgesteuerte Lageentwicklung / Einspielung für das Übungsszenario.
 */
@ValueObject
public record Lageentwicklung(
    int nachMinuten,
    String ereignis,
    String erwarteteMassnahme
) {

  public Lageentwicklung {
    if (nachMinuten < 0) {
      nachMinuten = 0;
    }
    ereignis = ereignis != null ? ereignis.trim() : "";
    erwarteteMassnahme = erwarteteMassnahme != null ? erwarteteMassnahme.trim() : "";
  }
}
