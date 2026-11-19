package hornero;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contenedor que permite registrar y obtener servicios.
 *
 * Los servicios se guardan asociados a su clase.
 * Es compartido por los controladores de la aplicación.
 */
public class ContenedorServicios {

    private final Map<Class<?>, Object> servicios = new ConcurrentHashMap<>();

    /**
     * Registra un servicio usando su clase como identificador.
     */
    public <T> void registrar(Class<T> tipo, T servicio) {
        servicios.put(tipo, servicio);
    }

    /**
     * Busca y devuelve un servicio por su clase.
     */
    public <T> T obtener(Class<T> tipo) {

        Object servicio = servicios.get(tipo);

        if (servicio == null) {
            throw new ServicioNoRegistradoException(tipo.getSimpleName());
        }

        return tipo.cast(servicio);
    }
}
