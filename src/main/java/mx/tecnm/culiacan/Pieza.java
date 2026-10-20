package mx.tecnm.culiacan;

public class Pieza {
    private final int id;
    private final Ficha color;
    private final Forma forma;

    public Pieza(int id, Ficha color, Orientacion orientacion) {
        this(id, color, convertirForma(orientacion));
    }

    public Pieza(int id, Ficha color, Forma forma) {
        this.id = id;
        this.color = color;
        this.forma = forma;
    }

    private static Forma convertirForma(Orientacion orientacion) {
        switch (orientacion) {
            case HORIZONTAL: return Forma.HORIZONTAL;
            case VERTICAL: return Forma.VERTICAL;
            default: return Forma.INDIVIDUAL;
        }
    }

    public int getId() { return id; }

    public Ficha getColor() { return color; }

    public Forma getForma() { return forma; }

    public int getTamano() { return forma.getTamano(); }

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