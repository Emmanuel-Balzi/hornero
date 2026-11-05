package hornero;

import java.util.Map;

/**
 * Interfaz que implementa cada controlador de la aplicación.
 * El framework solo conoce esta interfaz, nunca las clases concretas.
 */
public interface Controlador {

    /** Nombre del recurso que atiende, por ejemplo "turnos". */
    String getRecurso();

    /** El controlador agrega sus acciones: acciones.put("reservar", this::reservar). */
    void registrarAcciones(Map<String, Accion> acciones);
}
