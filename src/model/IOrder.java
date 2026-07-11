package model;

/**
 * Interfaz que define el contrato para una orden de compra.
 * Proporciona acceso a los datos de la orden de forma abstracta,
 * permitiendo usar diferentes implementaciones en la capa de GUI.
 */
public interface IOrder {

    /**
     * Retorna el identificador único de la orden.
     *
     * @return número de orden
     */
    int getOrderId();

    /**
     * Retorna el nombre del cliente que realizó la orden.
     *
     * @return nombre del cliente
     */
    String getCustomerName();

    /**
     * Retorna el servicio turístico asociado a esta orden.
     *
     * @return servicio turístico contratado
     */
    TourService getTour();

    /**
     * Retorna la cantidad de personas incluidas en la orden.
     *
     * @return cantidad de personas
     */
    int getPeopleCount();

    /**
     * Retorna el total de la orden calculado como
     * {@code precio unitario × cantidad de personas}.
     *
     * @return total de la orden
     */
    double getTotal();

    /**
     * Muestra un resumen completo de la orden por consola.
     */
    void showSummary();

    /**
     * Retorna una línea con formato CSV con los campos de la orden.
     *
     * @return línea CSV con los datos de la orden
     */
    String toCsvLine();
}
