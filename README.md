# Llanquihue Tour

Sistema de digitalización de procesos para la agencia de turismo Llanquihue Tour, ubicada en la comuna de Llanquihue, Región de Los Lagos. El proyecto implementa un modelo orientado a objetos con herencia y composición que representa los servicios turísticos ofrecidos, los guías asociados y las entidades clave del negocio.

## Estructura del proyecto

```
src/
├── data/
│   └── DataManager.java               — Carga, guardado y filtros sobre CSV
├── model/
│   ├── Address.java                   — Dirección física (composición)
│   ├── Person.java                    — Persona genérica (clase base)
│   ├── Employee.java                  — Empleado de la agencia (hereda de Person)
│   ├── TouristGuide.java              — Guía turístico (hereda de Employee)
│   ├── TourService.java               — Servicio turístico (superclase abstracta)
│   ├── GastronomicRoute.java          — Ruta gastronómica (hereda de TourService)
│   ├── LakeCruise.java                — Paseo lacustre (hereda de TourService)
│   └── CulturalExcursion.java         — Excursión cultural (hereda de TourService)
├── ui/
│   └── Main.java                      — Punto de entrada (menú interactivo)
└── util/
    ├── InvalidRutException.java       — Excepción para RUT inválido
    └── RutValidator.java              — Validador de RUT chileno (módulo 11)

resources/
└── tours.csv                          — Datos de ejemplo (formato CSV con ;)
```

## Jerarquía de herencia

```
Person
  └── Employee
        └── TouristGuide

TourService  (abstracta)
  ├── GastronomicRoute
  ├── LakeCruise
  └── CulturalExcursion
```

Cada subclase implementa `getPrice()`, `getGuide()` y `getServiceType()` como métodos abstractos de `TourService`, y sobrescribe `displayInformation()` para exponer su información específica, lo que permite operar polimórficamente sin usar `instanceof`.

## Clases implementadas

| Clase | Paquete | Descripción |
|---|---|---|
| `Address` | model | Dirección con calle, número, ciudad y región. Composición en `Person`. |
| `Person` | model | Clase base con RUT, nombre, apellido y dirección. Valida RUT en constructor y setter. |
| `Employee` | model | Hereda de `Person`. Incorpora cargo y sueldo base (validado > 0). |
| `TouristGuide` | model | Hereda de `Employee`. Agrega lengua materna y segunda lengua del guía. |
| `TourService` | model | Superclase abstracta con `id`, `name` y `durationHours`. Define métodos abstractos `getPrice()`, `getGuide()`, `getServiceType()`. Incluye `displayInformation()` con implementación base. |
| `GastronomicRoute` | model | Hereda de `TourService`. Agrega `numberOfStops` (validado > 0), `price` (> 0) y `guide` (composición con `TouristGuide`). Sobrescribe `displayInformation()` con datos de paradas, precio y guía. |
| `LakeCruise` | model | Hereda de `TourService`. Agrega `boatType` (no vacío), `price` (> 0) y `guide` (composición). Sobrescribe `displayInformation()` con datos de embarcación, precio y guía. |
| `CulturalExcursion` | model | Hereda de `TourService`. Agrega `historicalPlace` (no vacío), `price` (> 0) y `guide` (composición). Sobrescribe `displayInformation()` con datos de lugar histórico, precio y guía. |
| `DataManager` | data | Utilidad estática que lee `resources/tours.csv`, construye objetos de la jerarquía `TourService` según la columna `type`, filtra por precio o lengua materna del guía, y persiste nuevos servicios. Incluye validación de datos y codificación UTF-8. |
| `InvalidRutException` | util | Excepción personalizada para RUT inválido. |
| `RutValidator` | util | Implementa el algoritmo de validación de RUT chileno (módulo 11). |
| `Main` | ui | Punto de entrada con menú interactivo: listar, buscar por precio, buscar por lengua materna, agregar nuevo servicio, filtrar por tipo (demostración polimórfica) y salir. |

## Menú interactivo

```
=== LLANQUIHUE TOUR ===
1. Listar todos los servicios
2. Buscar por precio máximo
3. Buscar por lengua materna del guía
4. Agregar un nuevo servicio
5. Ver último servicio agregado
6. Salir
```

### Opciones

1. **Listar todos los servicios** — Muestra todos los servicios registrados usando `toString()` polimórfico de cada subclase.
2. **Buscar por precio máximo** — Solicita un valor y muestra los servicios con precio menor o igual al indicado.
3. **Buscar por lengua materna del guía** — Solicita un código ISO de idioma y muestra los servicios cuyo guía tenga esa lengua materna.
4. **Agregar un nuevo servicio** — Permite elegir el tipo (Ruta Gastronómica, Paseo Lacustre o Excursión Cultural) y completar campos comunes y específicos. Todos los valores numéricos deben ser positivos. El RUT se valida con el algoritmo módulo 11.
5. **Ver último servicio agregado** — Muestra el último registro del archivo CSV.
6. **Salir** — Termina la ejecución.
7. **Filtrar servicios por tipo** — Demostración de polimorfismo: recorre la colección con un `for-each` usando referencias `TourService`, filtra mediante `getServiceType()` (abstracto → resuelto en cada subclase) y despliega con `displayInformation()` (sobrescrito en cada subclase). Sin `instanceof`. Campos nulos o vacíos muestran "datos no encontrados".

### Validaciones incluidas

- **Entrada de usuario**: duración, precio, número de paradas y sueldo base deben ser valores positivos.
- **Modelo**: los setters y constructores lanzan `IllegalArgumentException` si los valores violan las reglas de negocio.
- **CSV**: cada línea se valida individualmente; las líneas con errores se reportan con el número de línea y la causa específica, sin detener la carga del resto.

## Formato del archivo de datos

`resources/tours.csv` contiene registros separados por `;` (19 campos por línea, codificación UTF-8):

```
id;type;name;durationHours;numberOfStops;boatType;historicalPlace;price;rut;firstName;lastName;street;number;city;region;position;baseSalary;motherTongue;secondLanguage
```

Los campos específicos de cada tipo (`numberOfStops`, `boatType`, `historicalPlace`) se dejan vacíos cuando no corresponden al tipo de servicio.

### Datos de ejemplo (6 registros, 2 por subclase)

| # | Tipo | Nombre | Precio |
|---|---|---|---|
| 1 | GastronomicRoute | Ruta de los Quesos Artesanales | $45.000 |
| 2 | GastronomicRoute | Ruta de la Cerveza Artesanal | $35.000 |
| 3 | LakeCruise | Navegación Lago Todos los Santos | $65.000 |
| 4 | LakeCruise | Travesía por el Canal de Chacao | $55.000 |
| 5 | CulturalExcursion | Visita Iglesia de Achao | $35.000 |
| 6 | CulturalExcursion | Recorrido por el Fuerte de Niebla | $40.000 |

## Requisitos

- Java JDK 17 o superior.

## Compilar y ejecutar

```bash
# Compilar todas las clases
javac -d out src/util/*.java src/model/*.java src/data/*.java src/ui/*.java

# Ejecutar
java -cp out ui.Main
```

O en una línea:

```bash
javac -d out src/util/*.java src/model/*.java src/data/*.java src/ui/*.java && java -cp out ui.Main
```

## Limpiar archivos compilados

```bash
rm -rf out
```
