package mx.tecnm.culiacan;

/**
 * Representa una pieza del juego y las propiedades de su forma.
 */
public record Pieza(int id, Ficha color, Forma forma) {
    /**
     * Construye una instancia de Pieza con los parámetros indicados.
     * @param id Id utilizado en la operación.
     * @param color Color utilizado en la operación.
     * @param orientacion Orientacion utilizado en la operación.
     */
    public Pieza(int id, Ficha color, Orientacion orientacion) {
        this(id, color, convertirForma(orientacion));
    }

    /**
     * Convierte forma.
     * @param orientacion Orientacion utilizado en la operación.
     * @return Resultado de convertir forma.
     */
    private static Forma convertirForma(Orientacion orientacion) {
        return switch (orientacion) {
            case HORIZONTAL -> Forma.HORIZONTAL;
            case VERTICAL -> Forma.VERTICAL;
            default -> Forma.INDIVIDUAL;
        };
    }

    /**
     * Obtiene tamano.
     * @return Resultado de get tamano.
     */
    public int getTamano() {
        return forma.getTamano();
    }

    /**
     * Obtiene desplazamiento fila.
     * @param parte Parte utilizado en la operación.
     * @return Resultado de get desplazamiento fila.
     */
    public int getDesplazamientoFila(int parte) {
        return forma.getFila(parte);
    }

    /**
     * Obtiene desplazamiento columna.
     * @param parte Parte utilizado en la operación.
     * @return Resultado de get desplazamiento columna.
     */
    public int getDesplazamientoColumna(int parte) {
        return forma.getColumna(parte);
    }

    /**
     * Obtiene orientacion.
     * @return Resultado de get orientacion.
     */
    public Orientacion getOrientacion() {
        if (forma == Forma.HORIZONTAL) return Orientacion.HORIZONTAL;
        if (forma == Forma.VERTICAL) return Orientacion.VERTICAL;
        return Orientacion.INDIVIDUAL;
    }

    /**
     * Comprueba si salida.
     * @param lado Lado utilizado en la operación.
     * @return Resultado de permite salida.
     */
    public boolean permiteSalida(LadoSalida lado) {
        return forma.permiteSalida(lado);
    }
}