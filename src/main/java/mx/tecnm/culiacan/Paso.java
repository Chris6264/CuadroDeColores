package mx.tecnm.culiacan;

/**
 * Representa un movimiento concreto de una pieza.
 */
public record Paso(int fila, int columna, Movimiento movimiento, boolean fichaSale, Pieza pieza) {
    @Override
    /**
     * Devuelve la representación textual del objeto.
     * @return Resultado de to string.
     */
    public String toString() {
        return (fichaSale ? "Sacar " : "Mover ")
                + pieza.color() + " (" + pieza.color().getSimbolo()
                + ") de [" + fila + "," + columna + "] hacia " + movimiento;
    }
}