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
solicitud, verificar permisos, llamar al controlador que corresponde y
responder. La aplicación solo programa sus controladores y servicios:
**el framework llama a la aplicación, no al revés** (inversión de control).

## Aplicación: Turnos de Cancha

Sistema para reservar turnos en canchas de fútbol. Los usuarios se conectan,
inician sesión, consultan las canchas y reservan o cancelan turnos. El
administrador da de alta canchas y ve la agenda completa.

Como varios clientes se conectan a la vez, dos usuarios pueden intentar
reservar el mismo turno al mismo tiempo: el servicio de turnos lo resuelve
con `synchronized` para que solo uno lo consiga.

## Estructura del proyecto

```
src/
├── hornero/               → el framework (no importa nada de app)
│   ├── validadores/       → validadores reutilizables
│   └── ...
└── app/                   → la aplicación Turnos de Cancha
cliente/
└── ClienteConsola.java    → cliente de la cátedra para probar
```

La regla del TP se cumple: en el paquete `hornero` no aparece ninguna clase
de `app` (se puede verificar mirando los `import`).

## Cómo se levanta el servidor

El puerto se configura en `app/Main.java` (`PUERTO = 3000`).

**Desde Eclipse:** *File → Import → Existing Projects into Workspace*, elegir
la carpeta del proyecto. Después, clic derecho en `app/Main.java` →
*Run As → Java Application*.

**Desde la terminal** (PowerShell, parados en la carpeta del proyecto):

```powershell
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp bin app.Main
```

El servidor muestra:

```
Turnos de Cancha - usando Hornero
[Hornero] Escuchando en el puerto 3000
```

**Para conectar un cliente**, en otra terminal (se pueden abrir varios a la vez):

```powershell
java cliente/ClienteConsola.java
```

> En Windows, para que la ñ y los acentos se vean bien en la consola, correr
> antes en esa terminal:
> `[Console]::InputEncoding = [Console]::OutputEncoding = [System.Text.Encoding]::UTF8`

### Usuarios de prueba

| Usuario | Clave | Rol |
|---|---|---|
| `admin` | `admin` | ADMIN |
| `emma` | `1234` | USUARIO |
| `bruno` | `1234` | USUARIO |

## Protocolo

### Solicitud

Una línea de texto, con las partes separadas por `/`:

```
recurso/accion/parametro1/parametro2/...
```

Recurso y acción son obligatorios; los parámetros dependen de la acción.

### Respuesta

Toda respuesta empieza con `OK: ` o con `ERROR: `, seguido de un mensaje.
Los listados pueden ocupar varias líneas (la primera siempre empieza con `OK:`).

### Recursos y acciones

| Solicitud | Qué hace | Rol mínimo |
|---|---|---|
| `sesion/login/{usuario}/{clave}` | Inicia sesión | INVITADO |
| `sesion/logout` | Cierra la sesión | INVITADO |
| `canchas/alta/{nombre}/{jugadores}` | Da de alta una cancha | **ADMIN** |
| `canchas/listar` | Lista las canchas | USUARIO |
| `turnos/reservar/{idCancha}/{hora}` | Reserva un turno (hora de 8 a 23) | USUARIO |
| `turnos/cancelar/{idTurno}` | Cancela un turno propio | USUARIO |
| `turnos/mios` | Lista los turnos del usuario logueado | USUARIO |
| `turnos/del-dia` | Agenda completa de turnos | **ADMIN** |

Los roles van de menor a mayor: `INVITADO` (conectado sin login) <
`USUARIO` < `ADMIN`. Un rol puede hacer todo lo que puede uno menor.

### Ejemplos

```
> canchas/listar
< ERROR: No tenés permisos para realizar esta acción.
> sesion/login/admin/admin
< OK: Bienvenido, admin (ADMIN).
> canchas/alta/Cancha 5/10
< OK: Cancha creada correctamente. Cancha 1 - Nombre: Cancha 5 - Cantidad de jugadores: 10
> canchas/alta/Cancha 7/abc
< ERROR: El valor debe ser un número entero.
> turnos/reservar/1/18
< OK: Turno reservado correctamente. Turno 1 - Cancha: 1 - Hora: 18:00 - Usuario: admin
> turnos/reservar/1/25
< ERROR: El número debe estar entre 8 y 23.
> turnos/del-dia
< OK: Agenda de turnos:
< Turno 1 - Cancha: 1 - Hora: 18:00 - Usuario: admin
> hola
< ERROR: Formato inválido. Se esperaba recurso/accion/parametros.
> autos/alta/AB123
< ERROR: El recurso 'autos' no existe.
> sesion/logout
< OK: Sesión cerrada. Hasta luego, admin.
```

Si otro usuario intenta reservar un turno ya tomado:

```
< ERROR: Ese horario ya está reservado.
```

## Cómo funciona por dentro

Por cada línea que llega, el framework hace:

