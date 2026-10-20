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

    public void iniciar() {
        Tablero tablero = null;
        List<Paso> solucion = null;

        int numeroDeLados = version == Version.BASICA ? 2 : pedirNumeroDeLados();
        int intentos = 0;
        int maximoIntentos = 30;

        while (solucion == null && intentos < maximoIntentos) {
            intentos++;

            try {
                tablero = version == Version.BASICA
                        ? new Tablero(reglas)
                        : new Tablero(numeroDeLados, reglas);

                SolucionadorTablero solucionador = new SolucionadorTablero(
                        tablero.getSalidas(),
                        tablero.getNumeroDeLados(),
                        tablero.getPiezas()
                );

                solucion = solucionador.resolverTablero(tablero.getEstadoInicial());

            } catch (IllegalStateException e) {
                solucion = null;
            }
        }

        if (solucion == null)
            throw new IllegalStateException("No se encontró una solución después de " + maximoIntentos + " intentos");

        vista.mostrarTablero(tablero);

        System.out.println("Algoritmo utilizado: "
                + (numeroDeLados <= 5 ? "A*" : "A* ponderado"));

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

    private int pedirNumeroDeLados() {
        while (true) {
            try {
                int numeroDeLados = vista.pedirNumeroDeLados();
                reglas.validarCampos(numeroDeLados);
                return numeroDeLados;
            } catch (ReglasException e) {
                vista.mostrarError(e.getMessage());
            }
        }
    }
}