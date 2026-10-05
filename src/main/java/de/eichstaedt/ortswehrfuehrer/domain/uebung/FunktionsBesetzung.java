package de.eichstaedt.ortswehrfuehrer.domain.uebung;

import de.eichstaedt.ortswehrfuehrer.domain.Ausbildung;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Besetzung einer taktischen Funktion nach FwDV 3 durch einen Kameraden.
 */
@ValueObject
public record FunktionsBesetzung(
    TaktischeFunktion funktion,
    String kameradId,
    String kameradName,
    Set<Ausbildung> qualifikationen
) {

  public FunktionsBesetzung {
    Objects.requireNonNull(funktion, "Taktische Funktion darf nicht null sein.");
    qualifikationen = qualifikationen != null ? Collections.unmodifiableSet(new LinkedHashSet<>(qualifikationen)) : Collections.emptySet();
  }

  public boolean hatQualifikation(Ausbildung ausbildung) {
    return ausbildung != null && qualifikationen.contains(ausbildung);
  }

  public boolean hatQualifikation(String nameOderZiffer) {
    if (nameOderZiffer == null || nameOderZiffer.isBlank() || qualifikationen == null) {
      return false;
    }
    return Ausbildung.von(nameOderZiffer)
        .map(qualifikationen::contains)
        .orElse(false);
  }

  public boolean istAgt() {
    return hatQualifikation(Ausbildung.ATEMSCHUTZGERAETETRAEGER);
  }

  public boolean istMaschinist() {
    return hatQualifikation(Ausbildung.MASCHINIST);
  }

  public boolean istFuehrungskraft() {
    return hatQualifikation(Ausbildung.GRUPPENFUEHRER) || hatQualifikation(Ausbildung.TRUPPFUEHRER);
  }

  public boolean istBesetzt() {
    return kameradId != null && !kameradId.isBlank();
  }
}
