package mx.tecnm.culiacan;

import java.util.ArrayList;
import java.util.List;

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
        Tablero tablero;
        List<Paso> solucion;

        do {
            tablero = crearTablero();
            solucion = new SolucionadorTablero(tablero.getSalidas(), tablero.getNumeroDeLados())
                    .resolverTablero(tablero.getEstadoInicial());
        } while (solucion == null);

        vista.mostrarTablero(tablero);
        vista.mostrarCantidadMovimientos(solucion.size());

        List<String> historialDeMovimientos = new ArrayList<>();
        int numeroDePaso = 1;
        for (Paso paso : solucion) {
            tablero.realizarMovimiento(paso);
            String movimiento = numeroDePaso++ + ") " + paso;
            historialDeMovimientos.add(movimiento);
            vista.mostrarMovimiento(movimiento);
            vista.mostrarTablero(tablero);
        }
        vista.mostrarHistorialMovimientos(historialDeMovimientos);
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