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
    CUADRADO(new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}});

    private final int[][] posiciones;

    Forma(int[][] posiciones) {
        this.posiciones = posiciones;
    }

    public int getTamano() { return posiciones.length; }

    public int getFila(int parte) { return posiciones[parte][0]; }

    public int getColumna(int parte) { return posiciones[parte][1]; }

    public boolean esL() {
        return this == L1 || this == L2 || this == L3 || this == L4;
    }

    public int[] getIndicesCara(LadoSalida lado) {
        int extremo = lado == LadoSalida.ARRIBA || lado == LadoSalida.IZQUIERDA
                ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        for (int[] posicion : posiciones) {
            int valor = lado == LadoSalida.ARRIBA || lado == LadoSalida.ABAJO
                    ? posicion[0] : posicion[1];

            if (lado == LadoSalida.ARRIBA || lado == LadoSalida.IZQUIERDA)
                extremo = Math.min(extremo, valor);
            else extremo = Math.max(extremo, valor);
        }

        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < posiciones.length; i++) {
            int valor = lado == LadoSalida.ARRIBA || lado == LadoSalida.ABAJO
                    ? posiciones[i][0] : posiciones[i][1];

            if (valor == extremo) indices.add(i);
        }

        return indices.stream().mapToInt(Integer::intValue).toArray();
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