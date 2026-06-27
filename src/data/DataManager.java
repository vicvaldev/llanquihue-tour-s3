package data;

import model.*;

import java.io.*;
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
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length >= 19) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String type = parts[1].trim();
                        String name = parts[2].trim();
                        double durationHours = Double.parseDouble(parts[3].trim());
                        double price = Double.parseDouble(parts[7].trim());
                        String rut = parts[8].trim();
                        String firstName = parts[9].trim();
                        String lastName = parts[10].trim();
                        String street = parts[11].trim();
                        String number = parts[12].trim();
                        String city = parts[13].trim();
                        String region = parts[14].trim();
                        String position = parts[15].trim();
                        double baseSalary = Double.parseDouble(parts[16].trim());
                        String motherTongue = parts[17].trim();
                        String secondLanguage = parts[18].trim();

                        Address address = new Address(street, number, city, region);
                        TouristGuide guide = new TouristGuide(rut, firstName, lastName,
                                address, position, baseSalary, motherTongue, secondLanguage);

                        TourService service = createService(type, id, name, durationHours, parts, price, guide);
                        if (service != null) {
                            services.add(service);
                        }
                    } catch (Exception e) {
                        System.err.println("Error al procesar línea: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + filePath);
            e.printStackTrace();
        }
        return services;
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
                System.err.println("Tipo de servicio desconocido: " + type);
                return null;
        }
    }

    public static int getNextId(String filePath) {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
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
        } catch (IOException ignored) {}
        return maxId + 1;
    }

    public static void appendService(String filePath, TourService service) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = String.join(DELIMITER,
                    String.valueOf(service.getId()),
                    service.getServiceType(),
                    valueOf(service.getName()),
                    String.valueOf(service.getDurationHours()),
                    specificField(service, 0),
                    specificField(service, 1),
                    specificField(service, 2),
                    String.valueOf(service.getPrice()),
                    valueOf(service.getGuide().getRut()),
                    valueOf(service.getGuide().getFirstName()),
                    valueOf(service.getGuide().getLastName()),
                    valueOf(service.getGuide().getAddress().getStreet()),
                    valueOf(service.getGuide().getAddress().getNumber()),
                    valueOf(service.getGuide().getAddress().getCity()),
                    valueOf(service.getGuide().getAddress().getRegion()),
                    valueOf(service.getGuide().getPosition()),
                    String.valueOf(service.getGuide().getBaseSalary()),
                    valueOf(service.getGuide().getMotherTongue()),
                    valueOf(service.getGuide().getSecondLanguage())
            );
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar el servicio: " + e.getMessage());
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
