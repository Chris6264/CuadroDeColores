package mx.tecnm.culiacan;

import java.util.List;

public class Vista {
    public int pedirNumeroDeLados(){
        System.out.print("Ingresa Numero De Lados: ");
        return Keyboard.readInt();
    }

    public void mostrarError(String error){
        System.out.println(error + "\n");
    }

    public void mostrarTablero(Tablero tablero){
        System.out.println(tablero);
    }

    public void mostrarCantidadMovimientos(int canitdadMovimientos){ System.out.println("Solución en " + canitdadMovimientos + " movimientos:\n"); }

    public void mostrarMovimiento(String movimiento) {System.out.println(movimiento);}

    public void mostrarHistorialMovimientos(List<String> historialDeMovimientos){
        System.out.println("Historial de movimientos:");
        historialDeMovimientos.forEach(System.out::println);
    }
}
