package hornero;

import java.util.List;

/**
 * Representa una solicitud ya parseada.
 * Ejemplo: "turnos/reservar/1/18" -> recurso "turnos", accion "reservar",
 * parametros ["1", "18"].
 */
public class Solicitud {

    private final String recurso;
    private final String accion;
    private final List<String> parametros;

    public Solicitud(String recurso, String accion, List<String> parametros) {
        this.recurso = recurso;
        this.accion = accion;
        this.parametros = List.copyOf(parametros);
    }

    public String getRecurso() {
        return recurso;
    }

    public String getAccion() {
        return accion;
    }

    public List<String> getParametros() {
        return parametros;
    }

    @Override
    public String toString() {
        return recurso + "/" + accion + " " + parametros;
    }
}
