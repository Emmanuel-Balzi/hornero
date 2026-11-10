package hornero;

/**
 * Excepción base de todo el framework.
 *
 * Es CHECKED (extiende Exception) a propósito: son errores esperables
 * (el cliente escribió mal, pidió algo que no existe) y el compilador
 * obliga al framework a atraparlos y responder "ERROR ..." en lugar
 * de dejar que se corte la conexión.
 */
public class HorneroException extends Exception {

    public HorneroException(String mensaje) {
        super(mensaje);
    }
}
