package mx.tecnm.culiacan;

import java.util.*;

public class SolucionadorTablero {
    private final List<Salida> salidas;
    private final List<Pieza> piezas;
    private final int numeroFilas;
    private final int numeroColumnas;
    private final double peso;
    private final int maximoNodos;
    private final int maximoEstados;

    private int nodosExplorados;
    private int nodosGenerados;
    private ResultadoBusqueda resultadoBusqueda;

    public SolucionadorTablero(List<Salida> salidas, int numeroDeLados, List<Pieza> piezas) {
        this(salidas, numeroDeLados, numeroDeLados, piezas);
    }

    public SolucionadorTablero(List<Salida> salidas, int numeroFilas, int numeroColumnas, List<Pieza> piezas) {
        this.salidas = salidas;
        this.piezas = piezas;
        this.numeroFilas = numeroFilas;
        this.numeroColumnas = numeroColumnas;

        int casillas = numeroFilas * numeroColumnas;

        this.peso = casillas <= 25 ? 1.0 : 1.5;
        this.maximoNodos = casillas <= 25 ? 100000 : casillas <= 49 ? 150000 : 300000;
        this.maximoEstados = casillas <= 25 ? 150000 : casillas <= 49 ? 200000 : 500000;
    }

    public int getNodosExplorados() { return nodosExplorados; }

    public int getNodosGenerados() { return nodosGenerados; }

    public ResultadoBusqueda getResultadoBusqueda() { return resultadoBusqueda; }

    public List<Paso> resolverTablero(Estado inicial) {
        PriorityQueue<Nodo> abiertos = new PriorityQueue<>(
                Comparator.comparingDouble(Nodo::getCostoTotal)
                        .thenComparingInt(Nodo::getCostoAcumulado)
        );

        Map<String, Integer> mejorCosto = new HashMap<>();

        nodosExplorados = 0;
        nodosGenerados = 1;
        resultadoBusqueda = null;

        abiertos.add(new Nodo(inicial, null, null, 0, heuristica(inicial), peso));
        mejorCosto.put(inicial.getClave(), 0);

        while (!abiertos.isEmpty()) {
            Nodo actual = abiertos.poll();
            Estado estado = actual.getEstado();

            if (actual.getCostoAcumulado() >
                    mejorCosto.getOrDefault(estado.getClave(), Integer.MAX_VALUE)) continue;

            if (estado.estaResuelto()) {
                resultadoBusqueda = ResultadoBusqueda.SOLUCION_ENCONTRADA;
                return actual.reconstruirCamino();
            }

            if (nodosExplorados >= maximoNodos) {
                resultadoBusqueda = ResultadoBusqueda.LIMITE_ALCANZADO;
                return null;
            }

            nodosExplorados++;

            for (Paso paso : sucesores(estado)) {
                Estado siguiente = estado.realizarMovimiento(paso);
                int costo = actual.getCostoAcumulado() + 1;
                Integer anterior = mejorCosto.get(siguiente.getClave());

                if (anterior != null && costo >= anterior) continue;

                if (anterior == null && mejorCosto.size() >= maximoEstados) {
                    resultadoBusqueda = ResultadoBusqueda.LIMITE_ALCANZADO;
                    return null;
                }

                mejorCosto.put(siguiente.getClave(), costo);

                abiertos.add(new Nodo(
                        siguiente,
                        actual,
                        paso,
                        costo,
                        heuristica(siguiente),
                        peso
                ));

                nodosGenerados++;
            }
        }

        resultadoBusqueda = ResultadoBusqueda.SIN_SOLUCION;
        return null;
    }

    private List<Paso> sucesores(Estado estado) {
        List<Paso> pasos = new ArrayList<>();

        for (Pieza pieza : piezas) {
            int[] origen = estado.buscarOrigen(pieza.id());
            if (origen == null) continue;

            int fila = origen[0];
            int columna = origen[1];

            for (Movimiento movimiento : Movimiento.values()) {
                int df = movimiento.getMovimientoFila();
                int dc = movimiento.getMovimientoColumna();

                boolean dentro = true;
                boolean libre = true;

                for (int parte = 0; parte < pieza.getTamano(); parte++) {
                    int nf = fila + pieza.getDesplazamientoFila(parte) + df;
                    int nc = columna + pieza.getDesplazamientoColumna(parte) + dc;

                    if (nf < 0 || nc < 0 || nf >= numeroFilas || nc >= numeroColumnas) {
                        dentro = false;
                        continue;
                    }

                    int ocupante = estado.get(nf, nc);

                    if (ocupante != 0 && ocupante != pieza.id()) {
                        libre = false;
                        break;
                    }
                }

                if (!libre) continue;

                if (dentro)
                    pasos.add(new Paso(fila, columna, movimiento, false, pieza));
                else if (puedeSalirCompleta(pieza, movimiento, fila, columna))
                    pasos.add(new Paso(fila, columna, movimiento, true, pieza));
            }
        }

        return pasos;
    }

