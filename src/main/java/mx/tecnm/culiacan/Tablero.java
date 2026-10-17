package mx.tecnm.culiacan;

import java.util.*;
import java.util.stream.Collectors;

public class Tablero {
    private Ficha[][] tablero;
    private Ficha[] coloresActivos;
    private int numeroFilas;
    private int numeroColumnas;
    private Random random;

    public Tablero(int numeroFilas, int numeroColumnas) {
        this.numeroFilas = numeroFilas;
        this.numeroColumnas = numeroColumnas;
        this.tablero = new Ficha[numeroFilas][numeroColumnas];
        this.random = new Random();
        llenarTablero();
    }

    private void llenarTablero(){
        int numeroDeColores = 2;
        escogerColores(numeroDeColores);
        for (int i = 0; i < numeroFilas; i++){
            for (int j = 0; j < numeroColumnas; j++){
                int posicionColor = random.nextInt(coloresActivos.length);
                this.tablero[i][j] = coloresActivos[posicionColor];
            }
        }
    }

    private void escogerColores(int numeroDeColores){
        List<Ficha> coloresCandidatos = Arrays.stream(Ficha.values())
                .filter(ficha -> ficha != Ficha.VACIA)
                .collect(Collectors.toList());

        Collections.shuffle(coloresCandidatos,random);

        coloresActivos = coloresCandidatos.stream()
                .limit(numeroDeColores)
                .toArray(Ficha[]::new);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String separador = "   +" + "---+".repeat(numeroColumnas) + "\n";

        stringBuilder.append("    ");
        for (int j = 0; j < numeroColumnas; j++) {
            stringBuilder.append(String.format(" %-3d", j));
        }
        stringBuilder.append("\n").append(separador);

        for (int i = 0; i < numeroFilas; i++) {
            stringBuilder.append(String.format("%2d |", i));
            for (int j = 0; j < numeroColumnas; j++) {
                stringBuilder.append(" ").append(tablero[i][j].getSimbolo()).append(" |");
            }
            stringBuilder.append("\n").append(separador);
        }
        return stringBuilder.toString();
    }
}