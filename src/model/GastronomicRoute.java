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
        this.numberOfStops = numberOfStops;
        this.price = price;
        this.guide = guide;
    }

    public int getNumberOfStops() {
        return numberOfStops;
    }

    public void setNumberOfStops(int numberOfStops) {
        this.numberOfStops = numberOfStops;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setGuide(TouristGuide guide) {
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
