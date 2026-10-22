package mx.tecnm.culiacan;

/**
 * Señala un incumplimiento de las reglas del juego.
 */
public class ReglasException extends RuntimeException {
    /**
     * Construye una instancia de ReglasException con los parámetros indicados.
     * @param message Message utilizado en la operación.
     */
    public ReglasException(String message) {
        super(message);
    }

    /**
     * Construye una instancia de ReglasException con los parámetros indicados.
     * @param message Message utilizado en la operación.
     * @param causa Causa utilizado en la operación.
     */
    public ReglasException(String message, Throwable causa) {
        super(message, causa);
    }
}
