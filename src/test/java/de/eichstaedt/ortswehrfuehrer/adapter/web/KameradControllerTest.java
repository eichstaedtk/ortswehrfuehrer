package de.eichstaedt.ortswehrfuehrer.adapter.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.application.WehrApplicationService;
import de.eichstaedt.ortswehrfuehrer.domain.Kamerad;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class KameradControllerTest {

  @Inject
  WehrApplicationService wehrService;

  @Test
  @DisplayName("POST /kameraden mit vollständigen Formulardaten nimmt Kamerad auf und leitet weiter")
  void testKameradHinzufuegenErfolgreich() {
    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("vorname", "Anna")
        .formParam("nachname", "Fischer")
        .formParam("geburtsdatum", "1998-07-22")
        .formParam("strasse", "Birkenweg")
        .formParam("hausnummer", "5")
        .formParam("postleitzahl", "12345")
        .formParam("ort", "Musterstadt")
        .formParam("telefonnummer", "0175 1122334")
        .formParam("emailAdresse", "anna.fischer@feuerwehr.de")
        .when()
        .post("/kameraden")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?erfolg=kamerad_hinzugefuegt#kameraden"));

    Optional<Kamerad> anna = wehrService.getAlleKameraden().stream()
        .filter(k -> "Anna".equals(k.getVorname()) && "Fischer".equals(k.getNachname()))
        .findFirst();

    assertTrue(anna.isPresent());
    assertEquals(LocalDate.of(1998, 7, 22), anna.get().getGeburtsdatum());
    assertEquals("0175 1122334", anna.get().getTelefonnummer());
    assertEquals("anna.fischer@feuerwehr.de", anna.get().getEmailAdresse());
    assertNotNull(anna.get().getAdresse());
    assertEquals("Birkenweg", anna.get().getAdresse().strasse());
  }

  @Test
  @DisplayName("POST /kameraden mit fehlendem Pflichtfeld Vorname leitet mit Fehlermeldung weiter")
  void testKameradHinzufuegenFehlerOhneName() {
    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("vorname", "")
        .formParam("nachname", "Meier")
        .when()
        .post("/kameraden")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?fehler=name_fehlt#kameraden"));

    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("vorname", "Max")
        .formParam("nachname", "")
        .when()
        .post("/kameraden")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?fehler=name_fehlt#kameraden"));
  }

  @Test
  @DisplayName("Default-Konstruktor von KameradController")
  void testDefaultKonstruktor() {
    KameradController defaultResource = new KameradController();
    assertNotNull(defaultResource);
  }

  @Test
  @DisplayName("Direkter Methodenaufruf an KameradController mit ungültigem Datum und partieller Adresse")
  void testDirekterMethodenaufruf() {
    WehrApplicationService mockService = new WehrApplicationService();
    KameradController resource = new KameradController(mockService);

    try (Response response = resource.kameradHinzufuegen(
        "Klara",
        "Berger",
        "ungueltiges-datum",
        "Wiesenweg",
        "",
        "",
        "",
        "",
        ""
    )) {
      assertEquals(303, response.getStatus());
    }

    Optional<Kamerad> klara = mockService.getAlleKameraden().stream()
        .filter(k -> "Klara".equals(k.getVorname()))
        .findFirst();

    assertTrue(klara.isPresent());
    assertNull(klara.get().getGeburtsdatum());
    assertEquals("Wiesenweg", klara.get().getAdresse().strasse());
  }
}
