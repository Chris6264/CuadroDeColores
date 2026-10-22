package mx.tecnm.culiacan;

/**
 * Define los identificadores o colores de las fichas del juego.
 */
public enum Ficha {
    ROJA('R', "\u001B[41m"),
    NEGRA('N', "\u001B[40m"),
    AZUL('Z', "\u001B[44m"),
    AMARILLA('A', "\u001B[43m"),
    VERDE('V', "\u001B[42m"),
    NARANJA('O', "\u001B[48;5;208m"),
    MORADA('M', "\u001B[45m"),
    ROSA('P', "\u001B[48;5;213m"),
    MARRON('C', "\u001B[48;5;94m"),
    GRIS('G', "\u001B[100m"),
    VACIA(' ', "\u001B[0m");

    private final char simbolo;
    private final String colorANSI;

    /**
     * Inicializa los valores de Ficha.
     * @param simbolo Valor utilizado para inicializar la instancia.
     * @param colorANSI Valor utilizado para inicializar la instancia.
     */
    Ficha(char simbolo, String colorANSI) {
        this.simbolo = simbolo;
        this.colorANSI = colorANSI;
    }

    /**
     * Obtiene simbolo.
     * @return Resultado de get simbolo.
     */
    public char getSimbolo() { return simbolo; }

    /**
     * Obtiene color ansi.
     * @return Resultado de get color ansi.
     */
    public String getColorANSI() { return colorANSI; }
}