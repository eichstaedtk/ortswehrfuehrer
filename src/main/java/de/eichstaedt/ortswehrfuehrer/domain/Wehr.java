package de.eichstaedt.ortswehrfuehrer.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

  private final Abteilung jugendabteilung = new Abteilung("Jugendabteilung");

  private final Abteilung einsatzabteilung = new Abteilung("Einsatzabteilung");

  private final Abteilung altersUndEhrenabteilung = new Abteilung("Alters- und Ehrenabteilung");

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
    if (this.jugendabteilung.getKameraden() != null) {
      alleKameraden.addAll(this.jugendabteilung.getKameraden());
    }
    if (this.einsatzabteilung.getKameraden() != null) {
      alleKameraden.addAll(this.einsatzabteilung.getKameraden());
    }
    if (this.altersUndEhrenabteilung.getKameraden() != null) {
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

  public Wehr einsatzfahrzeugEntfernen(String fahrzeugId) {
    return fahrzeugEntfernen(fahrzeugId);
  }

  public Wehr einsatzfahrzeugEntfernen(Einsatzfahrzeug fahrzeug) {
    return fahrzeugEntfernen(fahrzeug);
  }

  public Wehr fahrzeugAendern(Einsatzfahrzeug fahrzeug, String bezeichnung, String kennung, Fahrzeugtyp fahrzeugtyp) {
    if (fahrzeug != null) {
      if (this.fahrzeuge != null && !this.fahrzeuge.contains(fahrzeug.getId())) {
        this.fahrzeuge.add(fahrzeug.getId());
      }
      fahrzeug.aendern(bezeichnung, kennung, fahrzeugtyp);
    }
    return this;
  }

  public Wehr fahrzeugAendern(Einsatzfahrzeug fahrzeug, String kennung) {
    if (fahrzeug != null) {
      if (this.fahrzeuge != null && !this.fahrzeuge.contains(fahrzeug.getId())) {
        this.fahrzeuge.add(fahrzeug.getId());
      }
      fahrzeug.fahrzeugAendern(kennung);
    }
    return this;
  }

  public Wehr einsatzfahrzeugAendern(Einsatzfahrzeug fahrzeug, String bezeichnung, String kennung, Fahrzeugtyp fahrzeugtyp) {
    return fahrzeugAendern(fahrzeug, bezeichnung, kennung, fahrzeugtyp);
  }

  public Wehr einsatzfahrzeugAendern(Einsatzfahrzeug fahrzeug, String kennung) {
    return fahrzeugAendern(fahrzeug, kennung);
  }

  public Wehr kameradHinzufuegen(Kamerad kamerad) {
    return kameradHinzufuegen(kamerad, LocalDate.now());
  }

  public Wehr kameradHinzufuegen(Kamerad kamerad, LocalDate stichtag) {
    if (kamerad != null) {
      ermittleAbteilung(kamerad, stichtag).kameradHinzufuegen(kamerad);
    }
    return this;
  }

  public Optional<Kamerad> findeKamerad(String kameradId) {
    if (kameradId == null || kameradId.isBlank()) {
      return Optional.empty();
    }
    String trimmedId = kameradId.trim();
    return getKameraden().stream()
        .filter(k -> Objects.equals(k.getId(), trimmedId))
        .findFirst();
  }

  public Wehr kameradAendern(
      String kameradId,
      String vorname,
      String nachname,
      LocalDate geburtsdatum,
      Adresse adresse,
      String telefonnummer,
      String emailAdresse
  ) {
    Kamerad kamerad = findeKamerad(kameradId)
        .orElseThrow(() -> new IllegalArgumentException(
            "Kamerad mit ID '" + kameradId + "' wurde nicht gefunden."));
    kamerad.aendern(vorname, nachname, geburtsdatum, adresse, telefonnummer, emailAdresse);

    Abteilung zielAbteilung = ermittleAbteilung(kamerad, LocalDate.now());
    if (zielAbteilung.getKameraden() == null || !zielAbteilung.getKameraden().contains(kamerad)) {
      kameradEntfernen(kamerad);
      zielAbteilung.kameradHinzufuegen(kamerad);
    }
    return this;
  }

  private Abteilung ermittleAbteilung(Kamerad kamerad, LocalDate stichtag) {
    if (kamerad.getGeburtsdatum() == null) {
      return this.einsatzabteilung;
    }
    int alter = kamerad.berechneAlter(stichtag != null ? stichtag : LocalDate.now());
    if (alter <= 16) {
      return this.jugendabteilung;
    }
    if (alter <= 65) {
      return this.einsatzabteilung;
    }
    return this.altersUndEhrenabteilung;
  }

  public Wehr kameradEntfernen(Kamerad kamerad) {
    if (kamerad != null) {
      this.jugendabteilung.kameradEntfernen(kamerad);
      this.einsatzabteilung.kameradEntfernen(kamerad);
      this.altersUndEhrenabteilung.kameradEntfernen(kamerad);
    }
    return this;
  }

  public Wehr kameradEntfernen(String kameradId) {
    if (kameradId != null && !kameradId.isBlank()) {
      this.jugendabteilung.kameradEntfernen(kameradId);
      this.einsatzabteilung.kameradEntfernen(kameradId);
      this.altersUndEhrenabteilung.kameradEntfernen(kameradId);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Wehr wehr = (Wehr) o;
    return Objects.equals(id, wehr.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
