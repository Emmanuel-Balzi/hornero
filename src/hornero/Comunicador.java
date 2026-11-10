package hornero;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Recibe y envía líneas de texto.
 *
 * No sabe si del otro lado hay una consola o un socket: solo conoce un
 * InputStream y un OutputStream. Por consola sería
 * new Comunicador(System.in, System.out); por la red,
 * new Comunicador(socket.getInputStream(), socket.getOutputStream()).
 */
public class Comunicador {

    private final BufferedReader entrada;
    private final PrintWriter salida;

    public Comunicador(InputStream in, OutputStream out) {
        this.entrada = new BufferedReader(new InputStreamReader(in));
        this.salida = new PrintWriter(out, true); // true = autoflush en cada println
    }

    /** Devuelve la próxima línea, o null si el cliente se desconectó. */
    public String recibir() throws IOException {
        return entrada.readLine();
    }

    public void enviar(String mensaje) {
        salida.println(mensaje);
    }
}
