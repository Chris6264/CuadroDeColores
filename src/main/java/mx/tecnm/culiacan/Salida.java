package mx.tecnm.culiacan;

/**
 * Representa una salida del tablero, su posición, color y tamaño.
 */
public record Salida(LadoSalida ladoSalida, int indice, Ficha colorFicha, int tamano) {
    /**
     * Construye una instancia de Salida con los parámetros indicados.
     * @param ladoSalida Lado salida utilizado en la operación.
     * @param indice Indice utilizado en la operación.
     * @param colorFicha Color ficha utilizado en la operación.
     */
    public Salida(LadoSalida ladoSalida, int indice, Ficha colorFicha) {
        this(ladoSalida, indice, colorFicha, 1);
    }

    /**
     * Comprueba si ocupa posicion.
     * @param lado Lado utilizado en la operación.
     * @param posicion Posicion utilizado en la operación.
     * @return Resultado de ocupa posicion.
     */
    public boolean ocupaPosicion(LadoSalida lado, int posicion) {
        return ladoSalida == lado
                && posicion >= indice
                && posicion < indice + tamano;
    }

    /**
     * Obtiene fila.
     * @param numeroFilas Numero filas utilizado en la operación.
     * @return Resultado de get fila.
     */
    public int getFila(int numeroFilas) {
        return switch (ladoSalida) {
            case ARRIBA -> 0;
            case ABAJO -> numeroFilas - 1;
            default -> indice;
        };
    }

    /**
     * Obtiene columna.
     * @param numeroColumnas Numero columnas utilizado en la operación.
     * @return Resultado de get columna.
     */
    public int getColumna(int numeroColumnas) {
        return switch (ladoSalida) {
            case IZQUIERDA -> 0;
            case DERECHA -> numeroColumnas - 1;
            default -> indice;
        };
    }
}