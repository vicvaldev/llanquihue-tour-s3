package model;

import util.ExceededCapacityException;
import util.FormatUtils;

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
public class PurchaseOrder implements IOrder {
    private String customerName;
    private TourService tour;
    private int peopleCount;
    private double total;
    private static int nextOrderId = 1;
    private int orderId;

    private PurchaseOrder() {}

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
     * Crea una instancia de {@code PurchaseOrder} a partir de datos
     * serializados en CSV, sincronizando el contador estático de ID para
     * que nuevas órdenes no colisionen con las ya persistidas.
     * Este método se utiliza exclusivamente para la deserialización
     * de órdenes previamente persistidas.
     *
     * @param orderId      número de orden almacenado
     * @param customerName nombre del cliente
     * @param tour         servicio turístico asociado
     * @param peopleCount  cantidad de personas
     * @param total        total almacenado de la orden
     * @return instancia reconstruida desde CSV
     */
    public static PurchaseOrder fromCsv(int orderId, String customerName,
                                         TourService tour, int peopleCount, double total) {
        PurchaseOrder po = new PurchaseOrder();
        po.orderId = orderId;
        po.customerName = customerName;
        po.tour = tour;
        po.peopleCount = peopleCount;
        po.total = total;
        if (orderId >= nextOrderId) {
            nextOrderId = orderId + 1;
        }
        return po;
    }

    /**
     * Retorna una representación legible de la orden de compra en una
     * sola línea con el formato:
     * {@code Orden #X | Cliente: X | Tour: X (X) | Personas: X | Total: $X}.
     *
     * @return cadena con el resumen de la orden
     */
    @Override
    public String toString() {
        return "Orden #" + orderId + " | Cliente: " + customerName
                + " | Tour: " + tour.getName() + " (" + tour.getServiceType() + ")"
                + " | Personas: " + peopleCount
                + " | Total: $" + FormatUtils.formatPrice(total);
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
