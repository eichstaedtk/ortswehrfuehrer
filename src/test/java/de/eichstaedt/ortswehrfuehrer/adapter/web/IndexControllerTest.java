package de.eichstaedt.ortswehrfuehrer.adapter.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.eichstaedt.ortswehrfuehrer.application.AnmeldungApplicationService;
import de.eichstaedt.ortswehrfuehrer.domain.Benutzer;
import de.eichstaedt.ortswehrfuehrer.domain.Rolle;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class IndexControllerTest {

  @Inject
  AnmeldungApplicationService anmeldungService;

  private String sitzungsId;

  @BeforeEach
  void setUp() {
    Benutzer benutzer = new Benutzer("owf", "Ortswehrführer Meier", Set.of(Rolle.ORTSWEHRFUEHRER));
    sitzungsId = anmeldungService.erstelleSitzung(benutzer);
  }

  @Test
  @DisplayName("Unauthentifizierter Zugriff auf Startseite leitet zu /anmeldung um")
  void testIndexUnauthenticatedRedirect() {
    given()
        .redirects().follow(false)
        .when()
        .get("/")
        .then()
        .statusCode(303)
        .header("Location", containsString("/anmeldung"));
  }

  @Test
  @DisplayName("Startseite liefert Status 200 und HTML-Inhalt mit Dashboard-, Fahrzeug- und Kameraden-Elementen für authentifizierten Benutzer")
  void testIndexEndpoint() {
    String html = given()
        .cookie(AnmeldungController.SITZUNG_COOKIE_NAME, sitzungsId)
        .when()
        .get("/")
        .then()
        .statusCode(200)
        .contentType(ContentType.HTML)
        .extract().asString();

    org.junit.jupiter.api.Assertions.assertTrue(html.contains("Ortswehrführer"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("Verwaltung der"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("Freiwillige Feuerwehr Göttlin"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("Einsatzabteilungen"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("Gerätehaus Göttliner Chaussee"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("Zugeordnete Einsatzfahrzeuge"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("TSF"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("HVL/5/467/2"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("TLF4000"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("HVL/5/24/3"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("Kamerad aufnehmen"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("Ausbildungen (FwDV 2)"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("Truppmann Teil 1"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("id=\"kameradModal\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("action=\"/kameraden\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"vorname\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"nachname\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"geburtsdatum\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"strasse\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"hausnummer\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"postleitzahl\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"ort\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"telefonnummer\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"emailAdresse\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"ausbildungen\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("id=\"fahrzeugModal\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("action=\"/fahrzeuge\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"bezeichnung\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"kennung\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("name=\"fahrzeugtyp\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("Neues Einsatzfahrzeug zur Wehr"));
  }

  @Test
  @DisplayName("Startseite zeigt Erfolgsmeldung bei ?erfolg=kamerad_hinzugefuegt")
  void testIndexMitErfolgsmeldungKamerad() {
    given()
        .cookie(AnmeldungController.SITZUNG_COOKIE_NAME, sitzungsId)
        .queryParam("erfolg", "kamerad_hinzugefuegt")
        .when()
        .get("/")
        .then()
        .statusCode(200)
        .contentType(ContentType.HTML)
        .body(containsString("Kamerad / die Kameradin wurde erfolgreich aufgenommen"));
  }

  @Test
  @DisplayName("Startseite zeigt Erfolgsmeldung bei ?erfolg=fahrzeug_hinzugefuegt")
  void testIndexMitErfolgsmeldungFahrzeug() {
    given()
        .cookie(AnmeldungController.SITZUNG_COOKIE_NAME, sitzungsId)
        .queryParam("erfolg", "fahrzeug_hinzugefuegt")
        .when()
        .get("/")
        .then()
        .statusCode(200)
        .contentType(ContentType.HTML)
        .body(containsString("Einsatzfahrzeug wurde erfolgreich angelegt"));
  }

  @Test
  @DisplayName("Startseite zeigt Fehlermeldung bei ?fehler=name_fehlt")
  void testIndexMitFehlermeldungName() {
    given()
        .cookie(AnmeldungController.SITZUNG_COOKIE_NAME, sitzungsId)
        .queryParam("fehler", "name_fehlt")
        .when()
        .get("/")
        .then()
        .statusCode(200)
        .contentType(ContentType.HTML)
        .body(containsString("Fehler beim Speichern"))
        .body(containsString("mindestens Vorname und"));
  }

  @Test
  @DisplayName("Startseite zeigt Fehlermeldung bei ?fehler=fahrzeug_kennung_fehlt")
  void testIndexMitFehlermeldungFahrzeugKennung() {
    given()
        .cookie(AnmeldungController.SITZUNG_COOKIE_NAME, sitzungsId)
        .queryParam("fehler", "fahrzeug_kennung_fehlt")
        .when()
        .get("/")
        .then()
        .statusCode(200)
        .contentType(ContentType.HTML)
        .body(containsString("Fehler beim Speichern"))
        .body(containsString("Funkkennung für das"));
  }

  @Test
  @DisplayName("Direkter Aufruf der Controller-Methode erzeugt eine valide TemplateInstance")
  void testIndexDirectMethodCall() {
    IndexController controller = new IndexController();
    TemplateInstance templateInstance = controller.index();
    assertNotNull(templateInstance);

    TemplateInstance mitParams = controller.index("kamerad_hinzugefuegt", null);
    assertNotNull(mitParams);
  }

  @Test
  @DisplayName("Barrierefreiheit: Startseite enthält Skip-Link, Überschriftenhierarchie, for/id-Verknüpfungen, Autocomplete und ARIA")
  void testIndexBarrierefreiheit() {
    String html = given()
        .cookie(AnmeldungController.SITZUNG_COOKIE_NAME, sitzungsId)
        .when()
        .get("/")
        .then()
        .statusCode(200)
        .contentType(ContentType.HTML)
        .extract().asString();

    org.junit.jupiter.api.Assertions.assertTrue(html.contains("<html lang=\"de\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("href=\"#main-content\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("id=\"main-content\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("Statistik-Übersicht"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("autocomplete=\"given-name\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("autocomplete=\"family-name\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("autocomplete=\"bday\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("autocomplete=\"street-address\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("aria-label=\"Kamerad"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("aria-label=\"Einsatzfahrzeug"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("aria-hidden=\"true\""));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("id=\"editKennung_"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("for=\"editKennung_"));
    org.junit.jupiter.api.Assertions.assertTrue(html.contains("Erklärung zur Barrierefreiheit"));
  }
}
