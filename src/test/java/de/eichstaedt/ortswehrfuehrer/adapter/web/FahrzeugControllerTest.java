package de.eichstaedt.ortswehrfuehrer.adapter.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.application.WehrApplicationService;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Fahrzeugtyp;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class FahrzeugControllerTest {

  @Inject
  WehrApplicationService wehrService;

  @Test
  @DisplayName("POST /fahrzeuge mit vollständigen Daten legt Einsatzfahrzeug an und leitet weiter")
  void testFahrzeugHinzufuegenErfolgreich() {
    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("kennung", "Florian Musterstadt 1/24-1")
        .formParam("fahrzeugtyp", "TLF3000")
        .formParam("bezeichnung", "Tanklöschfahrzeug 3000")
        .when()
        .post("/fahrzeuge")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?erfolg=fahrzeug_hinzugefuegt#fahrzeuge"));

    Optional<Einsatzfahrzeug> tlf = wehrService.getFahrzeugeDerAktivenWehr().stream()
        .filter(f -> "Florian Musterstadt 1/24-1".equals(f.getKennung()))
        .findFirst();

    assertTrue(tlf.isPresent());
    assertEquals(Fahrzeugtyp.TLF3000, tlf.get().getFahrzeugtyp());
    assertEquals("Tanklöschfahrzeug 3000", tlf.get().getBezeichnung());
  }

  @Test
  @DisplayName("POST /fahrzeuge ohne Funkkennung leitet mit Fehlermeldung weiter")
  void testFahrzeugHinzufuegenFehlerOhneKennung() {
    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("kennung", "")
        .formParam("fahrzeugtyp", "HLF20")
        .when()
        .post("/fahrzeuge")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?fehler=fahrzeug_kennung_fehlt#fahrzeuge"));
  }

  @Test
  @DisplayName("POST /fahrzeuge/loeschen löscht Einsatzfahrzeug und leitet weiter")
  void testFahrzeugLoeschenErfolgreich() {
    Einsatzfahrzeug fz = wehrService.fahrzeugHinzufuegen("Florian Loeschen 1/48-1", Fahrzeugtyp.TSF, "TSF Loeschen");
    assertTrue(wehrService.getFahrzeugeDerAktivenWehr().contains(fz));

    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("fahrzeugId", fz.getId())
        .when()
        .post("/fahrzeuge/loeschen")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?erfolg=fahrzeug_geloescht#fahrzeuge"));

    assertTrue(wehrService.getFahrzeugeDerAktivenWehr().stream().noneMatch(f -> f.getId().equals(fz.getId())));
  }

  @Test
  @DisplayName("POST /fahrzeuge/entfernen entfernt Einsatzfahrzeug und leitet weiter")
  void testFahrzeugEntfernenErfolgreich() {
    Einsatzfahrzeug fz = wehrService.fahrzeugHinzufuegen("Florian Entfernen 1/48-2", Fahrzeugtyp.TSF, "TSF Entfernen");
    assertTrue(wehrService.getFahrzeugeDerAktivenWehr().contains(fz));

    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("fahrzeugId", fz.getId())
        .when()
        .post("/fahrzeuge/entfernen")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?erfolg=fahrzeug_geloescht#fahrzeuge"));

    assertTrue(wehrService.getFahrzeugeDerAktivenWehr().stream().noneMatch(f -> f.getId().equals(fz.getId())));
  }

  @Test
  @DisplayName("POST /fahrzeuge/loeschen mit leerer ID leitet mit Fehlermeldung weiter")
  void testFahrzeugLoeschenFehlerLeereId() {
    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("fahrzeugId", "")
        .when()
        .post("/fahrzeuge/loeschen")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?fehler=fahrzeug_loeschen_fehlgeschlagen#fahrzeuge"));
  }

  @Test
  @DisplayName("Direkter Methodenaufruf fahrzeugLoeschen und fahrzeugEntfernen an Controller")
  void testDirekterMethodenaufrufLoeschen() {
    WehrApplicationService mockService = new WehrApplicationService();
    FahrzeugController controller = new FahrzeugController(mockService);

    Einsatzfahrzeug fz = mockService.fahrzeugHinzufuegen("Florian Direct 1/1-1", Fahrzeugtyp.LF10, "LF 10");
    assertEquals(3, mockService.getFahrzeugeDerAktivenWehr().size());

    try (Response r1 = controller.fahrzeugLoeschen(fz.getId())) {
      assertEquals(303, r1.getStatus());
      assertEquals(2, mockService.getFahrzeugeDerAktivenWehr().size());
    }

    Einsatzfahrzeug fz2 = mockService.fahrzeugHinzufuegen("Florian Direct 1/1-2", Fahrzeugtyp.LF10, "LF 10");
    try (Response r2 = controller.fahrzeugEntfernen(fz2.getId())) {
      assertEquals(303, r2.getStatus());
      assertEquals(2, mockService.getFahrzeugeDerAktivenWehr().size());
    }

    try (Response r3 = controller.fahrzeugLoeschen("")) {
      assertEquals(303, r3.getStatus());
      assertEquals("/?fehler=fahrzeug_loeschen_fehlgeschlagen#fahrzeuge", r3.getLocation().toString());
    }
  }

  @Test
  @DisplayName("Default-Konstruktor von FahrzeugController")
  void testDefaultKonstruktor() {
    FahrzeugController defaultResource = new FahrzeugController();
    assertNotNull(defaultResource);
  }

  @Test
  @DisplayName("Direkter Methodenaufruf an FahrzeugController mit ungültigem Typ und automatischer Bezeichnung")
  void testDirekterMethodenaufruf() {
    WehrApplicationService mockService = new WehrApplicationService();
    FahrzeugController resource = new FahrzeugController(mockService);

    try (Response response = resource.fahrzeugHinzufuegen(
        "Florian Test 1/11-1",
        "UNGUELTIGER_TYP",
        ""
    )) {
      assertEquals(303, response.getStatus());
    }
    Optional<Einsatzfahrzeug> testFz = mockService.getFahrzeugeDerAktivenWehr().stream()
        .filter(f -> "Florian Test 1/11-1".equals(f.getKennung()))
        .findFirst();

    assertTrue(testFz.isPresent());
    assertNull(testFz.get().getFahrzeugtyp());
    assertEquals("Florian Test 1/11-1", testFz.get().getBezeichnung());
  }

  @Test
  @DisplayName("Direkter Methodenaufruf an FahrzeugController ohne explizite Bezeichnung übernimmt Typname")
  void testDirekterMethodenaufrufMitTypnameAlsFallback() {
    WehrApplicationService mockService = new WehrApplicationService();
    FahrzeugController resource = new FahrzeugController(mockService);

    try (Response response = resource.fahrzeugHinzufuegen(
        "Florian Test 1/44-1",
        "LF20",
        ""
    )) {
      assertEquals(303, response.getStatus());
    }
    Optional<Einsatzfahrzeug> lf20 = mockService.getFahrzeugeDerAktivenWehr().stream()
        .filter(f -> "Florian Test 1/44-1".equals(f.getKennung()))
        .findFirst();

    assertTrue(lf20.isPresent());
    assertEquals(Fahrzeugtyp.LF20, lf20.get().getFahrzeugtyp());
    assertEquals("LF20", lf20.get().getBezeichnung());
  }

  @Test
  @DisplayName("POST /fahrzeuge/aendern aktualisiert Fahrzeug und leitet weiter")
  void testFahrzeugAendernErfolgreich() {
    Einsatzfahrzeug fz = wehrService.fahrzeugHinzufuegen("Florian Vorher 1/24-1", "TSF", "Vorher TSF");

    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("fahrzeugId", fz.getId())
        .formParam("kennung", "Florian Nachher 1/24-2")
        .formParam("fahrzeugtyp", "LF10")
        .formParam("bezeichnung", "Nachher LF 10")
        .when()
        .post("/fahrzeuge/aendern")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?erfolg=fahrzeug_geaendert#fahrzeuge"));

    Einsatzfahrzeug aktualisiert = wehrService.getFahrzeug(fz.getId());
    assertNotNull(aktualisiert);
    assertEquals("Florian Nachher 1/24-2", aktualisiert.getKennung());
    assertEquals(Fahrzeugtyp.LF10, aktualisiert.getFahrzeugtyp());
    assertEquals("Nachher LF 10", aktualisiert.getBezeichnung());
  }

  @Test
  @DisplayName("POST /fahrzeuge/aendern mit leerer ID oder ungültiger Kennung leitet mit Fehlermeldung weiter")
  void testFahrzeugAendernFehler() {
    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("fahrzeugId", "")
        .formParam("kennung", "Florian 1/1-1")
        .when()
        .post("/fahrzeuge/aendern")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?fehler=fahrzeug_aendern_fehlgeschlagen#fahrzeuge"));

    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("fahrzeugId", "unbekannte-id")
        .formParam("kennung", "")
        .when()
        .post("/fahrzeuge/aendern")
        .then()
        .statusCode(303)
        .header("Location", containsString("/?fehler=fahrzeug_aendern_fehlgeschlagen#fahrzeuge"));
  }

  @Test
  @DisplayName("Direkter Aufruf fahrzeugAendern an FahrzeugController")
  void testDirekterMethodenaufrufAendern() {
    WehrApplicationService mockService = new WehrApplicationService();
    FahrzeugController controller = new FahrzeugController(mockService);

    Einsatzfahrzeug fz = mockService.fahrzeugHinzufuegen("Florian Direct 1/1-1", "TSF", "TSF");

    try (Response response = controller.fahrzeugAendern(fz.getId(), "Florian Direct 1/1-2", "TSF_W", "TSF-W")) {
      assertEquals(303, response.getStatus());
      assertEquals("/?erfolg=fahrzeug_geaendert#fahrzeuge", response.getLocation().toString());
    }

    try (Response response = controller.fahrzeugAendern("", "Florian Direct 1/1-2", "TSF_W", "TSF-W")) {
      assertEquals(303, response.getStatus());
      assertEquals("/?fehler=fahrzeug_aendern_fehlgeschlagen#fahrzeuge", response.getLocation().toString());
    }
  }
}
