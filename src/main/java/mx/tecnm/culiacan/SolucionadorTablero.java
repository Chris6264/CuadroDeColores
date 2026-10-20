package mx.tecnm.culiacan;

import java.util.*;

public class SolucionadorTablero {
    private final List<Salida> salidas;
    private final List<Pieza> piezas;
    private final int numeroDeLados;
    private final double peso;

    public SolucionadorTablero(List<Salida> salidas, int numeroDeLados, List<Pieza> piezas) {
        this.salidas = salidas;
        this.numeroDeLados = numeroDeLados;
        this.piezas = piezas;
        this.peso = numeroDeLados <= 5 ? 1.0 : 1.5;
    }

    public List<Paso> resolverTablero(Estado inicial) {
        PriorityQueue<Nodo> abiertos = new PriorityQueue<>(Comparator.comparingDouble(Nodo::getCostoTotal));
        Map<String, Integer> mejorCosto = new HashMap<>();

        abiertos.add(new Nodo(inicial, null, null, 0, heuristica(inicial), peso));
        mejorCosto.put(inicial.getClave(), 0);

        while (!abiertos.isEmpty()) {
            Nodo actual = abiertos.poll();
            Estado estadoActual = actual.getEstado();

            if (actual.getCostoAcumulado() > mejorCosto.getOrDefault(estadoActual.getClave(), Integer.MAX_VALUE)) continue;
            if (estadoActual.estaResuelto()) return actual.reconstruirCamino();

            for (Paso paso : sucesores(estadoActual)) {
                Estado siguiente = estadoActual.realizarMovimiento(paso);
                int costo = actual.getCostoAcumulado() + 1;
                Integer previo = mejorCosto.get(siguiente.getClave());

                if (previo == null || costo < previo) {
                    mejorCosto.put(siguiente.getClave(), costo);
                    abiertos.add(new Nodo(siguiente, actual, paso, costo, heuristica(siguiente), peso));
                }
            }
        }

        return null;
    }

    private List<Paso> sucesores(Estado estado) {
        List<Paso> pasos = new ArrayList<>();

        for (Pieza pieza : piezas) {
            int[] origen = estado.buscarOrigen(pieza.getId());
            if (origen == null) continue;

            int fila = origen[0];
            int columna = origen[1];

            for (Movimiento movimiento : Movimiento.values()) {
                int df = movimiento.getMovimientoFila();
                int dc = movimiento.getMovimientoColumna();
                boolean todosDentro = true;
                boolean libre = true;

                for (int parte = 0; parte < pieza.getTamano(); parte++) {
                    int nf = fila + pieza.getDesplazamientoFila(parte) + df;
                    int nc = columna + pieza.getDesplazamientoColumna(parte) + dc;

                    if (nf < 0 || nc < 0 || nf >= numeroDeLados || nc >= numeroDeLados) {
                        todosDentro = false;
                        continue;
                    }

                    int ocupante = estado.get(nf, nc);
                    if (ocupante != 0 && ocupante != pieza.getId()) {
                        libre = false;
                        break;
                    }
                }

                if (todosDentro && libre) {
                    pasos.add(new Paso(fila, columna, movimiento, false, pieza));
                } else if (!todosDentro && libre && puedeSalirCompleta(pieza, movimiento, fila, columna)) {
                    pasos.add(new Paso(fila, columna, movimiento, true, pieza));
                }
            }
        }

        return pasos;
    }

    private boolean puedeSalirCompleta(Pieza pieza, Movimiento movimiento, int fila, int columna) {
        LadoSalida lado = movimiento.getLadoSalida();

        if (pieza.getTamano() == 2) {
            if (pieza.getOrientacion() == Orientacion.VERTICAL
                    && lado != LadoSalida.ARRIBA
                    && lado != LadoSalida.ABAJO) return false;

            if (pieza.getOrientacion() == Orientacion.HORIZONTAL
                    && lado != LadoSalida.IZQUIERDA
                    && lado != LadoSalida.DERECHA) return false;
        }

        for (Salida salida : salidas) {
            if (salida.getLadoSalida() != lado || salida.getColorFicha() != pieza.getColor()) continue;

            if (pieza.getTamano() == 1) {
                int f = fila + movimiento.getMovimientoFila();
                int c = columna + movimiento.getMovimientoColumna();

                if (f < 0 || c < 0 || f >= numeroDeLados || c >= numeroDeLados)
                    return fila == salida.getFila(numeroDeLados) && columna == salida.getColumna(numeroDeLados);
            } else if (pieza.getOrientacion() == Orientacion.VERTICAL) {
                int columnaPieza = columna;

                if (salida.getIndice() != columnaPieza) continue;

                if (lado == LadoSalida.ARRIBA && fila == 0) return true;
                if (lado == LadoSalida.ABAJO && fila + 1 == numeroDeLados - 1) return true;
            } else if (pieza.getOrientacion() == Orientacion.HORIZONTAL) {
                int filaPieza = fila;

                if (salida.getIndice() != filaPieza) continue;

                if (lado == LadoSalida.IZQUIERDA && columna == 0) return true;
                if (lado == LadoSalida.DERECHA && columna + 1 == numeroDeLados - 1) return true;
            }
        }

        return false;
    }

    private int heuristica(Estado estado) {
        int total = 0;

        for (Pieza pieza : piezas) {
            int[] origen = estado.buscarOrigen(pieza.getId());
            if (origen == null) continue;

            int mejor = Integer.MAX_VALUE;

            for (Salida salida : salidas) {
                if (salida.getColorFicha() != pieza.getColor()) continue;

                for (int parte = 0; parte < pieza.getTamano(); parte++) {
                    int fila = origen[0] + pieza.getDesplazamientoFila(parte);
                    int columna = origen[1] + pieza.getDesplazamientoColumna(parte);

                    int distancia = Math.abs(fila - salida.getFila(numeroDeLados))
                            + Math.abs(columna - salida.getColumna(numeroDeLados)) + 1;

                    mejor = Math.min(mejor, distancia);
                }
            }

            if (mejor == Integer.MAX_VALUE) throw new IllegalStateException("Pieza sin salida compatible");

            total += mejor;
        }

        return total;
    }
}