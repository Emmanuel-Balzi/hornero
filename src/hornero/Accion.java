package hornero;

/**
 * Una acción de un controlador: recibe la solicitud y devuelve el texto
 * de la respuesta. Al ser interfaz funcional, el controlador la registra
 * con una referencia a método (por ejemplo this::reservar).
 */
@FunctionalInterface
public interface Accion {
    String ejecutar(Solicitud solicitud);
}
