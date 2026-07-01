package ui;

import data.DataManager;
import model.*;
import util.*;

import java.util.*;
import java.util.stream.*;

/**
 * Punto de entrada del sistema Llanquihue Tour. Presenta un menú
 * interactivo con las siguientes opciones:
 * <ol>
 *   <li>Listar todos los servicios turísticos registrados.</li>
 *   <li>Buscar servicios por precio máximo.</li>
 *   <li>Buscar servicios por lengua materna del guía.</li>
 *   <li>Agregar un nuevo servicio (con selección del tipo y validación
 *       de campos obligatorios y valores positivos).</li>
 *   <li>Ver el último servicio agregado.</li>
 *   <li>Salir.</li>
 * </ol>
 * Los datos se cargan y persisten en el archivo {@code resources/tours.csv}
 * a través de la clase {@link data.DataManager}.
 */
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
        List<TourService> services = DataManager.loadServices(DATA_FILE);
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            printMenu();
            option = readInt(scanner, "Seleccione una opción: ");
            System.out.println();

            switch (option) {
                case 1 -> listAll(services);
                case 2 -> searchByPrice(scanner, services);
                case 3 -> searchByMotherTongue(scanner, services);
                case 4 -> {
                    addService(scanner, services);
                    services = DataManager.loadServices(DATA_FILE);
                }
                case 5 -> showLastAdded();
                case 6 -> System.out.println("¡Hasta luego!");
                case 7 -> filterByType(scanner, services);
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
            System.out.println();
        } while (option != 6);

        scanner.close();
    }

    /**
     * Imprime el menú principal de opciones en la consola.
     */
    private static void printMenu() {
        System.out.println("=== LLANQUIHUE TOUR ===");
        System.out.println("1. Listar todos los servicios");
        System.out.println("2. Buscar por precio máximo");
        System.out.println("3. Buscar por lengua materna del guía");
        System.out.println("4. Agregar un nuevo servicio");
        System.out.println("5. Ver último servicio agregado");
        System.out.println("6. Salir");
        System.out.println("7. Filtrar servicios por tipo");
    }

    /**
     * Muestra todos los servicios turísticos registrados utilizando
     * el método {@code toString()} polimórfico de cada subclase.
     *
     * @param services lista de servicios a mostrar
     */
    private static void listAll(List<TourService> services) {
        if (services.isEmpty()) {
            System.out.println("No hay servicios registrados.");
            return;
        }
        System.out.println("=== Todos los servicios (" + services.size() + ") ===");
        services.forEach(System.out::println);
    }

    /**
     * Solicita al usuario un tipo de servicio y muestra únicamente
     * aquellos servicios cuya clase concreta coincide con el tipo
     * seleccionado. El filtrado utiliza el método polimórfico
     * {@link TourService#getServiceType()} y el despliegue utiliza
     * {@link TourService#displayInformation()}, demostrando el
     * comportamiento polimórfico sin usar {@code instanceof}.
     *
     * @param scanner  escáner conectado a la entrada estándar
     * @param services lista de servicios sobre la cual filtrar
     */
    private static void filterByType(Scanner scanner, List<TourService> services) {
        System.out.println("=== Filtrar servicios por tipo ===");
        System.out.println("1. GastronomicRoute (Ruta Gastronómica)");
        System.out.println("2. LakeCruise (Paseo Lacustre)");
        System.out.println("3. CulturalExcursion (Excursión Cultural)");
        int typeOption = readInt(scanner, "Seleccione el tipo: ");

        String targetType;
        switch (typeOption) {
            case 1 -> targetType = "GastronomicRoute";
            case 2 -> targetType = "LakeCruise";
            case 3 -> targetType = "CulturalExcursion";
            default -> {
                System.out.println("Opción inválida.");
                return;
            }
        }

        boolean found = false;
        for (TourService service : services) {
            if (service.getServiceType().equals(targetType)) {
                if (!found) {
                    System.out.println("=== Servicios de tipo " + targetType + " ===");
                    found = true;
                }
                System.out.println();
                service.displayInformation();
            }
        }

        if (!found) {
            System.out.println("No se encontraron servicios del tipo \"" + targetType + "\".");
        }
    }

    /**
     * Carga nuevamente el archivo CSV y muestra el último servicio
     * registrado (última línea del archivo).
     */
    private static void showLastAdded() {
        List<TourService> all = DataManager.loadServices(DATA_FILE);
        if (all.isEmpty()) {
            System.out.println("No hay servicios registrados.");
            return;
        }
        TourService last = all.get(all.size() - 1);
        System.out.println("=== Último servicio registrado ===");
        System.out.println(last);
    }

    /**
     * Solicita un precio máximo al usuario y muestra todos los
     * servicios con precio menor o igual al valor ingresado.
     *
     * @param scanner  escáner conectado a la entrada estándar
     * @param services lista de servicios sobre la cual filtrar
     */
    private static void searchByPrice(Scanner scanner, List<TourService> services) {
        double maxPrice = readDouble(scanner, "Ingrese el precio máximo: ");
        List<TourService> result = DataManager.filterByPrice(services, maxPrice);
        if (result.isEmpty()) {
            System.out.println("No se encontraron servicios con precio <= $" + String.format("%.0f", maxPrice));
            return;
        }
        System.out.println("=== Servicios con precio <= $" + String.format("%.0f", maxPrice) + " ===");
        result.forEach(System.out::println);
        System.out.println("(" + result.size() + " servicios encontrados)");
    }

    /**
     * Solicita un código ISO de idioma al usuario y muestra los
     * servicios cuyo guía tiene esa lengua materna. Previamente
     * lista los idiomas disponibles en los datos cargados.
     *
     * @param scanner  escáner conectado a la entrada estándar
     * @param services lista de servicios sobre la cual filtrar
     */
    private static void searchByMotherTongue(Scanner scanner, List<TourService> services) {
        showAvailableLanguages(services);
        String motherTongue = readLanguageCode(scanner, "Ingrese código ISO de la lengua materna: ");
        List<TourService> result = DataManager.filterByMotherTongue(services, motherTongue);
        if (result.isEmpty()) {
            System.out.println("No se encontraron servicios con guía de lengua materna '" + label(motherTongue) + "'");
            return;
        }
        System.out.println("=== Servicios con guía de lengua materna '" + label(motherTongue) + "' ===");
        result.forEach(System.out::println);
        System.out.println("(" + result.size() + " servicios encontrados)");
    }

    /**
     * Muestra los códigos ISO de idiomas disponibles como lengua
     * materna entre los guías de los servicios cargados.
     *
     * @param services lista de servicios de la cual extraer los idiomas
     */
    private static void showAvailableLanguages(List<TourService> services) {
        Set<String> codes = services.stream()
                .map(t -> t.getGuide().getMotherTongue())
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

    /**
     * Guía al usuario a través de un formulario para agregar un nuevo
     * servicio turístico. El usuario debe seleccionar el tipo de
     * servicio y luego ingresar los campos comunes y específicos con
     * validación de valores positivos para campos numéricos.
     *
     * @param scanner  escáner conectado a la entrada estándar
     * @param services lista actual de servicios (se usa solo para
     *                 mostrar idiomas disponibles)
     */
    private static void addService(Scanner scanner, List<TourService> services) {
        System.out.println("=== Agregar nuevo servicio ===");
        System.out.println("Seleccione el tipo de servicio:");
        System.out.println("1. Ruta Gastronómica (GastronomicRoute)");
        System.out.println("2. Paseo Lacustre (LakeCruise)");
        System.out.println("3. Excursión Cultural (CulturalExcursion)");
        int typeOption = readInt(scanner, "Opción: ");
        System.out.println();

        String serviceType;
        switch (typeOption) {
            case 1 -> serviceType = "GastronomicRoute";
            case 2 -> serviceType = "LakeCruise";
            case 3 -> serviceType = "CulturalExcursion";
            default -> {
                System.out.println("Opción inválida. Operación cancelada.");
                return;
            }
        }

        String name = readMandatory(scanner, "Nombre del servicio: ");
        double durationHours = readPositiveDouble(scanner, "Duración (horas): ", "La duración debe ser un número positivo.");

        int numberOfStops = 0;
        String boatType = "";
        String historicalPlace = "";

        switch (serviceType) {
            case "GastronomicRoute" ->
                numberOfStops = readPositiveInt(scanner, "Número de paradas: ", "El número de paradas debe ser un entero positivo.");
            case "LakeCruise" ->
                boatType = readMandatory(scanner, "Tipo de embarcación: ");
            case "CulturalExcursion" ->
                historicalPlace = readMandatory(scanner, "Lugar histórico: ");
        }

        double price = readPositiveDouble(scanner, "Precio: ", "El precio debe ser un valor positivo.");
        String rut = readRut(scanner);
        String firstName = readMandatory(scanner, "Nombre del guía: ");
        String lastName = readMandatory(scanner, "Apellido del guía: ");
        String street = readMandatory(scanner, "Calle: ");
        String number = readOptional(scanner, "Número");
        String city = readOptional(scanner, "Ciudad");
        String region = readOptional(scanner, "Región");
        String position = readOptional(scanner, "Cargo");
        double baseSalary = readPositiveDouble(scanner, "Sueldo base: ", "El sueldo base debe ser un valor positivo.");
        showAvailableLanguages(services);
        String motherTongue = readLanguageCode(scanner, "Lengua materna (código ISO): ");
        String secondLanguage = readLanguageOptional(scanner, "Segunda lengua");

        try {
            Address address = new Address(street, number, city, region);
            TouristGuide guide = new TouristGuide(rut, firstName, lastName,
                    address, position, baseSalary, motherTongue, secondLanguage);
            int id = DataManager.getNextId(DATA_FILE);

            TourService service;
            switch (serviceType) {
                case "GastronomicRoute" ->
                    service = new GastronomicRoute(id, name, durationHours, numberOfStops, price, guide);
                case "LakeCruise" ->
                    service = new LakeCruise(id, name, durationHours, boatType, price, guide);
                case "CulturalExcursion" ->
                    service = new CulturalExcursion(id, name, durationHours, historicalPlace, price, guide);
                default -> {
                    System.out.println("Error interno: tipo de servicio no reconocido.");
                    return;
                }
            }

            DataManager.appendService(DATA_FILE, service);
            System.out.println("Servicio agregado exitosamente (ID " + id + ").");

        } catch (InvalidRutException e) {
            System.out.println("RUT inválido (" + e.getMessage() + "). El servicio no fue guardado.");
        }
    }

    /**
     * Lee un valor decimal desde la entrada estándar y valida que
     * sea un número positivo (> 0). Reintenta la lectura hasta que
     * el usuario ingrese un valor válido.
     *
     * @param scanner  escáner conectado a la entrada estándar
     * @param prompt   mensaje mostrado al usuario
     * @param errorMsg mensaje de error cuando el valor no es positivo
     * @return número decimal positivo ingresado por el usuario
     */
    private static double readPositiveDouble(Scanner scanner, String prompt, String errorMsg) {
        while (true) {
            double value = readDoubleMandatory(scanner, prompt);
            if (value > 0) {
                return value;
            }
            System.out.println(errorMsg);
        }
    }

    /**
     * Lee un valor entero desde la entrada estándar y valida que
     * sea un número positivo (> 0). Reintenta la lectura hasta que
     * el usuario ingrese un valor válido.
     *
     * @param scanner  escáner conectado a la entrada estándar
     * @param prompt   mensaje mostrado al usuario
     * @param errorMsg mensaje de error cuando el valor no es positivo
     * @return número entero positivo ingresado por el usuario
     */
    private static int readPositiveInt(Scanner scanner, String prompt, String errorMsg) {
        while (true) {
            int value = readIntMandatory(scanner, prompt);
            if (value > 0) {
                return value;
            }
            System.out.println(errorMsg);
        }
    }

    /**
     * Lee un campo de texto obligatorio. Reintenta hasta que el
     * usuario ingrese un valor no vacío.
     *
     * @param scanner escáner conectado a la entrada estándar
     * @param prompt  mensaje mostrado al usuario
     * @return cadena no vacía ingresada por el usuario
     */
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

    /**
     * Lee un valor decimal obligatorio. Reintenta hasta que el
     * usuario ingrese un número válido.
     *
     * @param scanner escáner conectado a la entrada estándar
     * @param prompt  mensaje mostrado al usuario
     * @return número decimal ingresado por el usuario
     */
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

    /**
     * Lee un valor entero obligatorio. Reintenta hasta que el
     * usuario ingrese un número entero válido.
     *
     * @param scanner escáner conectado a la entrada estándar
     * @param prompt  mensaje mostrado al usuario
     * @return número entero ingresado por el usuario
     */
    private static int readIntMandatory(Scanner scanner, String prompt) {
        while (true) {
            String input = readMandatory(scanner, prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
    }

    /**
     * Lee un código ISO de idioma obligatorio y valida que exista
     * en el mapa de idiomas soportados.
     *
     * @param scanner escáner conectado a la entrada estándar
     * @param prompt  mensaje mostrado al usuario
     * @return código ISO válido ingresado por el usuario
     */
    private static String readLanguageCode(Scanner scanner, String prompt) {
        while (true) {
            String input = readMandatory(scanner, prompt);
            if (ISO_LANGUAGES.containsKey(input)) {
                return input;
            }
            System.out.println("Código ISO inválido. Use uno de los códigos listados arriba.");
        }
    }

    /**
     * Lee un RUT chileno obligatorio y lo valida con el algoritmo
     * del módulo 11 mediante {@link RutValidator}.
     *
     * @param scanner escáner conectado a la entrada estándar
     * @return RUT válido ingresado por el usuario
     */
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

    /**
     * Lee un código ISO de idioma opcional. Si el usuario presiona
     * Enter sin ingresar valor retorna cadena vacía; en caso contrario
     * valida que el código exista en el mapa de idiomas soportados.
     *
     * @param scanner   escáner conectado a la entrada estándar
     * @param fieldName nombre descriptivo del campo para el mensaje
     * @return código ISO válido o cadena vacía si se omite
     */
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

    /**
     * Lee un campo de texto opcional. Si el usuario presiona Enter
     * sin ingresar valor retorna cadena vacía.
     *
     * @param scanner   escáner conectado a la entrada estándar
     * @param fieldName nombre descriptivo del campo para el mensaje
     * @return cadena ingresada o cadena vacía si se omite
     */
    private static String readOptional(Scanner scanner, String fieldName) {
        System.out.print(fieldName + " (opcional, presione Enter para omitir): ");
        String input = scanner.nextLine().trim();
        return input;
    }

    /**
     * Lee un valor decimal sin validación de signo (puede ser cero
     * o negativo). Se utiliza para el filtro de precio máximo.
     *
     * @param scanner escáner conectado a la entrada estándar
     * @param prompt  mensaje mostrado al usuario
     * @return número decimal ingresado por el usuario
     */
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

    /**
     * Lee un valor entero para la selección de opción del menú.
     *
     * @param scanner escáner conectado a la entrada estándar
     * @param prompt  mensaje mostrado al usuario
     * @return número entero ingresado por el usuario
     */
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
