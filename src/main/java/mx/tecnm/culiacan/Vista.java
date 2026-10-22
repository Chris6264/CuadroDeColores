package mx.tecnm.culiacan;

import java.util.List;

/**
 * Presenta en consola los mensajes y resultados del juego.
 */
public class Vista {
    /**
     * Solicita numero de lados.
     * @return Resultado de pedir numero de lados.
     */
    public int pedirNumeroDeLados() {
        System.out.print("Ingresa Numero De Lados: ");
        return Keyboard.readInt();
    }

    /**
     * Solicita numero filas.
     * @return Resultado de pedir numero filas.
     */
    public int pedirNumeroFilas() {
        System.out.print("Ingresa Numero De Filas: ");
        return Keyboard.readInt();
    }

    /**
     * Solicita numero columnas.
     * @return Resultado de pedir numero columnas.
     */
    public int pedirNumeroColumnas() {
        System.out.print("Ingresa Numero De Columnas: ");
        return Keyboard.readInt();
    }

    /**
     * Muestra error.
     * @param error Error utilizado en la operación.
     */
    public void mostrarError(String error) {
        System.out.println(error + "\n");
    }

    /**
     * Muestra tablero.
     * @param tablero Tablero utilizado en la operación.
     */
    public void mostrarTablero(TableroJuego tablero) {
        System.out.println(tablero);
    }

    /**
     * Muestra cantidad movimientos.
     * @param cantidadMovimientos Cantidad movimientos utilizado en la operación.
     */
    public void mostrarCantidadMovimientos(int cantidadMovimientos) {
        System.out.println("Solución en " + cantidadMovimientos + " movimientos:\n");
    }

    /**
     * Muestra movimiento.
     * @param movimiento Movimiento utilizado en la operación.
     */
    public void mostrarMovimiento(String movimiento) {
        System.out.println(movimiento);
    }

    /**
     * Muestra historial movimientos.
     * @param historialDeMovimientos Historial de movimientos utilizado en la operación.
     */
    public void mostrarHistorialMovimientos(List<String> historialDeMovimientos) {
        System.out.println("Historial de movimientos:");
        historialDeMovimientos.forEach(System.out::println);
    }

    /**
     * Muestra informacion intento.
     * @param intento Intento utilizado en la operación.
     * @param solucionador Solucionador utilizado en la operación.
     * @param tiempo Tiempo utilizado en la operación.
     */
    public void mostrarInformacionIntento(int intento,SolucionadorTablero solucionador,double tiempo){
        System.out.println("\nIntento " + intento);
        System.out.println("Resultado: " + solucionador.getResultadoBusqueda());
        System.out.println("Nodos explorados: " + solucionador.getNodosExplorados());
        System.out.println("Nodos generados: " + solucionador.getNodosGenerados());
        System.out.printf("Tiempo: %.3f segundos%n", tiempo);
    }

    /**
     * Muestra intento fallido.
     * @param intento Intento utilizado en la operación.
     * @param e E utilizado en la operación.
     */
    public void mostrarIntentoFallido(int intento, ReglasException e){
        System.out.println("\nIntento " + intento + ": " + e.getMessage());
    }

    /**
     * Muestra error indices.
     * @param intento Intento utilizado en la operación.
     */
    public void mostrarErrorIndices(int intento){
        System.out.println("\nError de indices en intento " + intento);
    }

    /**
     * Muestra sin solucion.
     * @param sinSolucion Sin solucion utilizado en la operación.
     * @param limiteAlcanzado Limite alcanzado utilizado en la operación.
     * @param erroresGeneracion Errores generacion utilizado en la operación.
     */
    public void mostrarSinSolucion(int sinSolucion,int limiteAlcanzado,int erroresGeneracion){
        System.out.println("\nNo se encontró una solución.");
        System.out.println("Tableros sin solución comprobada: " + sinSolucion);
        System.out.println("Búsquedas detenidas por límite: " + limiteAlcanzado);
        System.out.println("Errores de generación o validación: " + erroresGeneracion);
    }

    /**
     * Muestra algoritmo usado.
     * @param filas Filas utilizado en la operación.
     * @param columnas Columnas utilizado en la operación.
     */
    public void mostrarAlgoritmoUsado(int filas, int columnas){
        System.out.println("Algoritmo utilizado: "
                + (filas * columnas < 25 ? "A*" : "A* ponderado"));
    }
}