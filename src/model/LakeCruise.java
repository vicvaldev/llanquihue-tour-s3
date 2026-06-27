package model;

/**
 * Representa un paseo lacustre o navegación por lagos y canales,
 * con un tipo de embarcación específico, precio y guía asignado.
 */
public class LakeCruise extends TourService {
    private String boatType;
    private double price;
    private TouristGuide guide;

    public LakeCruise() {}

    public LakeCruise(int id, String name, double durationHours,
                      String boatType, double price, TouristGuide guide) {
        super(id, name, durationHours);
        this.boatType = boatType;
        this.price = price;
        this.guide = guide;
    }

    public String getBoatType() {
        return boatType;
    }

    public void setBoatType(String boatType) {
        this.boatType = boatType;
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
        return "LakeCruise";
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
