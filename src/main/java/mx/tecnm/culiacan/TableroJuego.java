package mx.tecnm.culiacan;

import java.util.List;

public interface TableroJuego {
    int getNumeroFilas();
    int getNumeroColumnas();
    List<Salida> getSalidas();
    List<Pieza> getPiezas();
    Estado getEstadoInicial();
    void realizarMovimiento(Paso paso);
}