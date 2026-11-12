package hornero;

import java.util.List;

public class ParserSolicitudes {

    public Solicitud parsear(String linea)
            throws SolicitudInvalidaException {

        // Comprobar que la solicitud no esté vacía
        if (linea == null || linea.isBlank()) {
            throw new SolicitudInvalidaException(
                "La solicitud no puede estar vacía."
            );
        }

        // Separar el texto utilizando "/"
        String[] partes = linea.trim().split("/", -1);

        // Comprobar que existan al menos recurso y acción
        if (partes.length < 2) {
            throw new SolicitudInvalidaException(
                "Formato inválido. Se esperaba recurso/accion/parametros."
            );
        }

        // Obtener recurso y acción
        String recurso = partes[0].trim();
        String accion = partes[1].trim();

        // Comprobar que recurso y acción no estén vacíos
        if (recurso.isEmpty() || accion.isEmpty()) {
            throw new SolicitudInvalidaException(
                "El recurso y la acción no pueden estar vacíos."
            );
        }

        // Obtener los parámetros restantes
        List<String> parametros = List.of(partes).subList(2, partes.length);

        // Crear y devolver la solicitud
        return new Solicitud(recurso, accion, parametros);
    }
}
