package model;

/**
 * Interfaz que define el contrato común para todas las entidades
 * gestionables del sistema. Las clases que implementan esta interfaz
 * deben proporcionar una implementación de {@code showSummary()} que
 * muestre un resumen descriptivo de la entidad.
 */
public interface Registerable {
    /**
     * Muestra un resumen descriptivo de la entidad por consola.
     * Cada clase implementa este método con el formato y los datos
     * específicos de su tipo de entidad.
     */
    void showSummary();
}
