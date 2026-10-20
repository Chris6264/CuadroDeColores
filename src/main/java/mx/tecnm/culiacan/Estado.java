package mx.tecnm.culiacan;

public class Estado {
    private final int[][] celdas;
    private final String clave;

    public Estado(int[][] origen) {
        celdas = new int[origen.length][];
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < origen.length; i++) {
            celdas[i] = origen[i].clone();
            for (int id : celdas[i]) sb.append(id).append(',');
        }

        clave = sb.toString();
    }

    public int getTamano() { return celdas.length; }

    public int getNumeroFilas() { return celdas.length; }

    public int getNumeroColumnas() { return celdas[0].length; }

    public int get(int fila, int columna) {
        return celdas[fila][columna];
    }

    public String getClave() { return clave; }

    public boolean estaResuelto() {
        for (int[] fila : celdas)
            for (int id : fila)
                if (id != 0) return false;
        return true;
    }

    public int[] buscarOrigen(int id) {
        int minimoFila = Integer.MAX_VALUE;
        int minimoColumna = Integer.MAX_VALUE;

        for (int fila = 0; fila < getNumeroFilas(); fila++) {
            for (int columna = 0; columna < getNumeroColumnas(); columna++) {
                if (celdas[fila][columna] != id) continue;

                minimoFila = Math.min(minimoFila, fila);
                minimoColumna = Math.min(minimoColumna, columna);
            }
        }

        if (minimoFila == Integer.MAX_VALUE) return null;

        return new int[]{minimoFila, minimoColumna};
    }

    public Estado realizarMovimiento(Paso paso) {
        int[][] copia = new int[celdas.length][];

        for (int i = 0; i < celdas.length; i++)
            copia[i] = celdas[i].clone();

        Pieza pieza = paso.getPieza();
        int fila = paso.getFila();
        int columna = paso.getColumna();

        for (int parte = 0; parte < pieza.getTamano(); parte++) {
            int f = fila + pieza.getDesplazamientoFila(parte);
            int c = columna + pieza.getDesplazamientoColumna(parte);
            copia[f][c] = 0;
        }

        if (!paso.fichaSale()) {
            int nf = fila + paso.getMovimiento().getMovimientoFila();
            int nc = columna + paso.getMovimiento().getMovimientoColumna();

            for (int parte = 0; parte < pieza.getTamano(); parte++) {
                int f = nf + pieza.getDesplazamientoFila(parte);
                int c = nc + pieza.getDesplazamientoColumna(parte);
                copia[f][c] = pieza.getId();
            }
        }

        return new Estado(copia);
    }
}