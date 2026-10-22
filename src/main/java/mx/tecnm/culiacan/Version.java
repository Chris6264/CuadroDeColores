package mx.tecnm.culiacan;

/**
 * Representa las modalidades disponibles del juego.
 */
public enum Version {
    BASICA(2, 2),
    INTERMEDIA(3, 7),
    AVANZADA(5, 14);

    private final int minimoNumeroDeLados;
    private final int maximoNumeroDeLados;

    /**
     * Inicializa los valores de Version.
     * @param minimoNumeroDeLados Valor utilizado para inicializar la instancia.
     * @param maximoNumeroDeLados Valor utilizado para inicializar la instancia.
     */
    Version(int minimoNumeroDeLados, int maximoNumeroDeLados) {
        this.minimoNumeroDeLados = minimoNumeroDeLados;
        this.maximoNumeroDeLados = maximoNumeroDeLados;
    }

    /**
     * Crea reglas.
     * @return Resultado de crear reglas.
     */
    public Reglas crearReglas() {
        return new ReglasCuadro(minimoNumeroDeLados, maximoNumeroDeLados);
    }
}