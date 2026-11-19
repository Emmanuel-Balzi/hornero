package hornero;

/**
 * Datos de UNA conexión: quién se logueó y con qué rol.
 *
 * La crea ManejadorCliente cuando el cliente se conecta y se pierde cuando
 * se desconecta (no se guarda en disco). Como cada cliente tiene su propio
 * hilo y su propia Sesion, no hace falta sincronizarla.
 */
public class Sesion {

    private String usuario;          // null mientras no se loguee
    private Rol rol = Rol.INVITADO;

    public void iniciar(String usuario, Rol rol) {
        this.usuario = usuario;
        this.rol = rol;
    }

    public void cerrar() {
        this.usuario = null;
        this.rol = Rol.INVITADO;
    }

    public boolean estaLogueado() {
        return usuario != null;
    }

    public String getUsuario() {
        return usuario;
    }

    public Rol getRol() {
        return rol;
    }

    @Override
    public String toString() {
        return estaLogueado() ? usuario + " (" + rol + ")" : "invitado";
    }
}
