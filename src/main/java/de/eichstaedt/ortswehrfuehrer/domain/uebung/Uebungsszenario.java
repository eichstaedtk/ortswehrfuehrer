package de.eichstaedt.ortswehrfuehrer.domain.uebung;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

/**
 * Aggregat-Wurzel für ein Übungsszenario nach FwDV 3 und FwDV 2.
 */
@AggregateRoot
public class Uebungsszenario {

  @Identity
  private String id;
  private String titel;
  private Uebungsart art;
  private Uebungsstatus status;
  private LocalDateTime erstelltAm;
  private String ersteller;
  private Set<String> fahrzeugIds = new LinkedHashSet<>();
  private TaktischeEinheit taktischeEinheit;
  private List<FunktionsBesetzung> besetzungen = new ArrayList<>();
  private SzenarioParameter parameter;
  private String alarmstichwort;
  private String ausgangslage;
  private String schadenslage;
  private List<Einsatzauftrag> einsatzauftraege = new ArrayList<>();
  private List<Lageentwicklung> lageentwicklungen = new ArrayList<>();
  private List<Sicherheitshinweis> sicherheitshinweise = new ArrayList<>();
  private List<Bewertungspunkt> bewertungspunkte = new ArrayList<>();
  private GuardrailPruefbericht guardrailBericht;

  public Uebungsszenario() {
    this.id = UUID.randomUUID().toString();
    this.status = Uebungsstatus.ENTWURF;
    this.erstelltAm = LocalDateTime.now();
    this.parameter = SzenarioParameter.standard();
    this.taktischeEinheit = TaktischeEinheit.GRUPPE;
    this.guardrailBericht = GuardrailPruefbericht.erfolgreich();
  }

  public Uebungsszenario(String id, String titel, Uebungsart art, String ersteller) {
    this.id = id != null && !id.isBlank() ? id : UUID.randomUUID().toString();
    this.titel = titel != null ? titel : "Neues Übungsszenario";
    this.art = art != null ? art : Uebungsart.LOESCHUEBUNG;
    this.status = Uebungsstatus.ENTWURF;
    this.erstelltAm = LocalDateTime.now();
    this.ersteller = ersteller != null ? ersteller : "Ortswehrführer";
    this.parameter = SzenarioParameter.standard();
    this.taktischeEinheit = TaktischeEinheit.GRUPPE;
    this.guardrailBericht = GuardrailPruefbericht.erfolgreich();
  }

  public static Uebungsszenario starten(Uebungsart art, String ersteller) {
    Uebungsszenario szenario = new Uebungsszenario(UUID.randomUUID().toString(), "Übung " + (art != null ? art.getBezeichnung() : "Löschangriff"), art, ersteller);
    return szenario;
  }

  public void fahrzeugHinzufuegen(String fahrzeugId) {
    if (fahrzeugId != null && !fahrzeugId.isBlank()) {
      if (this.fahrzeugIds == null) {
        this.fahrzeugIds = new LinkedHashSet<>();
      }
      this.fahrzeugIds.add(fahrzeugId);
    }
  }

  public void fahrzeugEntfernen(String fahrzeugId) {
    if (this.fahrzeugIds != null && fahrzeugId != null) {
      this.fahrzeugIds.remove(fahrzeugId);
    }
  }

  public void fahrzeugeSetzen(Set<String> fahrzeugIds) {
    this.fahrzeugIds = fahrzeugIds != null ? new LinkedHashSet<>(fahrzeugIds) : new LinkedHashSet<>();
  }

  public void taktischeEinheitFestlegen(TaktischeEinheit taktischeEinheit) {
    this.taktischeEinheit = taktischeEinheit != null ? taktischeEinheit : TaktischeEinheit.GRUPPE;
  }

  public void besetzungenAktualisieren(List<FunktionsBesetzung> neueBesetzungen) {
    this.besetzungen = neueBesetzungen != null ? new ArrayList<>(neueBesetzungen) : new ArrayList<>();
  }

  public void parameterAktualisieren(SzenarioParameter neueParameter) {
    this.parameter = neueParameter != null ? neueParameter : SzenarioParameter.standard();
  }

  public void guardrailBerichtAktualisieren(GuardrailPruefbericht bericht) {
    this.guardrailBericht = bericht != null ? bericht : GuardrailPruefbericht.erfolgreich();
  }

  public void generieren(
      String titel,
      String alarmstichwort,
      String ausgangslage,
      String schadenslage,
      List<Einsatzauftrag> auftraege,
      List<Lageentwicklung> lageentwicklungen,
      List<Sicherheitshinweis> sicherheitshinweise,
      List<Bewertungspunkt> bewertungspunkte
  ) {
    if (titel != null && !titel.isBlank()) {
      this.titel = titel;
    }
    this.alarmstichwort = alarmstichwort != null ? alarmstichwort : "";
    this.ausgangslage = ausgangslage != null ? ausgangslage : "";
    this.schadenslage = schadenslage != null ? schadenslage : "";
    this.einsatzauftraege = auftraege != null ? new ArrayList<>(auftraege) : new ArrayList<>();
    this.lageentwicklungen = lageentwicklungen != null ? new ArrayList<>(lageentwicklungen) : new ArrayList<>();
    this.sicherheitshinweise = sicherheitshinweise != null ? new ArrayList<>(sicherheitshinweise) : new ArrayList<>();
    this.bewertungspunkte = bewertungspunkte != null ? new ArrayList<>(bewertungspunkte) : new ArrayList<>();
    this.status = Uebungsstatus.GENERIERT;
  }

