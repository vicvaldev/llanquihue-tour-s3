package model;

import util.InvalidRutException;
import util.RutValidator;

/**
 * Clase base del dominio que representa a una persona genérica.
 * Contiene datos básicos como RUT, nombre, apellido y una dirección asociada
 * mediante composición.
 */
public class Person implements Registerable {
    private String rut;
    private String firstName;
    private String lastName;
    private Address address;

    /**
     * Constructor con todos los atributos. Valida el RUT antes de asignarlo.
     *
     * @param rut       RUT de la persona
     * @param firstName nombre de pila
     * @param lastName  apellido
     * @param address   dirección física (composición)
     * @throws InvalidRutException si el RUT no es válido
     */
    public Person(String rut, String firstName, String lastName, Address address)
            throws InvalidRutException {
        RutValidator.validate(rut);
        this.rut = rut;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    /**
     * @return RUT de la persona
     */
    public String getRut() {
        return rut;
    }

    /**
     * @return nombre de pila
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return apellido
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return dirección física asociada
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Muestra un resumen de la persona incluyendo nombre completo y RUT.
     * Implementación del contrato definido en {@link Registerable}.
     */
    @Override
    public void showSummary() {
        System.out.println("Persona: " + firstName + " " + lastName + " (RUT: " + rut + ")");
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (RUT: " + rut + "), Dirección: " + address;
    }
}
