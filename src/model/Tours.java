package model;

/**
 * Representa un tour turístico ofrecido por la agencia Llanquihue Tour.
 * Hereda de Products e incorpora datos específicos del tour como ubicación,
 * duración y el guía turístico asignado mediante composición.
 */
public class Tours extends Products {
    private String location;
    private String duration;
    private TouristGuide touristGuide;

    /**
     * Constructor por defecto.
     */
    public Tours() {}

    /**
     * Constructor con todos los atributos, incluyendo los heredados de Products.
     *
     * @param id            identificador único del tour
     * @param productName   nombre del tour
     * @param price         precio del tour
     * @param location      ubicación o destino del tour
     * @param duration      duración estimada del tour
     * @param touristGuide  guía turístico asignado al tour
     */
    public Tours(int id, String productName, double price, String location, String duration, TouristGuide touristGuide) {
        super(id, productName, price);
        this.location = location;
        this.duration = duration;
        this.touristGuide = touristGuide;
    }

    /**
     * @return ubicación o destino del tour
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location ubicación o destino del tour
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return duración estimada del tour
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @param duration duración estimada del tour
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * @return guía turístico asignado al tour
     */
    public TouristGuide getTouristGuide() {
        return touristGuide;
    }

    /**
     * @param touristGuide guía turístico asignado al tour
     */
    public void setTouristGuide(TouristGuide touristGuide) {
        this.touristGuide = touristGuide;
    }

    @Override
    public String toString() {
        return "{ \"id\": " + getId() + ", \"producto\": \"" + getProductName()
                + "\", \"ubicacion\": \"" + location
                + "\", \"duracion\": \"" + duration
                + "\", \"precio\": " + getPrice()
                + ", \"guia\": " + touristGuide + " }";
    }
}
