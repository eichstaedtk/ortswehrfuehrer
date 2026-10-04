package de.eichstaedt.ortswehrfuehrer.adapter.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.quarkus.qute.TemplateInstance;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class IndexWebResourceTest {

  @Test
  @DisplayName("Startseite liefert Status 200 und HTML-Inhalt mit Dashboard-, Fahrzeug- und Kameraden-Elementen")
  void testIndexEndpoint() {
    given()
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
        .body(containsString("name=\"emailAdresse\""));
  }

  @Test
  @DisplayName("Startseite zeigt Erfolgsmeldung bei ?erfolg=kamerad_hinzugefuegt")
  void testIndexMitErfolgsmeldung() {
    given()
        .queryParam("erfolg", "kamerad_hinzugefuegt")
        .when()
        .get("/")
        .then()
        .statusCode(200)
        .contentType(ContentType.HTML)
        .body(containsString("erfolgreich aufgenommen"));
  }

  @Test
  @DisplayName("Startseite zeigt Fehlermeldung bei ?fehler=name_fehlt")
  void testIndexMitFehlermeldung() {
    given()
        .queryParam("fehler", "name_fehlt")
        .when()
        .get("/")
        .then()
        .statusCode(200)
        .contentType(ContentType.HTML)
        .body(containsString("Fehler beim Speichern"));
  }

  @Test
  @DisplayName("Direkter Aufruf der WebResource-Methode erzeugt eine valide TemplateInstance")
  void testIndexDirectMethodCall() {
    IndexWebResource webResource = new IndexWebResource();
    TemplateInstance templateInstance = webResource.index();
    assertNotNull(templateInstance);

    TemplateInstance mitParams = webResource.index("kamerad_hinzugefuegt", null);
    assertNotNull(mitParams);
  }
}
