package mx.tecnm.culiacan;

public enum Version {
    BASICA(2,2),
    INTERMEDIA(3, 7),
    AVANZADA(5, 14);

    private final int minimoNumeroDeLados;
    private final int maximoNumeroDeLados;

    Version(int minimoNumeroDeLados,int maximoNumeroDeLados) {
        this.minimoNumeroDeLados = minimoNumeroDeLados;
        this.maximoNumeroDeLados = maximoNumeroDeLados;
    }

    public Reglas crearReglas(){
        return new ReglasCuadro(minimoNumeroDeLados, maximoNumeroDeLados);
    }
}
