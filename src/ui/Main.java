package ui;

import data.DataManager;
import model.*;
import util.*;

import java.util.*;
import java.util.stream.*;

public class Main {

    private static final String DATA_FILE = "resources/tours.csv";

    private static final Map<String, String> ISO_LANGUAGES = new LinkedHashMap<>();
    static {
        ISO_LANGUAGES.put("es", "Español");
        ISO_LANGUAGES.put("en", "Inglés");
        ISO_LANGUAGES.put("fr", "Francés");
        ISO_LANGUAGES.put("pt", "Portugués");
        ISO_LANGUAGES.put("de", "Alemán");
        ISO_LANGUAGES.put("it", "Italiano");
        ISO_LANGUAGES.put("ja", "Japonés");
        ISO_LANGUAGES.put("zh", "Chino");
        ISO_LANGUAGES.put("ru", "Ruso");
        ISO_LANGUAGES.put("ko", "Coreano");
    }

    public static void main(String[] args) {
        List<Tours> tours = DataManager.loadTours(DATA_FILE);
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            printMenu();
            option = readInt(scanner, "Seleccione una opción: ");
            System.out.println();

            switch (option) {
                case 1 -> listAll(tours);
                case 2 -> searchByPrice(scanner, tours);
                case 3 -> searchByMotherTongue(scanner, tours);
                case 4 -> {
                    addTour(scanner, tours);
                    tours = DataManager.loadTours(DATA_FILE);
                }
                case 5 -> showLastAdded();
                case 6 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
            System.out.println();
        } while (option != 6);

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("=== LLANQUIHUE TOUR ===");
        System.out.println("1. Listar todos los tours");
        System.out.println("2. Buscar por precio máximo");
        System.out.println("3. Buscar por lengua materna del guía");
        System.out.println("4. Agregar un nuevo tour");
        System.out.println("5. Ver último tour agregado");
        System.out.println("6. Salir");
    }

    private static void listAll(List<Tours> tours) {
        if (tours.isEmpty()) {
            System.out.println("No hay tours registrados.");
            return;
        }
        System.out.println("=== Todos los tours (" + tours.size() + ") ===");
        tours.forEach(System.out::println);
    }

    private static void showLastAdded() {
        List<Tours> all = DataManager.loadTours(DATA_FILE);
        if (all.isEmpty()) {
            System.out.println("No hay tours registrados.");
            return;
        }
        Tours last = all.get(all.size() - 1);
        System.out.println("=== Último tour registrado ===");
        System.out.println(last);
    }

    private static void searchByPrice(Scanner scanner, List<Tours> tours) {
        double maxPrice = readDouble(scanner, "Ingrese el precio máximo: ");
        List<Tours> result = DataManager.filterByPrice(tours, maxPrice);
        if (result.isEmpty()) {
            System.out.println("No se encontraron tours con precio <= $" + String.format("%.0f", maxPrice));
            return;
        }
        System.out.println("=== Tours con precio <= $" + String.format("%.0f", maxPrice) + " ===");
        result.forEach(System.out::println);
        System.out.println("(" + result.size() + " tours encontrados)");
    }

    private static void searchByMotherTongue(Scanner scanner, List<Tours> tours) {
        showAvailableLanguages(tours);
        String motherTongue = readLanguageCode(scanner, "Ingrese código ISO de la lengua materna: ");
        List<Tours> result = DataManager.filterByMotherTongue(tours, motherTongue);
        if (result.isEmpty()) {
            System.out.println("No se encontraron tours con guía de lengua materna '" + label(motherTongue) + "'");
            return;
        }
        System.out.println("=== Tours con guía de lengua materna '" + label(motherTongue) + "' ===");
        result.forEach(System.out::println);
        System.out.println("(" + result.size() + " tours encontrados)");
    }

