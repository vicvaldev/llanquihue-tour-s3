package model;

import util.ExceededCapacityException;

/**
 * Representa una orden de compra de un servicio turístico generada por
 * un cliente. Contiene los datos del cliente, el tour seleccionado, la
 * cantidad de personas y el total calculado (precio unitario × personas).
 * <p>
 * Valida que la cantidad de personas no exceda la capacidad máxima del
 * tour, lanzando {@link ExceededCapacityException} en caso contrario.
 * </p>
 *
 * @see TourService
 * @see ExceededCapacityException
 */
public class PurchaseOrder {
    private String customerName;
    private TourService tour;
    private int peopleCount;
    private double total;
    private static int nextOrderId = 1;
    private int orderId;

    /**
     * Construye una orden de compra validando que la cantidad de personas
     * no supere la capacidad máxima del tour.
     *
     * @param customerName nombre del cliente, no puede estar vacío
     * @param tour         servicio turístico contratado, no puede ser nulo
     * @param peopleCount  cantidad de personas, debe ser positiva y no
     *                     superar la capacidad máxima del tour
     * @throws IllegalArgumentException  si {@code customerName} está vacío,
     *                                   {@code tour} es nulo o {@code peopleCount}
     *                                   no es positivo
     * @throws ExceededCapacityException si {@code peopleCount} supera la
     *                                   capacidad máxima del tour
     */
    public PurchaseOrder(String customerName, TourService tour, int peopleCount)
            throws ExceededCapacityException {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }
        if (tour == null) {
            throw new IllegalArgumentException("El tour no puede ser nulo.");
        }
        if (peopleCount <= 0) {
            throw new IllegalArgumentException("La cantidad de personas debe ser positiva.");
        }
        if (peopleCount > tour.getMaxCapacity()) {
            throw new ExceededCapacityException("La capacidad máxima del tour es "
                    + tour.getMaxCapacity() + " personas, pero se solicitaron "
                    + peopleCount + ".");
        }
        this.customerName = customerName.trim();
        this.tour = tour;
        this.peopleCount = peopleCount;
        this.total = tour.getPrice() * peopleCount;
        this.orderId = nextOrderId++;
    }

    /**
     * Retorna el nombre del cliente que realizó la orden.
     *
     * @return nombre del cliente
     */
    public String getCustomerName() { return customerName; }

    /**
     * Retorna el servicio turístico asociado a esta orden.
     *
     * @return servicio turístico contratado
     */
    public TourService getTour() { return tour; }

    /**
     * Retorna la cantidad de personas incluidas en la orden.
     *
     * @return cantidad de personas
     */
    public int getPeopleCount() { return peopleCount; }

    /**
     * Retorna el total de la orden calculado como
     * {@code precio unitario × cantidad de personas}.
     *
     * @return total de la orden
     */
    public double getTotal() { return total; }

    /**
     * Retorna el identificador único de la orden.
     *
     * @return número de orden
     */
    public int getOrderId() { return orderId; }

    /**
     * Muestra por consola un resumen completo de la orden de compra,
     * incluyendo el número de orden, nombre del cliente, datos del
     * tour, capacidad, precio unitario y total.
     */
    public void showSummary() {
        System.out.println("=== Orden de Compra #" + orderId + " ===");
        System.out.println("Cliente: " + customerName);
        System.out.println("Tour: " + tour.getName() + " (" + tour.getServiceType() + ")");
        System.out.println("Personas: " + peopleCount + " / Capacidad máxima: " + tour.getMaxCapacity());
        System.out.println("Precio unitario: $" + String.format("%.2f", tour.getPrice()));
        System.out.println("Total: $" + String.format("%.2f", total));
    }

    /**
     * Retorna una representación JSON de la orden de compra con todos
     * sus campos: número de orden, cliente, tour, cantidad de personas
     * y total.
     *
     * @return cadena en formato JSON con los datos de la orden
     */
    @Override
    public String toString() {
        return "{ \"orderId\": " + orderId
                + ", \"cliente\": \"" + customerName
                + "\", \"tourId\": " + tour.getId()
                + ", \"tipoTour\": \"" + tour.getServiceType()
                + "\", \"nombreTour\": \"" + tour.getName()
                + "\", \"personas\": " + peopleCount
                + ", \"total\": " + total + " }";
    }

    /**
     * Retorna una línea con formato CSV con los 8 campos de la orden
     * separados por punto y coma, en el orden:
     * {@code orderId;customerName;tourId;tourType;tourName;peopleCount;unitPrice;total}.
     *
     * @return línea CSV con los datos de la orden
     */
    public String toCsvLine() {
        return orderId + ";" + customerName + ";" + tour.getId()
                + ";" + tour.getServiceType() + ";" + tour.getName()
                + ";" + peopleCount + ";" + tour.getPrice() + ";" + total;
    }
}
