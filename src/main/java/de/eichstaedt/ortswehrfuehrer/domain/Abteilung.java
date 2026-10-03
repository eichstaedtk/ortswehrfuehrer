package de.eichstaedt.ortswehrfuehrer.domain;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

/**
 * Created by konrad.eichstaedt@gmx.de on 03.10.26.
 * <p>
 * This Class represents a Department (Einsatzabteilung) as DDD Entity
 */
@Entity
public class Abteilung {

  @Identity
  private String id;

  private String bezeichnung;

  private Set<Kamerad> kameraden = new LinkedHashSet<>();

  public Abteilung() {
    this.id = UUID.randomUUID().toString();
  }

  public Abteilung(String bezeichnung) {
    this.id = UUID.randomUUID().toString();
    this.bezeichnung = bezeichnung;
  }

  public Abteilung(String id, String bezeichnung) {
    this.id = id;
    this.bezeichnung = bezeichnung;
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

  public Set<Kamerad> getKameraden() {
    return kameraden;
  }

  public void setKameraden(Set<Kamerad> kameraden) {
    this.kameraden = kameraden;
  }

  public Abteilung kameradHinzufuegen(Kamerad kamerad) {
    if (this.kameraden == null) {
      this.kameraden = new LinkedHashSet<>();
    }
    this.kameraden.add(kamerad);
    return this;
  }
}
