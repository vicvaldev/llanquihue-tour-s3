package ui;

import data.DataManager;
import model.*;
import util.InvalidRutException;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.List;

public class TourServiceManager {

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

    private final JFrame parent;
    private final JTextArea outputArea;

    public TourServiceManager(JFrame parent, JTextArea outputArea) {
        this.parent = parent;
        this.outputArea = outputArea;
    }

    public List<Registerable> loadServices() {
        List<Registerable> services = DataManager.loadServices(DATA_FILE);
        if (services.isEmpty()) {
            outputArea.setText("No hay servicios registrados.\n");
        }
        return services;
    }

    public void showSummary() {
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

    public void listAll() {
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

    public void filterByPrice() {
        List<Registerable> services = loadServices();
        if (services.isEmpty()) return;

        String input = JOptionPane.showInputDialog(parent,
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
            JOptionPane.showMessageDialog(parent, "Debe ingresar un número válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void filterByMotherTongue() {
        List<Registerable> services = loadServices();
        if (services.isEmpty()) return;

        String input = JOptionPane.showInputDialog(parent,
                "Ingrese código ISO de la lengua materna (ej: es, en, fr):",
                "Filtrar por Lengua Materna", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;

        String code = input.trim().toLowerCase();
        if (!ISO_LANGUAGES.containsKey(code)) {
            JOptionPane.showMessageDialog(parent,
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

    public void showAddDialog() {
        AddServiceDialog dialog = new AddServiceDialog(parent);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            outputArea.setText("Servicio agregado exitosamente.\n");
        }
    }

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

        boolean isSaved() {
            return saved;
        }
    }
}
