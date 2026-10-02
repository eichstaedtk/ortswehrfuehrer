package de.eichstaedt.ortswehrfuehrer.domain;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Created by konrad.eichstaedt@gmx.de on 02.10.26.
 * <p>
 * This Class represents a Fire department as Aggregate Root
 */
public class Wehr {

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