```
Servidor (accept + un hilo por cliente)
  └─ ManejadorCliente (crea la Sesion del cliente)
       recibir (Comunicador) → ParserSolicitudes → Solicitud + Sesion
       → Router: busca recurso y acción, chequea el rol, ejecuta la Accion
       → responder "OK: ..." (Comunicador)
       si algo falla en cualquier paso → "ERROR: ..."
```

| Pieza | Clase(s) |
|---|---|
| Conexiones e hilos | `Servidor`, `ManejadorCliente` |
| Entrada/salida de texto (UTF-8) | `Comunicador` |
| Parseo | `ParserSolicitudes`, `Solicitud` |
| Ruteo sin `if` | `Router`, `Controlador`, `Accion` |
| Sesión y roles | `Sesion`, `Rol` (enum) |
| Validación | `Validador` y `validadores/EnteroPositivo`, `LargoMaximo`, `Rango` |
| Servicios compartidos | `ContenedorServicios` |
| Excepciones | `HorneroException` y sus subclases |

### Ruteo sin `if`

El `Router` guarda un `Map<String, Map<String, Accion>>`: recurso → acción →
método. Cada controlador registra sus acciones con referencias a método
(`this::alta`), así que despachar es buscar en el Map y llamar a `ejecutar`.

### Excepciones

| Excepción | Tipo | Cuándo |
|---|---|---|
| `HorneroException` | checked (base) | — |
| `SolicitudInvalidaException` | checked | el texto no respeta el formato |
| `RutaNoEncontradaException` | checked | el recurso o la acción no existen |
| `PermisoDenegadoException` | checked | el rol de la sesión no alcanza |
| `ValidacionException` | checked | un parámetro no es válido |
| `ServicioNoRegistradoException` | **unchecked** | se pide un servicio que nadie registró |

**Por qué:** las checked son errores que el cliente puede provocar (escribir
mal, pedir algo sin permiso). Al ser checked, el compilador obliga al
framework a atraparlas y responder `ERROR: ...`. `ServicioNoRegistradoException`
es unchecked porque es un error de programación de quien arma la aplicación,
no algo que el cliente pueda causar.

Además, `ManejadorCliente` tiene un `catch (Exception e)` final: cualquier
error inesperado se responde como `ERROR interno: ...` y ni la conexión ni el
servidor se caen.

### Concurrencia

- Cada cliente tiene su propio hilo, su propio `Comunicador` y su propia
  `Sesion`, por eso la sesión no necesita sincronización.
- Los servicios compartidos (`ServicioCanchas`, `ServicioTurnos`) tienen sus
  métodos `synchronized`: dos clientes no pueden reservar el mismo turno ni
  generar ids repetidos.
- `ContenedorServicios` usa un `ConcurrentHashMap`.
- El `Router` y `ServicioUsuarios` se llenan una sola vez al arrancar y
  después solo se leen.

## Cómo se escribe y se registra un controlador nuevo

**1. Crear la clase en `app`, implementando `Controlador`:**

```java
package app;

import hornero.*;
import hornero.validadores.LargoMaximo;
import java.util.Map;

public class ControladorEquipos implements Controlador {

    private final ServicioEquipos servicio;
    private final LargoMaximo validarNombre = new LargoMaximo(30);

    // Los servicios se piden al contenedor, no se instancian acá.
    public ControladorEquipos(ContenedorServicios contenedor) {
        this.servicio = contenedor.obtener(ServicioEquipos.class);
    }

    @Override
    public String getRecurso() {
        return "equipos";                       // equipos/...
    }

    @Override
    public void registrarAcciones(Map<String, Accion> acciones) {
        acciones.put("alta", this::alta);       // equipos/alta/{nombre}
        acciones.put("listar", this::listar);   // equipos/listar
    }

    @Override
    public void registrarPermisos(Map<String, Rol> permisos) {
        permisos.put("alta", Rol.ADMIN);        // si no se indica: USUARIO
    }

    private String alta(Solicitud s) throws HorneroException {
        String nombre = s.getParametros().get(0);
        validarNombre.validar(nombre);          // si falla → "ERROR: ..."
        servicio.alta(nombre);
        return "Equipo creado: " + nombre;      // el framework agrega "OK: "
    }

    private String listar(Solicitud s) {
        return String.join(" | ", servicio.listar());
    }
}
```

**2. Registrarlo en `app/Main.java`:**

```java
contenedor.registrar(ServicioEquipos.class, new ServicioEquipos());
router.registrar(new ControladorEquipos(contenedor));
```

No hace falta tocar nada del framework.

## Opcionales y extras

- UTF-8 explícito en el `Comunicador`, igual que el `ClienteConsola`, para
  que los acentos y la ñ lleguen bien.
- Reservas concurrentes resueltas con `synchronized`: si dos clientes
  reservan el mismo turno a la vez, solo uno lo consigue.
- Un usuario solo puede cancelar sus propios turnos.
- Uso de `Optional` en el login y de Streams en los listados de turnos.
