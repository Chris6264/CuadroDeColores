package mx.tecnm.culiacan;

public class Pieza {
    private final int id;
    private final Ficha color;
    private final Orientacion orientacion;
    private final int tamano;

    public Pieza(int id, Ficha color, Orientacion orientacion) {
        this.id = id;
        this.color = color;
        this.orientacion = orientacion;
        this.tamano = orientacion == Orientacion.INDIVIDUAL ? 1 : 2;
    }

    public int getId() { return id; }

    public Ficha getColor() { return color; }

    public Orientacion getOrientacion() { return orientacion; }

    public int getTamano() { return tamano; }

    public int getDesplazamientoFila(int parte) {
        return orientacion == Orientacion.VERTICAL ? parte : 0;
    }

    public int getDesplazamientoColumna(int parte) {
        return orientacion == Orientacion.HORIZONTAL ? parte : 0;
    }
}