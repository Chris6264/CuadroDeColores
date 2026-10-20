package mx.tecnm.culiacan;

import java.util.*;
import java.util.stream.Collectors;

public class Tablero implements TableroJuego{
    private final int[][] tablero;
    private final Estado estadoInicial;
    private Estado estadoActual;
    private final int numeroDeLados;
    private final List<Salida> salidas = new ArrayList<>();
    private final List<Pieza> piezas = new ArrayList<>();
    private final Random random = new Random();

    public Tablero(Reglas reglas) {
        this(2, reglas);
    }

    public Tablero(int numeroDeLados, Reglas reglas) {
        reglas.validarCampos(numeroDeLados);
        this.numeroDeLados = numeroDeLados;
        this.tablero = new int[numeroDeLados][numeroDeLados];

        llenarTablero();

        estadoInicial = new Estado(tablero);
        estadoActual = estadoInicial;
    }

    public int getNumeroDeLados() { return numeroDeLados; }

    @Override
    public int getNumeroFilas() { return numeroDeLados; }

    @Override
    public int getNumeroColumnas() { return numeroDeLados; }

    public List<Salida> getSalidas() { return salidas; }

    public List<Pieza> getPiezas() { return piezas; }

    public Estado getEstadoInicial() { return estadoInicial; }

    public Estado getEstadoActual() { return estadoActual; }

    public void realizarMovimiento(Paso paso) {
        estadoActual = estadoActual.realizarMovimiento(paso);
    }

    private void llenarTablero() {
        if (numeroDeLados == 2) llenarBasico();
        else llenarIntermedio();
    }

    private void llenarBasico() {
        List<Ficha> colores = obtenerColores();
        Collections.shuffle(colores, random);

        List<Ficha> fichas = new ArrayList<>();

        fichas.add(colores.get(0));
        fichas.add(colores.get(0));
        fichas.add(colores.get(1));
        fichas.add(colores.get(1));

        Collections.shuffle(fichas, random);

        int id = 1;

        for (int fila = 0; fila < numeroDeLados; fila++) {
            for (int columna = 0; columna < numeroDeLados; columna++) {
                tablero[fila][columna] = id;
                piezas.add(new Pieza(id, fichas.get(id - 1), Orientacion.INDIVIDUAL));
                id++;
            }
        }

        colocarSalidasBasicas();
    }

    private void llenarIntermedio() {
        List<Orientacion> orientaciones = new ArrayList<>();
        int id = 1;

        tablero[0][0] = id;
        tablero[0][1] = id++;
        orientaciones.add(Orientacion.HORIZONTAL);

        tablero[1][numeroDeLados - 1] = id;
        tablero[2][numeroDeLados - 1] = id++;
        orientaciones.add(Orientacion.VERTICAL);

        tablero[1][0] = id++;
        orientaciones.add(Orientacion.INDIVIDUAL);

        List<int[]> casillas = new ArrayList<>();

        for (int fila = 0; fila < numeroDeLados; fila++)
            for (int columna = 0; columna < numeroDeLados; columna++)
                casillas.add(new int[]{fila, columna});

        Collections.shuffle(casillas, random);

        for (int[] casilla : casillas) {
            int fila = casilla[0];
            int columna = casilla[1];

            if (tablero[fila][columna] != 0) continue;

            List<Orientacion> opciones = new ArrayList<>();

            if (columna + 1 < numeroDeLados && tablero[fila][columna + 1] == 0)
                opciones.add(Orientacion.HORIZONTAL);

            if (fila + 1 < numeroDeLados && tablero[fila + 1][columna] == 0)
                opciones.add(Orientacion.VERTICAL);

            Orientacion orientacion = Orientacion.INDIVIDUAL;

            if (!opciones.isEmpty() && random.nextDouble() < 0.65)
                orientacion = opciones.get(random.nextInt(opciones.size()));

            tablero[fila][columna] = id;

            if (orientacion == Orientacion.HORIZONTAL)
                tablero[fila][columna + 1] = id;

            if (orientacion == Orientacion.VERTICAL)
                tablero[fila + 1][columna] = id;

            orientaciones.add(orientacion);
            id++;
        }

        asignarColores(orientaciones);
        colocarSalidasIntermedias();
        validarTablero();
    }

    private List<Ficha> obtenerColores() {
        return Arrays.stream(Ficha.values())
                .filter(ficha -> ficha != Ficha.VACIA)
                .collect(Collectors.toList());
    }

    private void asignarColores(List<Orientacion> orientaciones) {
        List<Ficha> colores = obtenerColores();
        Collections.shuffle(colores, random);

        Map<Orientacion, List<Ficha>> coloresPorOrientacion = new EnumMap<>(Orientacion.class);

        coloresPorOrientacion.put(Orientacion.INDIVIDUAL, new ArrayList<>(colores.subList(0, 4)));
        coloresPorOrientacion.put(Orientacion.HORIZONTAL, new ArrayList<>(colores.subList(4, 7)));
        coloresPorOrientacion.put(Orientacion.VERTICAL, new ArrayList<>(colores.subList(7, 10)));

        for (int i = 0; i < orientaciones.size(); i++) {
            int id = i + 1;
            Orientacion orientacion = orientaciones.get(i);
            Set<Ficha> coloresVecinos = new HashSet<>();

            for (int fila = 0; fila < numeroDeLados; fila++) {
                for (int columna = 0; columna < numeroDeLados; columna++) {
                    if (tablero[fila][columna] != id) continue;

                    for (Movimiento movimiento : Movimiento.values()) {
                        int nf = fila + movimiento.getMovimientoFila();
                        int nc = columna + movimiento.getMovimientoColumna();

                        if (nf < 0 || nc < 0 || nf >= numeroDeLados || nc >= numeroDeLados) continue;

                        int vecino = tablero[nf][nc];

                        if (vecino == 0 || vecino == id || vecino > piezas.size()) continue;

                        coloresVecinos.add(piezas.get(vecino - 1).getColor());
                    }
                }
            }

            List<Ficha> disponibles = new ArrayList<>();

            for (Ficha color : coloresPorOrientacion.get(orientacion))
                if (!coloresVecinos.contains(color))
                    disponibles.add(color);

            if (disponibles.isEmpty())
                throw new IllegalStateException("No hay colores disponibles para la pieza");

            Ficha color = disponibles.get(random.nextInt(disponibles.size()));

            piezas.add(new Pieza(id, color, orientacion));
        }
    }

