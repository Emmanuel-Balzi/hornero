package hornero;

/**
 * Interfaz funcional para validar valores.
 *
 * Cada validador implementa su propia regla de validación.
 */
@FunctionalInterface
public interface Validador {

    void validar(String valor) throws ValidacionException;
}
