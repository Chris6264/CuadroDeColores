package mx.tecnm.culiacan;

public class CuadroDeColores {
    private final Version version;
    private final Reglas reglas;
    private final Vista vista;

    public CuadroDeColores(Version version, Reglas reglas, Vista vista) {
        this.version = version;
        this.reglas = reglas;
        this.vista = vista;
    }

    public void iniciar(){
        Tablero tablero = crearTablero();
        vista.mostrarTablero(tablero);
    }

    private Tablero crearTablero() {
        if (version == Version.BASICA) return new Tablero(reglas);
        while (true) {
            try {
                int numeroDeLados = vista.pedirNumeroDeLados();
                return new Tablero(numeroDeLados, reglas);
            } catch (ReglasException e) {
                vista.mostrarError(e.getMessage());
            }
        }
    }
}