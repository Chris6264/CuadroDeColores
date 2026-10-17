package mx.tecnm.culiacan;

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
}
