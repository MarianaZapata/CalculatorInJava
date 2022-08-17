package proyectoprimerparcial_zapata.verduzco;

/**
 * @author Mariana Zapata & Mauricio Verduzco
 */

public interface StackADT <T>{
    public void push(T dato);
    public T pop();
    public T peek();
    public boolean isEmpty();
}
