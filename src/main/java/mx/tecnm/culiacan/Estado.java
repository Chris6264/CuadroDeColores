package mx.tecnm.culiacan;

public class Estado {
    private final Ficha[][] celdas;
    private final String clave;

    public Estado(Ficha[][] origin){
        int numeroDeLados = origin.length;
        celdas = new Ficha[numeroDeLados][];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < numeroDeLados; i++){
            celdas[i] = origin[i].clone();
            for (Ficha ficha : celdas[i]) stringBuilder.append(ficha.getSimbolo());
        }
        clave = stringBuilder.toString();
    }

    public int getTamano() { return celdas.length; }
    public Ficha get(int fila, int columna) { return celdas[fila][columna]; }
    public String getClave() { return clave; }

    public boolean estaResuelto(){
        boolean resuelto = true;
        for (Ficha[] fila : celdas)
            for (Ficha ficha : fila)
                if(ficha != Ficha.VACIA) resuelto = false;
        return resuelto;
    }

    public Estado realizarMovimiento(Paso paso){
        Ficha[][] copia = new Ficha[celdas.length][];
        for (int i = 0; i < celdas.length; i++) copia[i] = celdas[i].clone();

        int f = paso.getFila(), c = paso.getColumna();
        Ficha ficha = copia[f][c];
        copia[f][c] = Ficha.VACIA;
        if (!paso.fichaSale()) {
            copia[f + paso.getMovimiento().getMovimientoFila()]
                    [c + paso.getMovimiento().getMovimientoColumna()] = ficha;
        }
        return new Estado(copia);
    }
}
