package data;

import model.*;
import util.InvalidRutException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.*;

/**
 * Utilidad estática encargada de la carga, filtrado y persistencia
 * de servicios turísticos. Lee el archivo tours.csv, parsea sus
 * registros y construye objetos de la jerarquía TourService según
 * la columna "type".
 */
public class DataManager {

    public static final String DELIMITER = ";";
    private static final String[] VALID_TYPES =
            {"GastronomicRoute", "LakeCruise", "CulturalExcursion"};

    private static final Set<String> VALID_TYPE_SET =
            Set.of(VALID_TYPES);

    private DataManager() {}

    /**
     * Lee un archivo de texto con registros separados por {@code ;} y construye
     * una lista de objetos {@link TourService}. Cada línea debe contener 19 campos
     * en el siguiente orden:
     * <pre>
     * id;type;name;durationHours;numberOfStops;boatType;historicalPlace;price;rut;firstName;lastName;street;number;city;region;position;baseSalary;motherTongue;secondLanguage
     * </pre>
     *
     * @param filePath ruta al archivo de datos
     * @return lista de servicios turísticos cargados desde el archivo
     */
    public static List<TourService> loadServices(String filePath) {
        List<TourService> services = new ArrayList<>();
        int lineNumber = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length < 19) {
                    System.err.println("Línea " + lineNumber + " ignorada: número incorrecto de campos " +
                            "(" + parts.length + " de 19).");
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

                    Address address = new Address(street, number, city, region);
                    TouristGuide guide = new TouristGuide(rut, firstName, lastName,
                            address, position, baseSalary, motherTongue, secondLanguage);

                    TourService service = createService(type, id, name, durationHours, parts, price, guide);
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

    private static TourService createService(String type, int id, String name, double durationHours,
                                              String[] parts, double price, TouristGuide guide) {
        switch (type) {
            case "GastronomicRoute": {
                int numberOfStops = Integer.parseInt(parts[4].trim());
                return new GastronomicRoute(id, name, durationHours, numberOfStops, price, guide);
            }
            case "LakeCruise": {
                String boatType = parts[5].trim();
                return new LakeCruise(id, name, durationHours, boatType, price, guide);
            }
            case "CulturalExcursion": {
                String historicalPlace = parts[6].trim();
                return new CulturalExcursion(id, name, durationHours, historicalPlace, price, guide);
            }
            default:
                throw new IllegalArgumentException("Tipo de servicio no soportado: " + type);
        }
    }

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
                    valueOf(guide.getSecondLanguage())
            );
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar el servicio en " + filePath + ": " + e.getMessage());
        }
    }

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

    private static String valueOf(String s) {
        return s == null ? "" : s;
    }

    public static List<TourService> filterByPrice(List<TourService> list, double maxPrice) {
        return list.stream()
                .filter(t -> t.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public static List<TourService> filterByMotherTongue(List<TourService> list, String motherTongue) {
        return list.stream()
                .filter(t -> t.getGuide().getMotherTongue()
                        .equalsIgnoreCase(motherTongue))
                .collect(Collectors.toList());
    }
}
