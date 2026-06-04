package model;

import util.InvalidRutException;

/**
 * Representa a un empleado de la agencia Llanquihue Tour.
 * Hereda de Persona e incorpora atributos laborales como cargo y sueldo base.
 */
public class Employee extends Person {
    private String position;
    private double baseSalary;

    /**
     * Constructor por defecto.
     */
    public Employee() {}

    /**
     * Constructor con todos los atributos, incluyendo los heredados de Persona.
     *
     * @param rut        RUT del empleado
     * @param firstName  nombre de pila
     * @param lastName   apellido
     * @param address    dirección física
     * @param position   cargo o puesto de trabajo
     * @param baseSalary sueldo base del empleado
     * @throws InvalidRutException si el RUT no es válido
     */
    public Employee(String rut, String firstName, String lastName, Address address,
                    String position, double baseSalary) throws InvalidRutException {
        super(rut, firstName, lastName, address);
        this.position = position;
        this.baseSalary = baseSalary;
    }

    /**
     * @return cargo o puesto de trabajo
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position cargo o puesto de trabajo
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @return sueldo base del empleado
     */
    public double getBaseSalary() {
        return baseSalary;
    }

    /**
     * @param baseSalary sueldo base del empleado
     */
    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    @Override
    public String toString() {
        return super.toString() + " | Cargo: " + position + ", Sueldo Base: $" + baseSalary;
    }
}
