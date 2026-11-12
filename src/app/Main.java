
package app;

import hornero.ParserSolicitudes;
import hornero.Router;
import hornero.Servidor;
import java.io.IOException;

public class Main {

    public static final int PUERTO = 3000;

    public static void main(String[] args) throws IOException {

        System.out.println("Turnos de Cancha - usando el framework Hornero");

        // Creamos el parser que interpreta las solicitudes.
        ParserSolicitudes parser = new ParserSolicitudes();

        // Creamos el router que dirige las solicitudes.
        Router router = new Router();

        // Registramos el controlador de canchas en el router.
        router.registrar(new ControladorCanchas());

        // Creamos el servidor y le pasamos el parser y el router.
        Servidor servidor = new Servidor(PUERTO, parser, router);

        // Iniciamos el servidor.
        servidor.iniciar();
    }
}
