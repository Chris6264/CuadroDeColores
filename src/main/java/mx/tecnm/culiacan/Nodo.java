package mx.tecnm.culiacan;

import java.util.LinkedList;
import java.util.List;

public class Nodo {
    private final Estado estado;
    private final Nodo padre;
    private final Paso paso;
    private final int costoAcumulado;   // g: movimientos hechos hasta aquí
    private final int costoTotal;       // f = g + heurística

    public Nodo(Estado estado, Nodo padre, Paso paso, int costoAcumulado, int heuristica) {
        this.estado = estado;
        this.padre = padre;
        this.paso = paso;
        this.costoAcumulado = costoAcumulado;
        this.costoTotal = costoAcumulado + heuristica;
    }

    public Estado getEstado() { return estado; }
    public int getCostoAcumulado() { return costoAcumulado; }
    public int getCostoTotal() { return costoTotal; }

    public List<Paso> reconstruirCamino() {
        LinkedList<Paso> camino = new LinkedList<>();
        for (Nodo nodo = this; nodo.padre != null; nodo = nodo.padre)
            camino.addFirst(nodo.paso);
        return camino;
    }
}