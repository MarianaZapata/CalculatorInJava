 package proyectoprimerparcial_zapata.verduzco;

/**
 * @author Mariana Zapata & Mauricio Verduzco
 */

public class StackA <T> implements StackADT<T>{
    private T[] stack;
    private int top;
    private final int MAX=100;
    
    
    public StackA(){
        stack=(T[]) new Object[MAX];
        top=0;
    }
    
    public StackA(int initialCapacity){
        stack=(T[]) new Object[initialCapacity];
        top=0;
    }

    public void push(T dato){
        if(top==stack.length)//es length no sabes si ya habia pasado por ahi
            expandCapacity();        
        stack[top]=dato;
        top++;
    }
    
    public T pop(){
        T result;
        if(isEmpty())
            throw new EmptyCollectionException("Pila vacia");//esto aborta el programa al iterrumpir un error la excepcion te saca
        else{
            top--;
            result=stack[top];
            stack[top]=null;   
            return result;
        }  
    }   
    
    public T peek(){
        if(!isEmpty())
            return stack[top-1];//pasa el control de la maquina por lo que no se necesita el y porque ya se sale dado a que es un solo if
        else
            throw new EmptyCollectionException("Pila vacia");
    }       
    
    public boolean isEmpty(){
        return top==0;
    }

    public void expandCapacity(){//or maybe private
        T[] nuevo=(T[]) new Object[stack.length*2];
        for(int i=0;i<stack.length;i++)
            nuevo[i]=stack[i];
        stack=nuevo;
    }
}
