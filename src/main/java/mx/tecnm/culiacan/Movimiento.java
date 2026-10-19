package mx.tecnm.culiacan;

public enum Movimiento {
    ARRIBA(-1,0),
    ABAJO(1,0),
    IZQUIERDA(0,-1),
    DERECHA(0,1);

    private final int movimientoFila;
    private final int movimientoColumna;

    Movimiento(int movimientoFila, int movimientoColumna) {
        this.movimientoFila = movimientoFila;
        this.movimientoColumna = movimientoColumna;
    }

    public int getMovimientoFila() { return movimientoFila; }

    public int getMovimientoColumna() { return movimientoColumna; }

    public LadoSalida getLadoSalida() {
        switch (this) {
            case ARRIBA: return LadoSalida.ARRIBA;
            case ABAJO: return LadoSalida.ABAJO;
            case IZQUIERDA: return LadoSalida.IZQUIERDA;
            default: return LadoSalida.DERECHA;
        }
    }
}
