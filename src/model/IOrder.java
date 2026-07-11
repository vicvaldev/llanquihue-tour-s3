package model;

/**
 * Interfaz que define el contrato para una orden de compra.
 * Proporciona la serialización a CSV para persistencia.
 */
public interface IOrder {

    /**
     * Retorna una línea con formato CSV con los campos de la orden.
     *
     * @return línea CSV con los datos de la orden
     */
    String toCsvLine();
}
