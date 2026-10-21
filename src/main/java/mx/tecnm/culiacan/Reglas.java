package mx.tecnm.culiacan;

public interface Reglas {
    void validarCampos(int numeroDeLados);

    // Estado y Salida
    void validarIdentificadorPieza(int id);

    void validarTamanoSalida(int tamano);

    // Solucionador
    void validarSalidaCompatible(boolean existe, Ficha color);

    // Generación de tableros
    void validarEspacioParaPieza(boolean puedeColocar);

    void validarEspacioParaForma(boolean hayEspacio, Forma forma);

    void validarIndicePieza(int indice, int cantidad);

    void validarIndiceVecino(int indice, int cantidad);

    void validarColorDisponible(boolean hayDisponible);

    void validarOrientacionDelColor(Orientacion anterior, Orientacion actual);

    void validarColoresCompatibles(boolean hayCompatible);

    void validarColorAsignado(int asignado, int totalColores, int numeroDePieza);

    void validarPiezaSeleccionada(int seleccionada);

    void validarSalidaDisponible(boolean hayDisponible, Ficha color);

    void validarTableroGenerado(boolean generado, ReglasException causa);

    // Tablero terminado
    void validarTableroCompleto(int[][] tablero);

    void validarCantidadColores(long cantidad);
}
