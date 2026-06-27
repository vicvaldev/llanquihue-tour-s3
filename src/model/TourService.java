package model;

/**
 * Superclase abstracta que representa un servicio turístico genérico.
 * Contiene los atributos comunes (id, nombre y duración en horas) y define
 * métodos abstractos para acceder al precio y al guía turístico,
 * permitiendo el polimorfismo sin necesidad de instanceof.
 */
public abstract class TourService {
    private int id;
    private String name;
    private double durationHours;

    public TourService() {}

    public TourService(int id, String name, double durationHours) {
        setId(id);
        setName(name);
        setDurationHours(durationHours);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo.");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del servicio no puede estar vacío.");
        }
        this.name = name.trim();
    }

    public double getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(double durationHours) {
        if (durationHours <= 0) {
            throw new IllegalArgumentException("La duración debe ser un número positivo.");
        }
        this.durationHours = durationHours;
    }

    public abstract double getPrice();

    public abstract TouristGuide getGuide();

    public abstract String getServiceType();

    @Override
    public String toString() {
        return "{ \"id\": " + id + ", \"nombre\": \"" + name
                + "\", \"duracionHoras\": " + durationHours + " }";
    }
}
