package data;

import model.IOrder;
import model.PurchaseOrder;
import model.Registerable;
import model.TourService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Utilidad estática para la carga, escritura y persistencia de órdenes
 * de compra. Lee y escribe el archivo {@code orders.csv} con codificación
 * UTF-8, donde cada línea representa una orden con 8 campos separados
 * por punto y coma.
 * <p>
 * El orden de los campos es:
 * {@code orderId;customerName;tourId;tourType;tourName;peopleCount;unitPrice;total}
 * </p>
 *
 * @see PurchaseOrder
 */
public class OrderDataManager {

    private static final String DELIMITER = ";";

    private OrderDataManager() {}

    /**
     * Lee un archivo de órdenes de compra y retorna una lista de objetos
     * {@link PurchaseOrder} reconstruidos a partir del CSV. Para cada
     * línea busca el {@link TourService} correspondiente por ID dentro
     * de la lista de servicios proporcionada.
     * <p>
     * Si el archivo no existe, retorna una lista vacía sin errores.
     * Las líneas con tourId sin coincidencia en servicios se omiten.
     * </p>
     *
     * @param filePath ruta al archivo de órdenes
     * @param services lista de servicios turísticos disponibles para
     *                 resolver el tourId de cada orden
     * @return lista de objetos PurchaseOrder reconstruidos
     */
    public static List<IOrder> loadOrders(String filePath, List<Registerable> services) {
        Map<Integer, TourService> serviceMap = new HashMap<>();
        for (Registerable reg : services) {
            if (reg instanceof TourService ts) {
                serviceMap.put(ts.getId(), ts);
            }
        }

        List<IOrder> orders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length < 8) continue;

                int orderId = Integer.parseInt(parts[0].trim());
                String customerName = parts[1].trim();
                int tourId = Integer.parseInt(parts[2].trim());
                int peopleCount = Integer.parseInt(parts[5].trim());
                double total = Double.parseDouble(parts[7].trim());

                TourService tour = serviceMap.get(tourId);
                if (tour == null) continue;

                orders.add(PurchaseOrder.fromCsv(orderId, customerName, tour, peopleCount, total));
            }
        } catch (FileNotFoundException ignored) {
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al leer órdenes: " + e.getMessage());
        }
        return orders;
    }

    /**
     * Agrega una nueva orden al final del archivo CSV en modo append.
     * Si el archivo no existe, se crea automáticamente.
     *
     * @param filePath ruta al archivo de órdenes
     * @param order    orden de compra a persistir, no puede ser nula
     */
    public static void appendOrder(String filePath, IOrder order) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            bw.write(order.toCsvLine());
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            System.err.println("Error al escribir en " + filePath + ": " + e.getMessage());
        }
    }

}
