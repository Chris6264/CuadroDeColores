package mx.tecnm.culiacan;
public class Main {
    public static void main(String[] args) {
        System.out.print("Ingresa Numero De Filas: ");
        int numeroFilas = Keyboard.readInt();
        System.out.print("Ingresa Numero De Columnas: ");
        int numeroColumnas = Keyboard.readInt();
        Tablero tablero = new Tablero(numeroFilas,numeroColumnas);
        System.out.println(tablero);
    }
}