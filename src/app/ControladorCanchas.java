package app;

import hornero.Accion;
import hornero.ContenedorServicios;
import hornero.Controlador;
import hornero.Rol;
import hornero.Solicitud;
import hornero.ValidacionException;
import hornero.validadores.EnteroPositivo;
import hornero.validadores.LargoMaximo;
import java.util.List;
import java.util.Map;

public class ControladorCanchas implements Controlador {

    private final ServicioCanchas servicioCanchas;

    // Validadores reutilizables.
    private final EnteroPositivo validarJugadores =
            new EnteroPositivo();

    private final LargoMaximo validarNombre =
            new LargoMaximo(40);

    // Recibimos el contenedor y obtenemos el servicio compartido.
    public ControladorCanchas(ContenedorServicios contenedor) {
        this.servicioCanchas =
                contenedor.obtener(ServicioCanchas.class);
    }

    @Override
    public String getRecurso() {
        return "canchas";
    }

    // Registramos las acciones.
    @Override
    public void registrarAcciones(Map<String, Accion> acciones) {
        acciones.put("alta", this::alta);
        acciones.put("listar", this::listar);
    }

    // Definimos los permisos.
    @Override
    public void registrarPermisos(Map<String, Rol> permisos) {
        permisos.put("alta", Rol.ADMIN);
        permisos.put("listar", Rol.USUARIO);
    }

    // Registra una nueva cancha.
    private String alta(Solicitud solicitud)
            throws ValidacionException {

        List<String> parametros = solicitud.getParametros();

        if (parametros.size() != 2) {
            throw new ValidacionException(
                    "Se esperaba nombre y cantidad de jugadores."
            );
        }

        String nombre = parametros.get(0);
        String jugadores = parametros.get(1);

        validarNombre.validar(nombre);
        validarJugadores.validar(jugadores);

        int cantidadJugadores = Integer.parseInt(jugadores);

        Cancha cancha = servicioCanchas.alta(
                nombre,
                cantidadJugadores
        );

        return "Cancha creada correctamente. " + cancha;
    }

    // Lista todas las canchas.
    private String listar(Solicitud solicitud) {

        List<Cancha> canchas = servicioCanchas.listar();

        if (canchas.isEmpty()) {
            return "No hay canchas registradas.";
        }

        StringBuilder resultado =
                new StringBuilder("Canchas registradas:");

        for (Cancha cancha : canchas) {
            resultado.append("\n").append(cancha);
        }

        return resultado.toString();
    }
}
