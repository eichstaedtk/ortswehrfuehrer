package de.eichstaedt.ortswehrfuehrer.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

/**
 * Created by konrad.eichstaedt@gmx.de on 02.10.26.
 * <p>
 * This Class represents a Fire department as Aggregate Root
 */
@AggregateRoot
public class Wehr {

  @Identity
  private String id;

  private String name;

  private LocalDate gruendungsdatum;

  private List<Gebaeude> gebaeude = new ArrayList<>();

  private List<Einsatzfahrzeug> fahrzeuge = new ArrayList<>();

  private Abteilung jugendabteilung = new Abteilung("Jugendabteilung");

  private Abteilung einsatzabteilung = new Abteilung("Einsatzabteilung");

  private Abteilung altersUndEhrenabteilung = new Abteilung("Alters- und Ehrenabteilung");

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public LocalDate getGruendungsdatum() {
    return gruendungsdatum;
  }

  public List<Gebaeude> getGebaeude() {
    return gebaeude;
  }

  public List<Einsatzfahrzeug> getFahrzeuge() {
    return fahrzeuge;
  }

  public List<Einsatzfahrzeug> getEinsatzfahrzeuge() {
    return fahrzeuge;
  }

  public void setFahrzeuge(List<Einsatzfahrzeug> fahrzeuge) {
    this.fahrzeuge = fahrzeuge;
  }

  public Set<Kamerad> getKameraden() {
    Set<Kamerad> alleKameraden = new LinkedHashSet<>();
    if (this.jugendabteilung != null && this.jugendabteilung.getKameraden() != null) {
      alleKameraden.addAll(this.jugendabteilung.getKameraden());
    }
    if (this.einsatzabteilung != null && this.einsatzabteilung.getKameraden() != null) {
      alleKameraden.addAll(this.einsatzabteilung.getKameraden());
    }
    if (this.altersUndEhrenabteilung != null && this.altersUndEhrenabteilung.getKameraden() != null) {
      alleKameraden.addAll(this.altersUndEhrenabteilung.getKameraden());
    }
    return alleKameraden;
  }

  public Abteilung getJugendabteilung() {
    return jugendabteilung;
  }

  public Abteilung getEinsatzabteilung() {
    return einsatzabteilung;
  }

  public Abteilung getAltersUndEhrenabteilung() {
    return altersUndEhrenabteilung;
  }

  public Wehr gebaeudeHinzufuegen(Gebaeude gebaeude) {
    if (this.gebaeude == null) {
      this.gebaeude = new ArrayList<>();
    }
    this.gebaeude.add(gebaeude);
    return this;
  }

  public Wehr fahrzeugHinzufuegen(Einsatzfahrzeug fahrzeug) {
    if (this.fahrzeuge == null) {
      this.fahrzeuge = new ArrayList<>();
    }
    if (fahrzeug != null) {
      this.fahrzeuge.add(fahrzeug);
    }
    return this;
  }

  public Wehr einsatzfahrzeugHinzufuegen(Einsatzfahrzeug fahrzeug) {
    return fahrzeugHinzufuegen(fahrzeug);
  }

  public Wehr kameradHinzufuegen(Kamerad kamerad) {
    return kameradHinzufuegen(kamerad, LocalDate.now());
  }

  public Wehr kameradHinzufuegen(Kamerad kamerad, LocalDate stichtag) {
    if (kamerad != null) {
      if (kamerad.getGeburtsdatum() != null) {
        int alter = kamerad.berechneAlter(stichtag != null ? stichtag : LocalDate.now());
        if (alter <= 16) {
          this.jugendabteilung.kameradHinzufuegen(kamerad);
        } else if (alter <= 65) {
          this.einsatzabteilung.kameradHinzufuegen(kamerad);
        } else {
          this.altersUndEhrenabteilung.kameradHinzufuegen(kamerad);
        }
      } else {
        this.einsatzabteilung.kameradHinzufuegen(kamerad);
      }
    }
    return this;
  }

  public Wehr gruenden(String name, LocalDate gruendungsdatum) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.gruendungsdatum = gruendungsdatum;
    return this;
  }
}
