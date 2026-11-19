package hornero;

/**
 * El usuario de la sesión no tiene el rol que pide la acción.
 * Checked, como el resto de los errores que puede provocar el cliente:
 * el framework la atrapa y responde "ERROR: ...".
 */
public class PermisoDenegadoException extends HorneroException {

    public PermisoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
