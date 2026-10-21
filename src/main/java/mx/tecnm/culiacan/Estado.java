
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

                if (id < 0 || id > 255) throw new IllegalArgumentException("Identificador de pieza fuera de rango");

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

    public int getNumeroFilas() { return numeroFilas; }

    public int getNumeroColumnas() { return numeroColumnas; }

    public int get(int fila, int columna) {
        return Byte.toUnsignedInt(celdas[indice(fila, columna)]);
    }

    public String getClave() { return clave; }

    public boolean estaResuelto() {
        boolean resuelto = true;
        for (byte celda : celdas)
            if (celda != 0) {
                resuelto = false;
                break;
            }
        return resuelto;
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

        Pieza pieza = paso.pieza();
        int fila = paso.fila();
        int columna = paso.columna();

        for (int parte = 0; parte < pieza.getTamano(); parte++) {
            int filas = fila + pieza.getDesplazamientoFila(parte);
            int columnas = columna + pieza.getDesplazamientoColumna(parte);

            copia[indice(filas, columnas)] = 0;
        }

        if (!paso.fichaSale()) {
            int nuevaFila = fila + paso.movimiento().getMovimientoFila();
            int nuevaColumna = columna + paso.movimiento().getMovimientoColumna();

            for (int parte = 0; parte < pieza.getTamano(); parte++) {
                int filas = nuevaFila + pieza.getDesplazamientoFila(parte);
                int columnas = nuevaColumna + pieza.getDesplazamientoColumna(parte);

                copia[indice(filas, columnas)] = (byte) pieza.id();
            }
        }

        return new Estado(copia, numeroFilas, numeroColumnas);
    }
}
