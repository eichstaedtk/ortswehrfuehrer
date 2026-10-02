package de.eichstaedt.ortswehrfuehrer.domain;

import java.time.LocalDate;
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

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public LocalDate getGruendungsdatum() {
    return gruendungsdatum;
  }

  public boolean gruenden(String name, LocalDate gruendungsdatum) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.gruendungsdatum = gruendungsdatum;
    return true;
  }
}
