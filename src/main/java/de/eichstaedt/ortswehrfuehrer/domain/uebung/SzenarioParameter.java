package de.eichstaedt.ortswehrfuehrer.domain.uebung;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Rahmenparameter für ein Übungsszenario.
 */
@ValueObject
public record SzenarioParameter(
    int dauerMinuten,
    Schwierigkeitsgrad schwierigkeit,
    Tageszeit tageszeit,
    String ortObjekt,
    List<String> lernziele
) {

  public SzenarioParameter {
    if (dauerMinuten <= 0) {
      dauerMinuten = 90;
    }
    schwierigkeit = schwierigkeit != null ? schwierigkeit : Schwierigkeitsgrad.MITTEL;
    tageszeit = tageszeit != null ? tageszeit : Tageszeit.TAG;
    ortObjekt = ortObjekt != null ? ortObjekt.trim() : "";
    lernziele = lernziele != null ? Collections.unmodifiableList(List.copyOf(lernziele)) : Collections.emptyList();
  }

  public static SzenarioParameter standard() {
    return new SzenarioParameter(90, Schwierigkeitsgrad.MITTEL, Tageszeit.TAG, "Gerätehaus / Übungsplatz", List.of("Standardeinsatz nach FwDV 3"));
  }
}
