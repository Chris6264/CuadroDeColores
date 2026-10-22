
package mx.tecnm.culiacan;

import java.nio.charset.StandardCharsets;

/**
 * Representa una configuración del tablero durante la búsqueda.
 */
public class Estado {
    private final byte[] celdas;
    private final int numeroFilas;
    private final int numeroColumnas;
    private final String clave;

    /**
     * Construye una instancia de Estado con los parámetros indicados.
     * @param origen Origen utilizado en la operación.
     * @param reglas Reglas utilizado en la operación.
     */
    public Estado(int[][] origen, Reglas reglas) {
        numeroFilas = origen.length;
        numeroColumnas = origen[0].length;
        celdas = new byte[numeroFilas * numeroColumnas];

        for (int fila = 0; fila < numeroFilas; fila++) {
            for (int columna = 0; columna < numeroColumnas; columna++) {
                int id = origen[fila][columna];

                reglas.validarIdentificadorPieza(id);

                celdas[indice(fila, columna)] = (byte) id;
            }
        }

        clave = new String(celdas, StandardCharsets.ISO_8859_1);
    }

    /**
     * Construye una instancia de Estado con los parámetros indicados.
     * @param celdas Celdas utilizado en la operación.
     * @param numeroFilas Numero filas utilizado en la operación.
     * @param numeroColumnas Numero columnas utilizado en la operación.
     */
    private Estado(byte[] celdas, int numeroFilas, int numeroColumnas) {
        this.celdas = celdas;
        this.numeroFilas = numeroFilas;
        this.numeroColumnas = numeroColumnas;
        this.clave = new String(celdas, StandardCharsets.ISO_8859_1);
    }

    /**
     * Implementa la operación indice.
     * @param fila Fila utilizado en la operación.
     * @param columna Columna utilizado en la operación.
     * @return Resultado de indice.
     */
    private int indice(int fila, int columna) {
        return fila * numeroColumnas + columna;
    }

    /**
     * Obtiene numero filas.
     * @return Resultado de get numero filas.
     */
    public int getNumeroFilas() { return numeroFilas; }

    /**
     * Obtiene numero columnas.
     * @return Resultado de get numero columnas.
     */
    public int getNumeroColumnas() { return numeroColumnas; }

    /**
     * Obtiene la operación correspondiente.
     * @param fila Fila utilizado en la operación.
     * @param columna Columna utilizado en la operación.
     * @return Resultado de get.
     */
    public int get(int fila, int columna) {
        return Byte.toUnsignedInt(celdas[indice(fila, columna)]);
    }

    /**
     * Obtiene clave.
     * @return Resultado de get clave.
     */
    public String getClave() { return clave; }

    /**
     * Comprueba si resuelto.
     * @return Resultado de esta resuelto.
     */
    public boolean estaResuelto() {
        boolean resuelto = true;
        for (byte celda : celdas)
            if (celda != 0) {
                resuelto = false;
                break;
            }
        return resuelto;
    }

    /**
     * Busca origen.
     * @param id Id utilizado en la operación.
     * @return Resultado de buscar origen.
     */
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

    /**
     * Realiza movimiento.
     * @param paso Paso utilizado en la operación.
     */
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
