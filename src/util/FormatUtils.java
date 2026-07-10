package util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Utilidad para formatear valores numéricos con el estándar chileno:
 * punto como separador de miles y coma como separador decimal.
 */
public class FormatUtils {

    private static final DecimalFormat PRICE_FORMAT;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        PRICE_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    private FormatUtils() {}

    /**
     * Formatea un valor numérico como precio en pesos chilenos,
     * usando punto para los miles y coma para los decimales.
     *
     * @param amount valor a formatear
     * @return cadena formateada (ej: "1.890.000")
     */
    public static String formatPrice(double amount) {
        return PRICE_FORMAT.format(amount);
    }
}
