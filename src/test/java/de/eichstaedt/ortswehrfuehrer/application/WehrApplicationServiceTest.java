package de.eichstaedt.ortswehrfuehrer.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.domain.Adresse;
import de.eichstaedt.ortswehrfuehrer.domain.Kamerad;
import de.eichstaedt.ortswehrfuehrer.domain.Wehr;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WehrApplicationServiceTest {

  private WehrApplicationService service;

  @BeforeEach
  void setUp() {
    service = new WehrApplicationService();
  }

  @Test
  @DisplayName("Service initialisiert standardmäßig eine aktive Wehr mit 3 Kameraden")
  void testInitialisiereStandardWehr() {
    Wehr wehr = service.getAktiveWehr();
    assertNotNull(wehr);
    assertEquals("Freiwillige Feuerwehr Musterstadt", wehr.getName());
    assertEquals(3, service.getAlleKameraden().size());
  }

  @Test
  @DisplayName("Kamerad über Objekt hinzufügen und automatisch der Abteilung zuweisen")
  void testKameradHinzufuegenObjekt() {
    Kamerad jugend = Kamerad.builder()
        .vorname("Tim")
        .nachname("Müller")
        .geburtsdatum(LocalDate.now().minusYears(14))
        .build();

    service.kameradHinzufuegen(jugend);

    Set<Kamerad> alle = service.getAlleKameraden();
    assertTrue(alle.contains(jugend));
    assertEquals("Jugendabteilung", service.ermittleAbteilungName(jugend));
  }

  @Test
  @DisplayName("Kamerad über Parameter-Methode hinzufügen")
  void testKameradHinzufuegenParameter() {
    Adresse adresse = new Adresse("Dorfstraße", "10", "12345", "Musterstadt");
    Kamerad neu = service.kameradHinzufuegen(
        "Klaus",
        "Schulz",
        LocalDate.now().minusYears(70),
        adresse,
        "0172 9876543",
        "klaus.schulz@feuerwehr.de"
    );

    assertNotNull(neu);
    assertEquals("Klaus", neu.getVorname());
    assertEquals("Alters- und Ehrenabteilung", service.ermittleAbteilungName(neu));
  }

  @Test
  @DisplayName("Hinzufügen von null-Kamerad wirft IllegalArgumentException")
  void testKameradNullWirftException() {
    assertThrows(IllegalArgumentException.class, () -> service.kameradHinzufuegen((Kamerad) null));
  }

  @Test
  @DisplayName("Abteilungsname für Einsatzabteilung und unbekannte Kameraden korrekt ermitteln")
  void testErmittleAbteilungName() {
    Kamerad einsatz = Kamerad.builder()
        .vorname("Erika")
        .nachname("Musterfrau")
        .geburtsdatum(LocalDate.now().minusYears(30))
        .build();
    service.kameradHinzufuegen(einsatz);

    assertEquals("Einsatzabteilung", service.ermittleAbteilungName(einsatz));

    Kamerad unbekannt = Kamerad.builder().vorname("Fremd").nachname("User").build();
    assertEquals("Nicht zugeordnet", service.ermittleAbteilungName(unbekannt));
    assertEquals("Unbekannt", service.ermittleAbteilungName(null));
  }

  @Test
  @DisplayName("Service ohne aktive Wehr wirft Exception bzw. liefert leere Menge")
  void testServiceOhneAktiveWehr() {
    service.setAktiveWehr(null);
    assertEquals(Set.of(), service.getAlleKameraden());
    assertEquals("Unbekannt", service.ermittleAbteilungName(Kamerad.builder().vorname("A").nachname("B").build()));

    Kamerad testK = Kamerad.builder().vorname("A").nachname("B").build();
    assertThrows(IllegalStateException.class, () -> service.kameradHinzufuegen(testK));
  }
}
