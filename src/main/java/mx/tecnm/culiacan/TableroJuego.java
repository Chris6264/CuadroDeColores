package mx.tecnm.culiacan;

import java.util.List;

public interface TableroJuego {
    int getNumeroFilas();
    int getNumeroColumnas();
    List<Salida> getSalidas();
    List<Pieza> getPiezas();
    Estado getEstadoInicial();
    Estado getEstadoActual();
    void realizarMovimiento(Paso paso);
}