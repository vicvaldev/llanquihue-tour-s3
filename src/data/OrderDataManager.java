package data;

import model.PurchaseOrder;
import util.FormatUtils;

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
     * Lee un archivo de órdenes de compra y retorna una lista con las
     * líneas formateadas para mostrar en la interfaz gráfica.
     * Cada línea incluye: número de orden, nombre del cliente, tour,
     * cantidad de personas, precio unitario y total.
     * <p>
     * Si el archivo no existe, retorna una lista vacía sin errores.
     * </p>
     *
     * @param filePath ruta al archivo de órdenes
     * @return lista de cadenas formateadas para mostrar en GUI
     */
    public static List<String> loadOrderLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length < 8) {
                    continue;
                }
                double unitPrice = Double.parseDouble(parts[6]);
                double total = Double.parseDouble(parts[7]);
                String orderLine = String.format("Orden #%s | Cliente: %s | Tour: %s (%s) | Personas: %s | Precio Unit.: $%s | Total: $%s",
                        parts[0], parts[1], parts[4], parts[3], parts[5],
                        FormatUtils.formatPrice(unitPrice), FormatUtils.formatPrice(total));
                lines.add(orderLine);
            }
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            lines.add("Error al leer órdenes: " + e.getMessage());
        }
        return lines;
    }

    /**
     * Agrega una nueva orden al final del archivo CSV en modo append.
     * Si el archivo no existe, se crea automáticamente.
     *
     * @param filePath ruta al archivo de órdenes
     * @param order    orden de compra a persistir, no puede ser nula
     */
    public static void appendOrder(String filePath, PurchaseOrder order) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            bw.write(order.toCsvLine());
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            System.err.println("Error al escribir en " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Sobrescribe completamente el archivo CSV con todas las órdenes
     * de la lista proporcionada.
     *
     * @param filePath ruta al archivo de órdenes
     * @param orders   lista de órdenes a persistir, no puede ser nula
     */
    public static void saveOrders(String filePath, List<PurchaseOrder> orders) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            for (PurchaseOrder order : orders) {
                bw.write(order.toCsvLine());
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            System.err.println("Error al escribir en " + filePath + ": " + e.getMessage());
        }
    }
}
