package ui;

import data.DataManager;
import model.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Tours> tours = DataManager.loadTours("resources/tours.txt");

        System.out.println("=== Todos los tours (" + tours.size() + ") ===");
        tours.forEach(System.out::println);

        System.out.println("\n=== Tours con precio <= $30.000 ===");
        List<Tours> baratos = DataManager.filterByPrice(tours, 30000);
        baratos.forEach(System.out::println);
        System.out.println("(" + baratos.size() + " tours encontrados)");

        System.out.println("\n=== Tours con guía de lengua materna 'Inglés' ===");
        List<Tours> guiasIngles = DataManager.filterByMotherTongue(tours, "Inglés");
        guiasIngles.forEach(System.out::println);
        System.out.println("(" + guiasIngles.size() + " tours encontrados)");

        System.out.println("\n=== Tours con guía de lengua materna 'Español' ===");
        List<Tours> guiasEspanol = DataManager.filterByMotherTongue(tours, "Español");
        guiasEspanol.forEach(System.out::println);
        System.out.println("(" + guiasEspanol.size() + " tours encontrados)");
    }
}
