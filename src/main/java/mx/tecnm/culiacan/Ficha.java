package mx.tecnm.culiacan;

public enum Ficha {
    ROJA('R'),
    NEGRA('N'),
    AZUL('Z'),
    AMARILLA('A'),
    VERDE('V'),
    NARANJA('O'),
    MORADA('M'),
    ROSA('P'),
    BLANCA('B'),
    GRIS('G'),
    VACIA(' ');

    private final char simbolo;

    Ficha(char simbolo) { this.simbolo = simbolo; }

    public char getSimbolo() { return simbolo; }
}