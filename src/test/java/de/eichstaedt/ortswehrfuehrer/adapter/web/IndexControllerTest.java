package de.eichstaedt.ortswehrfuehrer.adapter.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
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
    given()
        .cookie(AnmeldungController.SITZUNG_COOKIE_NAME, sitzungsId)
        .when()
        .get("/")
        .then()
        .statusCode(200)
        .contentType(ContentType.HTML)
        .body(containsString("Ortswehrführer"))
        .body(containsString("Verwaltung der Ortswehr"))
        .body(containsString("Freiwillige Feuerwehr Musterstadt"))
        .body(containsString("Einsatzabteilungen"))
        .body(containsString("Gerätehaus Mitte"))
        .body(containsString("Zugeordnete Einsatzfahrzeuge"))
        .body(containsString("TSF_W"))
        .body(containsString("Florian Musterstadt 1/48-1"))
        .body(containsString("HLF10"))
        .body(containsString("Florian Musterstadt 1/43-1"))
        .body(not(containsString("ID: fz-tsfw-01")))
        .body(containsString("Kamerad aufnehmen"))
        .body(containsString("Kameradinnen"))
        .body(containsString("id=\"kameradModal\""))
        .body(containsString("action=\"/kameraden\""))
        .body(containsString("name=\"vorname\""))
        .body(containsString("name=\"nachname\""))
        .body(containsString("name=\"geburtsdatum\""))
        .body(containsString("name=\"strasse\""))
        .body(containsString("name=\"hausnummer\""))
        .body(containsString("name=\"postleitzahl\""))
        .body(containsString("name=\"ort\""))
        .body(containsString("name=\"telefonnummer\""))
        .body(containsString("name=\"emailAdresse\""))
        .body(containsString("id=\"fahrzeugModal\""))
        .body(containsString("action=\"/fahrzeuge\""))
        .body(containsString("name=\"bezeichnung\""))
        .body(containsString("name=\"kennung\""))
        .body(containsString("name=\"fahrzeugtyp\""))
        .body(containsString("Neues Einsatzfahrzeug zur Wehr hinzufügen"));
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
        .body(containsString("mindestens Vorname und Nachname"));
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
        .body(containsString("Funkkennung für das Einsatzfahrzeug"));
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
}
