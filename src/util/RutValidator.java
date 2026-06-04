package util;

/**
 * Validador de RUT chileno basado en el algoritmo oficial del módulo 11.
 * Permite formatos con o sin puntos y guión (ej: 12.345.678-9, 12345678-9).
 */
public class RutValidator {

    private RutValidator() {}

    /**
     * Valida que un RUT chileno sea correcto en formato y dígito verificador.
     *
     * @param rut RUT a validar
     * @throws InvalidRutException si el RUT es nulo, tiene formato inválido
     *                             o el dígito verificador no coincide
     */
    public static void validate(String rut) throws InvalidRutException {
        if (rut == null || rut.isBlank()) {
            throw new InvalidRutException("El RUT no puede ser nulo o vacío.");
        }

        String clean = rut.replaceAll("[.\\s]", "").toUpperCase();

        if (!clean.matches("\\d+-[\\dK]")) {
            throw new InvalidRutException(
                    "Formato de RUT inválido: '" + rut + "'. Debe ser algo como 12.345.678-9.");
        }

        String[] parts = clean.split("-");
        String body = parts[0];
        char expectedDigit = parts[1].charAt(0);

        int sum = 0;
        int multiplier = 2;
        for (int i = body.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(body.charAt(i));
            sum += digit * multiplier;
            multiplier = multiplier == 7 ? 2 : multiplier + 1;
        }

        int remainder = sum % 11;
        int calculated = 11 - remainder;

        char actualDigit;
        if (calculated == 11) {
            actualDigit = '0';
        } else if (calculated == 10) {
            actualDigit = 'K';
        } else {
            actualDigit = (char) ('0' + calculated);
        }

        if (actualDigit != expectedDigit) {
            throw new InvalidRutException(
                    "Dígito verificador inválido para RUT: '" + rut + "'. Se esperaba " + actualDigit + ".");
        }
    }
}
