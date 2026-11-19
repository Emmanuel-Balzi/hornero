package app;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de administrar las canchas.
 *
 * Centraliza la lista y la generación de identificadores
 * para que todos los controladores compartan los mismos datos.
 */
public class ServicioCanchas {

    private final List<Cancha> canchas = new ArrayList<>();

    private int siguienteId = 1;

    /**
     * Crea una cancha nueva y la agrega a la lista.
     */
    public synchronized Cancha alta(String nombre, int cantidadJugadores) {

        Cancha cancha = new Cancha(
            siguienteId,
            nombre,
            cantidadJugadores
        );

        canchas.add(cancha);
        siguienteId++;

        return cancha;
    }

    /**
     * Devuelve una copia de la lista de canchas.
     */
    public synchronized List<Cancha> listar() {
        return new ArrayList<>(canchas);
    }

    /**
     * Comprueba si existe una cancha con el ID indicado.
     */
    public synchronized boolean existe(int id) {

        for (Cancha cancha : canchas) {
            if (cancha.getId() == id) {
                return true;
            }
        }

        return false;
    }
}
