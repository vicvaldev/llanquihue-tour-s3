package model;

/**
 * Representa una excursión cultural a un lugar de interés histórico
 * de la Región de Los Lagos, como iglesias patrimoniales, fuertes
 * coloniales o sitios arqueológicos.
 * <p>
 * Extiende {@link TourService} agregando el nombre del lugar histórico
 * visitado, el precio del servicio y el guía turístico asignado
 * mediante composición con {@link TouristGuide}.
 * </p>
 */
public class CulturalExcursion extends TourService {
    private String historicalPlace;
    private double price;
    private TouristGuide guide;

    /**
     * Constructor por defecto requerido por la infraestructura de
     * deserialización. Los atributos deben asignarse posteriormente
     * mediante los métodos setter correspondientes.
     */
    public CulturalExcursion() {}

    /**
     * Constructor con todos los atributos de la excursión cultural.
     *
     * @param id              identificador único del servicio
     * @param name            nombre de la excursión cultural
     * @param durationHours   duración total en horas
     * @param historicalPlace nombre del lugar histórico a visitar,
     *                        no puede estar vacío
     * @param price           precio del servicio, debe ser positivo
     * @param guide           guía turístico asignado, no puede ser nulo
     * @param maxCapacity     capacidad máxima de personas, debe ser positivo
     * @throws IllegalArgumentException si algún parámetro no cumple
     *                                  las restricciones de validación
     */
    public CulturalExcursion(int id, String name, double durationHours,
                             String historicalPlace, double price, TouristGuide guide,
                             int maxCapacity) {
        super(id, name, durationHours, maxCapacity);
        setHistoricalPlace(historicalPlace);
        setPrice(price);
        setGuide(guide);
    }

    /**
     * Retorna el nombre del lugar histórico que se visita en la excursión.
     *
     * @return lugar histórico
     */
    public String getHistoricalPlace() {
        return historicalPlace;
    }

    /**
     * Asigna el lugar histórico de la excursión cultural.
     *
     * @param historicalPlace nombre del lugar histórico, no puede estar vacío
     * @throws IllegalArgumentException si {@code historicalPlace} es
     *                                  {@code null} o está compuesto solo por espacios
     */
    public void setHistoricalPlace(String historicalPlace) {
        if (historicalPlace == null || historicalPlace.isBlank()) {
            throw new IllegalArgumentException("El lugar histórico no puede estar vacío.");
        }
        this.historicalPlace = historicalPlace.trim();
    }

    /**
     * Asigna el precio del servicio.
     *
     * @param price precio de la excursión cultural, debe ser positivo
     * @throws IllegalArgumentException si {@code price <= 0}
     */
    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("El precio debe ser un valor positivo.");
        }
        this.price = price;
    }

    /**
     * Asigna el guía turístico que guiará la excursión.
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
     * @return precio de la excursión cultural
     */
    @Override
    public double getPrice() {
        return price;
    }

    /**
     * Retorna el guía turístico asignado a esta excursión.
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
     * @return "CulturalExcursion"
     */
    @Override
    public String getServiceType() {
        return "CulturalExcursion";
    }

    /**
     * Muestra por consola la información completa de la excursión
     * cultural, incluyendo los campos heredados y los específicos
     * de esta subclase.
     */
    @Override
    public void displayInformation() {
        super.displayInformation();
        String place = (historicalPlace != null && !historicalPlace.isBlank()) ? historicalPlace : "datos no encontrados";
        System.out.println("Lugar histórico: " + place);
        System.out.println("Precio: $" + String.format("%.0f", price));
        String guideName = (guide != null) ? guide.getFirstName() + " " + guide.getLastName() : "datos no encontrados";
        System.out.println("Guía: " + guideName);
    }

    /**
     * Retorna una representación JSON completa de la excursión cultural,
     * incluyendo los campos heredados y los específicos de esta subclase.
     *
     * @return cadena en formato JSON con todos los datos del servicio
     */
    /**
     * Muestra un resumen de la excursión cultural incluyendo nombre,
     * lugar histórico y precio.
     */
    @Override
    public void showSummary() {
        System.out.println("Excursión Cultural: " + getName()
                + " | Lugar histórico: " + historicalPlace
                + " | Precio: $" + String.format("%.0f", price));
    }

    @Override
    public String toString() {
        return "{ \"id\": " + getId() + ", \"nombre\": \"" + getName()
                + "\", \"duracionHoras\": " + getDurationHours()
                + ", \"lugarHistorico\": \"" + historicalPlace
                + "\", \"precio\": " + price
                + ", \"guia\": " + guide + " }";
    }
}
