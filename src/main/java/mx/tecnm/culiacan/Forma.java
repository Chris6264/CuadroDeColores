package mx.tecnm.culiacan;

import java.util.ArrayList;
import java.util.List;

public enum Forma {
    INDIVIDUAL(new int[][]{{0, 0}}),
    HORIZONTAL(new int[][]{{0, 0}, {0, 1}}),
    VERTICAL(new int[][]{{0, 0}, {1, 0}}),
    L1(new int[][]{{0, 0}, {1, 0}, {1, 1}}),
    L2(new int[][]{{0, 0}, {0, 1}, {1, 0}}),
    L3(new int[][]{{0, 0}, {0, 1}, {1, 1}}),
    L4(new int[][]{{0, 1}, {1, 0}, {1, 1}}),
    LARGA1(new int[][]{{0, 0}, {1, 0}, {2, 0}, {2, 1}}),
    LARGA2(new int[][]{{0, 0}, {0, 1}, {0, 2}, {1, 0}}),
    LARGA3(new int[][]{{0, 0}, {0, 1}, {1, 1}, {2, 1}}),
    LARGA4(new int[][]{{0, 2}, {1, 0}, {1, 1}, {1, 2}}),
    CUADRADO(new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}});

    private final int[][] posiciones;
    private final int[][] caras;

    Forma(int[][] posiciones) {
        this.posiciones = posiciones;
        this.caras = new int[LadoSalida.values().length][];

        for (LadoSalida lado : LadoSalida.values())
            caras[lado.ordinal()] = calcularIndicesCara(lado);
    }

    public int getTamano() { return posiciones.length; }

    public int getFila(int parte) { return posiciones[parte][0]; }

    public int getColumna(int parte) { return posiciones[parte][1]; }

    public boolean esL() {
        return esLCorta() || esLLarga();
    }

    public boolean esLCorta() {
        return this == L1 || this == L2 || this == L3 || this == L4;
    }

    public boolean esLLarga() {
        return this == LARGA1 || this == LARGA2 || this == LARGA3 || this == LARGA4;
    }

    public static Forma[] getLsCortas() {
        return new Forma[]{L1, L2, L3, L4};
    }

    public static Forma[] getLsLargas() {
        return new Forma[]{LARGA1, LARGA2, LARGA3, LARGA4};
    }

    public int getTamanoSalida(LadoSalida lado) {
        return caras[lado.ordinal()].length;
    }

    private int[] calcularIndicesCara(LadoSalida lado) {
        boolean minimo = lado == LadoSalida.ARRIBA || lado == LadoSalida.IZQUIERDA;
        boolean horizontal = lado == LadoSalida.ARRIBA || lado == LadoSalida.ABAJO;

        int extremo = minimo ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        for (int[] posicion : posiciones) {
            int valor = horizontal ? posicion[0] : posicion[1];

            if (minimo) extremo = Math.min(extremo, valor);
            else extremo = Math.max(extremo, valor);
        }

        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < posiciones.length; i++) {
            int valor = horizontal ? posiciones[i][0] : posiciones[i][1];

            if (valor == extremo) indices.add(i);
        }

        return indices.stream().mapToInt(Integer::intValue).toArray();
    }

    public int[] getIndicesCara(LadoSalida lado) {
        return caras[lado.ordinal()];
    }

    public boolean permiteSalida(LadoSalida lado) {
        if (this == HORIZONTAL)
            return lado == LadoSalida.IZQUIERDA || lado == LadoSalida.DERECHA;

        if (this == VERTICAL)
            return lado == LadoSalida.ARRIBA || lado == LadoSalida.ABAJO;

        if (getTamano() <= 2) return true;

        return getIndicesCara(lado).length == 2;
    }
}