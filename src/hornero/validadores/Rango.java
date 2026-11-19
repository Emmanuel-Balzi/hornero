package hornero.validadores;

import hornero.Validador;
import hornero.ValidacionException;

/**
 * Valida que un número entero esté dentro de un rango.
 */
public class Rango implements Validador {

    private final int minimo;
    private final int maximo;

    public Rango(int minimo, int maximo) {

        if (minimo > maximo) {
            throw new IllegalArgumentException(
                "El mínimo no puede ser mayor que el máximo."
            );
        }

        this.minimo = minimo;
        this.maximo = maximo;
    }

    @Override
    public void validar(String valor) throws ValidacionException {

        int numero;

        try {
            numero = Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new ValidacionException(
                "El valor debe ser un número entero."
            );
        }

        if (numero < minimo || numero > maximo) {
            throw new ValidacionException(
                "El número debe estar entre " + minimo + " y " + maximo + "."
            );
        }
    }
}
