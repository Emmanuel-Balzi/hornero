package app;

import hornero.Rol;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Usuarios de la aplicación, fijos en memoria.
 *
 * Se cargan una sola vez en el constructor y después solo se leen,
 * por eso varios hilos pueden usarlo a la vez sin synchronized.
 */
public class ServicioUsuarios {

    private final Map<String, String> claves = new HashMap<>();
    private final Map<String, Rol> roles = new HashMap<>();

    public ServicioUsuarios() {
        agregar("admin", "admin", Rol.ADMIN);
        agregar("emma", "1234", Rol.USUARIO);
        agregar("bruno", "1234", Rol.USUARIO);
    }

    private void agregar(String usuario, String clave, Rol rol) {
        claves.put(usuario, clave);
        roles.put(usuario, rol);
    }

    /**
     * Si el usuario y la clave son correctos, devuelve su rol.
     * Si no, devuelve un Optional vacío.
     */
    public Optional<Rol> autenticar(String usuario, String clave) {
        if (!clave.equals(claves.get(usuario))) {
            return Optional.empty();
        }
        return Optional.of(roles.get(usuario));
    }
}
