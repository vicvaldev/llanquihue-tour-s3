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
        setHistoricalPlace(historicalPlace);
        setPrice(price);
        setGuide(guide);
    }

    public String getHistoricalPlace() {
        return historicalPlace;
    }

    public void setHistoricalPlace(String historicalPlace) {
        if (historicalPlace == null || historicalPlace.isBlank()) {
            throw new IllegalArgumentException("El lugar histórico no puede estar vacío.");
        }
        this.historicalPlace = historicalPlace.trim();
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
