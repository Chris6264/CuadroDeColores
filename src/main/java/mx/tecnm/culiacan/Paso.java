package mx.tecnm.culiacan;

public class Paso {
    private final int fila;
    private final int columna;
    private final Movimiento movimiento;
    private final boolean fichaSale;
    private final Ficha ficha;

    public Paso(int fila, int columna, Movimiento movimiento, boolean fichaSale, Ficha ficha) {
        this.fila = fila;
        this.columna = columna;
        this.movimiento = movimiento;
        this.fichaSale = fichaSale;
        this.ficha = ficha;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public boolean fichaSale() {
        return fichaSale;
    }

    public Ficha getFicha() {
        return ficha;
    }

    @Override
    public String toString() {
        return (fichaSale ? "Sacar " : "Mover ") + ficha + " (" + ficha.getSimbolo() + ") de ["
                + fila + "," + columna + "] hacia " + movimiento;
    }
}
