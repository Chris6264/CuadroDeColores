package mx.tecnm.culiacan;

import java.util.*;
import java.util.stream.Collectors;

public class Tablero {
    private static final int MINIMO_COLORES = 3;
    private static final int MAXIMO_COLORES = Ficha.values().length - 1;

    private final Ficha[][] tablero;
    private Ficha[] coloresActivos;
    private final Reglas reglas;
    private final int numeroDeColores;
    private final int numeroDeLados;
    private final List<Salida> salidas = new ArrayList<>();
    private final Random random;

    public Tablero(Reglas reglas) {
        this.reglas = reglas;
        this.numeroDeLados = 2;
        this.random = new Random();
        this.numeroDeColores = 2;
        this.tablero = new Ficha[numeroDeLados][numeroDeLados];
        llenarTablero();
    }

    public Tablero(int numeroDeLados, Reglas reglas) {
        this.reglas = reglas;
        reglas.validarCampos(numeroDeLados);
        this.numeroDeLados = numeroDeLados;
        this.tablero = new Ficha[numeroDeLados][numeroDeLados];
        this.random = new Random();
        this.numeroDeColores = MINIMO_COLORES + random.nextInt(MAXIMO_COLORES - MINIMO_COLORES + 1);
        llenarTablero();
    }

    private void llenarTablero(){
        escogerColores();

        List<int[]> casillas = new ArrayList<>();
        for (int i = 0; i < numeroDeLados; i++)
            for (int j = 0; j < numeroDeLados; j++)
                casillas.add(new int[]{i, j});
        Collections.shuffle(casillas, random);

        for (int k = 0; k < casillas.size(); k++) {
            int[] casilla = casillas.get(k);
            Ficha ficha = k < coloresActivos.length
                    ? coloresActivos[k]
                    : coloresActivos[random.nextInt(coloresActivos.length)];
            tablero[casilla[0]][casilla[1]] = ficha;
        }

        colocarSalidas();
    }

    private void escogerColores(){
        List<Ficha> coloresCandidatos = Arrays.stream(Ficha.values())
                .filter(ficha -> ficha != Ficha.VACIA)
                .collect(Collectors.toList());

        Collections.shuffle(coloresCandidatos,random);

        coloresActivos = coloresCandidatos.stream()
                .limit(numeroDeColores)
                .toArray(Ficha[]::new);
    }

    private int[] buscarFicha(Ficha color){
        for (int i = 0; i < numeroDeLados; i++)
            for (int j = 0; j < numeroDeLados; j++)
                if (tablero[i][j] == color) return new int[]{i, j};
        return null;
    }

    private void colocarSalidas(){
        List<int[]> espacios = new ArrayList<>();
        for (LadoSalida lado : LadoSalida.values())
            for (int i = 0; i < numeroDeLados; i++)
                espacios.add(new int[]{lado.ordinal(),i});
        Collections.shuffle(espacios,random);

        for (int j = 0; j < coloresActivos.length; j++){
            int[] espacio = espacios.get(j);
            salidas.add(new Salida(LadoSalida.values()[espacio[0]],espacio[1],coloresActivos[j]));
        }

        boolean hayCoincidencia = salidas.stream().anyMatch(salida -> tablero[salida.getFila(numeroDeLados)]
                [salida.getColumna(numeroDeLados)] == salida.getColorFicha());

        if (!hayCoincidencia){
            Salida salida = salidas.get(random.nextInt(salidas.size()));
            int fila = salida.getFila(numeroDeLados);
            int columna = salida.getColumna(numeroDeLados);
            int[] origen = buscarFicha(salida.getColorFicha());

            if (origen != null) {
                Ficha temporal = tablero[fila][columna];
                tablero[fila][columna] = tablero[origen[0]][origen[1]];
                tablero[origen[0]][origen[1]] = temporal;
            } else {
                tablero[fila][columna] = salida.getColorFicha();
            }
        }
    }

    private char simboloSalida(LadoSalida ladoSalida, int indice){
        for (Salida salida : salidas)
            if (salida.getLadoSalida() == ladoSalida && salida.getIndice() == indice)
                return salida.getColorFicha().getSimbolo();
        return ' ';
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String separador = "  +" + "---+".repeat(numeroDeLados) + "\n";

        sb.append("    ");
        for (int j = 0; j < numeroDeLados; j++)
            sb.append(simboloSalida(LadoSalida.ARRIBA, j)).append("   ");
        sb.append("\n").append(separador);

        for (int i = 0; i < numeroDeLados; i++) {
            sb.append(simboloSalida(LadoSalida.IZQUIERDA, i)).append(" |");
            for (int j = 0; j < numeroDeLados; j++)
                sb.append(" ").append(tablero[i][j].getSimbolo()).append(" |");
            sb.append(" ").append(simboloSalida(LadoSalida.DERECHA, i)).append("\n").append(separador);
        }

        sb.append("    ");
        for (int j = 0; j < numeroDeLados; j++)
            sb.append(simboloSalida(LadoSalida.ABAJO, j)).append("   ");
        sb.append("\n");
        return sb.toString();
    }
}