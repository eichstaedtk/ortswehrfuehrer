package de.eichstaedt.ortswehrfuehrer.domain;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import org.jmolecules.ddd.annotation.Factory;

/**
 * Created by konrad.eichstaedt@gmx.de on 03.10.26.
 * <p>
 * Factory for creating and instantiating {@link Wehr} Aggregate Roots according to GoF Factory Pattern and DDD principles.
 */
@Factory
public class WehrFactory {

  /**
   * Erzeugt eine neue, leere Wehr mit einer eindeutigen ID.
   *
   * @return Eine neu initialisierte Wehr-Instanz.
   */
  public Wehr erzeugeNeueWehr() {
    Wehr wehr = new Wehr();
    wehr.setId(UUID.randomUUID().toString());
    return wehr;
  }

  /**
   * Erzeugt eine neue Wehr mit dem angegebenen Namen.
   *
   * @param name Name der Wehr.
   * @return Eine konfigurierte Wehr-Instanz.
   */
  public Wehr erzeugeWehr(String name) {
    return erzeugeWehr(name, null);
  }

  /**
   * Erzeugt eine neue Wehr mit Name und Gründungsdatum.
   *
   * @param name Name der Wehr.
   * @param gruendungsdatum Gründungsdatum der Wehr.
   * @return Eine initialisierte und gegründete Wehr-Instanz.
   */
  public Wehr erzeugeWehr(String name, LocalDate gruendungsdatum) {
    Wehr wehr = new Wehr();
    wehr.gruenden(name, gruendungsdatum);
    return wehr;
  }

  /**
   * Erzeugt bzw. rekonstruiert eine bestehende Wehr mit bekannter ID, Name und Gründungsdatum.
   *
   * @param id Eindeutige ID der Wehr.
   * @param name Name der Wehr.
   * @param gruendungsdatum Gründungsdatum der Wehr.
   * @return Die rekonstruierte Wehr-Instanz.
   */
  public Wehr rekonstruiereWehr(String id, String name, LocalDate gruendungsdatum) {
    Wehr wehr = new Wehr();
    wehr.setId(id != null && !id.isBlank() ? id : UUID.randomUUID().toString());
    wehr.setName(name);
    wehr.setGruendungsdatum(gruendungsdatum);
    return wehr;
  }

  /**
   * Erzeugt eine vollständig ausgestattete Wehr mit Name, Gründungsdatum, Gebäuden, Fahrzeug-IDs und Kameraden.
   *
   * @param name Name der Wehr.
   * @param gruendungsdatum Gründungsdatum der Wehr.
   * @param gebaeude Optionale Kollektion von Gebäuden.
   * @param fahrzeugIds Optionale Kollektion von Fahrzeug-IDs.
   * @param kameraden Optionale Kollektion von Kameraden.
   * @return Vollständig initialisierte Wehr.
   */
  public Wehr erzeugeWehr(String name,
      LocalDate gruendungsdatum,
      Collection<Gebaeude> gebaeude,
      Collection<String> fahrzeugIds,
      Collection<Kamerad> kameraden) {
    Wehr wehr = erzeugeWehr(name, gruendungsdatum);
    if (gebaeude != null) {
      gebaeude.forEach(wehr::gebaeudeHinzufuegen);
    }
    if (fahrzeugIds != null) {
      fahrzeugIds.forEach(wehr::fahrzeugHinzufuegen);
    }
    if (kameraden != null) {
      kameraden.forEach(wehr::kameradHinzufuegen);
    }
    return wehr;
  }
}
