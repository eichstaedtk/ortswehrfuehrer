package de.eichstaedt.ortswehrfuehrer.adapter.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.application.WehrApplicationService;
import de.eichstaedt.ortswehrfuehrer.application.uebung.UebungsszenarioApplicationService;
import de.eichstaedt.ortswehrfuehrer.domain.Ausbildung;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Kamerad;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.FunktionsBesetzung;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeEinheit;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeFunktion;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsart;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsstatus;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsszenario;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class UebungsszenarioControllerTest {

  @Inject
  UebungsszenarioApplicationService uebungService;

  @Inject
  WehrApplicationService wehrService;

  @Test
  @DisplayName("GET /uebungen liefert HTML-Übersichtsseite mit Status 200")
  void testUebersichtListe() {
    given()
        .when()
        .get("/uebungen")
        .then()
        .statusCode(200)
        .contentType(ContentType.HTML)
        .body(containsString("Übungsdienst & Einsatzszenarien"));
  }

  @Test
  @DisplayName("GET /uebungen/neu liefert Wizard Schritt 1 mit Status 200")
  void testWizardSchritt1() {
    given()
        .when()
        .get("/uebungen/neu")
        .then()
        .statusCode(200)
        .contentType(ContentType.HTML)
        .body(containsString("Schritt 1: Übungsart & Fahrzeugauswahl"))
        .body(containsString("Löschübung"))
        .body(containsString("Technische Hilfeleistung"));
  }

  @Test
  @DisplayName("POST /uebungen/start erstellt Szenario und leitet zu Schritt 2 weiter")
  void testWizardStart() {
    Einsatzfahrzeug fz = wehrService.getAlleFahrzeuge().getFirst();

    String location = given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("art", "LOESCHUEBUNG")
        .formParam("fahrzeugIds", fz.getId())
        .formParam("taktischeEinheit", "STAFFEL")
        .when()
        .post("/uebungen/start")
        .then()
        .statusCode(303)
        .extract().header("Location");

    assertNotNull(location);
    assertTrue(location.contains("/uebungen/") && location.endsWith("/besetzung"));
  }

  @Test
  @DisplayName("Vollständiger Wizard-Durchlauf über alle Web-Routen")
  void testVollstaendigerWizardAblauf() {
    // 1. Initialisiere Szenario
    Uebungsszenario szenario = uebungService.neuesSzenarioStarten(Uebungsart.LOESCHUEBUNG, "Ortswehrführer");
    Einsatzfahrzeug fz = wehrService.getAlleFahrzeuge().getFirst();
    uebungService.fahrzeugeZuweisen(szenario.getId(), Set.of(fz.getId()), TaktischeEinheit.STAFFEL);

    // 2. GET /uebungen/{id}/besetzung
    given()
        .when()
        .get("/uebungen/" + szenario.getId() + "/besetzung")
        .then()
        .statusCode(200)
        .body(containsString("Schritt 2: Besetzungsmatrix"))
        .body(containsString("Guardrail-Prüfung"));

    // Kameraden vorbereiten
    Kamerad gf = wehrService.getAlleKameraden().stream().filter(k -> k.hatAusbildung(Ausbildung.GRUPPENFUEHRER)).findFirst().orElseThrow();
    Kamerad agt = wehrService.getAlleKameraden().stream().filter(k -> k.hatAusbildung(Ausbildung.ATEMSCHUTZGERAETETRAEGER)).findFirst().orElseThrow();
    Kamerad ma = wehrService.kameradHinzufuegen(Kamerad.builder().vorname("Klaus").nachname("M").mitAusbildung(Ausbildung.MASCHINIST).build());

    // 3. POST /uebungen/{id}/besetzung
    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("funktionen", "EINHEITSFUEHRER")
        .formParam("kameradIds", gf.getId())
        .formParam("funktionen", "MASCHINIST")
        .formParam("kameradIds", ma.getId())
        .formParam("funktionen", "ANGRIFFSTRUPPFUEHRER")
        .formParam("kameradIds", agt.getId())
        .formParam("funktionen", "ANGRIFFSTRUPPMANN")
        .formParam("kameradIds", "")
        .formParam("funktionen", "WASSERTRUPPFUEHRER")
        .formParam("kameradIds", "")
        .formParam("funktionen", "WASSERTRUPPMANN")
        .formParam("kameradIds", "")
        .when()
        .post("/uebungen/" + szenario.getId() + "/besetzung")
        .then()
        .statusCode(303)
        .header("Location", containsString("/uebungen/" + szenario.getId() + "/parameter"));

    // 4. GET /uebungen/{id}/parameter
    given()
        .when()
        .get("/uebungen/" + szenario.getId() + "/parameter")
        .then()
        .statusCode(200)
        .body(containsString("Schritt 3: Rahmenparameter"));

    // 5. POST /uebungen/{id}/parameter -> Generierung
    given()
        .redirects().follow(false)
        .contentType(ContentType.URLENC)
        .formParam("dauerMinuten", 90)
        .formParam("schwierigkeit", "MITTEL")
        .formParam("tageszeit", "TAG")
        .formParam("ortObjekt", "Dorfstraße 44")
        .formParam("lernziele", "FwDV 3, FwDV 7")
        .when()
        .post("/uebungen/" + szenario.getId() + "/parameter")
        .then()
        .statusCode(303)
        .header("Location", containsString("/uebungen/" + szenario.getId() + "?erfolg=szenario_generiert"));

    // 6. GET /uebungen/{id} (Vorschau)
    given()
        .when()
        .get("/uebungen/" + szenario.getId())
        .then()
        .statusCode(200)
        .body(containsString("Ausgangs- & Schadenslage"))
        .body(containsString("Einsatzaufträge nach FwDV 3"))
        .body(containsString("Lageentwicklung & Einspielungen"));

    // 7. POST /uebungen/{id}/freigeben
    given()
        .redirects().follow(false)
        .when()
        .post("/uebungen/" + szenario.getId() + "/freigeben")
        .then()
        .statusCode(303)
        .header("Location", containsString("erfolg=szenario_freigegeben"));

    assertEquals(Uebungsstatus.FREIGEGEBEN, uebungService.getSzenario(szenario.getId()).getStatus());

    // 8. GET /uebungen/{id}/export/pdf (Druckansicht)
    given()
        .when()
        .get("/uebungen/" + szenario.getId() + "/export/pdf")
        .then()
        .statusCode(200)
        .body(containsString("ÜBUNG"))
        .body(containsString("ROLLENKARTEN"));

    // 9. POST /uebungen/{id}/loeschen
    given()
        .redirects().follow(false)
        .when()
        .post("/uebungen/" + szenario.getId() + "/loeschen")
        .then()
        .statusCode(303)
        .header("Location", containsString("/uebungen?erfolg=szenario_geloescht"));
  }
}
