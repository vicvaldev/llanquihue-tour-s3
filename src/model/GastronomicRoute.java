package model;

/**
 * Representa una ruta gastronómica que consiste en un recorrido con
 * paradas en diferentes lugares para degustar productos locales típicos
 * de la Región de Los Lagos.
 * <p>
 * Extiende {@link TourService} agregando el número de paradas
 * gastronómicas, el precio del servicio y el guía turístico asignado
 * mediante composición con {@link TouristGuide}.
 * </p>
 */
public class GastronomicRoute extends TourService {
    private int numberOfStops;
    private double price;
    private TouristGuide guide;

    /**
     * Constructor por defecto requerido por la infraestructura de
     * deserialización. Los atributos deben asignarse posteriormente
     * mediante los métodos setter correspondientes.
     */
    public GastronomicRoute() {}

    /**
     * Constructor con todos los atributos de la ruta gastronómica.
     *
     * @param id            identificador único del servicio
     * @param name          nombre de la ruta gastronómica
     * @param durationHours duración total en horas
     * @param numberOfStops número de paradas gastronómicas, debe ser positivo
     * @param price         precio del servicio, debe ser positivo
     * @param guide         guía turístico asignado, no puede ser nulo
     * @throws IllegalArgumentException si algún parámetro no cumple
     *                                  las restricciones de validación
     */
    public GastronomicRoute(int id, String name, double durationHours,
                            int numberOfStops, double price, TouristGuide guide) {
        super(id, name, durationHours);
        setNumberOfStops(numberOfStops);
        setPrice(price);
        setGuide(guide);
    }

    /**
     * Retorna el número de paradas gastronómicas incluidas en la ruta.
     *
     * @return número de paradas
     */
    public int getNumberOfStops() {
        return numberOfStops;
    }

    /**
     * Asigna el número de paradas gastronómicas.
     *
     * @param numberOfStops número de paradas, debe ser positivo
     * @throws IllegalArgumentException si {@code numberOfStops <= 0}
     */
    public void setNumberOfStops(int numberOfStops) {
        if (numberOfStops <= 0) {
            throw new IllegalArgumentException("El número de paradas debe ser positivo.");
        }
        this.numberOfStops = numberOfStops;
    }

    /**
     * Asigna el precio del servicio.
     *
     * @param price precio de la ruta gastronómica, debe ser positivo
     * @throws IllegalArgumentException si {@code price <= 0}
     */
    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("El precio debe ser un valor positivo.");
        }
        this.price = price;
    }

    /**
     * Asigna el guía turístico que acompañará la ruta.
     *
     * @param guide guía turístico, no puede ser nulo
     * @throws IllegalArgumentException si {@code guide} es {@code null}
     */
    public void setGuide(TouristGuide guide) {
        if (guide == null) {
            throw new IllegalArgumentException("El guía turístico no puede ser nulo.");
        }
        this.guide = guide;
    }

    /**
     * Retorna el precio del servicio.
     *
     * @return precio de la ruta gastronómica
     */
    @Override
    public double getPrice() {
        return price;
    }

    /**
     * Retorna el guía turístico asignado a esta ruta.
     *
     * @return guía turístico asociado
     */
    @Override
    public TouristGuide getGuide() {
        return guide;
    }

    /**
     * Retorna el identificador del tipo concreto de servicio.
     *
     * @return "GastronomicRoute"
     */
    @Override
    public String getServiceType() {
        return "GastronomicRoute";
    }

    /**
     * Muestra por consola la información completa de la ruta
     * gastronómica, incluyendo los campos heredados y los específicos
     * de esta subclase.
     */
    @Override
    public void displayInformation() {
        super.displayInformation();
        System.out.println("Número de paradas: " + numberOfStops);
        System.out.println("Precio: $" + String.format("%.0f", price));
        String guideName = (guide != null) ? guide.getFirstName() + " " + guide.getLastName() : "datos no encontrados";
        System.out.println("Guía: " + guideName);
    }

    /**
     * Retorna una representación JSON completa de la ruta gastronómica,
     * incluyendo los campos heredados y los específicos de esta subclase.
     *
     * @return cadena en formato JSON con todos los datos del servicio
     */
    @Override
    public String toString() {
        return "{ \"id\": " + getId() + ", \"nombre\": \"" + getName()
                + "\", \"duracionHoras\": " + getDurationHours()
                + ", \"numeroDeParadas\": " + numberOfStops
                + ", \"precio\": " + price
                + ", \"guia\": " + guide + " }";
    }
}
