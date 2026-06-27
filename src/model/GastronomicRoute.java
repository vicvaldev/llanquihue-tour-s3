package model;

/**
 * Representa una ruta gastronómica con paradas en lugares
 * para degustar productos locales, con un precio y guía asignado.
 */
public class GastronomicRoute extends TourService {
    private int numberOfStops;
    private double price;
    private TouristGuide guide;

    public GastronomicRoute() {}

    public GastronomicRoute(int id, String name, double durationHours,
                            int numberOfStops, double price, TouristGuide guide) {
        super(id, name, durationHours);
        setNumberOfStops(numberOfStops);
        setPrice(price);
        setGuide(guide);
    }

    public int getNumberOfStops() {
        return numberOfStops;
    }

    public void setNumberOfStops(int numberOfStops) {
        if (numberOfStops <= 0) {
            throw new IllegalArgumentException("El número de paradas debe ser positivo.");
        }
        this.numberOfStops = numberOfStops;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("El precio debe ser un valor positivo.");
        }
        this.price = price;
    }

    public void setGuide(TouristGuide guide) {
        if (guide == null) {
            throw new IllegalArgumentException("El guía turístico no puede ser nulo.");
        }
        this.guide = guide;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public TouristGuide getGuide() {
        return guide;
    }

    @Override
    public String getServiceType() {
        return "GastronomicRoute";
    }

    @Override
    public String toString() {
        return "{ \"id\": " + getId() + ", \"nombre\": \"" + getName()
                + "\", \"duracionHoras\": " + getDurationHours()
                + ", \"numeroDeParadas\": " + numberOfStops
                + ", \"precio\": " + price
                + ", \"guia\": " + guide + " }";
    }
}
