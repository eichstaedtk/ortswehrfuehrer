package de.eichstaedt.ortswehrfuehrer.application.uebung;

import de.eichstaedt.ortswehrfuehrer.application.WehrApplicationService;
import de.eichstaedt.ortswehrfuehrer.domain.Ausbildung;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Kamerad;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Einsatzauftrag;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.FunktionsBesetzung;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.GuardrailPruefbericht;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.SzenarioParameter;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeEinheit;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeFunktion;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsart;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsszenario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jmolecules.ddd.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application Service für Use Case UC-001 (Übungsszenario für Übungsdienst generieren).
 */
@Service
@ApplicationScoped
public class UebungsszenarioApplicationService {

  private static final Logger log = LoggerFactory.getLogger(UebungsszenarioApplicationService.class);

  private final WehrApplicationService wehrApplicationService;
  private final SzenarioGuardrailService guardrailService;
  private final SzenarioGeneratorService generatorService;

  private final Map<String, Uebungsszenario> szenarien = new ConcurrentHashMap<>();

  @Inject
  public UebungsszenarioApplicationService(
      WehrApplicationService wehrApplicationService,
      SzenarioGuardrailService guardrailService,
      SzenarioGeneratorService generatorService
  ) {
    this.wehrApplicationService = Objects.requireNonNull(wehrApplicationService, "WehrApplicationService darf nicht null sein.");
    this.guardrailService = Objects.requireNonNull(guardrailService, "SzenarioGuardrailService darf nicht null sein.");
    this.generatorService = Objects.requireNonNull(generatorService, "SzenarioGeneratorService darf nicht null sein.");
  }

  public List<Uebungsszenario> getAlleSzenarien() {
    return szenarien.values().stream()
        .sorted(Comparator.comparing(Uebungsszenario::getErstelltAm).reversed())
        .toList();
  }

  public Optional<Uebungsszenario> findeSzenario(String id) {
    if (id == null || id.isBlank()) {
      return Optional.empty();
    }
    return Optional.ofNullable(szenarien.get(id.trim()));
  }

  public Uebungsszenario getSzenario(String id) {
    return findeSzenario(id).orElseThrow(() -> new IllegalArgumentException("Übungsszenario mit ID '" + id + "' wurde nicht gefunden."));
  }

  public Uebungsszenario neuesSzenarioStarten(Uebungsart art, String ersteller) {
    Uebungsart gewaehlteArt = art != null ? art : Uebungsart.LOESCHUEBUNG;
    String erstellerName = ersteller != null && !ersteller.isBlank() ? ersteller.trim() : "Ortswehrführer";

    Uebungsszenario szenario = Uebungsszenario.starten(gewaehlteArt, erstellerName);
    szenarien.put(szenario.getId(), szenario);
    log.info("Neues Übungsszenario '{}' gestartet (ID: {}, Art: {}).", szenario.getTitel(), szenario.getId(), szenario.getArt());
    return szenario;
  }

  public Uebungsszenario fahrzeugeZuweisen(String szenarioId, Set<String> fahrzeugIds, TaktischeEinheit einheit) {
    Uebungsszenario szenario = getSzenario(szenarioId);
    szenario.fahrzeugeSetzen(fahrzeugIds);

    TaktischeEinheit gewaehlteEinheit = einheit;
    if (gewaehlteEinheit == null) {
      // Automatische Ableitung anhand Fahrzeugtyp
      gewaehlteEinheit = ermittleTaktischeEinheitFuerFahrzeuge(fahrzeugIds);
    }
    szenario.taktischeEinheitFestlegen(gewaehlteEinheit);

    // Initialisiere Besetzungsliste passend zur Einheit, falls leer
    if (szenario.getBesetzungen().isEmpty()) {
      List<FunktionsBesetzung> defaultBesetzungen = gewaehlteEinheit.getErforderlicheFunktionen().stream()
          .map(f -> new FunktionsBesetzung(f, null, null, Set.of()))
          .toList();
      szenario.besetzungenAktualisieren(defaultBesetzungen);
    }

    pruefeGuardrails(szenario);
    return szenario;
  }

  public Uebungsszenario besetzungAktualisieren(String szenarioId, List<FunktionsBesetzung> besetzungen) {
    Uebungsszenario szenario = getSzenario(szenarioId);

    // Kameradendaten mit aktuellen Ausbildungen anreichern
    List<FunktionsBesetzung> angereicherteBesetzung = new ArrayList<>();
    if (besetzungen != null) {
      for (FunktionsBesetzung b : besetzungen) {
        if (b.istBesetzt()) {
          Kamerad k = wehrApplicationService.getKamerad(b.kameradId());
          if (k != null) {
            angereicherteBesetzung.add(new FunktionsBesetzung(
                b.funktion(),
                k.getId(),
                k.getVorname() + " " + k.getNachname(),
                k.getAusbildungen()
            ));
            continue;
          }
        }
        angereicherteBesetzung.add(b);
      }
    }
    szenario.besetzungenAktualisieren(angereicherteBesetzung);
    pruefeGuardrails(szenario);
    return szenario;
  }

  public Uebungsszenario parameterAktualisieren(String szenarioId, SzenarioParameter parameter) {
    Uebungsszenario szenario = getSzenario(szenarioId);
    szenario.parameterAktualisieren(parameter);
    pruefeGuardrails(szenario);
    return szenario;
  }