  public void anpassenAusgangslage(String neueAusgangslage) {
    this.ausgangslage = neueAusgangslage != null ? neueAusgangslage : "";
  }

  public void anpassenSchadenslage(String neueSchadenslage) {
    this.schadenslage = neueSchadenslage != null ? neueSchadenslage : "";
  }

  public void anpassenEinsatzauftraege(List<Einsatzauftrag> neueAuftraege) {
    this.einsatzauftraege = neueAuftraege != null ? new ArrayList<>(neueAuftraege) : new ArrayList<>();
  }

  public void anpassenLageentwicklungen(List<Lageentwicklung> neueLageentwicklungen) {
    this.lageentwicklungen = neueLageentwicklungen != null ? new ArrayList<>(neueLageentwicklungen) : new ArrayList<>();
  }

  public void anpassenSicherheitshinweise(List<Sicherheitshinweis> neueSicherheitshinweise) {
    this.sicherheitshinweise = neueSicherheitshinweise != null ? new ArrayList<>(neueSicherheitshinweise) : new ArrayList<>();
  }

  public void anpassenBewertungspunkte(List<Bewertungspunkt> neueBewertungspunkte) {
    this.bewertungspunkte = neueBewertungspunkte != null ? new ArrayList<>(neueBewertungspunkte) : new ArrayList<>();
  }

  public void freigeben() {
    if (this.status == Uebungsstatus.ARCHIVIERT) {
      throw new IllegalStateException("Ein archiviertes Szenario kann nicht freigegeben werden.");
    }
    if (this.guardrailBericht != null && this.guardrailBericht.hatFehler()) {
      throw new IllegalStateException("Szenario kann wegen bestehender Guardrail-Fehler nicht freigegeben werden: " + this.guardrailBericht.fehler());
    }
    if (this.ausgangslage == null || this.ausgangslage.isBlank()) {
      throw new IllegalStateException("Szenario muss vor der Freigabe generiert werden.");
    }
    this.status = Uebungsstatus.FREIGEGEBEN;
  }

  public void archivieren() {
    this.status = Uebungsstatus.ARCHIVIERT;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitel() {
    return titel;
  }

  public void setTitel(String titel) {
    this.titel = titel;
  }

  public Uebungsart getArt() {
    return art;
  }

  public void setArt(Uebungsart art) {
    this.art = art;
  }

  public Uebungsstatus getStatus() {
    return status;
  }

  public void setStatus(Uebungsstatus status) {
    this.status = status;
  }

  public LocalDateTime getErstelltAm() {
    return erstelltAm;
  }

  public void setErstelltAm(LocalDateTime erstelltAm) {
    this.erstelltAm = erstelltAm;
  }

  public String getErsteller() {
    return ersteller;
  }

  public void setErsteller(String ersteller) {
    this.ersteller = ersteller;
  }

  public Set<String> getFahrzeugIds() {
    return Collections.unmodifiableSet(fahrzeugIds != null ? fahrzeugIds : Collections.emptySet());
  }

  public TaktischeEinheit getTaktischeEinheit() {
    return taktischeEinheit;
  }

  public void setTaktischeEinheit(TaktischeEinheit taktischeEinheit) {
    this.taktischeEinheit = taktischeEinheit;
  }

  public List<FunktionsBesetzung> getBesetzungen() {
    return Collections.unmodifiableList(besetzungen != null ? besetzungen : Collections.emptyList());
  }

  public SzenarioParameter getParameter() {
    return parameter;
  }

  public void setParameter(SzenarioParameter parameter) {
    this.parameter = parameter;
  }

  public String getAlarmstichwort() {
    return alarmstichwort;
  }

  public void setAlarmstichwort(String alarmstichwort) {
    this.alarmstichwort = alarmstichwort;
  }

  public String getAusgangslage() {
    return ausgangslage;
  }

  public void setAusgangslage(String ausgangslage) {
    this.ausgangslage = ausgangslage;
  }

  public String getSchadenslage() {
    return schadenslage;
  }

  public void setSchadenslage(String schadenslage) {
    this.schadenslage = schadenslage;
  }

  public List<Einsatzauftrag> getEinsatzauftraege() {
    return Collections.unmodifiableList(einsatzauftraege != null ? einsatzauftraege : Collections.emptyList());
  }

  public List<Lageentwicklung> getLageentwicklungen() {
    return Collections.unmodifiableList(lageentwicklungen != null ? lageentwicklungen : Collections.emptyList());
  }

  public List<Sicherheitshinweis> getSicherheitshinweise() {
    return Collections.unmodifiableList(sicherheitshinweise != null ? sicherheitshinweise : Collections.emptyList());
  }

  public List<Bewertungspunkt> getBewertungspunkte() {
    return Collections.unmodifiableList(bewertungspunkte != null ? bewertungspunkte : Collections.emptyList());
  }

  public GuardrailPruefbericht getGuardrailBericht() {
    return guardrailBericht;
  }

  public void setGuardrailBericht(GuardrailPruefbericht guardrailBericht) {
    this.guardrailBericht = guardrailBericht;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Uebungsszenario that = (Uebungsszenario) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
