package util;

/**
 * Excepción controlada que se lanza cuando se intenta registrar una
 * orden de compra con una cantidad de personas superior a la capacidad
 * máxima permitida para el servicio turístico seleccionado.
 *
 * @see model.PurchaseOrder
 * @see model.TourService#getMaxCapacity()
 */
public class ExceededCapacityException extends Exception {

    /**
     * Construye una excepción con el mensaje descriptivo del error.
     *
     * @param message descripción de la causa del error
     */
    public ExceededCapacityException(String message) {
        super(message);
    }
}
