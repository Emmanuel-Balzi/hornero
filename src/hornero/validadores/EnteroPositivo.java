package hornero.validadores;

import hornero.Validador;
import hornero.ValidacionException;

public class EnteroPositivo implements Validador {

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

        if (numero <= 0) {
            throw new ValidacionException(
                "El número debe ser mayor a cero."
            );
        }
    }
}
