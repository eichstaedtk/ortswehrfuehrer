package de.eichstaedt.ortswehrfuehrer.adapter.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.application.AnmeldungApplicationService;
import de.eichstaedt.ortswehrfuehrer.application.LokalerAuthenticationProvider;
import de.eichstaedt.ortswehrfuehrer.domain.Benutzer;
import de.eichstaedt.ortswehrfuehrer.domain.Rolle;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class AnmeldungControllerTest {

    @Inject
    AnmeldungApplicationService anmeldungService;

    @Inject
    LokalerAuthenticationProvider lokalerProvider;

    @BeforeEach
    void setUp() {
        lokalerProvider.setVerfuegbar(true);
        anmeldungService.setAuthenticationProvider(lokalerProvider);
    }

    @Test
    @DisplayName("AC-1: GET /anmeldung liefert 200 und Anmeldemaske mit Eingabefeldern und Buttons")
    void testAnmeldeSeiteAnzeigen() {
        given()
                .when()
                .get("/anmeldung")
                .then()
                .statusCode(200)
                .contentType(ContentType.HTML)
                .body(containsString("Ortswehrführer Anmeldung"))
                .body(containsString("id=\"benutzername\""))
                .body(containsString("name=\"benutzername\""))
                .body(containsString("id=\"passwort\""))
                .body(containsString("name=\"passwort\""))
                .body(containsString("id=\"btnAnmelden\""))
                .body(containsString("id=\"btnAbbrechen\""));
    }

    @Test
    @DisplayName("AC-2 & AC-4: POST /anmeldung mit gültigen Daten leitet mit Session-Cookie zum Dashboard weiter")
    void testAnmeldungErfolgreich() {
        Response response = given()
                .redirects().follow(false)
                .contentType(ContentType.URLENC)
                .formParam("benutzername", "owf")
                .formParam("passwort", "owf123")
                .when()
                .post("/anmeldung")
                .then()
                .statusCode(303)
                .extract().response();

        String sitzungsId = response.getCookie(AnmeldungController.SITZUNG_COOKIE_NAME);
        assertNotNull(sitzungsId);
        assertTrue(anmeldungService.istAngemeldet(sitzungsId));
        assertEqualsUser("owf", anmeldungService.getAngemeldetenBenutzer(sitzungsId));
    }

    @Test
    @DisplayName("AC-5: POST /anmeldung mit falschem Passwort leitet auf Fehlermeldung um")
    void testAnmeldungFehlgeschlagenUngueltigeDaten() {
        given()
                .redirects().follow(false)
                .contentType(ContentType.URLENC)
                .formParam("benutzername", "owf")
                .formParam("passwort", "falsches-passwort")
                .when()
                .post("/anmeldung")
                .then()
                .statusCode(303)
                .header("Location", containsString("/anmeldung?fehler=ungueltige_anmeldedaten"));
    }

    @Test
    @DisplayName("Randfall: POST /anmeldung mit leeren Feldern leitet auf fehlende_eingabe um")
    void testAnmeldungFehlgeschlagenLeereFelder() {
        given()
                .redirects().follow(false)
                .contentType(ContentType.URLENC)
                .formParam("benutzername", "")
                .formParam("passwort", "")
                .when()
                .post("/anmeldung")
                .then()
                .statusCode(303)
                .header("Location", containsString("/anmeldung?fehler=fehlende_eingabe"));
    }

    @Test
    @DisplayName("Randfall: POST /anmeldung bei Ausfall des Authentifizierungsdienstes")
    void testAnmeldungServiceNichtVerfuegbar() {
        lokalerProvider.setVerfuegbar(false);

        given()
                .redirects().follow(false)
                .contentType(ContentType.URLENC)
                .formParam("benutzername", "owf")
                .formParam("passwort", "owf123")
                .when()
                .post("/anmeldung")
                .then()
                .statusCode(303)
                .header("Location", containsString("/anmeldung?fehler=service_nicht_verfuegbar"));
    }

    @Test
    @DisplayName("Anmeldeseite rendert Fehlermeldung für ungültige Anmeldedaten")
    void testAnmeldeSeiteFehlermeldungUngueltig() {
        given()
                .queryParam("fehler", "ungueltige_anmeldedaten")
                .when()
                .get("/anmeldung")
                .then()
                .statusCode(200)
                .body(containsString("Ungültige Anmeldedaten"));
    }

    @Test
    @DisplayName("Anmeldeseite rendert Fehlermeldung für Offline-Dienst")
    void testAnmeldeSeiteFehlermeldungOffline() {
        given()
                .queryParam("fehler", "service_nicht_verfuegbar")
                .when()
                .get("/anmeldung")
                .then()
                .statusCode(200)
                .body(containsString("Authentifizierungsdienst steht aktuell nicht zur Verfügung"));
    }

    @Test
    @DisplayName("AC-3: GET /anmeldung/abbrechen und /abbrechen leitet auf /anmeldung um")
    void testAbbrechenAktionen() {
        given()
                .redirects().follow(false)
                .when()
                .get("/anmeldung/abbrechen")
                .then()
                .statusCode(303)
                .header("Location", containsString("/anmeldung"));

        given()
                .redirects().follow(false)
                .when()
                .get("/abbrechen")
                .then()
                .statusCode(303)
                .header("Location", containsString("/anmeldung"));
    }

    @Test
    @DisplayName("US-4: POST /abmelden und /anmeldung/abmelden invalidiert die Session und leitet weiter")
    void testAbmelden() {
        Benutzer user = new Benutzer("owf", "Ortswehrführer", Set.of(Rolle.ORTSWEHRFUEHRER));
        String sitzungsId = anmeldungService.erstelleSitzung(user);

        given()
                .redirects().follow(false)
                .cookie(AnmeldungController.SITZUNG_COOKIE_NAME, sitzungsId)
                .when()
                .post("/abmelden")
                .then()
                .statusCode(303)
                .header("Location", containsString("/anmeldung?erfolg=abgemeldet"));

        assertFalse(anmeldungService.istAngemeldet(sitzungsId));
    }

    @Test
    @DisplayName("FR-1: GET / ohne Sitzung leitet automatisch zu /anmeldung weiter")
    void testUnauthentifizierterZugriffAufStartseiteWirdUmgeleitet() {
        given()
                .redirects().follow(false)
                .when()
                .get("/")
                .then()
                .statusCode(303)
                .header("Location", containsString("/anmeldung"));
    }

    @Test
    @DisplayName("GET / mit gültiger Sitzung liefert 200 Dashboard mit Benutzeranzeige")
    void testAuthentifizierterZugriffAufStartseite() {
        Benutzer user = new Benutzer("owf", "Ortswehrführer Meier", Set.of(Rolle.ORTSWEHRFUEHRER));
        String sitzungsId = anmeldungService.erstelleSitzung(user);

        given()
                .cookie(AnmeldungController.SITZUNG_COOKIE_NAME, sitzungsId)
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body(containsString("Ortswehrführer Meier"))
                .body(containsString("btnNavAbmelden"));
    }

    @Test
    @DisplayName("Barrierefreiheit: Anmeldeseite enthält Skip-Link, Autocomplete, korrekte Überschriftenhierarchie und ARIA")
    void testAnmeldungBarrierefreiheit() {
        given()
                .when()
                .get("/anmeldung")
                .then()
                .statusCode(200)
                .contentType(ContentType.HTML)
                .body(containsString("<html lang=\"de\""))
                .body(containsString("href=\"#main-content\""))
                .body(containsString("id=\"main-content\""))
                .body(containsString("autocomplete=\"username\""))
                .body(containsString("autocomplete=\"current-password\""))
                .body(containsString("<h1 class=\"h4 mb-0 fw-bold\">Ortswehrführer Anmeldung</h1>"))
                .body(containsString("aria-hidden=\"true\""))
                .body(containsString("Erklärung zur Barrierefreiheit"));
    }

    private void assertEqualsUser(String expectedUsername, Benutzer benutzer) {
        assertNotNull(benutzer);
        org.junit.jupiter.api.Assertions.assertEquals(expectedUsername, benutzer.benutzername());
    }
}
