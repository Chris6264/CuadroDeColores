package mx.tecnm.culiacan;

import java.util.LinkedList;
import java.util.List;

/**
 * Almacena un estado de búsqueda, su costo y el enlace con su predecesor.
 */
public class Nodo {
    private final Estado estado;
    private final Nodo padre;
    private final Paso paso;
    private final int costoAcumulado;
    private final double costoTotal;

    /**
     * Construye una instancia de Nodo con los parámetros indicados.
     * @param estado Estado utilizado en la operación.
     * @param padre Padre utilizado en la operación.
     * @param paso Paso utilizado en la operación.
     * @param costoAcumulado Costo acumulado utilizado en la operación.
     * @param heuristica Heuristica utilizado en la operación.
     * @param peso Peso utilizado en la operación.
     */
    public Nodo(Estado estado, Nodo padre, Paso paso, int costoAcumulado, int heuristica, double peso) {
        this.estado = estado;
        this.padre = padre;
        this.paso = paso;
        this.costoAcumulado = costoAcumulado;
        this.costoTotal = costoAcumulado + peso * heuristica;
    }

    /**
     * Obtiene estado.
     * @return Resultado de get estado.
     */
    public Estado getEstado() { return estado; }

    /**
     * Obtiene costo acumulado.
     * @return Resultado de get costo acumulado.
     */
    public int getCostoAcumulado() { return costoAcumulado; }

    /**
     * Obtiene costo total.
     * @return Resultado de get costo total.
     */
    public double getCostoTotal() { return costoTotal; }

    /**
     * Reconstruye camino.
     * @return Resultado de reconstruir camino.
     */
    public List<Paso> reconstruirCamino() {
        List<Paso> camino = new LinkedList<>();

        for (Nodo nodo = this; nodo.padre != null; nodo = nodo.padre)
            camino.addFirst(nodo.paso);

        return camino;
    }
}