package app;

import hornero.ContenedorServicios;
import hornero.ParserSolicitudes;
import hornero.Router;
import hornero.Servidor;
import java.io.IOException;

public class Main {

    public static final int PUERTO = 3000;

    public static void main(String[] args) throws IOException {

        System.out.println("Turnos de Cancha - usando Hornero");

        // 1. Creamos el contenedor de servicios.
        ContenedorServicios contenedor = new ContenedorServicios();

        // 2. Creamos los servicios compartidos.
        ServicioCanchas servicioCanchas = new ServicioCanchas();

        ServicioTurnos servicioTurnos =
                new ServicioTurnos(servicioCanchas);

        // 3. Registramos los servicios en el contenedor.
        contenedor.registrar(
                ServicioCanchas.class,
                servicioCanchas
        );

        contenedor.registrar(
                ServicioTurnos.class,
                servicioTurnos
        );

        contenedor.registrar(
                ServicioUsuarios.class,
                new ServicioUsuarios()
        );

        // 4. Creamos el parser.
        ParserSolicitudes parser = new ParserSolicitudes();

        // 5. Creamos el Router.
        Router router = new Router();

        // 6. Registramos los controladores.
        router.registrar(new ControladorSesion(contenedor));
        router.registrar(new ControladorCanchas(contenedor));
        router.registrar(new ControladorTurnos(contenedor));

        // 7. Creamos e iniciamos el servidor.
        Servidor servidor = new Servidor(
                PUERTO,
                parser,
                router
        );

        servidor.iniciar();
    }
}
