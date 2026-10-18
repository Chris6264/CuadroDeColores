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
        Tablero tablero;
        if (version == Version.BASICA) tablero = new Tablero(reglas);
        else while (true) {
            try {
                int numeroDeLados = vista.pedirNumeroDeLados();
                tablero = new Tablero(numeroDeLados, reglas);
                break;
            } catch (ReglasException e) {
                vista.mostrarError(e.getMessage());
            }
        }
        return tablero;
    }
}