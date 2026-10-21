package mx.tecnm.culiacan;

public class ReglasException extends RuntimeException {
    public ReglasException(String message) {
        super(message);
    }

    public ReglasException(String message, Throwable causa) {
        super(message, causa);
    }
}
