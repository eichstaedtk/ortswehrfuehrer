package de.eichstaedt.ortswehrfuehrer.domain;

import java.util.Objects;
import java.util.UUID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

/**
 * Created by konrad.eichstaedt@gmx.de on 03.10.26.
 * <p>
 * This Class represents a fire service vehicle (Einsatzfahrzeug) as Aggregate Root according to DIN 14530.
 */
@AggregateRoot
public class Einsatzfahrzeug {

  @Identity
  private String id;

  private String bezeichnung;

  private String kennung;

  private Fahrzeugtyp fahrzeugtyp;

  private String dinNorm;

  private String masseklasse;

  private String zulassigeGesamtmasse;

  private String besatzung;

  private String loeschwasser;

  private String schaummittel;

  private String pumpe;

  private Abmessungen abmessungen;

  private String fahrgestellKategorie;

  private String antrieb;

  private String fuehrerschein;

  public Einsatzfahrzeug() {
  }

  public Einsatzfahrzeug(String bezeichnung, String kennung, Fahrzeugtyp fahrzeugtyp) {
    this(UUID.randomUUID().toString(), bezeichnung, kennung, fahrzeugtyp);
  }

  public Einsatzfahrzeug(String id, String bezeichnung, String kennung, Fahrzeugtyp fahrzeugtyp) {
    this.id = id != null ? id : UUID.randomUUID().toString();
    this.bezeichnung = bezeichnung;
    this.kennung = kennung;
    this.fahrzeugtyp = fahrzeugtyp;
  }

