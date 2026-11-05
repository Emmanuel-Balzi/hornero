# Hornero

Trabajo Práctico Final — Tecnología Java (UNLZ, 2C 2026)

## Integrantes

- Emmanuel Balzi
- Bruno — *(completar apellido)*

## Framework: Hornero

El hornero es el pájaro que construye su propio nido, a mano y con barro.
Nuestro framework sigue la misma idea: está hecho completamente por nosotros,
sin librerías externas, con lo que vimos en la cursada.

Hornero se encarga de todo lo que no es del dominio: escuchar conexiones,
atender a cada cliente en su propio hilo, convertir el texto recibido en una
solicitud, verificar permisos, validar datos, llamar al controlador que
corresponde y responder. La aplicación solo programa sus controladores.

## Aplicación: Turnos de Cancha

Sistema para reservar turnos en canchas de fútbol. Los usuarios se conectan,
consultan las canchas disponibles y reservan o cancelan turnos. El
administrador da de alta canchas y puede ver la agenda completa del día.

Como varios clientes se conectan a la vez, dos usuarios pueden intentar
reservar el mismo turno al mismo tiempo: el servicio de turnos tiene que
resolverlo para que solo uno lo consiga.

## Estructura del proyecto

```
src/
├── hornero/   → el framework (no conoce nada de la aplicación)
└── app/       → la aplicación Turnos de Cancha
```

## Cómo se levanta el servidor

*(se completa en el control del 14/11)*
