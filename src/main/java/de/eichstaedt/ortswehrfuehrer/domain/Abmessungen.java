package de.eichstaedt.ortswehrfuehrer.domain;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Created by konrad.eichstaedt@gmx.de on 03.10.26.
 * <p>
 * This Record represents vehicle dimensions (Länge × Breite × Höhe in Metern) as a DDD Value Object.
 */
@ValueObject
public record Abmessungen(Double laengeMax, Double breiteMax, Double hoeheMax) {

  public Abmessungen(double laengeMax, double breiteMax, double hoeheMax) {
    this(Double.valueOf(laengeMax), Double.valueOf(breiteMax), Double.valueOf(hoeheMax));
  }

  public String formatiert() {
    if (laengeMax == null || breiteMax == null || hoeheMax == null) {
      return "n. v.";
    }
    return formatZahl(laengeMax) + " × " + formatZahl(breiteMax) + " × " + formatZahl(hoeheMax);
  }

  private static String formatZahl(Double val) {
    if (val == null) {
      return "n. v.";
    }
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
    DecimalFormat df = new DecimalFormat("0.0#", symbols);
    return df.format(val);
  }

  @Override
  public String toString() {
    return formatiert();
  }
}
