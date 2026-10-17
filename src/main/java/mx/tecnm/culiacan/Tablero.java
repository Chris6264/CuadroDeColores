package mx.tecnm.culiacan;

import java.util.*;
import java.util.stream.Collectors;

public class Tablero {
    private final Ficha[][] tablero;
    private final Reglas reglas;
    private Ficha[] coloresActivos;
    private final int numeroDeLados;
    private final Random random;

    public Tablero(Reglas reglas) {
        this.reglas = reglas;
        this.numeroDeLados = 2;
        this.random = new Random();
        this.tablero = new Ficha[numeroDeLados][numeroDeLados];
        llenarTablero();
    }

    public Tablero(int numeroDeLados, Reglas reglas) {
        this.reglas = reglas;
        reglas.validarCampos(numeroDeLados);
        this.numeroDeLados = numeroDeLados;
        this.tablero = new Ficha[numeroDeLados][numeroDeLados];
        this.random = new Random();
        llenarTablero();
    }

    private void llenarTablero(){
        int numeroDeColores = 2;
        escogerColores(numeroDeColores);
        for (int i = 0; i < numeroDeLados; i++){
            for (int j = 0; j < numeroDeLados; j++){
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
        String separador = "   +" + "---+".repeat(numeroDeLados) + "\n";

        stringBuilder.append("    ");
        for (int j = 0; j < numeroDeLados; j++) {
            stringBuilder.append(String.format(" %-3d", j));
        }
        stringBuilder.append("\n").append(separador);

        for (int i = 0; i < numeroDeLados; i++) {
            stringBuilder.append(String.format("%2d |", i));
            for (int j = 0; j < numeroDeLados; j++) {
                stringBuilder.append(" ").append(tablero[i][j].getSimbolo()).append(" |");
            }
            stringBuilder.append("\n").append(separador);
        }
        return stringBuilder.toString();
    }
}