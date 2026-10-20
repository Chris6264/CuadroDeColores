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
        for (int i = 0; i < celdas.length; i++)
            for (int j = 0; j < celdas[i].length; j++)
                if (celdas[i][j] == id) return new int[]{i, j};
        return null;
    }

    public Estado realizarMovimiento(Paso paso) {
        int[][] copia = new int[celdas.length][];

        for (int i = 0; i < celdas.length; i++)
            copia[i] = celdas[i].clone();

        Pieza pieza = paso.getPieza();
        int f = paso.getFila();
        int c = paso.getColumna();

        for (int parte = 0; parte < pieza.getTamano(); parte++) {
            int fila = f + pieza.getDesplazamientoFila(parte);
            int columna = c + pieza.getDesplazamientoColumna(parte);
            copia[fila][columna] = 0;
        }

        if (!paso.fichaSale()) {
            int nf = f + paso.getMovimiento().getMovimientoFila();
            int nc = c + paso.getMovimiento().getMovimientoColumna();

            for (int parte = 0; parte < pieza.getTamano(); parte++) {
                int fila = nf + pieza.getDesplazamientoFila(parte);
                int columna = nc + pieza.getDesplazamientoColumna(parte);
                copia[fila][columna] = pieza.getId();
            }
        }

        return new Estado(copia);
    }
}