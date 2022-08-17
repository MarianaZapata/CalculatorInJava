package proyectoprimerparcial_zapata.verduzco;

import java.text.DecimalFormat;

/**
 * @author Mariana Zapata & Mauricio Verduzco
 */

public class Funciones{
/*
    decidimos dividir el funcionamiento de la calculadora en cuatro tipo de funciones:
    1) la función principal (es la única pública)
    2) las funciones primarias (que se corren en la principal)
    3) las funciones secundarias (que dan el funcionamiento a las primarias en cosas específicas)
    4) las funciones generales (que se utilizan en más de una ocasión)
    

*/    
      

    //                          FUNCIÓN PRINCIPAL
    
    public static String evaluacionFinal (String entrada){
        DecimalFormat df = new DecimalFormat("#.0000"); // libreria para redondear a fix 4
        StackADT <String> infixStack;                   // Pila en notación infija
        StackADT <String> postfixStack;                 // Pila en notación postfija
        double aux;
        String salida;
        
        entrada = "("+entrada+")";                    
        
        if(primeraPrueba(entrada)){                     //evaluamos los signos y los paréntesis
            infixStack = String2Stack(entrada);         //convertimos la cadena en una pila en notación infija
            
            postfixStack = infix2Postfix(infixStack);   //pasamos de notación infija a postfija

            aux = evaluaPostfija(postfixStack);         //evaluamos la expresión
            salida = ("" + aux);                        // convertimos a String y aplicamos redondeo
            if(salida.equals("NaN"))                    // Si dividimos 0/0 o hacemos la raiz de un negativo
                salida="ALGEBRAIC ERROR"; 
            else
                salida = df.format(aux); 
            
        }else
            salida = "SYNTAX ERROR";

        return salida;  
    }

    
    //                          FUNCIONES PRIMARIAS
    
    //La función delata los errores causados por signos y paréntesis en primera instancia.
    private static boolean primeraPrueba(String entrada){
        boolean flag = false;
        
        if (revisaParentesis(entrada) && revisaSignos(entrada))
            flag = true;
        
        return flag;
    }
    
    // Convierte un String a un arrego tipo String:
    // NOTA: Con ayuda de la depuración.
    private static StackADT<String> String2Stack(String entrada){
        StackADT < String > salida = new StackA <> ();
        
        String [] aux = Funciones.depurar(entrada).split(" ");
        // depurar va a poner espacios entre operadores y números
        // split va a vaciar cada sección (número o signo) en un lugar en un arreglo 
        for (int i = aux.length - 1; i >= 0; i--)               
             salida.push(aux[i]); //vaciar el arreglo en una pila
        
        return salida;  
    }
    
    // Algoritmo que convierte de notación infija a notación postfija.
    // NOTA: Usamos "prioridad" para el orden de los operadores.
    private static StackADT<String> infix2Postfix (StackADT <String> entrada){
        StackADT < String > aux = new StackA <> ();     //Pila auxiliar
        StackADT < String > salida = new StackA <> ();  //Pila salida
        
        //Algoritmo Infijo a Postfijo --> IMPORTANTE
        while (!entrada.isEmpty()) {
            switch (prioridad(entrada.peek())){ //veamos que prioridad tiene el siguiente objeto
                case 1: //parentesis que abre
                    aux.push(entrada.pop());
                    break; 
                
                case 2: //paréntesis que cierra
                    while(!aux.peek().equals("(")) {
                      salida.push(aux.pop());
                    }
                    aux.pop();
                    entrada.pop();
                    break;
                
                case 3: //suma y resta
                    while(prioridad(aux.peek()) >= prioridad(entrada.peek())) {
                      salida.push(aux.pop());
                    }
                    aux.push(entrada.pop());
                    break;
                
                case 4: //multiplicación y división
                    while(prioridad(aux.peek()) >= prioridad(entrada.peek())) {
                      salida.push(aux.pop());
                    }
                    aux.push(entrada.pop());
                    break; 
              
                
                case 5: //exponentes
                    while(prioridad(aux.peek()) >= prioridad(entrada.peek())) {
                      salida.push(aux.pop());
                    }
                    aux.push(entrada.pop());
                    break;
                
              default: //números
                    salida.push(entrada.pop()); 
            } 
          }
        
        return salida;
    }
    
    // Evaluar una operación en notación postfija.
    // NOTA: Para cuando lleguemos aquí no puede haber error excepto uno algebráico.
    private static double evaluaPostfija(StackADT<String> postfija){
        
        StackADT<Double> pila = new StackA();
        int lon;
        double op1, op2;
        String ev;
        lon=stackSize(postfija);
        postfija = inviertePila(postfija);
        // Tenemos que invertir la pila, para que los operadores salgan en el orden deseado
        
        
        for(int i=0;i<lon;i++){
            ev=postfija.pop();
            if(esDouble(ev)){
                pila.push(Double.parseDouble(ev));
            }
            else{
                op1=pila.pop();
                op2=pila.pop();
                
                switch(ev){
                    case "+":
                        pila.push(op2+op1);
                        break;
                    case "-":
                        pila.push(op2-op1);
                        break;
                    case "*":
                        pila.push(op2*op1);
                        break;
                    case "/":
                        pila.push(op2/op1);
                        break;
                    case "^":
                        pila.push(Math.pow(op2, op1));
                        
                        break;   
                }
            }
        }

        return pila.pop();
    }
    
    
    //                          FUNCIONES SECUNDARIAS
    // con secundarias, nos referimos que son auxiliares de las primarias
    
