package proyectoprimerparcial_zapata.verduzco;

/**
 * @author Mariana Zapata & Mauricio Verduzco
 */

public class EmptyCollectionException extends RuntimeException {
    public EmptyCollectionException() {
        super("Coleccion vacia");
    }
    
    public EmptyCollectionException(String mensaje) {
        super(mensaje);
    }
}