package mx.tecnm.culiacan;

public class Salida {
    private final LadoSalida ladoSalida;
    private final int indice;
    private final Ficha colorFicha;


    public Salida(LadoSalida ladoSalida, int indice, Ficha colorFicha) {
        this.ladoSalida = ladoSalida;
        this.indice = indice;
        this.colorFicha = colorFicha;
    }

    public LadoSalida getLadoSalida() {
        return ladoSalida;
    }

    public int getIndice() {
        return indice;
    }

    public Ficha getColorFicha() {
        return colorFicha;
    }

    public int getFila(int n){
        switch (ladoSalida) {
            case ARRIBA: return 0;
            case ABAJO: return n - 1;
            default: return indice;
        }
    }

    public int getColumna(int n){
        switch (ladoSalida){
            case IZQUIERDA: return 0;
            case DERECHA: return n - 1;
            default: return indice;
        }
    }
}
