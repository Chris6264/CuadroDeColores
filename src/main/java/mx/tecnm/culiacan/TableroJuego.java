package mx.tecnm.culiacan;

import java.util.List;

/**
 * Define las operaciones comunes que ofrecen las implementaciones de tableros.
 */
public interface TableroJuego {
    /**
     * Obtiene numerofilas.
     * @return Valor obtenido por la operación.
     */
    int getNumeroFilas();
    /**
     * Obtiene numerocolumnas.
     * @return Valor obtenido por la operación.
     */
    int getNumeroColumnas();
    /**
     * Obtiene salidas.
     * @return Valor obtenido por la operación.
     */
    List<Salida> getSalidas();
    /**
     * Obtiene piezas.
     * @return Valor obtenido por la operación.
     */
    List<Pieza> getPiezas();
    /**
     * Obtiene estadoinicial.
     * @return Valor obtenido por la operación.
     */
    Estado getEstadoInicial();
    /**
     * Ejecuta realizarMovimiento.
     * @param paso Parámetro utilizado en la operación.
     */
    void realizarMovimiento(Paso paso);
}