package app;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de administrar los turnos.
 */
public class ServicioTurnos {

    private final List<Turno> turnos = new ArrayList<>();

    private final ServicioCanchas servicioCanchas;

    private int siguienteId = 1;

    public ServicioTurnos(ServicioCanchas servicioCanchas) {
        this.servicioCanchas = servicioCanchas;
    }

    /**
     * Reserva una cancha en un horario determinado.
     */
    public synchronized Turno reservar(
            int idCancha, int hora, String usuario) {

        if (!servicioCanchas.existe(idCancha)) {
            throw new IllegalArgumentException(
                "La cancha indicada no existe."
            );
        }

        for (Turno turno : turnos) {
            if (turno.getIdCancha() == idCancha
                    && turno.getHora() == hora) {

                throw new IllegalArgumentException(
                    "Ese horario ya está reservado."
                );
            }
        }

        Turno nuevoTurno = new Turno(
            siguienteId,
            idCancha,
            hora,
            usuario
        );

        turnos.add(nuevoTurno);
        siguienteId++;

        return nuevoTurno;
    }

    /**
     * Busca un turno por su identificador.
     */
    public synchronized Turno buscar(int idTurno) {

        for (Turno turno : turnos) {
            if (turno.getId() == idTurno) {
                return turno;
            }
        }

        return null;
    }

    /**
     * Cancela un turno.
     */
    public synchronized boolean cancelar(int idTurno) {

        Turno turno = buscar(idTurno);

        if (turno == null) {
            return false;
        }

        return turnos.remove(turno);
    }

    /**
     * Devuelve los turnos de un usuario.
     */
    public synchronized List<Turno> listarPorUsuario(String usuario) {

        List<Turno> resultado = new ArrayList<>();

        for (Turno turno : turnos) {
            if (turno.getUsuario().equals(usuario)) {
                resultado.add(turno);
            }
        }

        return resultado;
    }

    /**
     * Devuelve todos los turnos registrados.
     */
    public synchronized List<Turno> listarTodos() {
        return new ArrayList<>(turnos);
    }
}
