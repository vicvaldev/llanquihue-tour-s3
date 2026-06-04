package util;

/**
 * Excepción personalizada lanzada cuando un RUT no cumple con el formato
 * o el dígito verificador esperado según el algoritmo oficial chileno.
 */
public class InvalidRutException extends Exception {

    /**
     * Constructor con mensaje descriptivo del error.
     *
     * @param message descripción del motivo del rechazo
     */
    public InvalidRutException(String message) {
        super(message);
    }
}
