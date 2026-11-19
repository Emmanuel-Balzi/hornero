package hornero;

import java.util.HashMap;
import java.util.Map;

public class Router {

    // Guarda las acciones de cada recurso.
    private final Map<String, Map<String, Accion>> rutas =
            new HashMap<>();

    // Guarda los permisos de cada acción.
    private final Map<String, Map<String, Rol>> permisos =
            new HashMap<>();

    // Registra un controlador y sus acciones.
    public void registrar(Controlador controlador) {

        Map<String, Accion> acciones = new HashMap<>();
        Map<String, Rol> permisosControlador = new HashMap<>();

        controlador.registrarAcciones(acciones);
        controlador.registrarPermisos(permisosControlador);

        rutas.put(controlador.getRecurso(), acciones);
        permisos.put(controlador.getRecurso(), permisosControlador);
    }

    // Busca y ejecuta la acción correspondiente.
    public String despachar(Solicitud solicitud)
            throws HorneroException {

        Map<String, Accion> acciones =
                rutas.get(solicitud.getRecurso());

        if (acciones == null) {
            throw new RutaNoEncontradaException(
                "El recurso '" + solicitud.getRecurso()
                + "' no existe."
            );
        }

        Accion accion = acciones.get(solicitud.getAccion());

        if (accion == null) {
            throw new RutaNoEncontradaException(
                "La acción '" + solicitud.getAccion()
                + "' no existe."
            );
        }

        // Buscamos el rol requerido.
        Map<String, Rol> permisosControlador =
                permisos.get(solicitud.getRecurso());

        Rol requerido = permisosControlador.getOrDefault(
                solicitud.getAccion(),
                Rol.USUARIO
        );

        // Comprobamos el rol de la sesión.
        Sesion sesion = solicitud.getSesion();

        Rol rolActual = sesion == null
                ? Rol.INVITADO
                : sesion.getRol();

        if (!rolActual.alcanza(requerido)) {
            throw new PermisoDenegadoException(
                "No tenés permisos para realizar esta acción."
            );
        }

        // Ejecutamos la acción.
        return accion.ejecutar(solicitud);
    }
}
