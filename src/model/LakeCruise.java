package model;

import util.FormatUtils;

/**
 * Representa un paseo lacustre o navegación por los lagos, canales y
 * fiordos de la Región de Los Lagos, como el Lago Llanquihue, el Lago
 * Todos los Santos o el Canal de Chacao.
 * <p>
 * Extiende {@link TourService} agregando el tipo de embarcación
 * utilizada, el precio del servicio y el guía turístico asignado
 * mediante composición con {@link TouristGuide}.
 * </p>
 */
public class LakeCruise extends TourService {
    private String boatType;
    private double price;
    private TouristGuide guide;

    /**
     * Constructor con todos los atributos del paseo lacustre.
     *
     * @param id            identificador único del servicio
     * @param name          nombre del paseo lacustre
     * @param durationHours duración total en horas
     * @param boatType      tipo de embarcación (catamarán, lancha, etc.),
     *                      no puede estar vacío
     * @param price         precio del servicio, debe ser positivo
     * @param guide         guía turístico asignado, no puede ser nulo
     * @param maxCapacity   capacidad máxima de personas, debe ser positivo
     * @throws IllegalArgumentException si algún parámetro no cumple
     *                                  las restricciones de validación
     */
    public LakeCruise(int id, String name, double durationHours,
                      String boatType, double price, TouristGuide guide,
                      int maxCapacity) {
        super(id, name, durationHours, maxCapacity);
        setBoatType(boatType);
        setPrice(price);
        setGuide(guide);
    }

    /**
     * Retorna el tipo de embarcación utilizada en el paseo
     * (por ejemplo, "Catamarán", "Lancha", "Bote a motor").
     *
     * @return tipo de embarcación
     */
    public String getBoatType() {
        return boatType;
    }

    /**
     * Asigna el tipo de embarcación del paseo lacustre.
     *
     * @param boatType tipo de embarcación, no puede estar vacío
     * @throws IllegalArgumentException si {@code boatType} es {@code null}
     *                                  o está compuesto solo por espacios
     */
    public void setBoatType(String boatType) {
        if (boatType == null || boatType.isBlank()) {
            throw new IllegalArgumentException("El tipo de embarcación no puede estar vacío.");
        }
        this.boatType = boatType.trim();
    }

    /**
     * Asigna el precio del servicio.
     *
     * @param price precio del paseo lacustre, debe ser positivo
     * @throws IllegalArgumentException si {@code price <= 0}
     */
    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("El precio debe ser un valor positivo.");
        }
        this.price = price;
    }

    /**
     * Asigna el guía turístico que acompañará la navegación.
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
     * @return precio del paseo lacustre
     */
    @Override
    public double getPrice() {
        return price;
    }

    /**
     * Retorna el guía turístico asignado a este paseo.
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
     * @return "LakeCruise"
     */
    @Override
    public String getServiceType() {
        return "LakeCruise";
    }

    /**
     * Muestra un resumen del paseo lacustre incluyendo nombre,
     * tipo de embarcación y precio.
     */
    @Override
    public void showSummary() {
        System.out.println("Paseo Lacustre: " + getName()
                + " | Embarcación: " + boatType
                + " | Precio: $" + FormatUtils.formatPrice(price));
    }

    @Override
    public String toString() {
        return "{ \"id\": " + getId() + ", \"nombre\": \"" + getName()
                + "\", \"duracionHoras\": " + getDurationHours()
                + ", \"tipoEmbarcacion\": \"" + boatType
                + "\", \"precio\": " + price
                + ", \"guia\": " + guide + " }";
    }
}
