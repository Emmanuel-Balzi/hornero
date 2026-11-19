package hornero;

/**
 * Roles posibles de una sesión, ordenados de menor a mayor permiso.
 *
 * El orden importa: alcanza() compara la posición (ordinal) de cada rol,
 * así un ADMIN puede hacer todo lo que puede un USUARIO, y un USUARIO
 * todo lo que puede un INVITADO.
 */
public enum Rol {

    INVITADO,   // conectado pero sin loguearse
    USUARIO,
    ADMIN;

    /** true si este rol tiene al menos el permiso del rol requerido. */
    public boolean alcanza(Rol requerido) {
        return this.ordinal() >= requerido.ordinal();
    }
}