    private void colocarSalidasBasicas() {
        salidas.clear();

        List<Ficha> colores = piezas.stream()
                .map(Pieza::getColor)
                .distinct()
                .collect(Collectors.toList());

        List<int[]> espacios = obtenerEspaciosSalida();
        Collections.shuffle(espacios, random);

        for (int i = 0; i < colores.size(); i++) {
            int[] espacio = espacios.get(i);

            salidas.add(new Salida(
                    LadoSalida.values()[espacio[0]],
                    espacio[1],
                    colores.get(i)
            ));
        }

        boolean coincidencia = false;

        for (Salida salida : salidas) {
            int fila = salida.getFila(numeroDeLados);
            int columna = salida.getColumna(numeroDeLados);
            int id = tablero[fila][columna];

            if (piezas.get(id - 1).getColor() == salida.getColorFicha()) {
                coincidencia = true;
                break;
            }
        }

        if (!coincidencia) {
            Salida salida = salidas.get(0);
            int fila = salida.getFila(numeroDeLados);
            int columna = salida.getColumna(numeroDeLados);

            for (int i = 0; i < numeroDeLados; i++) {
                for (int j = 0; j < numeroDeLados; j++) {
                    int id = tablero[i][j];

                    if (piezas.get(id - 1).getColor() == salida.getColorFicha()) {
                        int temporal = tablero[fila][columna];
                        tablero[fila][columna] = tablero[i][j];
                        tablero[i][j] = temporal;
                        return;
                    }
                }
            }
        }
    }

    private void colocarSalidasIntermedias() {
        salidas.clear();

        Map<Ficha, Orientacion> orientaciones = new LinkedHashMap<>();

        for (Pieza pieza : piezas) {
            Orientacion anterior = orientaciones.putIfAbsent(
                    pieza.getColor(),
                    pieza.getOrientacion()
            );

            if (anterior != null && anterior != pieza.getOrientacion())
                throw new IllegalStateException("Color con orientaciones incompatibles");
        }

        List<Ficha> colores = new ArrayList<>(orientaciones.keySet());
        Set<String> espaciosOcupados = new HashSet<>();

        Pieza primera = piezas.get(0);

        Salida inicial = new Salida(
                LadoSalida.IZQUIERDA,
                0,
                primera.getColor()
        );

        salidas.add(inicial);
        espaciosOcupados.add("IZQUIERDA-0");
        colores.remove(primera.getColor());

        for (Ficha color : colores) {
            Orientacion orientacion = orientaciones.get(color);
            List<Salida> candidatas = new ArrayList<>();

            for (LadoSalida lado : LadoSalida.values()) {
                if (!ladoCompatible(orientacion, lado)) continue;

                for (int indice = 0; indice < numeroDeLados; indice++) {
                    String clave = lado + "-" + indice;

                    if (!espaciosOcupados.contains(clave))
                        candidatas.add(new Salida(lado, indice, color));
                }
            }

            if (candidatas.isEmpty())
                throw new IllegalStateException("No hay salida para el color " + color);

            Salida salida = candidatas.get(random.nextInt(candidatas.size()));

            salidas.add(salida);
            espaciosOcupados.add(salida.getLadoSalida() + "-" + salida.getIndice());
        }
    }

    private boolean ladoCompatible(Orientacion orientacion, LadoSalida lado) {
        if (orientacion == Orientacion.INDIVIDUAL) return true;

        if (orientacion == Orientacion.HORIZONTAL)
            return lado == LadoSalida.IZQUIERDA || lado == LadoSalida.DERECHA;

        return lado == LadoSalida.ARRIBA || lado == LadoSalida.ABAJO;
    }

    private List<int[]> obtenerEspaciosSalida() {
        List<int[]> espacios = new ArrayList<>();

        for (LadoSalida lado : LadoSalida.values())
            for (int i = 0; i < numeroDeLados; i++)
                espacios.add(new int[]{lado.ordinal(), i});

        return espacios;
    }

    private void validarTablero() {
        for (int fila = 0; fila < numeroDeLados; fila++)
            for (int columna = 0; columna < numeroDeLados; columna++)
                if (tablero[fila][columna] == 0)
                    throw new IllegalStateException("El tablero contiene espacios vacíos");

        long colores = piezas.stream()
                .map(Pieza::getColor)
                .distinct()
                .count();

        if (colores < 3 || colores > 10)
            throw new IllegalStateException("Cantidad de colores incorrecta");
    }

    private char simboloSalida(LadoSalida lado, int indice) {
        for (Salida salida : salidas)
            if (salida.getLadoSalida() == lado && salida.getIndice() == indice)
                return salida.getColorFicha().getSimbolo();

        return ' ';
    }

    private String colorSalida(LadoSalida lado, int indice) {
        for (Salida salida : salidas) {
            if (salida.ocupaPosicion(lado, indice))
                return salida.getColorFicha().getBloqueColor();
        }

        return "   ";
    }

    @Override
    public String toString() {
        return new ImpresorTablero(estadoActual, piezas, salidas).toString();
    }
}