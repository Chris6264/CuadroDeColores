
package mx.tecnm.culiacan;

import java.util.List;

/**
 * Construye la representación visual del tablero y sus piezas.
 */
public class ImpresorTablero {
    private static final String RESET = "\u001B[0m";
    private static final int ANCHO_CELDA = 3;
    private static final int ANCHO_SALIDA_VERTICAL = 1;
    private static final int ANCHO_SALIDA_HORIZONTAL = 2;

    private final Estado estado;
    private final List<Pieza> piezas;
    private final List<Salida> salidas;
    private final int filas;
    private final int columnas;

    /**
     * Construye una instancia de ImpresorTablero con los parámetros indicados.
     * @param estado Estado utilizado en la operación.
     * @param piezas Piezas utilizado en la operación.
     * @param salidas Salidas utilizado en la operación.
     */
    public ImpresorTablero(Estado estado, List<Pieza> piezas, List<Salida> salidas) {
        this.estado = estado;
        this.piezas = piezas;
        this.salidas = salidas;
        this.filas = estado.getNumeroFilas();
        this.columnas = estado.getNumeroColumnas();
    }

    /**
     * Implementa la operación color.
     * @param fila Fila utilizado en la operación.
     * @param columna Columna utilizado en la operación.
     * @return Resultado de color.
     */
    private Ficha color(int fila, int columna) {
        int id = estado.get(fila, columna);
        return id == 0 ? Ficha.VACIA : piezas.get(id - 1).color();
    }

    /**
     * Implementa la operación misma pieza.
     * @param fila1 Fila1 utilizado en la operación.
     * @param columna1 Columna1 utilizado en la operación.
     * @param fila2 Fila2 utilizado en la operación.
     * @param columna2 Columna2 utilizado en la operación.
     * @return Resultado de misma pieza.
     */
    private boolean mismaPieza(int fila1, int columna1, int fila2, int columna2) {
        int id = estado.get(fila1, columna1);
        return id != 0 && id == estado.get(fila2, columna2);
    }

    /**
     * Implementa la operación bloque.
     * @param ficha Ficha utilizado en la operación.
     * @param ancho Ancho utilizado en la operación.
     * @return Resultado de bloque.
     */
    private String bloque(Ficha ficha, int ancho) {
        if (ficha == Ficha.VACIA) return " ".repeat(ancho);
        return ficha.getColorANSI() + " ".repeat(ancho) + RESET;
    }

    /**
     * Implementa la operación salida.
     * @param lado Lado utilizado en la operación.
     * @param indice Indice utilizado en la operación.
     * @param ancho Ancho utilizado en la operación.
     * @return Resultado de salida.
     */
    private String salida(LadoSalida lado, int indice, int ancho) {
        for (Salida salida : salidas)
            if (salida.ocupaPosicion(lado, indice))
                return bloque(salida.colorFicha(), ancho);

        return " ".repeat(ancho);
    }

    /**
     * Imprime linea superior.
     * @param sb Sb utilizado en la operación.
     */
    private void imprimirLineaSuperior(StringBuilder sb) {
        sb.append("\n   ");

        for (int columna = 0; columna < columnas; columna++) {
            sb.append(" ");
            sb.append(salida(LadoSalida.ARRIBA, columna, ANCHO_SALIDA_HORIZONTAL));
            sb.append(" ");
        }

        sb.append("\n");
    }

    /**
     * Imprime linea horizontal.
     * @param sb Sb utilizado en la operación.
     * @param fila Fila utilizado en la operación.
     */
    private void imprimirLineaHorizontal(StringBuilder sb, int fila) {
        sb.append("  +");

        for (int columna = 0; columna < columnas; columna++) {
            if (fila > 0 && mismaPieza(fila, columna, fila - 1, columna))
                sb.append(bloque(color(fila, columna), ANCHO_CELDA));
            else sb.append("---");

            if (columna == columnas - 1) {
                sb.append("+");
            } else if (fila > 0
                    && mismaPieza(fila, columna, fila - 1, columna)
                    && mismaPieza(fila, columna, fila, columna + 1)
                    && mismaPieza(fila - 1, columna, fila - 1, columna + 1)) {
                sb.append(bloque(color(fila, columna), 1));
            } else {
                sb.append("+");
            }
        }

        sb.append("\n");
    }

    /**
     * Imprime fila.
     * @param sb Sb utilizado en la operación.
     * @param fila Fila utilizado en la operación.
     */
    private void imprimirFila(StringBuilder sb, int fila) {
        sb.append(salida(LadoSalida.IZQUIERDA, fila, ANCHO_SALIDA_VERTICAL)).append(" |");

        for (int columna = 0; columna < columnas; columna++) {
            Ficha ficha = color(fila, columna);
            sb.append(bloque(ficha, ANCHO_CELDA));

            if (columna == columnas - 1) {
                sb.append("|");
            } else if (mismaPieza(fila, columna, fila, columna + 1)) {
                sb.append(bloque(ficha, 1));
            } else {
                sb.append("|");
            }
        }

        sb.append(" ").append(salida(LadoSalida.DERECHA, fila, ANCHO_SALIDA_VERTICAL)).append("\n");
    }

    /**
     * Imprime linea inferior.
     * @param sb Sb utilizado en la operación.
     */
    private void imprimirLineaInferior(StringBuilder sb) {
        sb.append("  +");

        sb.repeat("---+", Math.max(0, columnas));

        sb.append("\n   ");

        for (int columna = 0; columna < columnas; columna++) {
            sb.append(" ");
            sb.append(salida(LadoSalida.ABAJO, columna, ANCHO_SALIDA_HORIZONTAL));
            sb.append(" ");
        }

        sb.append("\n");
    }

    @Override
    /**
     * Devuelve la representación textual del objeto.
     * @return Resultado de to string.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        imprimirLineaSuperior(sb);

        for (int fila = 0; fila < filas; fila++) {
            imprimirLineaHorizontal(sb, fila);
            imprimirFila(sb, fila);
        }

        imprimirLineaInferior(sb);

        return sb.toString();
    }
}
