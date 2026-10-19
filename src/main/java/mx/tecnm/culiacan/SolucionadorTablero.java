package mx.tecnm.culiacan;

import java.util.*;

public class SolucionadorTablero {
    private final List<Salida> salidas;
    private final int numeroDeLados;

    public SolucionadorTablero(List<Salida> salidas, int numeroDeLados) {
        this.salidas = salidas;
        this.numeroDeLados = numeroDeLados;
    }

    public List<Paso> resolverTablero(Estado inicial) {
        PriorityQueue<Nodo> abiertos = new PriorityQueue<>(Comparator.comparingInt(Nodo::getCostoTotal));
        Map<String, Integer> mejorCosto = new HashMap<>();

        abiertos.add(new Nodo(inicial, null, null, 0, heuristica(inicial)));
        mejorCosto.put(inicial.getClave(), 0);

        while (!abiertos.isEmpty()) {
            Nodo actual = abiertos.poll();
            Estado estadoActual = actual.getEstado();

            if (actual.getCostoAcumulado() > mejorCosto.get(estadoActual.getClave())) continue;
            if (estadoActual.estaResuelto()) return actual.reconstruirCamino();

            for (Paso paso : sucesores(estadoActual)) {
                Estado siguiente = estadoActual.realizarMovimiento(paso);
                int costo = actual.getCostoAcumulado() + 1;
                Integer previo = mejorCosto.get(siguiente.getClave());

                if (previo == null || costo < previo) {
                    mejorCosto.put(siguiente.getClave(), costo);
                    abiertos.add(new Nodo(siguiente, actual, paso, costo, heuristica(siguiente)));
                }
            }
        }
        return null;
    }

    private List<Paso> sucesores(Estado estado) {
        List<Paso> pasos = new ArrayList<>();
        for (int fila = 0; fila < numeroDeLados; fila++) {
            for (int columna = 0; columna < numeroDeLados; columna++) {
                Ficha ficha = estado.get(fila, columna);
                if (ficha == Ficha.VACIA) continue;

                for (Movimiento movimiento : Movimiento.values()) {
                    int nuevaFila = fila + movimiento.getMovimientoFila();
                    int nuevaColumna = columna + movimiento.getMovimientoColumna();
                    boolean dentro = nuevaFila >= 0 && nuevaFila < numeroDeLados
                            && nuevaColumna >= 0 && nuevaColumna < numeroDeLados;

                    if (dentro) {
                        if (estado.get(nuevaFila, nuevaColumna) == Ficha.VACIA)
                            pasos.add(new Paso(fila, columna, movimiento, false, ficha));
                    } else if (puedeSalir(ficha, movimiento, fila, columna)) {
                        pasos.add(new Paso(fila, columna, movimiento, true, ficha));
                    }
                }
            }
        }
        return pasos;
    }

    private boolean puedeSalir(Ficha ficha, Movimiento movimiento, int fila, int columna) {
        LadoSalida lado = movimiento.getLadoSalida();
        int indice = (movimiento == Movimiento.ARRIBA || movimiento == Movimiento.ABAJO) ? columna : fila;
        for (Salida salida : salidas)
            if (salida.getLadoSalida() == lado
                    && salida.getIndice() == indice
                    && salida.getColorFicha() == ficha)
                return true;
        return false;
    }

    private int heuristica(Estado estado) {
        int total = 0;
        for (int fila = 0; fila < numeroDeLados; fila++) {
            for (int columna = 0; columna < numeroDeLados; columna++) {
                Ficha ficha = estado.get(fila, columna);
                if (ficha == Ficha.VACIA) continue;

                int mejor = Integer.MAX_VALUE;
                for (Salida salida : salidas) {
                    if (salida.getColorFicha() != ficha) continue;
                    int distancia = Math.abs(fila - salida.getFila(numeroDeLados))
                            + Math.abs(columna - salida.getColumna(numeroDeLados)) + 1;
                    mejor = Math.min(mejor, distancia);
                }
                if (mejor != Integer.MAX_VALUE) total += mejor;
            }
        }
        return total;
    }
}