package mx.tecnm.culiacan;

/**
 * Única clase del proyecto que lanza excepciones.
 * El resto de las clases solo llaman a sus métodos de validación.
 */
public class ReglasCuadro implements Reglas {
    private static final int IDENTIFICADOR_MINIMO = 0;
    private static final int IDENTIFICADOR_MAXIMO = 255;
    private static final int TAMANO_SALIDA_MINIMO = 1;
    private static final int TAMANO_SALIDA_MAXIMO = 2;
    private static final int COLORES_MINIMOS = 3;
    private static final int COLORES_MAXIMOS = 10;

    private final int minimoNumeroDeLados;
    private final int maximoNumeroDeLados;

    /**
     * Construye una instancia de ReglasCuadro con los parámetros indicados.
     * @param minimoNumeroDeLados Minimo numero de lados utilizado en la operación.
     * @param maximoNumeroDeLados Maximo numero de lados utilizado en la operación.
     */
    public ReglasCuadro(int minimoNumeroDeLados, int maximoNumeroDeLados) {
        this.minimoNumeroDeLados = minimoNumeroDeLados;
        this.maximoNumeroDeLados = maximoNumeroDeLados;
    }

    @Override
    /**
     * Valida campos.
     * @param numeroDeLados Numero de lados utilizado en la operación.
     */
    public void validarCampos(int numeroDeLados) {
        if (numeroDeLados < minimoNumeroDeLados) throw new ReglasException("El número de lados debe de ser minimo " + minimoNumeroDeLados);
        else if (numeroDeLados > maximoNumeroDeLados) throw new ReglasException("El número de lados debe de ser máximo " + maximoNumeroDeLados);
    }

    // ---------- Estado y Salida ----------

    @Override
    /**
     * Valida identificador pieza.
     * @param id Id utilizado en la operación.
     */
    public void validarIdentificadorPieza(int id) {
        if (id < IDENTIFICADOR_MINIMO || id > IDENTIFICADOR_MAXIMO)
            throw new ReglasException("Identificador de pieza fuera de rango");
    }

    @Override
    /**
     * Valida tamano salida.
     * @param tamano Tamano utilizado en la operación.
     */
    public void validarTamanoSalida(int tamano) {
        if (tamano < TAMANO_SALIDA_MINIMO || tamano > TAMANO_SALIDA_MAXIMO)
            throw new ReglasException("Tamano de salida incorrecto");
    }

    // ---------- Solucionador ----------

    @Override
    /**
     * Valida salida compatible.
     * @param existe Existe utilizado en la operación.
     * @param color Color utilizado en la operación.
     */
    public void validarSalidaCompatible(boolean existe, Ficha color) {
        if (!existe)
            throw new ReglasException("Pieza sin salida compatible: " + color);
    }

    // ---------- Generación de tableros ----------

    @Override
    /**
     * Valida espacio para pieza.
     * @param puedeColocar Puede colocar utilizado en la operación.
     */
    public void validarEspacioParaPieza(boolean puedeColocar) {
        if (!puedeColocar)
            throw new ReglasException("No se puede colocar la pieza");
    }

    @Override
    /**
     * Valida espacio para forma.
     * @param hayEspacio Hay espacio utilizado en la operación.
     * @param forma Forma utilizado en la operación.
     */
    public void validarEspacioParaForma(boolean hayEspacio, Forma forma) {
        if (!hayEspacio)
            throw new ReglasException("No hay espacio para " + forma);
    }

    @Override
    /**
     * Valida indice pieza.
     * @param indice Indice utilizado en la operación.
     * @param cantidad Cantidad utilizado en la operación.
     */
    public void validarIndicePieza(int indice, int cantidad) {
        if (indice < 0 || indice >= cantidad)
            throw new ReglasException("Identificador de pieza incorrecto");
    }

    @Override
    /**
     * Valida indice vecino.
     * @param indice Indice utilizado en la operación.
     * @param cantidad Cantidad utilizado en la operación.
     */
    public void validarIndiceVecino(int indice, int cantidad) {
        if (indice < 0 || indice >= cantidad)
            throw new ReglasException("Identificador vecino incorrecto");
    }

    @Override
    /**
     * Valida color disponible.
     * @param hayDisponible Hay disponible utilizado en la operación.
     */
    public void validarColorDisponible(boolean hayDisponible) {
        if (!hayDisponible)
            throw new ReglasException("No hay colores disponibles para la pieza");
    }

    @Override
    /**
     * Valida orientacion del color.
     * @param anterior Anterior utilizado en la operación.
     * @param actual Actual utilizado en la operación.
     */
    public void validarOrientacionDelColor(Orientacion anterior, Orientacion actual) {
        if (anterior != null && anterior != actual)
            throw new ReglasException("Color con orientaciones incompatibles");
    }

    @Override
    /**
     * Valida colores compatibles.
     * @param hayCompatible Hay compatible utilizado en la operación.
     */
    public void validarColoresCompatibles(boolean hayCompatible) {
        if (!hayCompatible)
            throw new ReglasException("No hay colores compatibles para esta distribución");
    }

    @Override
    /**
     * Valida color asignado.
     * @param asignado Asignado utilizado en la operación.
     * @param totalColores Total colores utilizado en la operación.
     * @param numeroDePieza Numero de pieza utilizado en la operación.
     */
    public void validarColorAsignado(int asignado, int totalColores, int numeroDePieza) {
        if (asignado < 0 || asignado >= totalColores)
            throw new ReglasException("Color no asignado a la pieza " + numeroDePieza);
    }

    @Override
    /**
     * Valida pieza seleccionada.
     * @param seleccionada Seleccionada utilizado en la operación.
     */
    public void validarPiezaSeleccionada(int seleccionada) {
        if (seleccionada == -1)
            throw new ReglasException("No se pudo seleccionar una pieza para colorear");
    }

    @Override
    /**
     * Valida salida disponible.
     * @param hayDisponible Hay disponible utilizado en la operación.
     * @param color Color utilizado en la operación.
     */
    public void validarSalidaDisponible(boolean hayDisponible, Ficha color) {
        if (!hayDisponible)
            throw new ReglasException("No hay salida disponible para el color " + color);
    }

    @Override
    /**
     * Valida tablero generado.
     * @param generado Generado utilizado en la operación.
     * @param causa Causa utilizado en la operación.
     */
    public void validarTableroGenerado(boolean generado, ReglasException causa) {
        if (!generado)
            throw new ReglasException("No se pudo generar un tablero avanzado válido", causa);
    }

    // ---------- Tablero terminado ----------

    @Override
    /**
     * Valida tablero completo.
     * @param tablero Tablero utilizado en la operación.
     */
    public void validarTableroCompleto(int[][] tablero) {
        for (int[] fila : tablero)
            for (int id : fila)
                if (id == 0)
                    throw new ReglasException("El tablero contiene casillas vacías");
    }

    @Override
    /**
     * Valida cantidad colores.
     * @param cantidad Cantidad utilizado en la operación.
     */
    public void validarCantidadColores(long cantidad) {
        if (cantidad < COLORES_MINIMOS || cantidad > COLORES_MAXIMOS)
            throw new ReglasException("Cantidad de colores incorrecta");
    }
}