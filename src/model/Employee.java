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
        setBaseSalary(baseSalary);
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
     * Asigna el sueldo base del empleado.
     *
     * @param baseSalary sueldo base del empleado, debe ser un valor positivo
     * @throws IllegalArgumentException si {@code baseSalary <= 0}
     */
    public void setBaseSalary(double baseSalary) {
        if (baseSalary <= 0) {
            throw new IllegalArgumentException("El sueldo base debe ser un valor positivo.");
        }
        this.baseSalary = baseSalary;
    }

    @Override
    public void showSummary() {
        System.out.println("Empleado: " + getFirstName() + " " + getLastName()
                + " | Cargo: " + position + " | Sueldo: $" + (long) baseSalary);
    }

    @Override
    public String toString() {
        return "{ \"rut\": \"" + getRut() + "\", \"nombre\": \"" + getFirstName()
                + "\", \"apellido\": \"" + getLastName() + "\", \"cargo\": \"" + position
                + "\", \"sueldoBase\": " + (long) baseSalary
                + ", \"direccion\": " + getAddress() + " }";
    }
}
