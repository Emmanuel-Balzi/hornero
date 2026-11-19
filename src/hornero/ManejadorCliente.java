package hornero;

import java.io.IOException;
import java.net.Socket;

public class ManejadorCliente implements Runnable {

    private final Socket socket;
    private final ParserSolicitudes parser;
    private final Router router;

    public ManejadorCliente(
            Socket socket,
            ParserSolicitudes parser,
            Router router) {

        this.socket = socket;
        this.parser = parser;
        this.router = router;
    }

    @Override
    public void run() {
        String cliente = socket.getRemoteSocketAddress().toString();
        System.out.println("[Hornero] Cliente conectado: " + cliente);

        // Una sesión por conexión: arranca como INVITADO y muere con el hilo.
        Sesion sesion = new Sesion();

        try (socket) {
            Comunicador comunicador =
                    new Comunicador(socket.getInputStream(), socket.getOutputStream());

            String linea;

            while ((linea = comunicador.recibir()) != null) {
                comunicador.enviar(procesar(linea, sesion));
            }

        } catch (IOException e) {
            System.out.println("[Hornero] Conexion perdida con "
                    + cliente + ": " + e.getMessage());
        }

        System.out.println("[Hornero] Cliente desconectado: " + cliente + " - " + sesion);
    }

    private String procesar(String linea, Sesion sesion) {
        try {
            // Convertimos el texto recibido en una solicitud.
            Solicitud solicitud = parser.parsear(linea);

            // Le pegamos la sesión de este cliente.
            solicitud.setSesion(sesion);

            // Enviamos la solicitud al Router para ejecutar la acción.
            return router.despachar(solicitud);

        } catch (HorneroException e) {
            return "ERROR: " + e.getMessage();

        } catch (Exception e) {
            return "ERROR interno: " + e.getMessage();
        }
    }
}
