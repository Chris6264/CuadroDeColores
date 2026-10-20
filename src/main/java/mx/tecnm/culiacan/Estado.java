
package mx.tecnm.culiacan;

import java.nio.charset.StandardCharsets;

public class Estado {
    private final byte[] celdas;
    private final int numeroFilas;
    private final int numeroColumnas;
    private final String clave;

    public Estado(int[][] origen) {
        numeroFilas = origen.length;
        numeroColumnas = origen[0].length;
        celdas = new byte[numeroFilas * numeroColumnas];

        for (int fila = 0; fila < numeroFilas; fila++) {
            for (int columna = 0; columna < numeroColumnas; columna++) {
                int id = origen[fila][columna];

                if (id < 0 || id > 255)
                    throw new IllegalArgumentException("Identificador de pieza fuera de rango");

                celdas[indice(fila, columna)] = (byte) id;
            }
        }

        clave = new String(celdas, StandardCharsets.ISO_8859_1);
    }

    private Estado(byte[] celdas, int numeroFilas, int numeroColumnas) {
        this.celdas = celdas;
        this.numeroFilas = numeroFilas;
        this.numeroColumnas = numeroColumnas;
        this.clave = new String(celdas, StandardCharsets.ISO_8859_1);
    }

    private int indice(int fila, int columna) {
        return fila * numeroColumnas + columna;
    }

    public int getTamano() { return numeroFilas; }

    public int getNumeroFilas() { return numeroFilas; }

    public int getNumeroColumnas() { return numeroColumnas; }

    public int get(int fila, int columna) {
        return Byte.toUnsignedInt(celdas[indice(fila, columna)]);
    }

    public String getClave() { return clave; }

    public boolean estaResuelto() {
        for (byte celda : celdas)
            if (celda != 0) return false;

        return true;
    }

    public int[] buscarOrigen(int id) {
        int minimoFila = Integer.MAX_VALUE;
        int minimoColumna = Integer.MAX_VALUE;

        for (int fila = 0; fila < numeroFilas; fila++) {
            for (int columna = 0; columna < numeroColumnas; columna++) {
                if (get(fila, columna) != id) continue;

                minimoFila = Math.min(minimoFila, fila);
                minimoColumna = Math.min(minimoColumna, columna);
            }
        }

        if (minimoFila == Integer.MAX_VALUE) return null;

        return new int[]{minimoFila, minimoColumna};
    }

    public Estado realizarMovimiento(Paso paso) {
        byte[] copia = celdas.clone();

        Pieza pieza = paso.getPieza();
        int fila = paso.getFila();
        int columna = paso.getColumna();

        for (int parte = 0; parte < pieza.getTamano(); parte++) {
            int f = fila + pieza.getDesplazamientoFila(parte);
            int c = columna + pieza.getDesplazamientoColumna(parte);

            copia[indice(f, c)] = 0;
        }

        if (!paso.fichaSale()) {
            int nuevaFila = fila + paso.getMovimiento().getMovimientoFila();
            int nuevaColumna = columna + paso.getMovimiento().getMovimientoColumna();

            for (int parte = 0; parte < pieza.getTamano(); parte++) {
                int f = nuevaFila + pieza.getDesplazamientoFila(parte);
                int c = nuevaColumna + pieza.getDesplazamientoColumna(parte);

                copia[indice(f, c)] = (byte) pieza.getId();
            }
        }

        return new Estado(copia, numeroFilas, numeroColumnas);
    }
}
