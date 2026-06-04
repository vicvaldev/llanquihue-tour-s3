package model;

/**
 * Representa una dirección física con los datos de calle, número, ciudad y región.
 * Utilizada como clase de composición dentro de Persona.
 */
public class Address {
    private String street;
    private String number;
    private String city;
    private String region;

    /**
     * Constructor por defecto.
     */
    public Address() {}

    /**
     * Constructor con todos los atributos.
     *
     * @param street  nombre de la calle
     * @param number  número de la vivienda o local
     * @param city    ciudad de residencia
     * @param region  región geográfica
     */
    public Address(String street, String number, String city, String region) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.region = region;
    }

    /**
     * @return nombre de la calle
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street nombre de la calle
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return número de la vivienda o local
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number número de la vivienda o local
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return ciudad de residencia
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city ciudad de residencia
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return región geográfica
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region región geográfica
     */
    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "{ \"calle\": \"" + street + "\", \"numero\": \"" + number
                + "\", \"ciudad\": \"" + city + "\", \"region\": \"" + region + "\" }";
    }
}
