package mx.tecnm.culiacan;

/**
 * Representa las direcciones de desplazamiento permitidas.
 */
public enum Movimiento {
    ARRIBA(-1,0),
    ABAJO(1,0),
    IZQUIERDA(0,-1),
    DERECHA(0,1);

    private final int movimientoFila;
    private final int movimientoColumna;

    /**
     * Inicializa los valores de Movimiento.
     * @param movimientoFila Valor utilizado para inicializar la instancia.
     * @param movimientoColumna Valor utilizado para inicializar la instancia.
     */
    Movimiento(int movimientoFila, int movimientoColumna) {
        this.movimientoFila = movimientoFila;
        this.movimientoColumna = movimientoColumna;
    }

    /**
     * Obtiene movimiento fila.
     * @return Resultado de get movimiento fila.
     */
    public int getMovimientoFila() { return movimientoFila; }

    /**
     * Obtiene movimiento columna.
     * @return Resultado de get movimiento columna.
     */
    public int getMovimientoColumna() { return movimientoColumna; }

    /**
     * Obtiene lado salida.
     * @return Resultado de get lado salida.
     */
    public LadoSalida getLadoSalida() {
        return switch (this) {
            case ARRIBA -> LadoSalida.ARRIBA;
            case ABAJO -> LadoSalida.ABAJO;
            case IZQUIERDA -> LadoSalida.IZQUIERDA;
            default -> LadoSalida.DERECHA;
        };
    }
}
