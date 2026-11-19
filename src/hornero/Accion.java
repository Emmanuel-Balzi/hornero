package hornero;

@FunctionalInterface
public interface Accion {

    String ejecutar(Solicitud solicitud) throws HorneroException;
}