  public GuardrailPruefbericht pruefeGuardrails(String szenarioId) {
    Uebungsszenario szenario = getSzenario(szenarioId);
    return pruefeGuardrails(szenario);
  }

  private GuardrailPruefbericht pruefeGuardrails(Uebungsszenario szenario) {
    List<Einsatzfahrzeug> fahrzeuge = getFahrzeugeFuerSzenario(szenario);
    GuardrailPruefbericht bericht = guardrailService.pruefeSzenario(szenario, fahrzeuge);
    szenario.guardrailBerichtAktualisieren(bericht);
    return bericht;
  }

  public Uebungsszenario generiereSzenario(String szenarioId) {
    Uebungsszenario szenario = getSzenario(szenarioId);
    List<Einsatzfahrzeug> fahrzeuge = getFahrzeugeFuerSzenario(szenario);
    return generatorService.generiereSzenario(szenario, fahrzeuge);
  }

  public Uebungsszenario anpassenAusgangslage(String szenarioId, String ausgangslage) {
    Uebungsszenario szenario = getSzenario(szenarioId);
    szenario.anpassenAusgangslage(ausgangslage);
    return szenario;
  }

  public Uebungsszenario anpassenSchadenslage(String szenarioId, String schadenslage) {
    Uebungsszenario szenario = getSzenario(szenarioId);
    szenario.anpassenSchadenslage(schadenslage);
    return szenario;
  }

  public Uebungsszenario anpassenEinsatzauftrag(String szenarioId, int index, Einsatzauftrag auftrag) {
    Uebungsszenario szenario = getSzenario(szenarioId);
    List<Einsatzauftrag> liste = new ArrayList<>(szenario.getEinsatzauftraege());
    if (index >= 0 && index < liste.size() && auftrag != null) {
      liste.set(index, auftrag);
      szenario.anpassenEinsatzauftraege(liste);
    }
    return szenario;
  }

  public Uebungsszenario einsatzauftragHinzufuegen(String szenarioId, Einsatzauftrag auftrag) {
    Uebungsszenario szenario = getSzenario(szenarioId);
    if (auftrag != null) {
      List<Einsatzauftrag> liste = new ArrayList<>(szenario.getEinsatzauftraege());
      liste.add(auftrag);
      szenario.anpassenEinsatzauftraege(liste);
    }
    return szenario;
  }

  public Uebungsszenario regeneriereAbschnitt(String szenarioId, String abschnitt) {
    Uebungsszenario szenario = getSzenario(szenarioId);
    List<Einsatzfahrzeug> fahrzeuge = getFahrzeugeFuerSzenario(szenario);

    if ("lage".equalsIgnoreCase(abschnitt)) {
      generatorService.regeneriereLageentwicklungen(szenario, fahrzeuge);
    } else if ("auftraege".equalsIgnoreCase(abschnitt)) {
      generatorService.regeneriereEinsatzauftraege(szenario, fahrzeuge);
    } else if ("sicherheit".equalsIgnoreCase(abschnitt)) {
      generatorService.regeneriereSicherheitshinweise(szenario, fahrzeuge);
    } else if ("bewertung".equalsIgnoreCase(abschnitt)) {
      generatorService.regeneriereBewertungspunkte(szenario, fahrzeuge);
    }

    return szenario;
  }

  public Uebungsszenario freigeben(String szenarioId) {
    Uebungsszenario szenario = getSzenario(szenarioId);
    pruefeGuardrails(szenario);
    szenario.freigeben();
    log.info("Übungsszenario '{}' ({}) erfolgreich freigegeben.", szenario.getTitel(), szenario.getId());
    return szenario;
  }

  public Uebungsszenario archivieren(String szenarioId) {
    Uebungsszenario szenario = getSzenario(szenarioId);
    szenario.archivieren();
    log.info("Übungsszenario '{}' ({}) archiviert.", szenario.getTitel(), szenario.getId());
    return szenario;
  }

  public void loescheSzenario(String szenarioId) {
    if (szenarioId != null) {
      szenarien.remove(szenarioId.trim());
    }
  }

  public List<Einsatzfahrzeug> getFahrzeugeFuerSzenario(Uebungsszenario szenario) {
    if (szenario == null || szenario.getFahrzeugIds() == null) {
      return List.of();
    }
    return szenario.getFahrzeugIds().stream()
        .map(wehrApplicationService::getFahrzeug)
        .filter(Objects::nonNull)
        .toList();
  }

  public TaktischeEinheit ermittleTaktischeEinheitFuerFahrzeuge(Set<String> fahrzeugIds) {
    if (fahrzeugIds == null || fahrzeugIds.isEmpty()) {
      return TaktischeEinheit.GRUPPE;
    }
    for (String id : fahrzeugIds) {
      Einsatzfahrzeug f = wehrApplicationService.getFahrzeug(id);
      if (f != null && f.getFahrzeugtyp() != null) {
        int soll = f.getBesatzungSollStaerke();
        if (soll == 6) {
          return TaktischeEinheit.STAFFEL;
        } else if (soll == 3) {
          return TaktischeEinheit.SELBSTSTAENDIGER_TRUPP;
        }
      }
    }
    return TaktischeEinheit.GRUPPE;
  }
}
