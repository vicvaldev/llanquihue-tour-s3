package model;

/**
 * Clase base del dominio que representa un producto turístico genérico.
 * Contiene los atributos básicos de identificación, nombre y precio,
 * y sirve como superclase para {@link Tours}.
 */
public class Products {
    private int id;
    private String productName;
    private double price;

    /**
     * Constructor con todos los atributos.
     *
     * @param id           identificador único del producto
     * @param productName  nombre del producto
     * @param price        precio del producto
     */
    public Products(int id, String productName, double price) {
        this.id = id;
        this.productName = productName;
        this.price = price;
    }

    /**
     * Constructor por defecto.
     */
    public Products() {}

    /**
     * @return identificador único del producto
     */
    public int getId() {
        return id;
    }

    /**
     * @param id identificador único del producto
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return nombre del producto
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName nombre del producto
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return precio del producto
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price precio del producto
     */
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{ \"id\": " + id + ", \"producto\": \"" + productName + "\", \"precio\": " + price + " }";
    }
}
