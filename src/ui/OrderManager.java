package ui;

import data.OrderDataManager;
import model.*;
import util.ExceededCapacityException;
import util.FormatUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private static final String ORDERS_FILE = "resources/orders.csv";

    private final JFrame parent;
    private final JTextArea outputArea;

    public OrderManager(JFrame parent, JTextArea outputArea) {
        this.parent = parent;
        this.outputArea = outputArea;
    }

    public void listOrders(List<Registerable> services) {
        List<IOrder> orders = OrderDataManager.loadOrders(ORDERS_FILE, services);
        if (orders.isEmpty()) {
            outputArea.setText("No hay órdenes de compra registradas.\n");
            return;
        }
        StringBuilder sb = new StringBuilder("=== Órdenes de Compra ===\n");
        for (IOrder order : orders) {
            sb.append(order).append("\n");
        }
        outputArea.setText(sb.toString());
    }

    public void showAddOrderDialog(List<Registerable> services) {
        if (services.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay servicios disponibles para ordenar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        AddOrderDialog dialog = new AddOrderDialog(parent, services);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            outputArea.setText("Orden de compra guardada exitosamente.\n");
        }
    }

    private static class AddOrderDialog extends JDialog {
        private boolean saved = false;
        private JComboBox<String> serviceCombo;
        private JTextField customerField;
        private JTextField peopleField;
        private JLabel capacityLabel;
        private List<TourService> availableServices;

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

        private void updateCapacity() {
            int idx = serviceCombo.getSelectedIndex();
            if (idx >= 0 && idx < availableServices.size()) {
                TourService s = availableServices.get(idx);
                capacityLabel.setText(String.valueOf(s.getMaxCapacity()));
            }
        }

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
                OrderDataManager.appendOrder(ORDERS_FILE, order);
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

        boolean isSaved() {
            return saved;
        }
    }
}
