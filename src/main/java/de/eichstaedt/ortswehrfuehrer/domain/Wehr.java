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

  private Set<String> fahrzeuge = new LinkedHashSet<>();

  private Abteilung jugendabteilung = new Abteilung("Jugendabteilung");

  private Abteilung einsatzabteilung = new Abteilung("Einsatzabteilung");

  private Abteilung altersUndEhrenabteilung = new Abteilung("Alters- und Ehrenabteilung");

  public Wehr() {
  }

  public Wehr(String name, LocalDate gruendungsdatum) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.gruendungsdatum = gruendungsdatum;
  }

  public Wehr(String id, String name, LocalDate gruendungsdatum) {
    this.id = id != null && !id.isBlank() ? id : UUID.randomUUID().toString();
    this.name = name;
    this.gruendungsdatum = gruendungsdatum;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getGruendungsdatum() {
    return gruendungsdatum;
  }

  public void setGruendungsdatum(LocalDate gruendungsdatum) {
    this.gruendungsdatum = gruendungsdatum;
  }

  public List<Gebaeude> getGebaeude() {
    return gebaeude;
  }

  public Set<String> getFahrzeugIds() {
    return fahrzeuge;
  }

  public Set<String> getFahrzeuge() {
    return fahrzeuge;
  }

  public Set<String> getEinsatzfahrzeuge() {
    return fahrzeuge;
  }

  public Set<String> getEinsatzfahrzeugIds() {
    return fahrzeuge;
  }

  public void setFahrzeugIds(Set<String> fahrzeugIds) {
    this.fahrzeuge = fahrzeugIds;
  }

  public void setFahrzeuge(Set<String> fahrzeuge) {
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

  public Wehr fahrzeugHinzufuegen(String fahrzeugId) {
    if (this.fahrzeuge == null) {
      this.fahrzeuge = new LinkedHashSet<>();
    }
    if (fahrzeugId != null && !fahrzeugId.isBlank()) {
      this.fahrzeuge.add(fahrzeugId);
    }
    return this;
  }

  public Wehr fahrzeugHinzufuegen(Einsatzfahrzeug fahrzeug) {
    if (fahrzeug != null) {
      return fahrzeugHinzufuegen(fahrzeug.getId());
    }
    return this;
  }

  public Wehr einsatzfahrzeugHinzufuegen(String fahrzeugId) {
    return fahrzeugHinzufuegen(fahrzeugId);
  }

  public Wehr einsatzfahrzeugHinzufuegen(Einsatzfahrzeug fahrzeug) {
    return fahrzeugHinzufuegen(fahrzeug);
  }

  public Wehr fahrzeugEntfernen(String fahrzeugId) {
    if (this.fahrzeuge != null && fahrzeugId != null) {
      this.fahrzeuge.remove(fahrzeugId);
    }
    return this;
  }

  public Wehr fahrzeugEntfernen(Einsatzfahrzeug fahrzeug) {
    if (fahrzeug != null) {
      return fahrzeugEntfernen(fahrzeug.getId());
    }
    return this;
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

  public Wehr gruenden(String name) {
    return gruenden(name, null);
  }

  public Wehr gruenden(String name, LocalDate gruendungsdatum) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.gruendungsdatum = gruendungsdatum;
    return this;
  }
}
