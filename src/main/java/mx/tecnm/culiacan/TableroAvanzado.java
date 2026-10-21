package mx.tecnm.culiacan;

import java.util.*;

public class TableroAvanzado implements TableroJuego {
    private final Reglas reglas;
    private final int numeroFilas;
    private final int numeroColumnas;
    private final int[][] tablero;
    private final List<Pieza> piezas = new ArrayList<>();
    private final List<Salida> salidas = new ArrayList<>();
    private final Random random = new Random();
    private final Estado estadoInicial;
    private Estado estadoActual;

    public TableroAvanzado(int numeroFilas, int numeroColumnas, Reglas reglas) {
        reglas.validarCampos(numeroFilas);
        reglas.validarCampos(numeroColumnas);

        this.reglas = reglas;
        this.numeroFilas = numeroFilas;
        this.numeroColumnas = numeroColumnas;
        this.tablero = new int[numeroFilas][numeroColumnas];

        boolean generado = false;
        ReglasException ultimoError = null;

        for (int intento = 0; intento < 100 && !generado; intento++) {
            for (int[] fila : tablero)
                Arrays.fill(fila, 0);

            piezas.clear();
            salidas.clear();

            try {
                llenarTablero();
                generado = true;
            } catch (ReglasException e) {
                ultimoError = e;
            }
        }

        reglas.validarTableroGenerado(generado, ultimoError);

        estadoInicial = new Estado(tablero, reglas);
        estadoActual = estadoInicial;
    }

    @Override
    public int getNumeroFilas() { return numeroFilas; }

    @Override
    public int getNumeroColumnas() { return numeroColumnas; }

    @Override
    public List<Salida> getSalidas() { return salidas; }

    @Override
    public List<Pieza> getPiezas() { return piezas; }

    @Override
    public Estado getEstadoInicial() { return estadoInicial; }

    @Override
    public void realizarMovimiento(Paso paso) {
        estadoActual = estadoActual.realizarMovimiento(paso);
    }

    private void llenarTablero() {
        List<Forma> formas = new ArrayList<>();

        Forma formaLCorta = elegirAleatoria(Forma.getLsCortas());
        Forma formaLLarga = elegirAleatoria(Forma.getLsLargas());

        colocarFormaEn(0, 0, Forma.INDIVIDUAL, formas);
        colocarFormaAleatoria(Forma.CUADRADO, formas);
        colocarFormaAleatoria(formaLLarga, formas);
        colocarFormaAleatoria(formaLCorta, formas);
        colocarFormaAleatoria(Forma.HORIZONTAL, formas);
        colocarFormaAleatoria(Forma.VERTICAL, formas);

        List<int[]> casillas = new ArrayList<>();

        for (int fila = 0; fila < numeroFilas; fila++)
            for (int columna = 0; columna < numeroColumnas; columna++)
                casillas.add(new int[]{fila, columna});

        Collections.shuffle(casillas, random);

        Forma[] candidatas = {
                Forma.CUADRADO,
                formaLCorta,
                formaLLarga,
                Forma.HORIZONTAL,
                Forma.VERTICAL,
                Forma.INDIVIDUAL
        };

        for (int[] casilla : casillas) {
            int fila = casilla[0];
            int columna = casilla[1];

            if (tablero[fila][columna] != 0) continue;

            List<Forma> disponibles = new ArrayList<>();

            for (Forma forma : candidatas)
                if (puedeColocar(fila, columna, forma))
                    disponibles.add(forma);

            Collections.shuffle(disponibles, random);

            Forma forma = disponibles.getFirst();

            if (random.nextDouble() < 0.20)
                forma = Forma.INDIVIDUAL;

            colocarFormaEn(fila, columna, forma, formas);
        }

        asignarColores(formas);
        colocarSalidas();
        validarTablero();
    }

    private Forma elegirAleatoria(Forma[] opciones) {
        return opciones[random.nextInt(opciones.length)];
    }

    private boolean puedeColocar(int fila, int columna, Forma forma) {
        for (int parte = 0; parte < forma.getTamano(); parte++) {
            int nf = fila + forma.getFila(parte);
            int nc = columna + forma.getColumna(parte);

            if (nf < 0 || nc < 0 || nf >= numeroFilas || nc >= numeroColumnas)
                return false;

            if (tablero[nf][nc] != 0) return false;
        }

        return true;
    }

    private void colocarFormaEn(int fila, int columna, Forma forma, List<Forma> formas) {
        reglas.validarEspacioParaPieza(puedeColocar(fila, columna, forma));

        int id = formas.size() + 1;

        for (int parte = 0; parte < forma.getTamano(); parte++) {
            int nf = fila + forma.getFila(parte);
            int nc = columna + forma.getColumna(parte);
            tablero[nf][nc] = id;
        }

        formas.add(forma);
    }

