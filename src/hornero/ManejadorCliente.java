package hornero;

import java.io.IOException;
import java.net.Socket;

/**
 * Lo que corre en el hilo de cada cliente: recibe una línea, la procesa
 * y responde, hasta que el cliente se desconecta.
 */
public class ManejadorCliente implements Runnable {

    private final Socket socket;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String cliente = socket.getRemoteSocketAddress().toString();
        System.out.println("[Hornero] Cliente conectado: " + cliente);

        try (socket) { // al salir del try se cierra el socket, pase lo que pase
            Comunicador comunicador =
                    new Comunicador(socket.getInputStream(), socket.getOutputStream());

            String linea;
            while ((linea = comunicador.recibir()) != null) {
                comunicador.enviar(procesar(linea));
            }
        } catch (IOException e) {
            System.out.println("[Hornero] Conexion perdida con " + cliente + ": " + e.getMessage());
        }

        System.out.println("[Hornero] Cliente desconectado: " + cliente);
    }

    /**
     * Try/catch global: pase lo que pase, devuelve una respuesta y la
     * conexión sigue viva.
     */
    private String procesar(String linea) {
        try {
            // ETAPA 1 (eco): solo para probar la red.
            // Cuando estén ParserSolicitudes y Router de Bruno, va el parseo y el ruteo.
            return "OK eco: " + linea;
        } catch (Exception e) {
            return "ERROR interno: " + e.getMessage();
        }
    }
}
