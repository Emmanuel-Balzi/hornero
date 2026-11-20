package app;

import java.util.ArrayList;
import java.util.List;

/** Servicio encargado de administrar los turnos compartidos. */
public class ServicioTurnos {

    private final List<Turno> turnos = new ArrayList<>();
    private final ServicioCanchas servicioCanchas;
    private int siguienteId = 1;

    public ServicioTurnos(ServicioCanchas servicioCanchas) {
        this.servicioCanchas = servicioCanchas;
    }

    /** Reserva una cancha en una hora. La operación completa es atómica. */
    public synchronized Turno reservar(int idCancha, int hora, String usuario) {
        if (!servicioCanchas.existe(idCancha)) {
            throw new IllegalArgumentException("La cancha indicada no existe.");
        }

        boolean ocupado = turnos.stream().anyMatch(turno ->
                turno.getIdCancha() == idCancha && turno.getHora() == hora);

        if (ocupado) {
            throw new IllegalArgumentException("Ese horario ya está reservado.");
        }

        Turno nuevo = new Turno(siguienteId++, idCancha, hora, usuario);
        turnos.add(nuevo);
        return nuevo;
    }

    public synchronized Turno buscar(int idTurno) {
        return turnos.stream()
                .filter(turno -> turno.getId() == idTurno)
                .findFirst()
                .orElse(null);
    }

    /**
     * Cancela solamente si el turno existe y pertenece al usuario.
     * La búsqueda, comprobación de dueño y eliminación ocurren bajo el mismo lock.
     */
    public synchronized boolean cancelar(int idTurno, String usuario) {
        Turno turno = buscar(idTurno);

        if (turno == null) {
            return false;
        }

        if (!turno.getUsuario().equals(usuario)) {
            throw new IllegalArgumentException(
                    "No podés cancelar un turno de otro usuario.");
        }

        return turnos.remove(turno);
    }

    public synchronized List<Turno> listarPorUsuario(String usuario) {
        return turnos.stream()
                .filter(turno -> turno.getUsuario().equals(usuario))
                .toList();
    }

    public synchronized List<Turno> listarTodos() {
        return new ArrayList<>(turnos);
    }
}
