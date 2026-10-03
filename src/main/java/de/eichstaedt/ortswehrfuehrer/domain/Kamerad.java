package de.eichstaedt.ortswehrfuehrer.domain;

import java.time.LocalDate;
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

  public Kamerad() {
  }

  private Kamerad(Builder builder) {
    this.id = builder.id != null ? builder.id : UUID.randomUUID().toString();
    this.vorname = builder.vorname;
    this.nachname = builder.nachname;
    this.geburtsdatum = builder.geburtsdatum;
    this.adresse = builder.adresse;
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
}
