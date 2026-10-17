package mx.tecnm.culiacan;

public class ReglasCuadro implements Reglas {
    private final int minimoNumeroDeLados;
    private final int maximoNumeroDeLados;

    public ReglasCuadro(int minimoNumeroDeLados, int maximoNumeroDeLados) {
        this.minimoNumeroDeLados = minimoNumeroDeLados;
        this.maximoNumeroDeLados = maximoNumeroDeLados;
    }

    @Override
    public void validarCampos(int numeroDeLados) {
        if (numeroDeLados < minimoNumeroDeLados) throw new ReglasException("El número de lados debe de ser minimo " + minimoNumeroDeLados);
        else if (numeroDeLados > maximoNumeroDeLados) throw new ReglasException("El número de lados debe de ser máximo " + maximoNumeroDeLados);
    }
}
