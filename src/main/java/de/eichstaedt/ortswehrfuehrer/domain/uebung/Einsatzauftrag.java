package de.eichstaedt.ortswehrfuehrer.domain.uebung;

import java.util.Objects;
import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Einsatzauftrag nach dem FwDV-3-Befehlsschema:
 * Einheit, Auftrag, Mittel, Ziel, Weg.
 */
@ValueObject
public record Einsatzauftrag(
    String einheit,
    String auftrag,
    String mittel,
    String ziel,
    String weg
) {

  public Einsatzauftrag {
    einheit = einheit != null ? einheit.trim() : "";
    auftrag = auftrag != null ? auftrag.trim() : "";
    mittel = mittel != null ? mittel.trim() : "";
    ziel = ziel != null ? ziel.trim() : "";
    weg = weg != null ? weg.trim() : "";
  }

  /**
   * Formatiert den vollständigen FwDV-3-Einsatzbefehl.
   * Beispiel: "Angriffstrupp zur Menschenrettung und Brandbekämpfung mit 1. C-Rohr in das 1. OG über Treppenraum vor!"
   */
  public String formatiereBefehl() {
    StringBuilder sb = new StringBuilder();
    if (!einheit.isBlank()) {
      sb.append(einheit);
    }
    if (!auftrag.isBlank()) {
      if (!sb.isEmpty()) {
        sb.append(" ");
      }
      if (!auftrag.toLowerCase().startsWith("zur ") && !auftrag.toLowerCase().startsWith("zum ")) {
        sb.append("zur ");
      }
      sb.append(auftrag);
    }
    if (!mittel.isBlank()) {
      if (!sb.isEmpty()) {
        sb.append(" ");
      }
      if (!mittel.toLowerCase().startsWith("mit ")) {
        sb.append("mit ");
      }
      sb.append(mittel);
    }
    if (!ziel.isBlank()) {
      if (!sb.isEmpty()) {
        sb.append(" ");
      }
      if (!ziel.toLowerCase().startsWith("nach ") && !ziel.toLowerCase().startsWith("in ") && !ziel.toLowerCase().startsWith("zu ") && !ziel.toLowerCase().startsWith("an ")) {
        sb.append("nach/zu ");
      }
      sb.append(ziel);
    }
    if (!weg.isBlank()) {
      if (!sb.isEmpty()) {
        sb.append(" ");
      }
      if (!weg.toLowerCase().startsWith("über ")) {
        sb.append("über ");
      }
      sb.append(weg);
    }
    if (!sb.isEmpty()) {
      sb.append(" vor!");
    }
    return sb.toString();
  }
}
