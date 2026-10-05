package de.eichstaedt.ortswehrfuehrer.application.uebung;

import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Bewertungspunkt;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Einsatzauftrag;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.GuardrailPruefbericht;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Lageentwicklung;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Sicherheitshinweis;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsart;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsszenario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Objects;

/**
 * Service zur Synthese von FwDV-3-konformen Übungsszenarien.
 */
@ApplicationScoped
public class SzenarioGeneratorService {

  private final SzenarioKatalog katalog;
  private final SzenarioGuardrailService guardrailService;

  @Inject
  public SzenarioGeneratorService(SzenarioKatalog katalog, SzenarioGuardrailService guardrailService) {
    this.katalog = Objects.requireNonNull(katalog, "SzenarioKatalog darf nicht null sein.");
    this.guardrailService = Objects.requireNonNull(guardrailService, "SzenarioGuardrailService darf nicht null sein.");
  }

  public Uebungsszenario generiereSzenario(Uebungsszenario szenario, List<Einsatzfahrzeug> ausgewaehlteFahrzeuge) {
    if (szenario == null) {
      throw new IllegalArgumentException("Übungsszenario darf nicht null sein.");
    }
    List<Einsatzfahrzeug> fahrzeuge = ausgewaehlteFahrzeuge != null ? ausgewaehlteFahrzeuge : List.of();

    // 1. Guardrail-Prüfung
    GuardrailPruefbericht bericht = guardrailService.pruefeSzenario(szenario, fahrzeuge);
    szenario.guardrailBerichtAktualisieren(bericht);

    if (bericht.hatFehler()) {
      throw new IllegalStateException("Szenario kann wegen bestehender Guardrail-Fehler nicht generiert werden: " + String.join("; ", bericht.fehler()));
    }

    // 2. Synthese anhand der Fachvorlagen
    SzenarioKatalog.VorlagenPaket paket;
    if (szenario.getArt() == Uebungsart.TECHNISCHE_HILFELEISTUNG) {
      paket = katalog.erstelleTechnischeHilfeleistung(
          szenario.getTaktischeEinheit(),
          szenario.getBesetzungen(),
          fahrzeuge,
          szenario.getParameter(),
          bericht
      );
    } else {
      paket = katalog.erstelleLoeschuebung(
          szenario.getTaktischeEinheit(),
          szenario.getBesetzungen(),
          fahrzeuge,
          szenario.getParameter(),
          bericht
      );
    }

    // 3. Szenario mit den generierten Inhalten befüllen
    szenario.generieren(
        paket.titel(),
        paket.alarmstichwort(),
        paket.ausgangslage(),
        paket.schadenslage(),
        paket.auftraege(),
        paket.lageentwicklungen(),
        paket.sicherheitshinweise(),
        paket.bewertungspunkte()
    );

    return szenario;
  }

  public void regeneriereLageentwicklungen(Uebungsszenario szenario, List<Einsatzfahrzeug> fahrzeuge) {
    if (szenario == null) {
      return;
    }
    SzenarioKatalog.VorlagenPaket paket = erstellePaket(szenario, fahrzeuge);
    szenario.anpassenLageentwicklungen(paket.lageentwicklungen());
  }

  public void regeneriereEinsatzauftraege(Uebungsszenario szenario, List<Einsatzfahrzeug> fahrzeuge) {
    if (szenario == null) {
      return;
    }
    SzenarioKatalog.VorlagenPaket paket = erstellePaket(szenario, fahrzeuge);
    szenario.anpassenEinsatzauftraege(paket.auftraege());
  }

  public void regeneriereSicherheitshinweise(Uebungsszenario szenario, List<Einsatzfahrzeug> fahrzeuge) {
    if (szenario == null) {
      return;
    }
    SzenarioKatalog.VorlagenPaket paket = erstellePaket(szenario, fahrzeuge);
    szenario.anpassenSicherheitshinweise(paket.sicherheitshinweise());
  }

  public void regeneriereBewertungspunkte(Uebungsszenario szenario, List<Einsatzfahrzeug> fahrzeuge) {
    if (szenario == null) {
      return;
    }
    SzenarioKatalog.VorlagenPaket paket = erstellePaket(szenario, fahrzeuge);
    szenario.anpassenBewertungspunkte(paket.bewertungspunkte());
  }

  private SzenarioKatalog.VorlagenPaket erstellePaket(Uebungsszenario szenario, List<Einsatzfahrzeug> fahrzeuge) {
    GuardrailPruefbericht bericht = szenario.getGuardrailBericht() != null
        ? szenario.getGuardrailBericht()
        : guardrailService.pruefeSzenario(szenario, fahrzeuge);

    if (szenario.getArt() == Uebungsart.TECHNISCHE_HILFELEISTUNG) {
      return katalog.erstelleTechnischeHilfeleistung(
          szenario.getTaktischeEinheit(),
          szenario.getBesetzungen(),
          fahrzeuge,
          szenario.getParameter(),
          bericht
      );
    } else {
      return katalog.erstelleLoeschuebung(
          szenario.getTaktischeEinheit(),
          szenario.getBesetzungen(),
          fahrzeuge,
          szenario.getParameter(),
          bericht
      );
    }
  }
}
