package mx.tecnm.culiacan;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Gestiona la generación y disposición de piezas en el tablero.
 */
public class Tablero implements TableroJuego{
    private final Reglas reglas;
    private final int[][] tablero;
    private final Estado estadoInicial;
    private Estado estadoActual;
    private final int numeroDeLados;
    private final List<Salida> salidas = new ArrayList<>();
    private final List<Pieza> piezas = new ArrayList<>();
    /**
     * Implementa la operación random.
     * @return Resultado de random.
     */
    private final Random random = new Random();

    /**
     * Construye una instancia de Tablero con los parámetros indicados.
     * @param reglas Reglas utilizado en la operación.
     */
    public Tablero(Reglas reglas) {
        this(2, reglas);
    }

    /**
     * Construye una instancia de Tablero con los parámetros indicados.
     * @param numeroDeLados Numero de lados utilizado en la operación.
     * @param reglas Reglas utilizado en la operación.
     */
    public Tablero(int numeroDeLados, Reglas reglas) {
        reglas.validarCampos(numeroDeLados);
        this.reglas = reglas;
        this.numeroDeLados = numeroDeLados;
        this.tablero = new int[numeroDeLados][numeroDeLados];

        llenarTablero();

        estadoInicial = new Estado(tablero, reglas);
        estadoActual = estadoInicial;
    }

    @Override
    /**
     * Obtiene numero filas.
     * @return Resultado de get numero filas.
     */
    public int getNumeroFilas() { return numeroDeLados; }

    @Override
    /**
     * Obtiene numero columnas.
     * @return Resultado de get numero columnas.
     */
    public int getNumeroColumnas() { return numeroDeLados; }

    /**
     * Obtiene salidas.
     * @return Resultado de get salidas.
     */
    public List<Salida> getSalidas() { return salidas; }

    /**
     * Obtiene piezas.
     * @return Resultado de get piezas.
     */
    public List<Pieza> getPiezas() { return piezas; }

    /**
     * Obtiene estado inicial.
     * @return Resultado de get estado inicial.
     */
    public Estado getEstadoInicial() { return estadoInicial; }

    /**
     * Realiza movimiento.
     * @param paso Paso utilizado en la operación.
     */
    public void realizarMovimiento(Paso paso) {
        estadoActual = estadoActual.realizarMovimiento(paso);
    }

    /**
     * Implementa la operación llenar tablero.
     */
    private void llenarTablero() {
        if (numeroDeLados == 2) llenarBasico();
        else llenarIntermedio();
    }

