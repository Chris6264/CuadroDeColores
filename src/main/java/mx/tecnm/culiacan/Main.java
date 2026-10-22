package mx.tecnm.culiacan;

/**
 * Inicia la aplicación y configura la ejecución del juego.
 */
public class Main {
    /**
     * Ejecuta el punto de entrada del programa.
     * @param args Args utilizado en la operación.
     */
    public static void main(String[] args) {
        Version version = Version.AVANZADA;
        Reglas reglas = version.crearReglas();

        Vista vista = new Vista();
        CuadroDeColores cuadroDeColores = new CuadroDeColores(version, reglas, vista);
        cuadroDeColores.iniciar();
    }
}