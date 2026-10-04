package de.eichstaedt.ortswehrfuehrer.adapter.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.quarkus.qute.TemplateInstance;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class IndexWebResourceTest {

  @Test
  @DisplayName("Startseite liefert Status 200 und HTML-Inhalt mit Dashboard-Elementen")
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
        .body(containsString("htmx.org"))
        .body(containsString("bootstrap"));
  }

  @Test
  @DisplayName("Direkter Aufruf der WebResource-Methode erzeugt eine valide TemplateInstance")
  void testIndexDirectMethodCall() {
    IndexWebResource webResource = new IndexWebResource();
    TemplateInstance templateInstance = webResource.index();
    assertNotNull(templateInstance);
  }
}
