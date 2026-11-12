package app;

public class Cancha {

    private int id;
    private String nombre;
    private int cantidadJugadores;

    public Cancha(int id, String nombre, int cantidadJugadores) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadJugadores = cantidadJugadores;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }

    @Override
    public String toString() {
        return "Cancha " + id +
                " - Nombre: " + nombre +
                " - Cantidad de jugadores: " + cantidadJugadores;
    }
}
