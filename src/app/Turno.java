package app;

/**
 * Representa un turno reservado por un usuario.
 */
public class Turno {

    private final int id;
    private final int idCancha;
    private final int hora;
    private final String usuario;

    public Turno(int id, int idCancha, int hora, String usuario) {
        this.id = id;
        this.idCancha = idCancha;
        this.hora = hora;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public int getIdCancha() {
        return idCancha;
    }

    public int getHora() {
        return hora;
    }

    public String getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return "Turno " + id
                + " - Cancha: " + idCancha
                + " - Hora: " + hora + ":00"
                + " - Usuario: " + usuario;
    }
}
