package hornero;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Escucha en un puerto y le da un hilo propio a cada cliente que se conecta.
 */
public class Servidor {

    private final int puerto;

    public Servidor(int puerto) {
        this.puerto = puerto;
    }

    public void iniciar() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("[Hornero] Escuchando en el puerto " + puerto);

            while (true) {
                Socket cliente = serverSocket.accept(); // se bloquea hasta que alguien se conecta
                new Thread(new ManejadorCliente(cliente)).start(); // un hilo por cliente
            }
        }
    }
}
