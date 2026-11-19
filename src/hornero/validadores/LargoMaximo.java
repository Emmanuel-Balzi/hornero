package hornero.validadores;

import hornero.Validador;
import hornero.ValidacionException;

/**
 * Valida que un texto no esté vacío
 * y que no supere una cantidad máxima de caracteres.
 */
public class LargoMaximo implements Validador {

    private final int maximo;

    public LargoMaximo(int maximo) {

        if (maximo <= 0) {
            throw new IllegalArgumentException(
                "El máximo debe ser mayor que cero."
            );
        }

        this.maximo = maximo;
    }

    @Override
    public void validar(String valor) throws ValidacionException {

        if (valor == null || valor.isBlank()) {
            throw new ValidacionException(
                "El texto no puede estar vacío."
            );
        }

        if (valor.length() > maximo) {
            throw new ValidacionException(
                "El texto no puede superar los " + maximo + " caracteres."
            );
        }
    }
}
