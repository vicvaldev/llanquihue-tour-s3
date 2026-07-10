package model;

import util.*;

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
     * Constructor por defecto.
     */
    public Person() {}

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
     * @param rut RUT de la persona
     * @throws InvalidRutException si el RUT no es válido
     */
    public void setRut(String rut) throws InvalidRutException {
        RutValidator.validate(rut);
        this.rut = rut;
    }

    /**
     * @return nombre de pila
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName nombre de pila
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return apellido
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName apellido
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return dirección física asociada
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address dirección física asociada
     */
    public void setAddress(Address address) {
        this.address = address;
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
