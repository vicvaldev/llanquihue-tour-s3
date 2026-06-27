package model;

/**
 * Representa una excursión cultural a un lugar histórico,
 * con precio y guía asignado.
 */
public class CulturalExcursion extends TourService {
    private String historicalPlace;
    private double price;
    private TouristGuide guide;

    public CulturalExcursion() {}

    public CulturalExcursion(int id, String name, double durationHours,
                             String historicalPlace, double price, TouristGuide guide) {
        super(id, name, durationHours);
        this.historicalPlace = historicalPlace;
        this.price = price;
        this.guide = guide;
    }

    public String getHistoricalPlace() {
        return historicalPlace;
    }

    public void setHistoricalPlace(String historicalPlace) {
        this.historicalPlace = historicalPlace;
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
        return "CulturalExcursion";
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
