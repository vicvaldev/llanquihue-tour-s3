package model;

import util.InvalidRutException;

/**
 * Representa a un guía turístico de la agencia Llanquihue Tour.
 * Hereda de Employee e incorpora idiomas que domina el guía.
 */
public class TouristGuide extends Employee {
    private String motherTongue;
    private String secondLanguage;

    /**
     * Constructor con todos los atributos, incluyendo los heredados de Employee y Person.
     *
     * @param rut             RUT del guía
     * @param firstName       nombre de pila
     * @param lastName        apellido
     * @param address         dirección física
     * @param position        cargo o puesto de trabajo
     * @param baseSalary      sueldo base
     * @param motherTongue    lengua materna del guía
     * @param secondLanguage  segunda lengua que domina el guía
     * @throws InvalidRutException si el RUT no es válido
     */
    public TouristGuide(String rut, String firstName, String lastName, Address address,
                        String position, double baseSalary,
                        String motherTongue, String secondLanguage) throws InvalidRutException {
        super(rut, firstName, lastName, address, position, baseSalary);
        this.motherTongue = motherTongue;
        this.secondLanguage = secondLanguage;
    }

    /**
     * @return lengua materna del guía
     */
    public String getMotherTongue() {
        return motherTongue;
    }

    /**
     * @return segunda lengua que domina el guía
     */
    public String getSecondLanguage() {
        return secondLanguage;
    }

    /**
     * Muestra un resumen del guía turístico incluyendo nombre, RUT,
     * lengua materna y segunda lengua si está registrada.
     * Sobrescribe la implementación de {@link Employee}.
     */
    @Override
    public void showSummary() {
        System.out.println("Guía Turístico: " + getFirstName() + " " + getLastName()
                + " | RUT: " + getRut()
                + " | Lengua materna: " + motherTongue
                + (secondLanguage.isEmpty() ? "" : " | 2ª lengua: " + secondLanguage));
    }

    @Override
    public String toString() {
        return "{ \"rut\": \"" + getRut() + "\", \"nombre\": \"" + getFirstName()
                + " " + getLastName() + "\", \"lenguaMaterna\": \"" + motherTongue
                + "\", \"segundaLengua\": \"" + secondLanguage + "\" }";
    }
}
