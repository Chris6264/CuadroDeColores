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

    public ReglasCuadro(int minimoNumeroDeLados, int maximoNumeroDeLados) {
        this.minimoNumeroDeLados = minimoNumeroDeLados;
        this.maximoNumeroDeLados = maximoNumeroDeLados;
    }

    @Override
    public void validarCampos(int numeroDeLados) {
        if (numeroDeLados < minimoNumeroDeLados) throw new ReglasException("El número de lados debe de ser minimo " + minimoNumeroDeLados);
        else if (numeroDeLados > maximoNumeroDeLados) throw new ReglasException("El número de lados debe de ser máximo " + maximoNumeroDeLados);
    }

    // ---------- Estado y Salida ----------

    @Override
    public void validarIdentificadorPieza(int id) {
        if (id < IDENTIFICADOR_MINIMO || id > IDENTIFICADOR_MAXIMO)
            throw new ReglasException("Identificador de pieza fuera de rango");
    }

    @Override
    public void validarTamanoSalida(int tamano) {
        if (tamano < TAMANO_SALIDA_MINIMO || tamano > TAMANO_SALIDA_MAXIMO)
            throw new ReglasException("Tamano de salida incorrecto");
    }

    // ---------- Solucionador ----------

    @Override
    public void validarSalidaCompatible(boolean existe, Ficha color) {
        if (!existe)
            throw new ReglasException("Pieza sin salida compatible: " + color);
    }

    // ---------- Generación de tableros ----------

    @Override
    public void validarEspacioParaPieza(boolean puedeColocar) {
        if (!puedeColocar)
            throw new ReglasException("No se puede colocar la pieza");
    }

    @Override
    public void validarEspacioParaForma(boolean hayEspacio, Forma forma) {
        if (!hayEspacio)
            throw new ReglasException("No hay espacio para " + forma);
    }

    @Override
    public void validarIndicePieza(int indice, int cantidad) {
        if (indice < 0 || indice >= cantidad)
            throw new ReglasException("Identificador de pieza incorrecto");
    }

    @Override
    public void validarIndiceVecino(int indice, int cantidad) {
        if (indice < 0 || indice >= cantidad)
            throw new ReglasException("Identificador vecino incorrecto");
    }

    @Override
    public void validarColorDisponible(boolean hayDisponible) {
        if (!hayDisponible)
            throw new ReglasException("No hay colores disponibles para la pieza");
    }

    @Override
    public void validarOrientacionDelColor(Orientacion anterior, Orientacion actual) {
        if (anterior != null && anterior != actual)
            throw new ReglasException("Color con orientaciones incompatibles");
    }

    @Override
    public void validarColoresCompatibles(boolean hayCompatible) {
        if (!hayCompatible)
            throw new ReglasException("No hay colores compatibles para esta distribución");
    }

    @Override
    public void validarColorAsignado(int asignado, int totalColores, int numeroDePieza) {
        if (asignado < 0 || asignado >= totalColores)
            throw new ReglasException("Color no asignado a la pieza " + numeroDePieza);
    }

    @Override
    public void validarPiezaSeleccionada(int seleccionada) {
        if (seleccionada == -1)
            throw new ReglasException("No se pudo seleccionar una pieza para colorear");
    }

    @Override
    public void validarSalidaDisponible(boolean hayDisponible, Ficha color) {
        if (!hayDisponible)
            throw new ReglasException("No hay salida disponible para el color " + color);
    }

    @Override
    public void validarTableroGenerado(boolean generado, ReglasException causa) {
        if (!generado)
            throw new ReglasException("No se pudo generar un tablero avanzado válido", causa);
    }

    // ---------- Tablero terminado ----------

    @Override
    public void validarTableroCompleto(int[][] tablero) {
        for (int[] fila : tablero)
            for (int id : fila)
                if (id == 0)
                    throw new ReglasException("El tablero contiene casillas vacías");
    }

    @Override
    public void validarCantidadColores(long cantidad) {
        if (cantidad < COLORES_MINIMOS || cantidad > COLORES_MAXIMOS)
            throw new ReglasException("Cantidad de colores incorrecta");
    }
}