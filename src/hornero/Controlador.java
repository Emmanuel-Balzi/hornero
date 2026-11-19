package hornero;

import java.util.Map;

public interface Controlador {

    String getRecurso();

    void registrarAcciones(Map<String, Accion> acciones);

    default void registrarPermisos(Map<String, Rol> permisos) {
    }
}