    private void colocarFormaAleatoria(Forma forma, List<Forma> formas) {
        List<int[]> disponibles = new ArrayList<>();

        for (int fila = 0; fila < numeroFilas; fila++)
            for (int columna = 0; columna < numeroColumnas; columna++)
                if (puedeColocar(fila, columna, forma))
                    disponibles.add(new int[]{fila, columna});

        reglas.validarEspacioParaForma(!disponibles.isEmpty(), forma);

        int[] posicion = disponibles.get(random.nextInt(disponibles.size()));

        colocarFormaEn(posicion[0], posicion[1], forma, formas);
    }

    private int grupo(Forma forma) {
        if (forma == Forma.INDIVIDUAL) return 0;
        if (forma == Forma.HORIZONTAL) return 1;
        if (forma == Forma.VERTICAL) return 2;
        if (forma.esLCorta()) return 3;
        if (forma.esLLarga()) return 5;
        return 4;
    }

    private void asignarColores(List<Forma> formas) {
        List<Ficha> colores = new ArrayList<>();

        for (Ficha ficha : Ficha.values())
            if (ficha != Ficha.VACIA) colores.add(ficha);

        Collections.shuffle(colores, random);

        int cantidad = formas.size();
        List<Set<Integer>> vecinos = new ArrayList<>();

        for (int i = 0; i < cantidad; i++)
            vecinos.add(new HashSet<>());

        for (int fila = 0; fila < numeroFilas; fila++) {
            for (int columna = 0; columna < numeroColumnas; columna++) {
                int id = tablero[fila][columna] - 1;

                reglas.validarIndicePieza(id, cantidad);

                for (Movimiento movimiento : Movimiento.values()) {
                    int nf = fila + movimiento.getMovimientoFila();
                    int nc = columna + movimiento.getMovimientoColumna();

                    if (nf < 0 || nc < 0 || nf >= numeroFilas || nc >= numeroColumnas) continue;

                    int vecino = tablero[nf][nc] - 1;

                    reglas.validarIndiceVecino(vecino, cantidad);

                    if (id != vecino) vecinos.get(id).add(vecino);
                }
            }
        }

        int[] asignados = new int[cantidad];
        Arrays.fill(asignados, -1);

        int[] gruposColor = new int[colores.size()];
        Arrays.fill(gruposColor, -1);

        for (int paso = 0; paso < cantidad; paso++) {
            int seleccionado = getSeleccionado(cantidad, asignados, vecinos);

            int grupoActual = grupo(formas.get(seleccionado));
            Set<Integer> prohibidos = new HashSet<>();

            for (int vecino : vecinos.get(seleccionado))
                if (asignados[vecino] != -1)
                    prohibidos.add(asignados[vecino]);

            int elegido = -1;

            for (int c = 0; c < colores.size(); c++) {
                if (gruposColor[c] == grupoActual && !prohibidos.contains(c)) {
                    elegido = c;
                    break;
                }
            }

            if (elegido == -1) {
                for (int c = 0; c < colores.size(); c++) {
                    if (gruposColor[c] == -1 && !prohibidos.contains(c)) {
                        elegido = c;
                        gruposColor[c] = grupoActual;
                        break;
                    }
                }
            }

            reglas.validarColoresCompatibles(elegido != -1);

            asignados[seleccionado] = elegido;
        }

        piezas.clear();

        for (int i = 0; i < cantidad; i++) {
            reglas.validarColorAsignado(asignados[i], colores.size(), i + 1);

            piezas.add(new Pieza(i + 1, colores.get(asignados[i]), formas.get(i)));
        }
    }

    private int getSeleccionado(int cantidad, int[] asignados, List<Set<Integer>> vecinos) {
        int seleccionado = -1;
        int mayorSaturacion = -1;
        int mayorGrado = -1;

        for (int i = 0; i < cantidad; i++) {
            if (asignados[i] != -1) continue;

            Set<Integer> coloresVecinos = new HashSet<>();

            for (int vecino : vecinos.get(i))
                if (asignados[vecino] != -1)
                    coloresVecinos.add(asignados[vecino]);

            int saturacion = coloresVecinos.size();
            int grado = vecinos.get(i).size();

            if (saturacion > mayorSaturacion
                    || (saturacion == mayorSaturacion && grado > mayorGrado)) {
                seleccionado = i;
                mayorSaturacion = saturacion;
                mayorGrado = grado;
            }
        }

        reglas.validarPiezaSeleccionada(seleccionado);
        return seleccionado;
    }

