package app;

import hornero.Accion;
import hornero.ContenedorServicios;
import hornero.Controlador;
import hornero.Rol;
import hornero.Sesion;
import hornero.Solicitud;
import hornero.ValidacionException;
import java.util.List;
import java.util.Map;

/**
 * Recurso "sesion": login y logout.
 *
 * Las dos acciones se pueden usar sin estar logueado (Rol.INVITADO);
 * todas las demás de la aplicación piden al menos Rol.USUARIO.
 */
public class ControladorSesion implements Controlador {

    private final ServicioUsuarios servicioUsuarios;

    public ControladorSesion(ContenedorServicios contenedor) {
        this.servicioUsuarios = contenedor.obtener(ServicioUsuarios.class);
    }

    @Override
    public String getRecurso() {
        return "sesion";
    }

    @Override
    public void registrarAcciones(Map<String, Accion> acciones) {
        acciones.put("login", this::login);
        acciones.put("logout", this::logout);
    }

    @Override
    public void registrarPermisos(Map<String, Rol> permisos) {
        permisos.put("login", Rol.INVITADO);
        permisos.put("logout", Rol.INVITADO);
    }

    // sesion/login/{usuario}/{clave}
    private String login(Solicitud solicitud) throws ValidacionException {
        List<String> parametros = solicitud.getParametros();
        if (parametros.size() != 2) {
            throw new ValidacionException("Se esperaba usuario y clave.");
        }

        Sesion sesion = solicitud.getSesion();
        if (sesion.estaLogueado()) {
            throw new ValidacionException(
                    "Ya iniciaste sesión como " + sesion.getUsuario() + ". Cerrá sesión primero.");
        }

        String usuario = parametros.get(0);
        String clave = parametros.get(1);

        // Si el Optional está vacío, orElseThrow tira la excepción.
        Rol rol = servicioUsuarios.autenticar(usuario, clave)
                .orElseThrow(() -> new ValidacionException("Usuario o clave incorrectos."));

        sesion.iniciar(usuario, rol);
        return "Bienvenido, " + usuario + " (" + rol + ").";
    }

    // sesion/logout
    private String logout(Solicitud solicitud) throws ValidacionException {
        Sesion sesion = solicitud.getSesion();
        if (!sesion.estaLogueado()) {
            throw new ValidacionException("No hay ninguna sesión iniciada.");
        }

        String usuario = sesion.getUsuario();
        sesion.cerrar();
        return "Sesión cerrada. Hasta luego, " + usuario + ".";
    }
}
