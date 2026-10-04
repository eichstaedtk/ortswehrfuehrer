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
  @DisplayName("POST /kameraden/entfernen entfernt Kamerad und leitet weiter")
  void testKameradEntfernenErfolgreich() {
    Kamerad k = wehrService.kameradHinzufuegen("Ben", "Entfernbar", LocalDate.of(1992, 2, 2), null, null, null);
    assertTrue(wehrService.getAlleKameraden().contains(k));

    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("kameradId", k.getId())
        .when()
        .post("/kameraden/entfernen")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?erfolg=kamerad_entfernt#kameraden"));

    assertTrue(wehrService.getAlleKameraden().stream().noneMatch(kam -> kam.getId().equals(k.getId())));
  }

  @Test
  @DisplayName("POST /kameraden/entfernen mit leerer ID leitet mit Fehlermeldung weiter")
  void testKameradEntfernenFehlerLeereId() {
    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("kameradId", "")
        .when()
        .post("/kameraden/entfernen")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?fehler=kamerad_entfernen_fehlgeschlagen#kameraden"));
  }

  @Test
  @DisplayName("Direkter Methodenaufruf kameradEntfernen an Controller")
  void testDirekterMethodenaufrufEntfernen() {
    WehrApplicationService mockService = new WehrApplicationService();
    KameradController controller = new KameradController(mockService);

    Kamerad k1 = mockService.kameradHinzufuegen("Dirk", "Direct1", LocalDate.of(1990, 1, 1), null, null, null);
    assertEquals(4, mockService.getAlleKameraden().size());

    try (Response r1 = controller.kameradEntfernen(k1.getId())) {
      assertEquals(303, r1.getStatus());
      assertEquals(3, mockService.getAlleKameraden().size());
      assertEquals("/?erfolg=kamerad_entfernt#kameraden", r1.getLocation().toString());
    }

    try (Response r2 = controller.kameradEntfernen("")) {
      assertEquals(303, r2.getStatus());
      assertEquals("/?fehler=kamerad_entfernen_fehlgeschlagen#kameraden", r2.getLocation().toString());
    }
  }

  @Test
  @DisplayName("POST /kameraden/aendern aktualisiert Kamerad und leitet weiter")
  void testKameradAendernErfolgreich() {
    Kamerad k = wehrService.kameradHinzufuegen("Max", "Aenderbar", LocalDate.of(1990, 5, 20), null, null, null);

    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("kameradId", k.getId())
        .formParam("vorname", "Konrad")
        .formParam("nachname", "Eichstädt")
        .formParam("geburtsdatum", "1990-05-20")
        .formParam("strasse", "Dorfstraße")
        .formParam("hausnummer", "12")
        .formParam("postleitzahl", "14712")
        .formParam("ort", "Göttlin")
        .formParam("telefonnummer", "0173 8884932")
        .formParam("emailAdresse", "konrad@example.com")
        .when()
        .post("/kameraden/aendern")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?erfolg=kamerad_geaendert#kameraden"));

    Kamerad geaendert = wehrService.getKamerad(k.getId());
    assertEquals("Konrad", geaendert.getVorname());
    assertEquals("Eichstädt", geaendert.getNachname());
    assertEquals("Göttlin", geaendert.getAdresse().ort());
    assertEquals("0173 8884932", geaendert.getTelefonnummer());
    assertEquals("konrad@example.com", geaendert.getEmailAdresse());
  }

  @Test
  @DisplayName("POST /kameraden/aendern mit fehlendem Namen oder unbekannter ID leitet mit Fehlermeldung weiter")
  void testKameradAendernFehler() {
    Kamerad k = wehrService.kameradHinzufuegen("Max", "Unveraendert", null, null, null, null);

    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("kameradId", k.getId())
        .formParam("vorname", "")
        .formParam("nachname", "Eichstädt")
        .when()
        .post("/kameraden/aendern")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?fehler=kamerad_aendern_fehlgeschlagen#kameraden"));

    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("kameradId", "unbekannte-id")
        .formParam("vorname", "Konrad")
        .formParam("nachname", "Eichstädt")
        .when()
        .post("/kameraden/aendern")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?fehler=kamerad_aendern_fehlgeschlagen#kameraden"));

    assertEquals("Unveraendert", wehrService.getKamerad(k.getId()).getNachname());
  }

  @Test
  @DisplayName("Direkter Methodenaufruf kameradAendern an Controller")
  void testDirekterMethodenaufrufAendern() {
    WehrApplicationService mockService = new WehrApplicationService();
    KameradController controller = new KameradController(mockService);
    Kamerad k = mockService.kameradHinzufuegen("Max", "Mustermann", LocalDate.of(1990, 1, 1), null, null, null);

    try (Response r1 = controller.kameradAendern(k.getId(), "Konrad", "Eichstädt", "1990-01-01",
        null, null, null, null, null, null)) {
      assertEquals(303, r1.getStatus());
      assertEquals("/?erfolg=kamerad_geaendert#kameraden", r1.getLocation().toString());
      assertEquals("Konrad", mockService.getKamerad(k.getId()).getVorname());
    }

    mockService.setAktiveWehr(null);
    try (Response r2 = controller.kameradAendern(k.getId(), "Konrad", "Eichstädt", null,
        null, null, null, null, null, null)) {
      assertEquals(303, r2.getStatus());
      assertEquals("/?fehler=kamerad_aendern_fehlgeschlagen#kameraden", r2.getLocation().toString());
    }
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