    //De primeraPrueba...
    // 1.- Evalua la veracidad de los paréntesis
    private static boolean revisaParentesis(String exp){
        StackADT<Character> pila = new StackA();
        int i=0;
        boolean flag = true;
        char var;
        
        while(i<exp.length() && flag){
            var=exp.charAt(i);
            if(var == ')' && pila.isEmpty()){
                flag = false;
            }else{
                if( var == '('){
                    pila.push(var);
                }else{
                    if(var == ')' && !pila.isEmpty()){
                        pila.pop();  
                    }
                }
            }
            i++;
        }
        // cada que encontremos un "(" lo agregaremos a la pila,
        // y cada que encontremos un ")" sacaremos algo de la pila...
        // al final, si los parentesis están balanceados, la pila deberá estar vacía
        return pila.isEmpty() && flag;
    }
    
    // 2.- Evalua la veradicad de los signos
    private static boolean revisaSignos(String exp){
        String simbols = "+-*/^";
        String par = "(";
        String simbols2 = "+*/^";
        int i=0;
        int j=0;
        boolean flag = false, flag2=true;
        if(!simbols.contains(""+exp.charAt(0)) && !simbols.contains(""+exp.charAt(exp.length()-1))){
            while(i<exp.length() && j!=2 && flag2){
                if(simbols.contains(""+exp.charAt(i)))
                    j++;
                else
                    j=0;
                if(par.contains(""+exp.charAt(i)) && simbols2.contains(""+exp.charAt(i+1)))
                    flag2=false;
            i++;
            }
            if (i==exp.length() && j!=2 && flag2)
                flag = true;
        }
        // si el primer o el ultimo caracter es un operando, eso es un error
        // si j llegara a valer 2, significa que hubieron dos operadores seguidos y eso es un error
        return flag;
    }
    

    // De String2Stack...
    // 1.- Eliminar espacios, paréntesis y dejarla como la necesitamos.
    private static String depurar (String entrada){
        String str = "";
        entrada = "(" + entrada + ")";                      //paréntesis en los extremos de la expresión
        String simbols = "+-*/()^";

        //Deja espacios entre operadores
        for (int i = 0; i < entrada.length(); i++) {
            if (simbols.contains("" + entrada.charAt(i))) {     //si el char es un simbolo...
                if(entrada.charAt(i)=='(' && entrada.charAt(i+1)=='-' ){ //si hay un "(-"...
                    str += " " + entrada.charAt(i) + " " + entrada.charAt(i+1) + entrada.charAt(i+2);
                    i = i+2; //Eso significa que el "-" no es una resta sino un negativo y no hay que ponerles espacios entre ellos
                }else if( i<entrada.length()-2 && entrada.charAt(i)==')' && entrada.charAt(i+1) == '('){ //si hay un ")("
                    str += " " + entrada.charAt(i) + " *"; //eso significa multiplicación y hay que agregar el "*"
                }else    
                    str += " " + entrada.charAt(i) + " ";       // poner una espacio antes y depués del él
            }else 
                str += entrada.charAt(i);                       // concatenarlo
        }
        str = str.replaceAll("\\s+", " ").trim();               // elimina espacios de más
        
        return str;
    }
    
    
    // De infix2Postfix...
    // 1.-  preferencias entre operadores
    private static int prioridad(String simbol) {
        int preferencia;
        
        
        switch(simbol){
            case ("^"):
                preferencia = 5;
                break;
            case ("*"):
                preferencia = 4;
                break;
            case ("/"):
                preferencia = 4;
                break;
            case ("+"):
                preferencia = 3;
                break;
            case ("-"):
                preferencia = 3;
                break;
            case (")"):
                preferencia = 2;
                break;
            case ("("):
                preferencia = 1;
                break;    
            default:
                preferencia = 10;
                break;    
        }
    
        return preferencia;
    }
    
    
    // De evaluaPostfija...
    // Intentamos convertir a Double con Number Format Exception
    private static boolean esDouble(String value) {
        boolean convierte;
        // Si el número puede ser double, true, caso contrario, atrapamos la exception, es false
        try {
            Double.parseDouble(value);
            convierte = true;
        } catch (NumberFormatException e) {
            convierte = false;
        }
        return convierte;
    }
    
    
    // En general...
    // Debido a que nuestra StackADT no tiene definida una función "Size()", creamos esta como un equivalente
    // Objetivo: Analizar el tamaño de una pila sin StackA.size();
    private static <T> int stackSize(StackADT<T> entrada){
        int res =0;
        StackADT<T> aux=new StackA();
        while(!entrada.isEmpty()){
            aux.push(entrada.pop());
            res++; //contador de todos los datos
        }
        while(!aux.isEmpty())
            entrada.push(aux.pop());
        return res;
    }
  
    // Usamos invierte pila durante la evaluación pues el orden de acceso estaba al revés
    private static StackADT<String>  inviertePila(StackADT<String> pila){
        StackADT<String> aux1=new StackA(), aux2=new StackA();
        
        while(!pila.isEmpty()){
            aux1.push(pila.pop());
        }
        while(!aux1.isEmpty()){
            aux2.push(aux1.pop());
        }        
        while(!aux2.isEmpty()){
            pila.push(aux2.pop());
        }        
        
        return pila;
    }
}