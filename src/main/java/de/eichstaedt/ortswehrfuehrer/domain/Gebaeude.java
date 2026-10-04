package de.eichstaedt.ortswehrfuehrer.domain;

import java.util.Objects;
import java.util.UUID;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

/**
 * Created by konrad.eichstaedt@gmx.de on 02.10.26.
 * <p>
 * This Class represents a Building as DDD Entity
 */
@Entity
public class Gebaeude {

  @Identity
  private String id;

  private String bezeichnung;

  private Adresse adresse;

  public Gebaeude() {
  }

  public Gebaeude(String bezeichnung, Adresse adresse) {
    this.id = UUID.randomUUID().toString();
    this.bezeichnung = bezeichnung;
    this.adresse = adresse;
  }

  public Gebaeude(String id, String bezeichnung, Adresse adresse) {
    this.id = id;
    this.bezeichnung = bezeichnung;
    this.adresse = adresse;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public Adresse getAdresse() {
    return adresse;
  }

  public void setAdresse(Adresse adresse) {
    this.adresse = adresse;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Gebaeude gebaeude = (Gebaeude) o;
    return Objects.equals(id, gebaeude.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
