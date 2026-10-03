package de.eichstaedt.ortswehrfuehrer.domain;

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

  public Kamerad() {
  }

  public Kamerad(String vorname, String nachname) {
    this.id = UUID.randomUUID().toString();
    this.vorname = vorname;
    this.nachname = nachname;
  }

  public Kamerad(String id, String vorname, String nachname) {
    this.id = id;
    this.vorname = vorname;
    this.nachname = nachname;
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
}
