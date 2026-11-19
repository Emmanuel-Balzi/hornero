package app;

import hornero.Accion;
import hornero.Controlador;
import hornero.Solicitud;
import hornero.Rol;
import hornero.ValidacionException;
import hornero.ContenedorServicios;
import hornero.validadores.EnteroPositivo;
import hornero.validadores.Rango;

import java.util.List;
import java.util.Map;

/**
 * Controlador encargado de gestionar las solicitudes de turnos.
 */
public class ControladorTurnos implements Controlador {

    private final ServicioTurnos servicioTurnos;

    private final EnteroPositivo validarId = new EnteroPositivo();

    // Los horarios permitidos van de 8 a 23.
    private final Rango validarHora = new Rango(8, 23);

    public ControladorTurnos(ContenedorServicios contenedor) {
        this.servicioTurnos = contenedor.obtener(ServicioTurnos.class);
    }

    @Override
    public String getRecurso() {
        return "turnos";
    }

    /**
     * Registra las cuatro acciones del controlador.
     */
    @Override
    public void registrarAcciones(Map<String, Accion> acciones) {
        acciones.put("reservar", this::reservar);
        acciones.put("cancelar", this::cancelar);
        acciones.put("mios", this::mios);
        acciones.put("del-dia", this::delDia);
    }

    /**
     * Define qué rol necesita cada acción.
     */
    @Override
    public void registrarPermisos(Map<String, Rol> permisos) {
        permisos.put("reservar", Rol.USUARIO);
        permisos.put("cancelar", Rol.USUARIO);
        permisos.put("mios", Rol.USUARIO);
        permisos.put("del-dia", Rol.ADMIN);
    }

    /**
     * Reserva una cancha en un horario.
     */
    private String reservar(Solicitud solicitud)
            throws ValidacionException {

        List<String> parametros = solicitud.getParametros();

        if (parametros.size() != 2) {
            throw new ValidacionException(
                "Se esperaba el ID de la cancha y la hora."
            );
        }

        String idTexto = parametros.get(0);
        String horaTexto = parametros.get(1);

        validarId.validar(idTexto);
        validarHora.validar(horaTexto);

        int idCancha = Integer.parseInt(idTexto);
        int hora = Integer.parseInt(horaTexto);

        String usuario = solicitud.getSesion().getUsuario();

        try {
            Turno turno = servicioTurnos.reservar(
                idCancha,
                hora,
                usuario
            );

            return "Turno reservado correctamente. " + turno;

        } catch (IllegalArgumentException e) {
            throw new ValidacionException(e.getMessage());
        }
    }

    /**
     * Cancela un turno si pertenece al usuario conectado.
     */
    private String cancelar(Solicitud solicitud)
            throws ValidacionException {

        List<String> parametros = solicitud.getParametros();

        if (parametros.size() != 1) {
            throw new ValidacionException(
                "Se esperaba el ID del turno."
            );
        }

        validarId.validar(parametros.get(0));

        int idTurno = Integer.parseInt(parametros.get(0));

        Turno turno = servicioTurnos.buscar(idTurno);

        if (turno == null) {
            throw new ValidacionException(
                "El turno indicado no existe."
            );
        }

        String usuario = solicitud.getSesion().getUsuario();

        if (!turno.getUsuario().equals(usuario)) {
            throw new ValidacionException(
                "No podés cancelar un turno de otro usuario."
            );
        }

        servicioTurnos.cancelar(idTurno);

        return "Turno cancelado correctamente.";
    }

    /**
     * Lista los turnos del usuario conectado.
     */
    private String mios(Solicitud solicitud) {

        String usuario = solicitud.getSesion().getUsuario();

        List<Turno> turnos =
            servicioTurnos.listarPorUsuario(usuario);

        if (turnos.isEmpty()) {
            return "No tenés turnos reservados.";
        }

        StringBuilder resultado =
            new StringBuilder("Tus turnos:");

        for (Turno turno : turnos) {
            resultado.append("\n").append(turno);
        }

        return resultado.toString();
    }

    /**
     * Lista todos los turnos. Solo ADMIN puede ejecutar esta acción.
     */
    private String delDia(Solicitud solicitud) {

        List<Turno> turnos = servicioTurnos.listarTodos();

        if (turnos.isEmpty()) {
            return "No hay turnos reservados.";
        }

        StringBuilder resultado =
            new StringBuilder("Agenda de turnos:");

        for (Turno turno : turnos) {
            resultado.append("\n").append(turno);
        }

        return resultado.toString();
    }
}
