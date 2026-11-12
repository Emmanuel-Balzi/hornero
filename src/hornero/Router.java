
package hornero;

import java.util.HashMap;
import java.util.Map;

public class Router {

    // Guarda los recursos y las acciones disponibles para cada uno.
    private final Map<String, Map<String, Accion>> rutas = new HashMap<>();

    // Registra un controlador y todas las acciones que ofrece.
    public void registrar(Controlador controlador) {

        Map<String, Accion> acciones = new HashMap<>();

        controlador.registrarAcciones(acciones);

        rutas.put(controlador.getRecurso(), acciones);
    }

    // Busca el recurso y la acción correspondiente a una solicitud.
    public String despachar(Solicitud solicitud)
            throws RutaNoEncontradaException {

        // Buscamos las acciones del recurso solicitado.
        Map<String, Accion> acciones = rutas.get(solicitud.getRecurso());

        if (acciones == null) {
            throw new RutaNoEncontradaException(
                "El recurso '" + solicitud.getRecurso() + "' no existe."
            );
        }

        // Buscamos la acción dentro del recurso.
        Accion accion = acciones.get(solicitud.getAccion());

        if (accion == null) {
            throw new RutaNoEncontradaException(
                "La acción '" + solicitud.getAccion() + "' no existe."
            );
        }

        // Ejecutamos la acción y devolvemos su respuesta.
        return accion.ejecutar(solicitud);
    }
}
