package data;

import model.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * Utilidad estática encargada de la carga y filtrado de datos de tours.
 * Lee el archivo tours.txt, parsea sus registros y construye objetos
 * Tours con sus respectivos TouristGuide asociados.
 */
public class DataManager {

    private DataManager() {}

    /**
     * Lee un archivo de texto con registros separados por {@code ;} y construye
     * una lista de objetos {@link Tours}. Cada línea debe contener 16 campos
     * en el siguiente orden:
     * <pre>
     * id;productName;location;duration;price;rut;firstName;lastName;street;number;city;region;position;baseSalary;motherTongue;secondLanguage
     * </pre>
     *
     * @param filePath ruta al archivo de datos
     * @return lista de tours cargados desde el archivo
     */
    public static List<Tours> loadTours(String filePath) {
        List<Tours> tours = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 16) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String productName = parts[1].trim();
                        String location = parts[2].trim();
                        String duration = parts[3].trim();
                        double price = Double.parseDouble(parts[4].trim());
                        String rut = parts[5].trim();
                        String firstName = parts[6].trim();
                        String lastName = parts[7].trim();
                        String street = parts[8].trim();
                        String number = parts[9].trim();
                        String city = parts[10].trim();
                        String region = parts[11].trim();
                        String position = parts[12].trim();
                        double baseSalary = Double.parseDouble(parts[13].trim());
                        String motherTongue = parts[14].trim();
                        String secondLanguage = parts[15].trim();

                        Address address = new Address(street, number, city, region);
                        TouristGuide guide = new TouristGuide(rut, firstName, lastName,
                                address, position, baseSalary, motherTongue, secondLanguage);
                        Tours tour = new Tours(id, productName, price, location, duration, guide);
                        tours.add(tour);
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
        return tours;
    }

    /**
     * Filtra una lista de tours cuyo precio sea menor o igual al valor indicado.
     *
     * @param list     lista de tours a filtrar
     * @param maxPrice precio máximo (inclusive)
     * @return lista de tours con precio ≤ {@code maxPrice}
     */
    public static List<Tours> filterByPrice(List<Tours> list, double maxPrice) {
        return list.stream()
                .filter(t -> t.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Filtra una lista de tours cuyo guía tenga la lengua materna indicada
     * (la comparación no distingue mayúsculas/minúsculas).
     *
     * @param list         lista de tours a filtrar
     * @param motherTongue lengua materna del guía a buscar
     * @return lista de tours cuyo guía habla {@code motherTongue} como lengua materna
     */
    public static List<Tours> filterByMotherTongue(List<Tours> list, String motherTongue) {
        return list.stream()
                .filter(t -> t.getTouristGuide().getMotherTongue()
                        .equalsIgnoreCase(motherTongue))
                .collect(Collectors.toList());
    }
}
