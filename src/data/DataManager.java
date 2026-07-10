package data;

import model.*;
import util.InvalidRutException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.*;

/**
 * Utilidad estática encargada de la carga, filtrado y persistencia
 * de servicios turísticos. Lee el archivo tours.csv con codificación
 * UTF-8, parsea sus registros y construye objetos concretos de la
 * jerarquía {@link TourService} según el valor de la columna "type".
 * <p>
 * Cada línea debe contener 20 campos separados por punto y coma. El
 * campo 20 ({@code maxCapacity}) es obligatorio y debe ser un entero
 * positivo.
 * <p>
 * Incluye validación de cada línea del CSV: si un registro no cumple
 * las reglas de negocio (tipo desconocido, valores numéricos inválidos,
 * campos vacíos obligatorios, RUT inválido, capacidad máxima inválida)
 * se omite con un mensaje descriptivo que indica el número de línea y
 * la causa, sin interrumpir la carga del resto del archivo.
 * </p>
 */
public class DataManager {

    /**
     * Delimitador de campos en el archivo CSV.
     */
    public static final String DELIMITER = ";";

    private static final String[] VALID_TYPES =
            {"GastronomicRoute", "LakeCruise", "CulturalExcursion"};

    private static final Set<String> VALID_TYPE_SET =
            Set.of(VALID_TYPES);

    private DataManager() {}

    /**
     * Lee un archivo de texto con registros separados por {@code ;} y construye
     * una lista de objetos {@link Registerable}. Cada línea debe contener 20 campos
     * en el siguiente orden:
     * <pre>
     * id;type;name;durationHours;numberOfStops;boatType;historicalPlace;price;rut;firstName;lastName;street;number;city;region;position;baseSalary;motherTongue;secondLanguage;maxCapacity
     * </pre>
     *
     * @param filePath ruta al archivo de datos
     * @return lista de entidades registrables cargadas desde el archivo
     */
    public static List<Registerable> loadServices(String filePath) {
        List<Registerable> services = new ArrayList<>();
        int lineNumber = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length < 20) {
                    System.err.println("Línea " + lineNumber + " ignorada: número incorrecto de campos " +
                            "(" + parts.length + " de 20).");
                    continue;
                }
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String type = parts[1].trim();

                    if (type.isEmpty() || !VALID_TYPE_SET.contains(type)) {
                        System.err.println("Línea " + lineNumber + " ignorada: tipo de servicio " +
                                "desconocido '" + type + "'.");
                        continue;
                    }

                    String name = parts[2].trim();
                    if (name.isEmpty()) {
                        System.err.println("Línea " + lineNumber + " ignorada: el nombre del servicio " +
                                "no puede estar vacío.");
                        continue;
                    }

                    double durationHours = parseDoubleSafe(parts[3], "duración");
                    if (durationHours <= 0) {
                        System.err.println("Línea " + lineNumber + " ignorada: duración inválida " +
                                "('" + parts[3] + "').");
                        continue;
                    }

                    double price = parseDoubleSafe(parts[7], "precio");
                    if (price <= 0) {
                        System.err.println("Línea " + lineNumber + " ignorada: precio inválido " +
                                "('" + parts[7] + "').");
                        continue;
                    }

                    double baseSalary = parseDoubleSafe(parts[16], "sueldo base");
                    if (baseSalary <= 0) {
                        System.err.println("Línea " + lineNumber + " ignorada: sueldo base inválido " +
                                "('" + parts[16] + "').");
                        continue;
                    }

                    String rut = parts[8].trim();
                    String firstName = parts[9].trim();
                    String lastName = parts[10].trim();
                    String street = parts[11].trim();
                    String number = parts[12].trim();
                    String city = parts[13].trim();
                    String region = parts[14].trim();
                    String position = parts[15].trim();
                    String motherTongue = parts[17].trim();
                    String secondLanguage = parts[18].trim();
                    int maxCapacity = Integer.parseInt(parts[19].trim());
                    if (maxCapacity <= 0) {
                        System.err.println("Línea " + lineNumber + " ignorada: capacidad máxima inválida " +
                                "('" + parts[19] + "').");
                        continue;
                    }