    private boolean puedeSalirCompleta(Pieza pieza, Movimiento movimiento, int fila, int columna) {
        LadoSalida lado = movimiento.getLadoSalida();

        if (!pieza.permiteSalida(lado)) return false;

        int[] cara = pieza.forma().getIndicesCara(lado);
        int tamanoSalida = cara.length;

        int extremo = Integer.MIN_VALUE;
        int minimo = Integer.MAX_VALUE;
        int maximo = Integer.MIN_VALUE;

        for (int parte : cara) {
            int filas = fila + pieza.getDesplazamientoFila(parte);
            int columas = columna + pieza.getDesplazamientoColumna(parte);

            int posicion = lado == LadoSalida.ARRIBA || lado == LadoSalida.ABAJO ? columas : filas;

            minimo = Math.min(minimo, posicion);
            maximo = Math.max(maximo, posicion);

            extremo = switch (lado) {
                case ARRIBA, ABAJO -> filas;
                case IZQUIERDA, DERECHA -> columas;
            };
        }

        boolean tocaBorde = switch (lado) {
            case ARRIBA, IZQUIERDA -> extremo == 0;
            case ABAJO -> extremo == numeroFilas - 1;
            default -> extremo == numeroColumnas - 1;
        };

        if (!tocaBorde) return false;

        for (Salida salida : salidas) {
            if (salida.colorFicha() != pieza.color()) continue;
            if (salida.ladoSalida() != lado) continue;
            if (salida.tamano() != tamanoSalida) continue;

            if (maximo != minimo + tamanoSalida - 1) continue;

            if (minimo == salida.indice())
                return true;
        }

        return false;
    }

    private int heuristica(Estado estado) {
        int total = 0;

        for (Pieza pieza : piezas) {
            int[] origen = estado.buscarOrigen(pieza.id());
            if (origen == null) continue;

            int mejor = Integer.MAX_VALUE;

            for (Salida salida : salidas) {
                LadoSalida lado = salida.ladoSalida();

                if (salida.colorFicha() != pieza.color()) continue;
                if (!pieza.permiteSalida(lado)) continue;
                if (salida.tamano() != pieza.forma().getTamanoSalida(lado)) continue;

                int[] cara = pieza.forma().getIndicesCara(lado);

                int minimo = Integer.MAX_VALUE;
                int extremo;

                for (int parte : cara) {
                    int f = origen[0] + pieza.getDesplazamientoFila(parte);
                    int c = origen[1] + pieza.getDesplazamientoColumna(parte);

                    int posicion = lado == LadoSalida.ARRIBA || lado == LadoSalida.ABAJO ? c : f;
                    minimo = Math.min(minimo, posicion);
                }

                switch (lado) {
                    case ARRIBA:
                        extremo = Integer.MAX_VALUE;
                        for (int parte : cara)
                            extremo = Math.min(extremo, origen[0] + pieza.getDesplazamientoFila(parte));
                        break;
                    case ABAJO:
                        extremo = Integer.MIN_VALUE;
                        for (int parte : cara)
                            extremo = Math.max(extremo, origen[0] + pieza.getDesplazamientoFila(parte));
                        break;
                    case IZQUIERDA:
                        extremo = Integer.MAX_VALUE;
                        for (int parte : cara)
                            extremo = Math.min(extremo, origen[1] + pieza.getDesplazamientoColumna(parte));
                        break;
                    default:
                        extremo = Integer.MIN_VALUE;
                        for (int parte : cara)
                            extremo = Math.max(extremo, origen[1] + pieza.getDesplazamientoColumna(parte));
                }

                int distanciaBorde = switch (lado) {
                    case ARRIBA, IZQUIERDA -> extremo;
                    case ABAJO -> numeroFilas - 1 - extremo;
                    default -> numeroColumnas - 1 - extremo;
                };

                int distancia = distanciaBorde
                        + Math.abs(minimo - salida.indice()) + 1;

                mejor = Math.min(mejor, distancia);
            }

            if (mejor == Integer.MAX_VALUE)
                throw new IllegalStateException("Pieza sin salida compatible: " + pieza.color());

            total += mejor;
        }

        return total;
    }
}