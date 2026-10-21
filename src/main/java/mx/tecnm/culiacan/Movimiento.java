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
        return switch (this) {
            case ARRIBA -> LadoSalida.ARRIBA;
            case ABAJO -> LadoSalida.ABAJO;
            case IZQUIERDA -> LadoSalida.IZQUIERDA;
            default -> LadoSalida.DERECHA;
        };
    }
}
