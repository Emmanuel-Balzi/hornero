package app;

/**
 * Punto de entrada de la aplicación Turnos de Cancha.
 *
 * Más adelante acá se elige el puerto, se registran los controladores
 * y los servicios, y se arranca el servidor de Hornero.
 */
public class Main {

    public static final int PUERTO = 3000;

    public static void main(String[] args) {
        System.out.println("Turnos de Cancha - usando el framework Hornero");
        System.out.println("Puerto configurado: " + PUERTO);
    }
}
