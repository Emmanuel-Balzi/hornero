
package app;

import hornero.Accion;
import hornero.Controlador;
import hornero.Solicitud;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControladorCanchas implements Controlador {

    // Lista donde guardamos las canchas registradas.
    private final List<Cancha> canchas = new ArrayList<>();

    // Identificador que se asigna a cada cancha.
    private int siguienteId = 1;

    // Indica qué recurso maneja este controlador.
    @Override
    public String getRecurso() {
        return "canchas";
    }

    // Registra las acciones disponibles para el Router.
    @Override
    public void registrarAcciones(Map<String, Accion> acciones) {
        acciones.put("alta", this::alta);
        acciones.put("listar", this::listar);
    }

    // Registra una nueva cancha.
    private String alta(Solicitud solicitud) {

        List<String> parametros = solicitud.getParametros();

        if (parametros.size() != 2) {
            return "ERROR: Se esperaba nombre y cantidad de jugadores.";
        }

        String nombre = parametros.get(0);

        if (nombre.isBlank()) {
            return "ERROR: El nombre no puede estar vacío.";
        }

        try {
            int cantidadJugadores = Integer.parseInt(parametros.get(1));

            if (cantidadJugadores <= 0) {
                return "ERROR: La cantidad de jugadores debe ser mayor a cero.";
            }

            Cancha cancha = new Cancha(
                siguienteId,
                nombre,
                cantidadJugadores
            );

            canchas.add(cancha);
            siguienteId++;

            return "OK: Cancha creada correctamente. " + cancha;

        } catch (NumberFormatException e) {
            return "ERROR: La cantidad de jugadores debe ser un número.";
        }
    }

    // Devuelve todas las canchas registradas.
    private String listar(Solicitud solicitud) {

        if (canchas.isEmpty()) {
            return "OK: No hay canchas registradas.";
        }

        StringBuilder resultado = new StringBuilder("OK: Canchas registradas:");

        for (Cancha cancha : canchas) {
            resultado.append("\n").append(cancha);
        }

        return resultado.toString();
    }
}
