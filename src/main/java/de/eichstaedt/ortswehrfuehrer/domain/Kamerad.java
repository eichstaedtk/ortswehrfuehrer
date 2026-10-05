package de.eichstaedt.ortswehrfuehrer.domain;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

/**
 * Created by konrad.eichstaedt@gmx.de on 03.10.26.
 * <p>
 * This Class represents a Firefighter (Kamerad) as DDD Entity
 */
@Entity
public class Kamerad {

  @Identity
  private String id;

  private String vorname;

  private String nachname;

  private LocalDate geburtsdatum;

  private Adresse adresse;

  private String telefonnummer;

  private String emailAdresse;

  private Set<Ausbildung> ausbildungen = new LinkedHashSet<>();

  public Kamerad() {
  }

  private Kamerad(Builder builder) {
    this.id = builder.id != null ? builder.id : UUID.randomUUID().toString();
    this.vorname = builder.vorname;
    this.nachname = builder.nachname;
    this.geburtsdatum = builder.geburtsdatum;
    this.adresse = builder.adresse;
    this.telefonnummer = builder.telefonnummer;
    this.emailAdresse = builder.emailAdresse;
    this.ausbildungen = builder.ausbildungen != null
        ? new LinkedHashSet<>(builder.ausbildungen)
        : new LinkedHashSet<>();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String id;
    private String vorname;
    private String nachname;
    private LocalDate geburtsdatum;
    private Adresse adresse;
    private String telefonnummer;
    private String emailAdresse;
    private Set<Ausbildung> ausbildungen = new LinkedHashSet<>();

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder mitId(String id) {
      return id(id);
    }

    public Builder vorname(String vorname) {
      this.vorname = vorname;
      return this;
    }

    public Builder mitVorname(String vorname) {
      return vorname(vorname);
    }

    public Builder nachname(String nachname) {
      this.nachname = nachname;
      return this;
    }

    public Builder mitNachname(String nachname) {
      return nachname(nachname);
    }

    public Builder geburtsdatum(LocalDate geburtsdatum) {
      this.geburtsdatum = geburtsdatum;
      return this;
    }

    public Builder mitGeburtsdatum(LocalDate geburtsdatum) {
      return geburtsdatum(geburtsdatum);
    }

    public Builder adresse(Adresse adresse) {
      this.adresse = adresse;
      return this;
    }

    public Builder mitAdresse(Adresse adresse) {
      return adresse(adresse);
    }

    public Builder telefonnummer(String telefonnummer) {
      this.telefonnummer = telefonnummer;
      return this;
    }

    public Builder mitTelefonnummer(String telefonnummer) {
      return telefonnummer(telefonnummer);
    }

    public Builder emailAdresse(String emailAdresse) {
      this.emailAdresse = emailAdresse;
      return this;
    }

    public Builder mitEmailAdresse(String emailAdresse) {
      return emailAdresse(emailAdresse);
    }

    public Builder email(String email) {
      return emailAdresse(email);
    }

    public Builder mitEmail(String email) {
      return emailAdresse(email);
    }

    public Builder ausbildungen(Collection<Ausbildung> ausbildungen) {
      this.ausbildungen = ausbildungen != null ? new LinkedHashSet<>(ausbildungen) : new LinkedHashSet<>();
      return this;
    }

    public Builder mitAusbildungen(Collection<Ausbildung> ausbildungen) {
      return ausbildungen(ausbildungen);
    }

    public Builder ausbildung(Ausbildung ausbildung) {
      if (ausbildung != null) {
        this.ausbildungen.add(ausbildung);
      }
      return this;
    }

    public Builder mitAusbildung(Ausbildung ausbildung) {
      return ausbildung(ausbildung);
    }

    public Kamerad build() {
      return new Kamerad(this);
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getVorname() {
    return vorname;
  }

  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  public String getNachname() {
    return nachname;
  }

  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  public LocalDate getGeburtsdatum() {
    return geburtsdatum;
  }

  public void setGeburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }

  public Adresse getAdresse() {
    return adresse;
  }

  public void setAdresse(Adresse adresse) {
    this.adresse = adresse;
  }

  public String getTelefonnummer() {
    return telefonnummer;
  }

  public void setTelefonnummer(String telefonnummer) {
    this.telefonnummer = telefonnummer;
  }

  public String getEmailAdresse() {
    return emailAdresse;
  }

  public void setEmailAdresse(String emailAdresse) {
    this.emailAdresse = emailAdresse;
  }

  public String getEmail() {
    return emailAdresse;
  }

  public void setEmail(String email) {
    this.emailAdresse = email;
  }

  public Set<Ausbildung> getAusbildungen() {
    return ausbildungen;
  }

  public void setAusbildungen(Set<Ausbildung> ausbildungen) {
    this.ausbildungen = ausbildungen != null ? new LinkedHashSet<>(ausbildungen) : new LinkedHashSet<>();
  }

  public Kamerad ausbildungHinzufuegen(Ausbildung ausbildung) {
    if (ausbildung != null) {
      if (this.ausbildungen == null) {
        this.ausbildungen = new LinkedHashSet<>();
      }
      this.ausbildungen.add(ausbildung);
    }
    return this;
  }

  public Kamerad ausbildungEntfernen(Ausbildung ausbildung) {
    if (this.ausbildungen != null && ausbildung != null) {
      this.ausbildungen.remove(ausbildung);
    }
    return this;
  }

  public boolean hatAusbildung(Ausbildung ausbildung) {
    return this.ausbildungen != null && ausbildung != null && this.ausbildungen.contains(ausbildung);
  }

  public boolean hatAusbildung(String nameOderZiffer) {
    if (nameOderZiffer == null || nameOderZiffer.isBlank() || this.ausbildungen == null) {
      return false;
    }
    return Ausbildung.von(nameOderZiffer)
        .map(this.ausbildungen::contains)
        .orElse(false);
  }

  public Kamerad aendern(
      String vorname,
      String nachname,
      LocalDate geburtsdatum,
      Adresse adresse,
      String telefonnummer,
      String emailAdresse
  ) {
    return aendern(vorname, nachname, geburtsdatum, adresse, telefonnummer, emailAdresse, this.ausbildungen);
  }

  public Kamerad aendern(
      String vorname,
      String nachname,
      LocalDate geburtsdatum,
      Adresse adresse,
      String telefonnummer,
      String emailAdresse,
      Set<Ausbildung> ausbildungen
  ) {
    if (vorname == null || vorname.isBlank() || nachname == null || nachname.isBlank()) {
      throw new IllegalArgumentException("Vor- und Nachname sind Pflichtangaben.");
    }
    this.vorname = vorname;
    this.nachname = nachname;
    this.geburtsdatum = geburtsdatum;
    this.adresse = adresse;
    this.telefonnummer = telefonnummer;
    this.emailAdresse = emailAdresse;
    this.ausbildungen = ausbildungen != null ? new LinkedHashSet<>(ausbildungen) : new LinkedHashSet<>();
    return this;
  }

  public int berechneAlter() {
    return berechneAlter(LocalDate.now());
  }

  public int berechneAlter(LocalDate stichtag) {
    if (this.geburtsdatum == null) {
      throw new IllegalStateException("Berechnung des Alters nicht möglich, da kein Geburtsdatum gesetzt ist.");
    }
    if (stichtag == null) {
      throw new IllegalArgumentException("Stichtag darf nicht null sein.");
    }
    return Period.between(this.geburtsdatum, stichtag).getYears();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Kamerad kamerad = (Kamerad) o;
    return Objects.equals(id, kamerad.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