    private static void addTour(Scanner scanner, List<Tours> tours) {
        System.out.println("=== Agregar nuevo tour ===");

        String productName = readMandatory(scanner, "Nombre del tour: ");
        String location = readOptional(scanner, "Ubicación");
        String duration = readOptional(scanner, "Duración");
        double price = readDoubleMandatory(scanner, "Precio: ");
        String rut = readRut(scanner);
        String firstName = readMandatory(scanner, "Nombre del guía: ");
        String lastName = readMandatory(scanner, "Apellido del guía: ");
        String street = readMandatory(scanner, "Calle: ");
        String number = readOptional(scanner, "Número");
        String city = readOptional(scanner, "Ciudad");
        String region = readOptional(scanner, "Región");
        String position = readOptional(scanner, "Cargo");
        double baseSalary = readDoubleMandatory(scanner, "Sueldo base: ");
        showAvailableLanguages(tours);
        String motherTongue = readLanguageCode(scanner, "Lengua materna (código ISO): ");
        String secondLanguage = readLanguageOptional(scanner, "Segunda lengua");

        try {
            Address address = new Address(street, number, city, region);
            TouristGuide guide = new TouristGuide(rut, firstName, lastName, address,
                    position, baseSalary, motherTongue, secondLanguage);
            int id = DataManager.getNextId(DATA_FILE);
            Tours tour = new Tours(id, productName, price, location, duration, guide);

            DataManager.appendTour(DATA_FILE, tour);
            System.out.println("Tour agregado exitosamente (ID " + id + ").");

        } catch (InvalidRutException e) {
            System.out.println("Error inesperado: " + e.getMessage() + " El tour no fue guardado.");
        } catch (NumberFormatException e) {
            System.out.println("Error: formato de número inválido. El tour no fue guardado.");
        }
    }

    private static String readMandatory(Scanner scanner, String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Este campo es obligatorio. Intente nuevamente.");
            }
        } while (input.isEmpty());
        return input;
    }

    private static void showAvailableLanguages(List<Tours> tours) {
        Set<String> codes = tours.stream()
                .map(t -> t.getTouristGuide().getMotherTongue())
                .collect(Collectors.toSet());
        if (codes.isEmpty()) codes = ISO_LANGUAGES.keySet();
        System.out.print("Idiomas disponibles: ");
        System.out.println(codes.stream()
                .sorted()
                .map(c -> c + " (" + label(c) + ")")
                .collect(Collectors.joining(", ")));
    }

    private static String label(String isoCode) {
        return ISO_LANGUAGES.getOrDefault(isoCode, isoCode);
    }

    private static String readLanguageCode(Scanner scanner, String prompt) {
        while (true) {
            String input = readMandatory(scanner, prompt);
            if (ISO_LANGUAGES.containsKey(input)) {
                return input;
            }
            System.out.println("Código ISO inválido. Use uno de los códigos listados arriba.");
        }
    }

    private static String readRut(Scanner scanner) {
        while (true) {
            String input = readMandatory(scanner, "RUT del guía (formato 12345678-9 o 1234567-K): ");
            try {
                RutValidator.validate(input);
                return input;
            } catch (InvalidRutException e) {
                System.out.println("RUT inválido: " + e.getMessage());
            }
        }
    }

    private static String readLanguageOptional(Scanner scanner, String fieldName) {
        while (true) {
            System.out.print(fieldName + " (código ISO, opcional, presione Enter para omitir): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty() || ISO_LANGUAGES.containsKey(input)) {
                return input;
            }
            System.out.println("Código ISO inválido. Use uno de los códigos listados arriba.");
        }
    }

    private static String readOptional(Scanner scanner, String fieldName) {
        System.out.print(fieldName + " (opcional, presione Enter para omitir): ");
        String input = scanner.nextLine().trim();
        return input;
    }

    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }

    private static double readDoubleMandatory(Scanner scanner, String prompt) {
        while (true) {
            String input = readMandatory(scanner, prompt);
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
    }
}
