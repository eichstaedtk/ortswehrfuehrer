package de.eichstaedt.ortswehrfuehrer.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public LocalDate getGruendungsdatum() {
    return gruendungsdatum;
  }

  public List<Gebaeude> getGebaeude() {
    return gebaeude;
  }

  public void setGebaeude(List<Gebaeude> gebaeude) {
    this.gebaeude = gebaeude;
  }

  public Wehr gebaeudeHinzufuegen(Gebaeude gebaeude) {
    if (this.gebaeude == null) {
      this.gebaeude = new ArrayList<>();
    }
    this.gebaeude.add(gebaeude);
    return this;
  }

  public Wehr gruenden(String name, LocalDate gruendungsdatum) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.gruendungsdatum = gruendungsdatum;
    return this;
  }
}
