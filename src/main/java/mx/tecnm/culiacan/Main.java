package mx.tecnm.culiacan;

public class Main {
    public static void main(String[] args) {
        Version version = Version.INTERMEDIA;
        Reglas reglas = version.crearReglas();

        Vista vista = new Vista();
        CuadroDeColores cuadroDeColores = new CuadroDeColores(version, reglas, vista);
        cuadroDeColores.iniciar();
    }
}