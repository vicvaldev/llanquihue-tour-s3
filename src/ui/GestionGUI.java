package ui;

import data.DataManager;
import data.OrderDataManager;
import model.*;
import util.ExceededCapacityException;
import util.FormatUtils;
import util.InvalidRutException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Interfaz gráfica principal del sistema Llanquihue Tour.
 * Proporciona botones para mostrar resumen (vía {@link Registerable#showSummary()}),
 * listar todos los registros, filtrar por precio o lengua materna del guía,
 * agregar un nuevo servicio turístico (con {@link JDialog} de formulario),
 * gestionar órdenes de compra (agregar y listar) y salir.
 * <p>
 * Los datos de servicios se cargan desde {@code resources/tours.csv} a través de
 * {@link data.DataManager}, y las órdenes de compra desde {@code resources/orders.csv}
 * mediante {@link data.OrderDataManager}.
 * </p>
 *
 * @see data.DataManager
 * @see data.OrderDataManager
 * @see model.Registerable
 */
public class GestionGUI extends JFrame {

    private static final String DATA_FILE = "resources/tours.csv";
    private static final String ORDERS_FILE = "resources/orders.csv";

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

    private JTextArea outputArea;

    /**
     * Construye la ventana principal con los botones de acción y el área
     * de texto de salida. Configura el título, tamaño y posición.
     */
    public GestionGUI() {
        setTitle("Llanquihue Tour");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSummary = new JButton("Mostrar Resumen");
        JButton btnListAll = new JButton("Listar Todos");
        JButton btnFilterPrice = new JButton("Filtrar por Precio");
        JButton btnFilterTongue = new JButton("Filtrar por Lengua Materna");
        JButton btnAdd = new JButton("Agregar Registro");
        JButton btnAddOrder = new JButton("Agregar Orden de Compra");
        JButton btnListOrders = new JButton("Listar Órdenes");
        JButton btnExit = new JButton("Salir");

        buttonPanel.add(btnSummary);
        buttonPanel.add(btnListAll);
        buttonPanel.add(btnFilterPrice);
        buttonPanel.add(btnFilterTongue);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnAddOrder);
        buttonPanel.add(btnListOrders);
        buttonPanel.add(btnExit);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnSummary.addActionListener(e -> showSummary());
        btnListAll.addActionListener(e -> listAll());
        btnFilterPrice.addActionListener(e -> filterByPrice());
        btnFilterTongue.addActionListener(e -> filterByMotherTongue());
        btnAdd.addActionListener(e -> showAddDialog());
        btnAddOrder.addActionListener(e -> showAddOrderDialog());
        btnListOrders.addActionListener(e -> listOrders());
        btnExit.addActionListener(e -> System.exit(0));
    }

    /**
     * Carga la lista de entidades registrables desde el archivo CSV.
     * Si la lista está vacía, muestra un mensaje en el área de texto.
     *
     * @return lista de entidades cargadas (nunca {@code null})
     */
    private List<Registerable> loadServices() {
        List<Registerable> services = DataManager.loadServices(DATA_FILE);
        if (services.isEmpty()) {
            outputArea.setText("No hay servicios registrados.\n");
        }
        return services;
    }

    /**
     * Invoca {@link Registerable#showSummary()} en cada entidad cargada,
     * demostrando el uso polimórfico de la interfaz {@link Registerable}.
     * Captura la salida de consola y la muestra en el área de texto.
     */
    private void showSummary() {
        List<Registerable> services = loadServices();
        if (services.isEmpty()) return;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(baos));

        for (Registerable reg : services) {
            reg.showSummary();
        }

        System.setOut(original);
        outputArea.setText(baos.toString());
    }

    /**
     * Muestra todos los registros cargados separados por {@code ---}.
     * Usa {@code instanceof TourService} para acceder a la representación
     * específica de cada servicio.
     */
    private void listAll() {
        List<Registerable> services = loadServices();
        if (services.isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < services.size(); i++) {
            Registerable reg = services.get(i);
            if (reg instanceof TourService) {
                sb.append(((TourService) reg).toString());
            } else {
                sb.append(reg.getClass().getSimpleName()).append(": ").append(reg);
            }
            if (i < services.size() - 1) {
                sb.append("\n---\n");
            }
        }
        outputArea.setText(sb.toString());
    }

    /**
     * Solicita un precio máximo mediante {@link JOptionPane} y filtra
     * las entradas usando {@link DataManager#filterByPrice}. Muestra
     * los resultados en el área de texto.
     */
    private void filterByPrice() {
        List<Registerable> services = loadServices();
        if (services.isEmpty()) return;

        String input = JOptionPane.showInputDialog(this,
                "Ingrese el precio máximo:", "Filtrar por Precio",
                JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;

        try {
            double maxPrice = Double.parseDouble(input.trim());
            List<Registerable> result = DataManager.filterByPrice(services, maxPrice);
            if (result.isEmpty()) {
                outputArea.setText("No se encontraron servicios con precio <= $"
                        + String.format("%.0f", maxPrice) + "\n");
            } else {
                StringBuilder sb = new StringBuilder("=== Servicios con precio <= $"
                        + String.format("%.0f", maxPrice) + " ===\n");
                for (Registerable reg : result) {
                    if (reg instanceof TourService) {
                        sb.append(((TourService) reg).toString()).append("\n");
                    }
                }
                sb.append("(").append(result.size()).append(" servicios encontrados)\n");
                outputArea.setText(sb.toString());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un número válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Solicita un código ISO de idioma mediante {@link JOptionPane} y
     * filtra las entradas usando {@link DataManager#filterByMotherTongue}.
     * Muestra los resultados en el área de texto.
     */
    private void filterByMotherTongue() {
        List<Registerable> services = loadServices();
        if (services.isEmpty()) return;

        String input = JOptionPane.showInputDialog(this,
                "Ingrese código ISO de la lengua materna (ej: es, en, fr):",
                "Filtrar por Lengua Materna", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;

        String code = input.trim().toLowerCase();
        if (!ISO_LANGUAGES.containsKey(code)) {
            JOptionPane.showMessageDialog(this,
                    "Código ISO inválido. Use uno de: " + String.join(", ", ISO_LANGUAGES.keySet()),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Registerable> result = DataManager.filterByMotherTongue(services, code);
        if (result.isEmpty()) {
            outputArea.setText("No se encontraron servicios con guía de lengua materna '"
                    + ISO_LANGUAGES.get(code) + "'\n");
        } else {
            StringBuilder sb = new StringBuilder("=== Servicios con guía de lengua materna '"
                    + ISO_LANGUAGES.get(code) + "' ===\n");
            for (Registerable reg : result) {
                if (reg instanceof TourService) {
                    sb.append(((TourService) reg).toString()).append("\n");
                }
            }
            sb.append("(").append(result.size()).append(" servicios encontrados)\n");
            outputArea.setText(sb.toString());
        }
    }

    /**
     * Abre un diálogo modal para agregar un nuevo servicio turístico.
     * Si el servicio se guarda correctamente, actualiza el área de texto
     * con un mensaje de confirmación.
     */
    private void showAddDialog() {
        AddServiceDialog dialog = new AddServiceDialog(this);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            outputArea.setText("Servicio agregado exitosamente.\n");
        }
    }

    /**
     * Abre un diálogo modal para crear una nueva orden de compra.
     * Muestra los servicios disponibles en un combo, la capacidad
     * máxima del tour seleccionado y permite ingresar el nombre del
     * cliente y la cantidad de personas. Si la orden se guarda
     * correctamente, actualiza el área de texto con una confirmación.
     */
    private void showAddOrderDialog() {
        List<Registerable> services = loadServices();
        if (services.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay servicios disponibles para ordenar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        AddOrderDialog dialog = new AddOrderDialog(this, services);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            outputArea.setText("Orden de compra guardada exitosamente.\n");
        }
    }

    /**
     * Carga las órdenes de compra desde el archivo CSV mediante
     * {@link OrderDataManager#loadOrderLines} y las muestra en el área
     * de texto. Si no hay órdenes registradas, lo indica en pantalla.
     */
    private void listOrders() {
        List<String> orderLines = OrderDataManager.loadOrderLines(ORDERS_FILE);
        if (orderLines.isEmpty()) {
            outputArea.setText("No hay órdenes de compra registradas.\n");
            return;
        }
        StringBuilder sb = new StringBuilder("=== Órdenes de Compra ===\n");
        for (String line : orderLines) {
            sb.append(line).append("\n");
        }
        outputArea.setText(sb.toString());
    }

    /**
     * Diálogo modal para el ingreso de un nuevo servicio turístico.
     * Contiene todos los campos necesarios: tipo de servicio, datos
     * comunes, campo específico según el tipo, y datos completos del
     * guía turístico incluyendo dirección validación de RUT.
     */
    private static class AddServiceDialog extends JDialog {
        private boolean saved = false;
        private JComboBox<String> typeCombo;
        private JTextField nameField;
        private JTextField durationField;
        private JTextField specificField;
        private JTextField priceField;
        private JTextField maxCapacityField;
        private JTextField rutField;
        private JTextField firstNameField;
        private JTextField lastNameField;
        private JTextField streetField;
        private JTextField numberField;
        private JTextField cityField;
        private JTextField regionField;
        private JTextField positionField;
        private JTextField salaryField;
        private JComboBox<String> motherTongueCombo;
        private JComboBox<String> secondLanguageCombo;
        private JLabel specificLabel;

        /**
         * Construye el diálogo de ingreso.
         *
         * @param parent ventana padre sobre la cual se centrará el diálogo
         */
        AddServiceDialog(JFrame parent) {
            super(parent, "Agregar Nuevo Servicio", true);
            setSize(480, 660);
            setLocationRelativeTo(parent);
            initComponents();
        }

        private void initComponents() {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(3, 8, 3, 8);
            int row = 0;

            typeCombo = new JComboBox<>(new String[]{
                    "GastronomicRoute", "LakeCruise", "CulturalExcursion"});
            typeCombo.addActionListener(e -> updateSpecificField());
            addRow(panel, gbc, row++, "Tipo de servicio:", typeCombo);

            nameField = new JTextField();
            addRow(panel, gbc, row++, "Nombre del servicio:", nameField);

            durationField = new JTextField();
            addRow(panel, gbc, row++, "Duración (horas):", durationField);

            specificLabel = new JLabel("Número de paradas:");
            specificField = new JTextField();
            addRow(panel, gbc, row++, specificLabel, specificField);

            priceField = new JTextField();
            addRow(panel, gbc, row++, "Precio:", priceField);

            maxCapacityField = new JTextField();
            addRow(panel, gbc, row++, "Capacidad máxima:", maxCapacityField);

            addSeparator(panel, gbc, row++, "Datos del Guía");

            rutField = new JTextField();
            addRow(panel, gbc, row++, "RUT (ej: 12345678-9):", rutField);

            firstNameField = new JTextField();
            addRow(panel, gbc, row++, "Nombre del guía:", firstNameField);

            lastNameField = new JTextField();
            addRow(panel, gbc, row++, "Apellido:", lastNameField);

            streetField = new JTextField();
            addRow(panel, gbc, row++, "Calle:", streetField);

            numberField = new JTextField();
            addRow(panel, gbc, row++, "Número (opcional):", numberField);

            cityField = new JTextField();
            addRow(panel, gbc, row++, "Ciudad (opcional):", cityField);

            regionField = new JTextField();
            addRow(panel, gbc, row++, "Región (opcional):", regionField);

            positionField = new JTextField();
            addRow(panel, gbc, row++, "Cargo (opcional):", positionField);

            salaryField = new JTextField();
            addRow(panel, gbc, row++, "Sueldo base:", salaryField);

            motherTongueCombo = new JComboBox<>();
            for (Map.Entry<String, String> e : ISO_LANGUAGES.entrySet()) {
                motherTongueCombo.addItem(e.getKey() + " - " + e.getValue());
            }
            addRow(panel, gbc, row++, "Lengua materna:", motherTongueCombo);

            secondLanguageCombo = new JComboBox<>();
            secondLanguageCombo.addItem("");
            for (Map.Entry<String, String> e : ISO_LANGUAGES.entrySet()) {
                secondLanguageCombo.addItem(e.getKey() + " - " + e.getValue());
            }
            addRow(panel, gbc, row++, "Segunda lengua (opcional):", secondLanguageCombo);

            JPanel btnPanel = new JPanel();
            JButton saveBtn = new JButton("Guardar");
            JButton cancelBtn = new JButton("Cancelar");
            btnPanel.add(saveBtn);
            btnPanel.add(cancelBtn);

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(btnPanel, gbc);

            saveBtn.addActionListener(e -> save());
            cancelBtn.addActionListener(e -> dispose());

            JScrollPane scrollPane = new JScrollPane(panel);
            add(scrollPane);
        }

        private void addRow(JPanel panel, GridBagConstraints gbc, int row,
                            Object label, JComponent field) {
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            if (label instanceof String) {
                panel.add(new JLabel((String) label), gbc);
            } else {
                panel.add((JLabel) label, gbc);
            }

            gbc.gridx = 1;
            gbc.weightx = 1;
            panel.add(field, gbc);
        }

        private void addSeparator(JPanel panel, GridBagConstraints gbc, int row, String text) {
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 1;
            JLabel sep = new JLabel("— " + text + " —");
            sep.setFont(sep.getFont().deriveFont(Font.BOLD));
            panel.add(sep, gbc);
        }

        private void updateSpecificField() {
            String type = (String) typeCombo.getSelectedItem();
            switch (type) {
                case "GastronomicRoute" -> specificLabel.setText("Número de paradas:");
                case "LakeCruise" -> specificLabel.setText("Tipo de embarcación:");
                case "CulturalExcursion" -> specificLabel.setText("Lugar histórico:");
            }
        }

        /**
         * Valida los campos ingresados, construye el objeto {@link TourService}
         * correspondiente y lo persiste al archivo CSV mediante
         * {@link DataManager#appendService}. En caso de error muestra un
         * mensaje descriptivo al usuario.
         */
        private void save() {
            try {
                String type = (String) typeCombo.getSelectedItem();
                String name = nameField.getText().trim();
                double duration = Double.parseDouble(durationField.getText().trim());
                String specific = specificField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int maxCapacity = Integer.parseInt(maxCapacityField.getText().trim());
                if (maxCapacity <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "La capacidad máxima debe ser positiva.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String rut = rutField.getText().trim();
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String street = streetField.getText().trim();
                String number = numberField.getText().trim();
                String city = cityField.getText().trim();
                String region = regionField.getText().trim();
                String position = positionField.getText().trim();
                double salary = Double.parseDouble(salaryField.getText().trim());

                String mtItem = (String) motherTongueCombo.getSelectedItem();
                String motherTongue = mtItem != null ? mtItem.substring(0, 2) : "";

                String slItem = (String) secondLanguageCombo.getSelectedItem();
                String secondLanguage = (slItem != null && slItem.length() >= 2) ? slItem.substring(0, 2) : "";

                if (name.isEmpty() || firstName.isEmpty() || lastName.isEmpty()
                        || street.isEmpty() || rut.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Complete todos los campos obligatorios.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Address address = new Address(street, number, city, region);
                TouristGuide guide = new TouristGuide(rut, firstName, lastName,
                        address, position, salary, motherTongue, secondLanguage);
                int id = DataManager.getNextId(DATA_FILE);

                TourService service;
                switch (type) {
                    case "GastronomicRoute" -> {
                        int stops = Integer.parseInt(specific);
                        service = new GastronomicRoute(id, name, duration, stops, price, guide, maxCapacity);
                    }
                    case "LakeCruise" -> {
                        service = new LakeCruise(id, name, duration, specific, price, guide, maxCapacity);
                    }
                    case "CulturalExcursion" -> {
                        service = new CulturalExcursion(id, name, duration, specific, price, guide, maxCapacity);
                    }
                    default -> throw new IllegalArgumentException("Tipo no válido");
                }

                DataManager.appendService(DATA_FILE, service);
                saved = true;
                dispose();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Error de formato numérico. Verifique los campos numéricos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidRutException e) {
                JOptionPane.showMessageDialog(this,
                        "RUT inválido: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this,
                        "Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        /**
         * Indica si el servicio fue guardado exitosamente durante la
         * interacción con este diálogo.
         *
         * @return {@code true} si se persistió el servicio, {@code false}
         *         en caso contrario
         */
        boolean isSaved() {
            return saved;
        }
    }

    /**
     * Diálogo modal para la creación de una orden de compra. Presenta
     * un combo con los servicios turísticos disponibles, muestra la
     * capacidad máxima del tour seleccionado y solicita el nombre del
     * cliente y la cantidad de personas.
     * <p>
     * Valida que la cantidad de personas no supere la capacidad máxima
     * del tour usando {@link PurchaseOrder#PurchaseOrder} y captura
     * {@link ExceededCapacityException} para mostrar el error al
     * usuario.
     * </p>
     */
    private static class AddOrderDialog extends JDialog {
        private boolean saved = false;
        private JComboBox<String> serviceCombo;
        private JTextField customerField;
        private JTextField peopleField;
        private JLabel capacityLabel;
        private List<TourService> availableServices;

        /**
         * Construye el diálogo de orden de compra filtrando solo los
         * servicios {@link TourService} de la lista general de entidades.
         *
         * @param parent   ventana padre sobre la cual se centrará el diálogo
         * @param services lista de entidades registrables de las que se
         *                 extraerán los servicios turísticos disponibles
         */
        AddOrderDialog(JFrame parent, List<Registerable> services) {
            super(parent, "Agregar Orden de Compra", true);
            availableServices = new ArrayList<>();
            for (Registerable reg : services) {
                if (reg instanceof TourService) {
                    availableServices.add((TourService) reg);
                }
            }
            setSize(450, 280);
            setLocationRelativeTo(parent);
            initComponents();
        }

        private void initComponents() {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(3, 8, 3, 8);
            int row = 0;

            String[] items = new String[availableServices.size()];
            for (int i = 0; i < availableServices.size(); i++) {
                TourService s = availableServices.get(i);
                items[i] = s.getId() + " - " + s.getName() + " ($" + FormatUtils.formatPrice(s.getPrice()) + ")";
            }
            serviceCombo = new JComboBox<>(items);
            serviceCombo.addActionListener(e -> updateCapacity());
            addRow(panel, gbc, row++, "Servicio:", serviceCombo);

            capacityLabel = new JLabel();
            addRow(panel, gbc, row++, "Capacidad:", capacityLabel);
            updateCapacity();

            customerField = new JTextField();
            addRow(panel, gbc, row++, "Nombre del cliente:", customerField);

            peopleField = new JTextField();
            addRow(panel, gbc, row++, "Cantidad de personas:", peopleField);

            JPanel btnPanel = new JPanel();
            JButton saveBtn = new JButton("Guardar");
            JButton cancelBtn = new JButton("Cancelar");
            btnPanel.add(saveBtn);
            btnPanel.add(cancelBtn);

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(btnPanel, gbc);

            saveBtn.addActionListener(e -> save());
            cancelBtn.addActionListener(e -> dispose());

            add(panel);
        }

        private void addRow(JPanel panel, GridBagConstraints gbc, int row,
                            String label, JComponent field) {
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(new JLabel(label), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1;
            panel.add(field, gbc);
        }

        /**
         * Actualiza la etiqueta de capacidad máxima con el valor del
         * servicio seleccionado en el combo.
         */
        private void updateCapacity() {
            int idx = serviceCombo.getSelectedIndex();
            if (idx >= 0 && idx < availableServices.size()) {
                TourService s = availableServices.get(idx);
                capacityLabel.setText(String.valueOf(s.getMaxCapacity()));
            }
        }

        /**
         * Valida los campos ingresados, construye el objeto
         * {@link PurchaseOrder} correspondiente y lo persiste al archivo
         * CSV mediante {@link OrderDataManager#appendOrder}. Captura
         * {@link ExceededCapacityException} y errores de formato mostrando
         * mensajes descriptivos al usuario.
         */
        private void save() {
            try {
                int idx = serviceCombo.getSelectedIndex();
                if (idx < 0 || idx >= availableServices.size()) {
                    JOptionPane.showMessageDialog(this,
                            "Seleccione un servicio válido.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                TourService tour = availableServices.get(idx);
                String customerName = customerField.getText().trim();
                if (customerName.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "El nombre del cliente no puede estar vacío.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int peopleCount = Integer.parseInt(peopleField.getText().trim());

                PurchaseOrder order = new PurchaseOrder(customerName, tour, peopleCount);
                OrderDataManager.appendOrder(GestionGUI.ORDERS_FILE, order);
                saved = true;
                dispose();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "La cantidad de personas debe ser un número válido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ExceededCapacityException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "Error de Capacidad", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this,
                        "Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        /**
         * Indica si la orden fue guardada exitosamente durante la
         * interacción con este diálogo.
         *
         * @return {@code true} si se persistió la orden, {@code false}
         *         en caso contrario
         */
        boolean isSaved() {
            return saved;
        }
    }
}
