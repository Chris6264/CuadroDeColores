package mx.tecnm.culiacan;

public record Pieza(int id, Ficha color, Forma forma) {
    public Pieza(int id, Ficha color, Orientacion orientacion) {
        this(id, color, convertirForma(orientacion));
    }

    private static Forma convertirForma(Orientacion orientacion) {
        return switch (orientacion) {
            case HORIZONTAL -> Forma.HORIZONTAL;
            case VERTICAL -> Forma.VERTICAL;
            default -> Forma.INDIVIDUAL;
        };
    }

    public int getTamano() {
        return forma.getTamano();
    }

    public int getDesplazamientoFila(int parte) {
        return forma.getFila(parte);
    }

    public int getDesplazamientoColumna(int parte) {
        return forma.getColumna(parte);
    }

    public Orientacion getOrientacion() {
        if (forma == Forma.HORIZONTAL) return Orientacion.HORIZONTAL;
        if (forma == Forma.VERTICAL) return Orientacion.VERTICAL;
        return Orientacion.INDIVIDUAL;
    }

    public boolean permiteSalida(LadoSalida lado) {
        return forma.permiteSalida(lado);
    }
}