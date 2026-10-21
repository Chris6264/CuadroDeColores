package mx.tecnm.culiacan;

public record Paso(int fila, int columna, Movimiento movimiento, boolean fichaSale, Pieza pieza) {
    @Override
    public String toString() {
        return (fichaSale ? "Sacar " : "Mover ")
                + pieza.color() + " (" + pieza.color().getSimbolo()
                + ") de [" + fila + "," + columna + "] hacia " + movimiento;
    }
}