  private Einsatzfahrzeug(Builder builder) {
    this.id = builder.id != null ? builder.id : UUID.randomUUID().toString();
    this.bezeichnung = builder.bezeichnung;
    this.kennung = builder.kennung;
    this.fahrzeugtyp = builder.fahrzeugtyp;
    this.dinNorm = builder.dinNorm;
    this.masseklasse = builder.masseklasse;
    this.zulassigeGesamtmasse = builder.zulassigeGesamtmasse;
    this.besatzung = builder.besatzung;
    this.loeschwasser = builder.loeschwasser;
    this.schaummittel = builder.schaummittel;
    this.pumpe = builder.pumpe;
    this.abmessungen = builder.abmessungen;
    this.fahrgestellKategorie = builder.fahrgestellKategorie;
    this.antrieb = builder.antrieb;
    this.fuehrerschein = builder.fuehrerschein;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String id;
    private String bezeichnung;
    private String kennung;
    private Fahrzeugtyp fahrzeugtyp;
    private String dinNorm;
    private String masseklasse;
    private String zulassigeGesamtmasse;
    private String besatzung;
    private String loeschwasser;
    private String schaummittel;
    private String pumpe;
    private Abmessungen abmessungen;
    private String fahrgestellKategorie;
    private String antrieb;
    private String fuehrerschein;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder mitId(String id) {
      return id(id);
    }

    public Builder bezeichnung(String bezeichnung) {
      this.bezeichnung = bezeichnung;
      return this;
    }

    public Builder mitBezeichnung(String bezeichnung) {
      return bezeichnung(bezeichnung);
    }

    public Builder kennung(String kennung) {
      this.kennung = kennung;
      return this;
    }

    public Builder mitKennung(String kennung) {
      return kennung(kennung);
    }

    public Builder fahrzeugtyp(Fahrzeugtyp fahrzeugtyp) {
      this.fahrzeugtyp = fahrzeugtyp;
      return this;
    }

    public Builder mitFahrzeugtyp(Fahrzeugtyp fahrzeugtyp) {
      return fahrzeugtyp(fahrzeugtyp);
    }

    public Builder fahrzeugTyp(Fahrzeugtyp fahrzeugTyp) {
      return fahrzeugtyp(fahrzeugTyp);
    }

    public Builder mitFahrzeugTyp(Fahrzeugtyp fahrzeugTyp) {
      return fahrzeugtyp(fahrzeugTyp);
    }

    public Builder dinNorm(String dinNorm) {
      this.dinNorm = dinNorm;
      return this;
    }

    public Builder mitDinNorm(String dinNorm) {
      return dinNorm(dinNorm);
    }

    public Builder din14530(String din14530) {
      this.dinNorm = din14530;
      return this;
    }

    public Builder mitDin14530(String din14530) {
      return din14530(din14530);
    }

    public Builder masseklasse(String masseklasse) {
      this.masseklasse = masseklasse;
      return this;
    }

    public Builder mitMasseklasse(String masseklasse) {
      return masseklasse(masseklasse);
    }

    public Builder zulassigeGesamtmasse(String zulassigeGesamtmasse) {
      this.zulassigeGesamtmasse = zulassigeGesamtmasse;
      return this;
    }

    public Builder mitZulassigeGesamtmasse(String zulassigeGesamtmasse) {
      return zulassigeGesamtmasse(zulassigeGesamtmasse);
    }

    public Builder gesamtmasse(String gesamtmasse) {
      return zulassigeGesamtmasse(gesamtmasse);
    }

    public Builder mitGesamtmasse(String gesamtmasse) {
      return zulassigeGesamtmasse(gesamtmasse);
    }

    public Builder besatzung(String besatzung) {
      this.besatzung = besatzung;
      return this;
    }

    public Builder mitBesatzung(String besatzung) {
      return besatzung(besatzung);
    }

    public Builder loeschwasser(String loeschwasser) {
      this.loeschwasser = loeschwasser;
      return this;
    }

    public Builder mitLoeschwasser(String loeschwasser) {
      return loeschwasser(loeschwasser);
    }

    public Builder schaummittel(String schaummittel) {
      this.schaummittel = schaummittel;
      return this;
    }

    public Builder mitSchaummittel(String schaummittel) {
      return schaummittel(schaummittel);
    }

    public Builder pumpe(String pumpe) {
      this.pumpe = pumpe;
      return this;
    }

    public Builder mitPumpe(String pumpe) {
      return pumpe(pumpe);
    }

    public Builder abmessungen(Abmessungen abmessungen) {
      this.abmessungen = abmessungen;
      return this;
    }

    public Builder mitAbmessungen(Abmessungen abmessungen) {
      return abmessungen(abmessungen);
    }

    public Builder abmessungen(Double laengeMax, Double breiteMax, Double hoeheMax) {
      this.abmessungen = new Abmessungen(laengeMax, breiteMax, hoeheMax);
      return this;
    }

    public Builder mitAbmessungen(Double laengeMax, Double breiteMax, Double hoeheMax) {
      return abmessungen(laengeMax, breiteMax, hoeheMax);
    }

    public Builder fahrgestellKategorie(String fahrgestellKategorie) {
      this.fahrgestellKategorie = fahrgestellKategorie;
      return this;
    }

    public Builder mitFahrgestellKategorie(String fahrgestellKategorie) {
      return fahrgestellKategorie(fahrgestellKategorie);
    }

    public Builder antrieb(String antrieb) {
      this.antrieb = antrieb;
      return this;
    }

    public Builder mitAntrieb(String antrieb) {
      return antrieb(antrieb);
    }

    public Builder fuehrerschein(String fuehrerschein) {
      this.fuehrerschein = fuehrerschein;
      return this;
    }

    public Builder mitFuehrerschein(String fuehrerschein) {
      return fuehrerschein(fuehrerschein);
    }

    public Builder fuehrerscheinklasse(String fuehrerscheinklasse) {
      return fuehrerschein(fuehrerscheinklasse);
    }

    public Builder mitFuehrerscheinklasse(String fuehrerscheinklasse) {
      return fuehrerschein(fuehrerscheinklasse);
    }

    public Einsatzfahrzeug build() {
      return new Einsatzfahrzeug(this);
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public String getKennung() {
    return kennung;
  }

  public void setKennung(String kennung) {
    this.kennung = kennung;
  }

  public Fahrzeugtyp getFahrzeugtyp() {
    return fahrzeugtyp;
  }

  public void setFahrzeugtyp(Fahrzeugtyp fahrzeugtyp) {
    this.fahrzeugtyp = fahrzeugtyp;
  }

  public Fahrzeugtyp getFahrzeugTyp() {
    return fahrzeugtyp;
  }

  public void setFahrzeugTyp(Fahrzeugtyp fahrzeugTyp) {
    this.fahrzeugtyp = fahrzeugTyp;
  }

  public String getDinNorm() {
    if (dinNorm != null) {
      return dinNorm;
    }
    return fahrzeugtyp != null ? fahrzeugtyp.getDinNorm() : null;
  }

  public String getDin14530() {
    if (dinNorm != null) {
      return dinNorm.startsWith("DIN 14530") ? dinNorm : "DIN 14530 " + dinNorm;
    }
    return fahrzeugtyp != null ? fahrzeugtyp.getDin14530() : null;
  }

  public void setDinNorm(String dinNorm) {
    this.dinNorm = dinNorm;
  }

  public String getMasseklasse() {
    if (masseklasse != null) {
      return masseklasse;
    }
    return fahrzeugtyp != null ? fahrzeugtyp.getMasseklasse() : null;
  }

  public void setMasseklasse(String masseklasse) {
    this.masseklasse = masseklasse;
  }

  public String getZulassigeGesamtmasse() {
    if (zulassigeGesamtmasse != null) {
      return zulassigeGesamtmasse;
    }
    return fahrzeugtyp != null ? fahrzeugtyp.getZulassigeGesamtmasse() : null;
  }

  public void setZulassigeGesamtmasse(String zulassigeGesamtmasse) {
    this.zulassigeGesamtmasse = zulassigeGesamtmasse;
  }

  public String getBesatzung() {
    if (besatzung != null) {
      return besatzung;
    }
    return fahrzeugtyp != null ? fahrzeugtyp.getBesatzung() : null;
  }

  public void setBesatzung(String besatzung) {
    this.besatzung = besatzung;
  }

  public int getBesatzungSollStaerke() {
    if (fahrzeugtyp != null) {
      return fahrzeugtyp.getBesatzungSollStaerke();
    }
    return 0;
  }

  public String getLoeschwasser() {
    if (loeschwasser != null) {
      return loeschwasser;
    }
    return fahrzeugtyp != null ? fahrzeugtyp.getLoeschwasser() : null;
  }

  public void setLoeschwasser(String loeschwasser) {
    this.loeschwasser = loeschwasser;
  }

  public String getSchaummittel() {
    if (schaummittel != null) {
      return schaummittel;
    }
    return fahrzeugtyp != null ? fahrzeugtyp.getSchaummittel() : null;
  }

  public void setSchaummittel(String schaummittel) {
    this.schaummittel = schaummittel;
  }

  public String getPumpe() {
    if (pumpe != null) {
      return pumpe;
    }
    return fahrzeugtyp != null ? fahrzeugtyp.getPumpe() : null;
  }

  public void setPumpe(String pumpe) {
    this.pumpe = pumpe;
  }

  public Abmessungen getAbmessungen() {
    if (abmessungen != null) {
      return abmessungen;
    }
    return fahrzeugtyp != null ? fahrzeugtyp.getAbmessungen() : null;
  }

  public void setAbmessungen(Abmessungen abmessungen) {
    this.abmessungen = abmessungen;
  }

  public String getFahrgestellKategorie() {
    if (fahrgestellKategorie != null) {
      return fahrgestellKategorie;
    }
    return fahrzeugtyp != null ? fahrzeugtyp.getFahrgestellKategorie() : null;
  }

  public void setFahrgestellKategorie(String fahrgestellKategorie) {
    this.fahrgestellKategorie = fahrgestellKategorie;
  }

  public String getAntrieb() {
    if (antrieb != null) {
      return antrieb;
    }
    return fahrzeugtyp != null ? fahrzeugtyp.getAntrieb() : null;
  }

  public void setAntrieb(String antrieb) {
    this.antrieb = antrieb;
  }

  public String getFuehrerschein() {
    if (fuehrerschein != null) {
      return fuehrerschein;
    }
    return fahrzeugtyp != null ? fahrzeugtyp.getFuehrerschein() : null;
  }

  public void setFuehrerschein(String fuehrerschein) {
    this.fuehrerschein = fuehrerschein;
  }

  public String getFuehrerscheinklasse() {
    return getFuehrerschein();
  }

  public void setFuehrerscheinklasse(String fuehrerscheinklasse) {
    setFuehrerschein(fuehrerscheinklasse);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Einsatzfahrzeug that = (Einsatzfahrzeug) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
