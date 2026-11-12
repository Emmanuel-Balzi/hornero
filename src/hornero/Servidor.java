package hornero;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    private final int puerto;
    private final ParserSolicitudes parser;
    private final Router router;

    public Servidor(int puerto, ParserSolicitudes parser, Router router) {
        this.puerto = puerto;
        this.parser = parser;
        this.router = router;
    }

    public void iniciar() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {

            System.out.println("[Hornero] Escuchando en el puerto " + puerto);

            while (true) {
                Socket cliente = serverSocket.accept();

                ManejadorCliente manejador =
                        new ManejadorCliente(cliente, parser, router);

                new Thread(manejador).start();
            }
        }
    }
}
