package mx.tecnm.culiacan;

public class Salida {
    private final LadoSalida ladoSalida;
    private final int indice;
    private final Ficha colorFicha;
    private final int tamano;

    public Salida(LadoSalida ladoSalida, int indice, Ficha colorFicha) {
        this(ladoSalida, indice, colorFicha, 1);
    }

    public Salida(LadoSalida ladoSalida, int indice, Ficha colorFicha, int tamano) {
        if (tamano < 1 || tamano > 2)
            throw new IllegalArgumentException("Tamano de salida incorrecto");

        this.ladoSalida = ladoSalida;
        this.indice = indice;
        this.colorFicha = colorFicha;
        this.tamano = tamano;
    }

    public LadoSalida getLadoSalida() { return ladoSalida; }

    public int getIndice() { return indice; }

    public Ficha getColorFicha() { return colorFicha; }

    public int getTamano() { return tamano; }

    public boolean ocupaPosicion(LadoSalida lado, int posicion) {
        return ladoSalida == lado
                && posicion >= indice
                && posicion < indice + tamano;
    }

    public int getFila(int numeroFilas) {
        switch (ladoSalida) {
            case ARRIBA: return 0;
            case ABAJO: return numeroFilas - 1;
            default: return indice;
        }
    }

    public int getColumna(int numeroColumnas) {
        switch (ladoSalida) {
            case IZQUIERDA: return 0;
            case DERECHA: return numeroColumnas - 1;
            default: return indice;
        }
    }
}