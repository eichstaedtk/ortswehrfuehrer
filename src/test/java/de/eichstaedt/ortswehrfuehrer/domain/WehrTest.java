package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class WehrTest {

  @Test
  void testInitialState() {
    Wehr wehr = new Wehr();
    assertNull(wehr.getId());
    assertNull(wehr.getName());
    assertNull(wehr.getGruendungsdatum());
    assertNotNull(wehr.getGebaeude());
    assertTrue(wehr.getGebaeude().isEmpty());
  }

  @Test
  void testGruenden() {
    Wehr wehr = new Wehr();
    String wehrName = "Freiwillige Feuerwehr Goettlin";
    LocalDate gruendungsdatum = LocalDate.of(1924, 5, 1);

    Wehr result = wehr.gruenden(wehrName, gruendungsdatum);

    assertSame(wehr, result);
    assertEquals(wehrName, wehr.getName());
    assertEquals(gruendungsdatum, wehr.getGruendungsdatum());
    assertNotNull(wehr.getId());
    assertDoesNotThrow(() -> UUID.fromString(wehr.getId()));
  }

  @Test
  void testGebaeudeHinzufuegen() {
    Wehr wehr = new Wehr();
    Adresse adresse = new Adresse("Hauptstraße", "12a", "14712", "Göttlin");
    Gebaeude gebaeude = new Gebaeude("Gerätehaus Göttlin", adresse);

    Wehr result = wehr.gebaeudeHinzufuegen(gebaeude);

    assertSame(wehr, result);
    assertEquals(1, wehr.getGebaeude().size());
    assertEquals(gebaeude, wehr.getGebaeude().get(0));
    assertEquals("Gerätehaus Göttlin", wehr.getGebaeude().get(0).getBezeichnung());
    assertEquals(adresse, wehr.getGebaeude().get(0).getAdresse());
  }

  @Test
  void testSetGebaeude() {
    Wehr wehr = new Wehr();
    Adresse adresse1 = new Adresse("Hauptstraße", "12a", "14712", "Göttlin");
    Gebaeude gebaeude1 = new Gebaeude("Gerätehaus Göttlin", adresse1);
    Adresse adresse2 = new Adresse("Seestraße", "5", "14712", "Göttlin");
    Gebaeude gebaeude2 = new Gebaeude("Bootshaus Göttlin", adresse2);

    wehr.setGebaeude(List.of(gebaeude1, gebaeude2));

    assertEquals(2, wehr.getGebaeude().size());
    assertEquals(gebaeude1, wehr.getGebaeude().get(0));
    assertEquals(gebaeude2, wehr.getGebaeude().get(1));
  }
}
