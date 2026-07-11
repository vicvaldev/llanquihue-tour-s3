package model;

/**
 * Superclase abstracta que representa la raíz de la jerarquía de servicios
 * turísticos ofrecidos por la agencia Llanquihue Tour.
 * <p>
 * Contiene los atributos comunes a todo servicio: identificador único,
 * nombre comercial, duración expresada en horas y capacidad máxima de
 * personas. Define métodos abstractos
 * que las subclases deben implementar para exponer el precio, el guía
 * asignado y el tipo concreto de servicio, permitiendo operar
 * polimórficamente sobre una colección de {@code TourService} sin
 * necesidad de utilizar {@code instanceof}.
 * </p>
 *
 * @see GastronomicRoute
 * @see LakeCruise
 * @see CulturalExcursion
 * @see Registerable
 */
public abstract class TourService implements Registerable {
    private int id;
    private String name;
    private double durationHours;
    private int maxCapacity;

    /**
     * Constructor con todos los atributos comunes del servicio turístico.
     *
     * @param id            identificador único del servicio, debe ser positivo
     * @param name          nombre del servicio, no puede estar vacío
     * @param durationHours duración en horas, debe ser un valor positivo
     * @param maxCapacity   capacidad máxima de personas, debe ser positivo
     * @throws IllegalArgumentException si alguno de los parámetros no
     *                                  cumple las restricciones de validación
     */
    public TourService(int id, String name, double durationHours, int maxCapacity) {
        setId(id);
        setName(name);
        setDurationHours(durationHours);
        setMaxCapacity(maxCapacity);
    }

    /**
     * Retorna el identificador único del servicio.
     *
     * @return id del servicio
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna el identificador único del servicio.
     *
     * @param id identificador único, debe ser positivo
     * @throws IllegalArgumentException si {@code id <= 0}
     */
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo.");
        }
        this.id = id;
    }

    /**
     * Retorna el nombre comercial del servicio.
     *
     * @return nombre del servicio
     */
    public String getName() {
        return name;
    }

    /**
     * Asigna el nombre comercial del servicio.
     *
     * @param name nombre del servicio, no puede ser nulo ni estar en blanco
     * @throws IllegalArgumentException si {@code name} es {@code null}
     *                                  o está compuesto solo por espacios
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del servicio no puede estar vacío.");
        }
        this.name = name.trim();
    }

    /**
     * Retorna la duración del servicio en horas.
     *
     * @return duración en horas
     */
    public double getDurationHours() {
        return durationHours;
    }

    /**
     * Asigna la duración del servicio en horas.
     *
     * @param durationHours duración en horas, debe ser un valor positivo
     * @throws IllegalArgumentException si {@code durationHours <= 0}
     */
    public void setDurationHours(double durationHours) {
        if (durationHours <= 0) {
            throw new IllegalArgumentException("La duración debe ser un número positivo.");
        }
        this.durationHours = durationHours;
    }

    /**
     * Retorna el precio del servicio. Cada subclase concreta implementa
     * este método según su propia definición del atributo precio.
     *
     * @return precio del servicio turístico
     */
    public abstract double getPrice();

    /**
     * Retorna el guía turístico asignado a este servicio.
     * Cada subclase concreta implementa este método retornando su
     * atributo de composición {@link TouristGuide}.
     *
     * @return guía turístico asociado al servicio
     */
    public abstract TouristGuide getGuide();

    /**
     * Retorna el identificador textual del tipo concreto de servicio.
     * Este valor se utiliza en la serialización CSV ({@code type}) y
     * en la lógica de {@link data.DataManager} para reconstruir la
     * subclase correcta al cargar los datos.
     *
     * @return tipo de servicio ("GastronomicRoute", "LakeCruise"
     *         o "CulturalExcursion")
     */
    public abstract String getServiceType();

    /**
     * Retorna la capacidad máxima de personas para este servicio turístico.
     *
     * @return capacidad máxima en cantidad de personas
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Asigna la capacidad máxima de personas para este servicio turístico.
     *
     * @param maxCapacity capacidad máxima, debe ser un valor positivo
     * @throws IllegalArgumentException si {@code maxCapacity <= 0}
     */
    public void setMaxCapacity(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser un número positivo.");
        }
        this.maxCapacity = maxCapacity;
    }

    /**
     * Muestra un resumen del servicio turístico. Cada subclase concreta
     * implementa este método con los datos específicos de su tipo.
     */
    @Override
    public abstract void showSummary();

    /**
     * Retorna una representación JSON del servicio con los campos
     * comunes: id, nombre, duración en horas y capacidad máxima.
     * Las subclases extienden este formato agregando sus atributos
     * específicos y el precio.
     *
     * @return cadena en formato JSON con los datos del servicio
     */
    @Override
    public String toString() {
        return "{ \"id\": " + id + ", \"nombre\": \"" + name
                + "\", \"duracionHoras\": " + durationHours
                + ", \"capacidadMaxima\": " + maxCapacity + " }";
    }
}
