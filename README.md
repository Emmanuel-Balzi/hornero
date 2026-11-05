# Hornero

Trabajo Práctico Final — Tecnología Java (UNLZ, 2C 2026)

## Integrantes

- Emmanuel Balzi
- Bruno Klein

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
## Protocolo (tentativo)

Cada solicitud es una línea de texto:

```
recurso/accion/parametro1/parametro2/...
```

Cada respuesta empieza con `OK` o con `ERROR`:

```
> canchas/listar
OK 1-Cancha 5 | 2-Cancha 7
> turnos/reservar/1/25
ERROR la hora debe estar entre 8 y 23
```

## Recursos y acciones (lista tentativa)

| Solicitud | Qué hace | Rol |
|---|---|---|
| `sesion/login/{usuario}/{clave}` | Inicia sesión | cualquiera |
| `sesion/logout` | Cierra la sesión | cualquiera |
| `canchas/alta/{nombre}/{jugadores}` | Da de alta una cancha | admin |
| `canchas/listar` | Lista las canchas | usuario |
| `canchas/baja/{id}` | Da de baja una cancha | admin |
| `turnos/reservar/{idCancha}/{hora}` | Reserva un turno | usuario |
| `turnos/cancelar/{idTurno}` | Cancela un turno propio | usuario |
| `turnos/mios` | Lista los turnos del usuario | usuario |
| `turnos/disponibles/{idCancha}` | Horarios libres de una cancha | usuario |
| `turnos/del-dia` | Agenda completa del día | admin |

## Cómo se levanta el servidor

*(se completa en el control del 14/11)*
