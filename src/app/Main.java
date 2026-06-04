package app;

import model.Address;
import model.Employee;
import util.InvalidRutException;

/**
 * Clase principal del sistema Llanquihue Tour.
 * Punto de entrada que demuestra la creación y uso de las clases del dominio.
 */
public class Main {
    public static void main(String[] args) {
        try {
            run();
        } catch (InvalidRutException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void run() throws InvalidRutException {
        Address addr1 = new Address("Av. Costanera", "123", "Llanquihue", "Región de Los Lagos");
        Address addr2 = new Address("Pedro Montt", "456", "Puerto Varas", "Región de Los Lagos");
        Address addr3 = new Address("Ruta 5 Sur", "Km 950", "Frutillar", "Región de Los Lagos");

        System.out.println("=== Prueba con RUT inválido ===");
        try {
            Employee invalido = new Employee("20.214.256-0", "Victor", "Valdivia", addr1,
                    "Asistente", 500000);
        } catch (InvalidRutException e) {
            System.out.println("RUT rechazado correctamente: " + e.getMessage());
        }
        System.out.println();

        Employee emp1 = new Employee("12.345.678-5", "Carlos", "Muñoz", addr1,
                "Guía Turístico", 850000);
        Employee emp2 = new Employee("23.456.789-6", "María", "González", addr2,
                "Coordinadora de Operaciones", 1200000);
        Employee emp3 = new Employee("34.567.890-5", "Pedro", "Soto", addr3,
                "Conductor Turístico", 720000);

        String json = """
                {
                  "empleados": [
                    %s,
                    %s,
                    %s
                  ]
                }
                """.formatted(
                emp1, emp2, emp3,
                emp1.getFirstName(), emp1.getLastName(), emp1.getBaseSalary(),
                emp2.getFirstName(), emp2.getLastName(), emp2.getBaseSalary(),
                emp3.getFirstName(), emp3.getLastName(), emp3.getBaseSalary());

        System.out.println(json);
    }
}