                    Address address = new Address(street, number, city, region);
                    TouristGuide guide = new TouristGuide(rut, firstName, lastName,
                            address, position, baseSalary, motherTongue, secondLanguage);

                    TourService service = createService(type, id, name, durationHours, parts, price, guide, maxCapacity);
                    services.add(service);

                } catch (InvalidRutException e) {
                    System.err.println("Línea " + lineNumber + " ignorada: RUT inválido (" +
                            e.getMessage() + ").");
                } catch (IllegalArgumentException e) {
                    System.err.println("Línea " + lineNumber + " ignorada: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Línea " + lineNumber + " ignorada: error inesperado (" +
                            e.getMessage() + ").");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + filePath
                    + ". Se iniciará con una lista vacía.");
        } catch (IOException e) {
            System.err.println("Error de lectura del archivo " + filePath + ": " + e.getMessage());
        }
        return services;
    }

    /**
     * Intenta parsear un campo textual como número decimal de forma segura.
     * Si el campo está vacío o no es un número válido retorna {@code -1},
     * valor que posteriormente se rechaza en las validaciones de negocio
     * (todo valor numérico debe ser positivo).
     *
     * @param field     valor textual del campo a parsear
     * @param fieldName nombre descriptivo del campo (solo para depuración)
     * @return el valor numérico parseado, o {@code -1} si el campo está
     *         vacío o tiene formato inválido
     */
    private static double parseDoubleSafe(String field, String fieldName) {
        String trimmed = field.trim();
        if (trimmed.isEmpty()) {
            return -1;
        }
        try {
            return Double.parseDouble(trimmed);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Construye la instancia concreta de {@link TourService} que
     * corresponde al tipo indicado, extrayendo de las partes del CSV
     * el campo adicional específico de cada subclase.
     *
     * @param type          tipo de servicio ("GastronomicRoute", "LakeCruise"
     *                      o "CulturalExcursion")
     * @param id            identificador único
     * @param name          nombre del servicio
     * @param durationHours duración en horas
     * @param parts         arreglo completo de campos del CSV
     * @param price         precio del servicio
     * @param guide         guía turístico asignado
     * @return instancia de la subclase correspondiente
     * @throws IllegalArgumentException si {@code type} no es un valor
     *                                  reconocido
     */
    private static TourService createService(String type, int id, String name, double durationHours,
                                              String[] parts, double price, TouristGuide guide,
                                              int maxCapacity) {
        switch (type) {
            case "GastronomicRoute": {
                int numberOfStops = Integer.parseInt(parts[4].trim());
                return new GastronomicRoute(id, name, durationHours, numberOfStops, price, guide, maxCapacity);
            }
            case "LakeCruise": {
                String boatType = parts[5].trim();
                return new LakeCruise(id, name, durationHours, boatType, price, guide, maxCapacity);
            }
            case "CulturalExcursion": {
                String historicalPlace = parts[6].trim();
                return new CulturalExcursion(id, name, durationHours, historicalPlace, price, guide, maxCapacity);
            }
            default:
                throw new IllegalArgumentException("Tipo de servicio no soportado: " + type);
        }
    }

    /**
     * Calcula el siguiente identificador disponible recorriendo el archivo
     * CSV y encontrando el valor máximo de ID existente.
     *
     * @param filePath ruta al archivo de datos
     * @return el siguiente ID disponible ({@code maxId + 1}), o {@code 1}
     *         si el archivo está vacío o no se pudo leer
     */
    public static int getNextId(String filePath) {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER);
                if (parts.length >= 1) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        if (id > maxId) maxId = id;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("Advertencia: no se pudo leer el archivo " + filePath
                    + " para calcular el siguiente ID. Se usará 1.");
        }
        return maxId + 1;
    }

    /**
     * Agrega un nuevo servicio turístico al final del archivo CSV,
     * serializando todos sus campos incluyendo los del guía asociado
     * y la dirección. El archivo se abre en modo append (adicional)
     * para no sobrescribir los registros existentes.
     *
     * @param filePath ruta al archivo de datos
     * @param service  servicio turístico a persistir; debe tener un guía
     *                 asignado no nulo con dirección completa
     */
    public static void appendService(String filePath, TourService service) {
        TouristGuide guide = service.getGuide();
        if (guide == null) {
            System.err.println("Error: el servicio no tiene un guía asignado. No se guardó.");
            return;
        }
        Address address = guide.getAddress();

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            String line = String.join(DELIMITER,
                    String.valueOf(service.getId()),
                    service.getServiceType(),
                    valueOf(service.getName()),
                    String.valueOf(service.getDurationHours()),
                    specificField(service, 0),
                    specificField(service, 1),
                    specificField(service, 2),
                    String.valueOf(service.getPrice()),
                    valueOf(guide.getRut()),
                    valueOf(guide.getFirstName()),
                    valueOf(guide.getLastName()),
                    valueOf(address != null ? address.getStreet() : null),
                    valueOf(address != null ? address.getNumber() : null),
                    valueOf(address != null ? address.getCity() : null),
                    valueOf(address != null ? address.getRegion() : null),
                    valueOf(guide.getPosition()),
                    String.valueOf(guide.getBaseSalary()),
                    valueOf(guide.getMotherTongue()),
                    valueOf(guide.getSecondLanguage()),
                    String.valueOf(service.getMaxCapacity())
            );
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar el servicio en " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Retorna el valor serializado del campo específico de cada subclase
     * según su posición en el CSV. Las posiciones son:
     * <ul>
     *   <li>{@code index = 0} — número de paradas (GastronomicRoute)</li>
     *   <li>{@code index = 1} — tipo de embarcación (LakeCruise)</li>
     *   <li>{@code index = 2} — lugar histórico (CulturalExcursion)</li>
     * </ul>
     * Los índices que no corresponden al tipo concreto retornan cadena vacía.
     *
     * @param service servicio turístico del cual extraer el campo
     * @param index   índice posicional del campo en el CSV (0-2)
     * @return valor del campo específico, o cadena vacía si no aplica
     */
    private static String specificField(TourService service, int index) {
        switch (service.getServiceType()) {
            case "GastronomicRoute":
                return index == 0 ? String.valueOf(((GastronomicRoute) service).getNumberOfStops()) : "";
            case "LakeCruise":
                return index == 1 ? valueOf(((LakeCruise) service).getBoatType()) : "";
            case "CulturalExcursion":
                return index == 2 ? valueOf(((CulturalExcursion) service).getHistoricalPlace()) : "";
            default:
                return "";
        }
    }

    /**
     * Convierte una cadena a su representación CSV, retornando cadena
     * vacía si el valor es {@code null}. Evita que valores nulos
     * escriban el literal "null" en el archivo.
     *
     * @param s cadena a convertir, puede ser {@code null}
     * @return la cadena original o {@code ""} si es {@code null}
     */
    private static String valueOf(String s) {
        return s == null ? "" : s;
    }

    /**
     * Filtra una lista de entidades registrables cuyo precio sea menor
     * o igual al valor indicado. Solo considera elementos que sean
     * instancias de {@link TourService}, usando {@code instanceof}
     * para resolver el tipo concreto.
     *
     * @param list     lista de entidades a filtrar
     * @param maxPrice precio máximo (inclusive)
     * @return lista de entidades con precio ≤ {@code maxPrice}
     */
    public static List<Registerable> filterByPrice(List<Registerable> list, double maxPrice) {
        return list.stream()
                .filter(r -> r instanceof TourService && ((TourService) r).getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Filtra una lista de entidades registrables cuyo guía tenga la
     * lengua materna indicada. Solo considera elementos que sean
     * instancias de {@link TourService}, usando {@code instanceof}
     * para resolver el tipo concreto. La comparación no distingue
     * entre mayúsculas y minúsculas.
     *
     * @param list         lista de entidades a filtrar
     * @param motherTongue código ISO de la lengua materna a buscar
     * @return lista de entidades cuyo guía habla {@code motherTongue}
     *         como lengua materna
     */
    public static List<Registerable> filterByMotherTongue(List<Registerable> list, String motherTongue) {
        return list.stream()
                .filter(r -> r instanceof TourService
                        && ((TourService) r).getGuide().getMotherTongue()
                                .equalsIgnoreCase(motherTongue))
                .collect(Collectors.toList());
    }
}
