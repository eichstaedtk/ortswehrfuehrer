package de.eichstaedt.ortswehrfuehrer.domain.uebung;

import java.util.Collections;
import java.util.List;
import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Ergebnis der Guardrail- und Sicherheitsprüfung für eine Übungskonfiguration.
 */
@ValueObject
public record GuardrailPruefbericht(
    boolean gueltig,
    List<String> fehler,
    List<String> warnungen,
    List<String> empfehlungen
) {

  public GuardrailPruefbericht {
    fehler = fehler != null ? Collections.unmodifiableList(List.copyOf(fehler)) : Collections.emptyList();
    warnungen = warnungen != null ? Collections.unmodifiableList(List.copyOf(warnungen)) : Collections.emptyList();
    empfehlungen = empfehlungen != null ? Collections.unmodifiableList(List.copyOf(empfehlungen)) : Collections.emptyList();
  }

  public static GuardrailPruefbericht erfolgreich() {
    return new GuardrailPruefbericht(true, List.of(), List.of(), List.of());
  }

  public static GuardrailPruefbericht erfolgreichMitHinweisen(List<String> warnungen, List<String> empfehlungen) {
    return new GuardrailPruefbericht(true, List.of(), warnungen, empfehlungen);
  }

  public static GuardrailPruefbericht fehlerhaft(List<String> fehler, List<String> warnungen, List<String> empfehlungen) {
    return new GuardrailPruefbericht(false, fehler, warnungen, empfehlungen);
  }

  public boolean hatFehler() {
    return !fehler.isEmpty();
  }

  public boolean hatWarnungen() {
    return !warnungen.isEmpty();
  }

  public boolean hatEmpfehlungen() {
    return !empfehlungen.isEmpty();
  }
}
