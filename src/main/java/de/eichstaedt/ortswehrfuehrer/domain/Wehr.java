package de.eichstaedt.ortswehrfuehrer.domain;

import java.util.UUID;

/**
 * Created by konrad.eichstaedt@gmx.de on 02.10.26.
 * <p>
 * This Class represents a Fire department as Aggregate Root
 */
public class Wehr {

  public Wehr(String name) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
  }

  private String id;

  private String name;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
