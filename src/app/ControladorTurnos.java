package app;

import hornero.Accion;
import hornero.ContenedorServicios;
import hornero.Controlador;
import hornero.Rol;
import hornero.Solicitud;
import hornero.ValidacionException;
import hornero.validadores.EnteroPositivo;
import hornero.validadores.Rango;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Controlador encargado de gestionar las solicitudes de turnos. */
public class ControladorTurnos implements Controlador {

    private final ServicioTurnos servicioTurnos;
    private final EnteroPositivo validarId = new EnteroPositivo();
    private final Rango validarHora = new Rango(8, 23);

    public ControladorTurnos(ContenedorServicios contenedor) {
        this.servicioTurnos = contenedor.obtener(ServicioTurnos.class);
    }

    @Override
    public String getRecurso() {
        return "turnos";
    }

    @Override
    public void registrarAcciones(Map<String, Accion> acciones) {
        acciones.put("reservar", this::reservar);
        acciones.put("cancelar", this::cancelar);
        acciones.put("mios", this::mios);
        acciones.put("del-dia", this::delDia);
    }

    @Override
    public void registrarPermisos(Map<String, Rol> permisos) {
        permisos.put("reservar", Rol.USUARIO);
        permisos.put("cancelar", Rol.USUARIO);
        permisos.put("mios", Rol.USUARIO);
        permisos.put("del-dia", Rol.ADMIN);
    }

    private String reservar(Solicitud solicitud) throws ValidacionException {
        List<String> parametros = solicitud.getParametros();
        if (parametros.size() != 2) {
            throw new ValidacionException(
                    "Se esperaba el ID de la cancha y la hora.");
        }

        String idTexto = parametros.get(0);
        String horaTexto = parametros.get(1);
        validarId.validar(idTexto);
        validarHora.validar(horaTexto);

        int idCancha = Integer.parseInt(idTexto);
        int hora = Integer.parseInt(horaTexto);
        String usuario = solicitud.getSesion().getUsuario();

        try {
            Turno turno = servicioTurnos.reservar(idCancha, hora, usuario);
            return "Turno reservado correctamente. " + turno;
        } catch (IllegalArgumentException e) {
            throw new ValidacionException(e.getMessage());
        }
    }

    private String cancelar(Solicitud solicitud) throws ValidacionException {
        List<String> parametros = solicitud.getParametros();
        if (parametros.size() != 1) {
            throw new ValidacionException("Se esperaba el ID del turno.");
        }

        validarId.validar(parametros.get(0));
        int idTurno = Integer.parseInt(parametros.get(0));
        String usuario = solicitud.getSesion().getUsuario();

        try {
            boolean cancelado = servicioTurnos.cancelar(idTurno, usuario);
            if (!cancelado) {
                throw new ValidacionException("El turno indicado no existe.");
            }
            return "Turno cancelado correctamente.";
        } catch (IllegalArgumentException e) {
            throw new ValidacionException(e.getMessage());
        }
    }

    private String mios(Solicitud solicitud) {
        String usuario = solicitud.getSesion().getUsuario();
        List<Turno> turnos = servicioTurnos.listarPorUsuario(usuario);

        if (turnos.isEmpty()) {
            return "No tenés turnos reservados.";
        }

        String detalle = turnos.stream()
                .map(Turno::toString)
                .collect(Collectors.joining("\n"));
        return "Tus turnos:\n" + detalle;
    }

    private String delDia(Solicitud solicitud) {
        List<Turno> turnos = servicioTurnos.listarTodos();

        if (turnos.isEmpty()) {
            return "No hay turnos reservados.";
        }

        String detalle = turnos.stream()
                .map(Turno::toString)
                .collect(Collectors.joining("\n"));
        return "Agenda de turnos:\n" + detalle;
    }
}
