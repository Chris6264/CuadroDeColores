package mx.tecnm.culiacan;

import java.util.ArrayList;
import java.util.List;

/**
 * Coordina la selección, generación, resolución y presentación de los tableros.
 */
public class CuadroDeColores {
    private final Version version;
    private final Reglas reglas;
    private final Vista vista;

    /**
     * Construye una instancia de CuadroDeColores con los parámetros indicados.
     * @param version Version utilizado en la operación.
     * @param reglas Reglas utilizado en la operación.
     * @param vista Vista utilizado en la operación.
     */
    public CuadroDeColores(Version version, Reglas reglas, Vista vista) {
        this.version = version;
        this.reglas = reglas;
        this.vista = vista;
    }

    /**
     * Inicia la operación correspondiente.
     */
    public void iniciar() {
        int filas = 2;
        int columnas = 2;

        if (version == Version.INTERMEDIA) {
            filas = pedirDimension();
            columnas = filas;
        } else if (version == Version.AVANZADA) {
            filas = pedirFilas();
            columnas = pedirColumnas();
        }

        TableroJuego tablero = null;
        List<Paso> solucion = null;

        int maximoIntentos = 100;
        int sinSolucion = 0;
        int limiteAlcanzado = 0;
        int erroresGeneracion = 0;

        for (int intento = 1; intento <= maximoIntentos && solucion == null; intento++) {
            try {
                tablero = crearTablero(filas,columnas);

                SolucionadorTablero solucionador = new SolucionadorTablero(
                        tablero.getSalidas(),
                        tablero.getNumeroFilas(),
                        tablero.getNumeroColumnas(),
                        tablero.getPiezas(),
                        reglas
                );

                long inicio = System.nanoTime();

                solucion = solucionador.resolverTablero(tablero.getEstadoInicial());

                double tiempo = (System.nanoTime() - inicio) / 1_000_000_000.0;

               vista.mostrarInformacionIntento(intento,solucionador,tiempo);

                if (solucionador.getResultadoBusqueda() == ResultadoBusqueda.SIN_SOLUCION)
                    sinSolucion++;

                if (solucionador.getResultadoBusqueda() == ResultadoBusqueda.LIMITE_ALCANZADO)
                    limiteAlcanzado++;

            } catch (ReglasException e) {
            erroresGeneracion++;
            solucion = null;
            vista.mostrarIntentoFallido(intento, e);
        } catch (IndexOutOfBoundsException e) {
            erroresGeneracion++;
            solucion = null;
            vista.mostrarErrorIndices(intento);
            }
        }

        if (solucion == null) {
            vista.mostrarSinSolucion(sinSolucion,limiteAlcanzado,erroresGeneracion);
            return;
        }

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

    /**
     * Crea tablero.
     * @param filas Filas utilizado en la operación.
     * @param columnas Columnas utilizado en la operación.
     * @return Resultado de crear tablero.
     */
    private TableroJuego crearTablero(int filas, int columnas){
        TableroJuego tablero;
        if (version == Version.BASICA)
            tablero = new Tablero(reglas);
        else if (version == Version.INTERMEDIA)
            tablero = new Tablero(filas, reglas);
        else
            tablero = new TableroAvanzado(filas, columnas, reglas);
        return tablero;
    }

    /**
     * Solicita dimension.
     * @return Resultado de pedir dimension.
     */
    private int pedirDimension() {
        while (true) {
            try {
                int numero = vista.pedirNumeroDeLados();
                reglas.validarCampos(numero);
                return numero;
            } catch (ReglasException e) {
                vista.mostrarError(e.getMessage());
            }
        }
    }

    /**
     * Solicita filas.
     * @return Resultado de pedir filas.
     */
    private int pedirFilas() {
        while (true) {
            try {
                int numero = vista.pedirNumeroFilas();
                reglas.validarCampos(numero);
                return numero;
            } catch (ReglasException e) {
                vista.mostrarError(e.getMessage());
            }
        }
    }

    /**
     * Solicita columnas.
     * @return Resultado de pedir columnas.
     */
    private int pedirColumnas() {
        while (true) {
            try {
                int numero = vista.pedirNumeroColumnas();
                reglas.validarCampos(numero);
                return numero;
            } catch (ReglasException e) {
                vista.mostrarError(e.getMessage());
            }
        }
    }
}