package mx.tecnm.culiacan;

import java.util.List;

public class Vista {
    public int pedirNumeroDeLados() {
        System.out.print("Ingresa Numero De Lados: ");
        return Keyboard.readInt();
    }

    public int pedirNumeroFilas() {
        System.out.print("Ingresa Numero De Filas: ");
        return Keyboard.readInt();
    }

    public int pedirNumeroColumnas() {
        System.out.print("Ingresa Numero De Columnas: ");
        return Keyboard.readInt();
    }

    public void mostrarError(String error) {
        System.out.println(error + "\n");
    }

    public void mostrarTablero(TableroJuego tablero) {
        System.out.println(tablero);
    }

    public void mostrarCantidadMovimientos(int cantidadMovimientos) {
        System.out.println("Solución en " + cantidadMovimientos + " movimientos:\n");
    }

    public void mostrarMovimiento(String movimiento) {
        System.out.println(movimiento);
    }

    public void mostrarHistorialMovimientos(List<String> historialDeMovimientos) {
        System.out.println("Historial de movimientos:");
        historialDeMovimientos.forEach(System.out::println);
    }

    public void mostrarInformacionIntento(int intento,SolucionadorTablero solucionador,double tiempo){
        System.out.println("\nIntento " + intento);
        System.out.println("Resultado: " + solucionador.getResultadoBusqueda());
        System.out.println("Nodos explorados: " + solucionador.getNodosExplorados());
        System.out.println("Nodos generados: " + solucionador.getNodosGenerados());
        System.out.printf("Tiempo: %.3f segundos%n", tiempo);
    }

    public void mostrarIntentoFallido(int intento, IllegalStateException e){
        System.out.println("\nIntento " + intento + ": " + e.getMessage());
    }

    public void mostrarErrorIndices(int intento){
        System.out.println("\nError de indices en intento " + intento);
    }

    public void mostrarSinSolucion(int sinSolucion,int limiteAlcanzado,int erroresGeneracion){
        System.out.println("\nNo se encontró una solución.");
        System.out.println("Tableros sin solución comprobada: " + sinSolucion);
        System.out.println("Búsquedas detenidas por límite: " + limiteAlcanzado);
        System.out.println("Errores de generación o validación: " + erroresGeneracion);
    }

    public void mostrarAlgoritmoUsado(int filas, int columnas){
        System.out.println("Algoritmo utilizado: "
                + (filas * columnas <= 25 ? "A*" : "A* ponderado"));
    }
}