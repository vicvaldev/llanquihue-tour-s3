package ui;

import javax.swing.*;
import java.awt.*;

public class GestionGUI extends JFrame {

    private final TourServiceManager tourServiceManager;
    private final OrderManager orderManager;
    private final JTextArea outputArea;

    public GestionGUI() {
        setTitle("Llanquihue Tour");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        tourServiceManager = new TourServiceManager(this, outputArea);
        orderManager = new OrderManager(this, outputArea);

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

        JScrollPane scrollPane = new JScrollPane(outputArea);

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnSummary.addActionListener(e -> tourServiceManager.showSummary());
        btnListAll.addActionListener(e -> tourServiceManager.listAll());
        btnFilterPrice.addActionListener(e -> tourServiceManager.filterByPrice());
        btnFilterTongue.addActionListener(e -> tourServiceManager.filterByMotherTongue());
        btnAdd.addActionListener(e -> tourServiceManager.showAddDialog());
        btnAddOrder.addActionListener(e -> orderManager.showAddOrderDialog(tourServiceManager.loadServices()));
        btnListOrders.addActionListener(e -> orderManager.listOrders(tourServiceManager.loadServices()));
        btnExit.addActionListener(e -> System.exit(0));
    }
}
