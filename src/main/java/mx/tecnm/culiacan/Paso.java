package mx.tecnm.culiacan;

public class Paso {
    private final int fila;
    private final int columna;
    private final Movimiento movimiento;
    private final boolean fichaSale;
    private final Pieza pieza;

    public Paso(int fila, int columna, Movimiento movimiento, boolean fichaSale, Pieza pieza) {
        this.fila = fila;
        this.columna = columna;
        this.movimiento = movimiento;
        this.fichaSale = fichaSale;
        this.pieza = pieza;
    }

    public int getFila() { return fila; }

    public int getColumna() { return columna; }

    public Movimiento getMovimiento() { return movimiento; }

    public boolean fichaSale() { return fichaSale; }

    public Pieza getPieza() { return pieza; }

    public Ficha getFicha() { return pieza.getColor(); }

    @Override
    public String toString() {
        return (fichaSale ? "Sacar " : "Mover ")
                + pieza.getColor() + " (" + pieza.getColor().getSimbolo()
                + ") de [" + fila + "," + columna + "] hacia " + movimiento;
    }
}