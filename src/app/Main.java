package app;

import java.io.IOException;

import hornero.Servidor;

/**
 * Punto de entrada de la aplicación Turnos de Cancha.
 * Elige el puerto y arranca el servidor de Hornero.
 */
public class Main {

    public static final int PUERTO = 3000;

    public static void main(String[] args) throws IOException {
        System.out.println("Turnos de Cancha - usando el framework Hornero");
        new Servidor(PUERTO).iniciar();
    }
}