    /**
     * Implementa la operación llenar basico.
     */
    private void llenarBasico() {
        List<Ficha> colores = obtenerColores();
        Collections.shuffle(colores, random);

        List<Ficha> fichas = new ArrayList<>();

        int cantidadPrimerColor = random.nextBoolean() ? 2 : 3;

        for (int indice = 0; indice < 4; indice++)
            fichas.add(indice < cantidadPrimerColor ? colores.get(0) : colores.get(1));

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

    /**
     * Implementa la operación llenar intermedio.
     */
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

    /**
     * Obtiene colores.
     * @return Resultado de obtener colores.
     */
    private List<Ficha> obtenerColores() {
        return Arrays.stream(Ficha.values())
                .filter(ficha -> ficha != Ficha.VACIA)
                .collect(Collectors.toList());
    }

    /**
     * Asigna colores.
     * @param orientaciones Orientaciones utilizado en la operación.
     */
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
                        int numeroFila = fila + movimiento.getMovimientoFila();
                        int numeroColumna = columna + movimiento.getMovimientoColumna();

                        if (numeroFila < 0 || numeroColumna < 0 || numeroFila >= numeroDeLados || numeroColumna >= numeroDeLados) continue;

                        int vecino = tablero[numeroFila][numeroColumna];

                        if (vecino == 0 || vecino == id || vecino > piezas.size()) continue;

                        coloresVecinos.add(piezas.get(vecino - 1).color());
                    }
                }
            }

            List<Ficha> disponibles = new ArrayList<>();

            for (Ficha color : coloresPorOrientacion.get(orientacion))
                if (!coloresVecinos.contains(color))
                    disponibles.add(color);

            reglas.validarColorDisponible(!disponibles.isEmpty());

            Ficha color = disponibles.get(random.nextInt(disponibles.size()));

            piezas.add(new Pieza(id, color, orientacion));
        }
    }

    /**
     * Coloca salidas basicas.
     */
    private void colocarSalidasBasicas() {
        salidas.clear();

        List<Ficha> colores = piezas.stream()
                .map(Pieza::color)
                .distinct()
                .toList();

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

            if (piezas.get(id - 1).color() == salida.colorFicha()) {
                coincidencia = true;
                break;
            }
        }

        if (!coincidencia) {
            Salida salida = salidas.getFirst();
            int fila = salida.getFila(numeroDeLados);
            int columna = salida.getColumna(numeroDeLados);

            for (int i = 0; i < numeroDeLados; i++) {
                for (int j = 0; j < numeroDeLados; j++) {
                    int id = tablero[i][j];

                    if (piezas.get(id - 1).color() == salida.colorFicha()) {
                        int temporal = tablero[fila][columna];
                        tablero[fila][columna] = tablero[i][j];
                        tablero[i][j] = temporal;
                        return;
                    }
                }
            }
        }
    }

    /**
     * Coloca salidas intermedias.
     */
    private void colocarSalidasIntermedias() {
        salidas.clear();

        Map<Ficha, Orientacion> orientaciones = new LinkedHashMap<>();

        for (Pieza pieza : piezas) {
            Orientacion anterior = orientaciones.putIfAbsent(
                    pieza.color(),
                    pieza.getOrientacion()
            );

            reglas.validarOrientacionDelColor(anterior, pieza.getOrientacion());
        }

        List<Ficha> colores = new ArrayList<>(orientaciones.keySet());
        Set<String> espaciosOcupados = new HashSet<>();

        Pieza primera = piezas.getFirst();

        Salida inicial = new Salida(
                LadoSalida.IZQUIERDA,
                0,
                primera.color()
        );

        salidas.add(inicial);
        espaciosOcupados.add("IZQUIERDA-0");
        colores.remove(primera.color());

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

            reglas.validarSalidaDisponible(!candidatas.isEmpty(), color);

            Salida salida = candidatas.get(random.nextInt(candidatas.size()));

            salidas.add(salida);
            espaciosOcupados.add(salida.ladoSalida() + "-" + salida.indice());
        }
    }

    /**
     * Implementa la operación lado compatible.
     * @param orientacion Orientacion utilizado en la operación.
     * @param lado Lado utilizado en la operación.
     * @return Resultado de lado compatible.
     */
    private boolean ladoCompatible(Orientacion orientacion, LadoSalida lado) {
        if (orientacion == Orientacion.INDIVIDUAL) return true;

        if (orientacion == Orientacion.HORIZONTAL)
            return lado == LadoSalida.IZQUIERDA || lado == LadoSalida.DERECHA;

        return lado == LadoSalida.ARRIBA || lado == LadoSalida.ABAJO;
    }

    /**
     * Obtiene espacios salida.
     * @return Resultado de obtener espacios salida.
     */
    private List<int[]> obtenerEspaciosSalida() {
        List<int[]> espacios = new ArrayList<>();

        for (LadoSalida lado : LadoSalida.values())
            for (int i = 0; i < numeroDeLados; i++)
                espacios.add(new int[]{lado.ordinal(), i});

        return espacios;
    }

    /**
     * Valida tablero.
     */
    private void validarTablero() {
        reglas.validarTableroCompleto(tablero);

        long colores = piezas.stream()
                .map(Pieza::color)
                .distinct()
                .count();

        reglas.validarCantidadColores(colores);
    }

    @Override
    /**
     * Devuelve la representación textual del objeto.
     * @return Resultado de to string.
     */
    public String toString() {
        return new ImpresorTablero(estadoActual, piezas, salidas).toString();
    }
}