    private void colocarSalidas() {
        salidas.clear();

        Map<Ficha, List<Pieza>> piezasPorColor = new LinkedHashMap<>();

        for (Pieza pieza : piezas)
            piezasPorColor.computeIfAbsent(pieza.color(), _ -> new ArrayList<>()).add(pieza);

        Set<String> ocupadas = new HashSet<>();

        Pieza inicial = piezas.getFirst();
        Salida primera = new Salida(LadoSalida.ARRIBA, 0, inicial.color(), 1);

        salidas.add(primera);
        ocupadas.add("ARRIBA-0");
        piezasPorColor.remove(inicial.color());

        for (Map.Entry<Ficha, List<Pieza>> entry : piezasPorColor.entrySet()) {
            Ficha color = entry.getKey();
            List<Pieza> grupo = entry.getValue();
            Pieza representante = grupo.getFirst();

            List<Salida> candidatas = new ArrayList<>();
            List<Salida> alineadas = new ArrayList<>();

            for (LadoSalida lado : LadoSalida.values()) {
                if (!representante.permiteSalida(lado)) continue;

                int tamanoSalida = representante.forma().getTamanoSalida(lado);

                int limite = lado == LadoSalida.ARRIBA || lado == LadoSalida.ABAJO
                        ? numeroColumnas : numeroFilas;

                for (int indice = 0; indice <= limite - tamanoSalida; indice++) {
                    if (salidaOcupada(lado, indice, tamanoSalida)) continue;

                    reglas.validarTamanoSalida(tamanoSalida);
                    Salida salida = new Salida(lado, indice, color, tamanoSalida);
                    candidatas.add(salida);

                    for (Pieza pieza : grupo) {
                        if (estaAlineada(pieza, salida)) {
                            alineadas.add(salida);
                            break;
                        }
                    }
                }
            }

            reglas.validarSalidaDisponible(!candidatas.isEmpty(), color);

            List<Salida> opciones = alineadas.isEmpty() ? candidatas : alineadas;
            Salida elegida = opciones.get(random.nextInt(opciones.size()));

            salidas.add(elegida);

            for (int i = 0; i < elegida.tamano(); i++)
                ocupadas.add(elegida.ladoSalida() + "-" + (elegida.indice() + i));
        }
    }

    private boolean estaAlineada(Pieza pieza, Salida salida) {
        if (!pieza.permiteSalida(salida.ladoSalida())) return false;

        int[] origen = buscarOrigen(pieza.id());
        if (origen == null) return false;

        int[] cara = pieza.forma().getIndicesCara(salida.ladoSalida());
        int[] indices = new int[cara.length];

        if (cara.length != salida.tamano()) return false;

        for (int i = 0; i < cara.length; i++) {
            int parte = cara[i];
            int f = origen[0] + pieza.getDesplazamientoFila(parte);
            int c = origen[1] + pieza.getDesplazamientoColumna(parte);

            switch (salida.ladoSalida()) {
                case ARRIBA:
                    if (f != 0) return false;
                    indices[i] = c;
                    break;
                case ABAJO:
                    if (f != numeroFilas - 1) return false;
                    indices[i] = c;
                    break;
                case IZQUIERDA:
                    if (c != 0) return false;
                    indices[i] = f;
                    break;
                case DERECHA:
                    if (c != numeroColumnas - 1) return false;
                    indices[i] = f;
                    break;
            }
        }

        Arrays.sort(indices);

        for (int i = 0; i < indices.length; i++)
            if (indices[i] != salida.indice() + i) return false;

        return true;
    }

    private int[] buscarOrigen(int id) {
        int minimoFila = Integer.MAX_VALUE;
        int minimoColumna = Integer.MAX_VALUE;

        for (int fila = 0; fila < numeroFilas; fila++) {
            for (int columna = 0; columna < numeroColumnas; columna++) {
                if (tablero[fila][columna] != id) continue;

                minimoFila = Math.min(minimoFila, fila);
                minimoColumna = Math.min(minimoColumna, columna);
            }
        }

        if (minimoFila == Integer.MAX_VALUE) return null;

        return new int[]{minimoFila, minimoColumna};
    }

    private boolean salidaOcupada(LadoSalida lado, int indice, int tamano) {
        for (Salida salida : salidas) {
            if (salida.ladoSalida() != lado) continue;

            if (indice < salida.indice() + salida.tamano()
                    && salida.indice() < indice + tamano)
                return true;
        }

        return false;
    }

    private void validarTablero() {
        reglas.validarTableroCompleto(tablero);

        long cantidadColores = piezas.stream()
                .map(Pieza::color)
                .distinct()
                .count();

        reglas.validarCantidadColores(cantidadColores);
    }

    @Override
    public String toString() {
        return new ImpresorTablero(estadoActual, piezas, salidas).toString();
    }
}