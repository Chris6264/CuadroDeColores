package mx.tecnm.culiacan;

public record Salida(LadoSalida ladoSalida, int indice, Ficha colorFicha, int tamano) {
    public Salida(LadoSalida ladoSalida, int indice, Ficha colorFicha) {
        this(ladoSalida, indice, colorFicha, 1);
    }

    public Salida {
        if (tamano < 1 || tamano > 2) throw new IllegalArgumentException("Tamano de salida incorrecto");
    }

    public boolean ocupaPosicion(LadoSalida lado, int posicion) {
        return ladoSalida == lado
                && posicion >= indice
                && posicion < indice + tamano;
    }

    public int getFila(int numeroFilas) {
        return switch (ladoSalida) {
            case ARRIBA -> 0;
            case ABAJO -> numeroFilas - 1;
            default -> indice;
        };
    }

    public int getColumna(int numeroColumnas) {
        return switch (ladoSalida) {
            case IZQUIERDA -> 0;
            case DERECHA -> numeroColumnas - 1;
            default -> indice;
        };
    }
}