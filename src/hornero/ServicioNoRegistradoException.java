package hornero;

/**
 * Se lanza cuando se intenta obtener un servicio
 * que todavía no fue registrado en el contenedor.
 *
 * Es unchecked porque representa un error de configuración
 * o programación, no un error provocado por el cliente.
 */
public class ServicioNoRegistradoException extends RuntimeException {

    public ServicioNoRegistradoException(String nombreServicio) {
        super("El servicio " + nombreServicio + " no está registrado.");
    }
}
