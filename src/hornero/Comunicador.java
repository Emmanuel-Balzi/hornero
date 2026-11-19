package hornero;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Recibe y envía líneas de texto.
 *
 * No sabe si del otro lado hay una consola o un socket: solo conoce un
 * InputStream y un OutputStream. Por consola sería
 * new Comunicador(System.in, System.out); por la red,
 * new Comunicador(socket.getInputStream(), socket.getOutputStream()).
 *
 * Usa UTF-8 explícito, igual que el ClienteConsola de la cátedra: así los
 * acentos llegan bien sin importar la configuración de la PC ni la versión de Java.
 */
public class Comunicador {

    private final BufferedReader entrada;
    private final PrintWriter salida;

    public Comunicador(InputStream in, OutputStream out) {
        this.entrada = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.salida = new PrintWriter(
                new OutputStreamWriter(out, StandardCharsets.UTF_8), true); // true = autoflush en cada println
    }

    /** Devuelve la próxima línea, o null si el cliente se desconectó. */
    public String recibir() throws IOException {
        return entrada.readLine();
    }

    public void enviar(String mensaje) {
        salida.println(mensaje);
    }
}
