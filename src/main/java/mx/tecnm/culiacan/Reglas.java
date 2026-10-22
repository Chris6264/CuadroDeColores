package mx.tecnm.culiacan;

/**
 * Declara las validaciones que se aplican al juego.
 */
public interface Reglas {
    /**
     * Valida campos.
     * @param numeroDeLados Parámetro utilizado en la operación.
     */
    void validarCampos(int numeroDeLados);

    // Estado y Salida
    /**
     * Valida identificadorpieza.
     * @param id Parámetro utilizado en la operación.
     */
    void validarIdentificadorPieza(int id);

    /**
     * Valida tamanosalida.
     * @param tamano Parámetro utilizado en la operación.
     */
    void validarTamanoSalida(int tamano);

    // Solucionador
    /**
     * Valida salidacompatible.
     * @param existe Parámetro utilizado en la operación.
     * @param color Parámetro utilizado en la operación.
     */
    void validarSalidaCompatible(boolean existe, Ficha color);

    // Generación de tableros
    /**
     * Valida espacioparapieza.
     * @param puedeColocar Parámetro utilizado en la operación.
     */
    void validarEspacioParaPieza(boolean puedeColocar);

    /**
     * Valida espacioparaforma.
     * @param hayEspacio Parámetro utilizado en la operación.
     * @param forma Parámetro utilizado en la operación.
     */
    void validarEspacioParaForma(boolean hayEspacio, Forma forma);

    /**
     * Valida indicepieza.
     * @param indice Parámetro utilizado en la operación.
     * @param cantidad Parámetro utilizado en la operación.
     */
    void validarIndicePieza(int indice, int cantidad);

    /**
     * Valida indicevecino.
     * @param indice Parámetro utilizado en la operación.
     * @param cantidad Parámetro utilizado en la operación.
     */
    void validarIndiceVecino(int indice, int cantidad);

    /**
     * Valida colordisponible.
     * @param hayDisponible Parámetro utilizado en la operación.
     */
    void validarColorDisponible(boolean hayDisponible);

    /**
     * Valida orientaciondelcolor.
     * @param anterior Parámetro utilizado en la operación.
     * @param actual Parámetro utilizado en la operación.
     */
    void validarOrientacionDelColor(Orientacion anterior, Orientacion actual);

    /**
     * Valida colorescompatibles.
     * @param hayCompatible Parámetro utilizado en la operación.
     */
    void validarColoresCompatibles(boolean hayCompatible);

    /**
     * Valida colorasignado.
     * @param asignado Parámetro utilizado en la operación.
     * @param totalColores Parámetro utilizado en la operación.
     * @param numeroDePieza Parámetro utilizado en la operación.
     */
    void validarColorAsignado(int asignado, int totalColores, int numeroDePieza);

    /**
     * Valida piezaseleccionada.
     * @param seleccionada Parámetro utilizado en la operación.
     */
    void validarPiezaSeleccionada(int seleccionada);

    /**
     * Valida salidadisponible.
     * @param hayDisponible Parámetro utilizado en la operación.
     * @param color Parámetro utilizado en la operación.
     */
    void validarSalidaDisponible(boolean hayDisponible, Ficha color);

    /**
     * Valida tablerogenerado.
     * @param generado Parámetro utilizado en la operación.
     * @param causa Parámetro utilizado en la operación.
     */
    void validarTableroGenerado(boolean generado, ReglasException causa);

    // Tablero terminado
    /**
     * Valida tablerocompleto.
     * @param tablero Parámetro utilizado en la operación.
     */
    void validarTableroCompleto(int[][] tablero);

    /**
     * Valida cantidadcolores.
     * @param cantidad Parámetro utilizado en la operación.
     */
    void validarCantidadColores(long cantidad);
}
