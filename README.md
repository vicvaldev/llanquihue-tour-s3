# Llanquihue Tour

Sistema de digitalización de procesos para la agencia de turismo Llanquihue Tour, ubicada en la comuna de Llanquihue, Región de Los Lagos. El proyecto implementa un modelo orientado a objetos con herencia y composición que representa los servicios turísticos ofrecidos, los guías asociados y las entidades clave del negocio.

## Estructura del proyecto

```
src/
├── data/
│   └── DataManager.java               — Carga, guardado y filtros sobre CSV
├── model/
│   ├── Registerable.java              — Interfaz con método showSummary()
│   ├── Address.java                   — Dirección física (composición)
│   ├── Person.java                    — Persona genérica (clase base, implementa Registerable)
│   ├── Employee.java                  — Empleado de la agencia (hereda de Person)
│   ├── TouristGuide.java              — Guía turístico (hereda de Employee)
│   ├── TourService.java               — Servicio turístico (superclase abstracta, implementa Registerable)
│   ├── GastronomicRoute.java          — Ruta gastronómica (hereda de TourService)
│   ├── LakeCruise.java                — Paseo lacustre (hereda de TourService)
│   └── CulturalExcursion.java         — Excursión cultural (hereda de TourService)
├── ui/
│   ├── Main.java                      — Punto de entrada (lanza la GUI)
│   └── GestionGUI.java               — Interfaz gráfica con JFrame y JOptionPane
└── util/
    ├── InvalidRutException.java       — Excepción para RUT inválido
    └── RutValidator.java              — Validador de RUT chileno (módulo 11)

resources/
└── tours.csv                          — Datos de ejemplo (formato CSV con ;)
```

## Jerarquía de herencia

```
Registerable  (interfaz — showSummary())
├── Person
│   └── Employee
│       └── TouristGuide
└── TourService  (abstracta)
    ├── GastronomicRoute
    ├── LakeCruise
    └── CulturalExcursion
```

Cada subclase concreta implementa `showSummary()` con un mensaje personalizado según el tipo de entidad. `TourService` declara además los métodos abstractos `getPrice()`, `getGuide()` y `getServiceType()`, y sus subclases sobrescriben `displayInformation()` para exponer información específica. La colección principal del sistema utiliza `List<Registerable>`, resolviendo el tipo concreto mediante `instanceof` cuando es necesario.

## Clases implementadas

| Clase / Interfaz | Paquete | Descripción |
|---|---|---|
| `Registerable` | model | Interfaz que define el contrato `showSummary()` para todas las entidades gestionables del sistema. |
| `Address` | model | Dirección con calle, número, ciudad y región. Composición en `Person`. |
| `Person` | model | Clase base con RUT, nombre, apellido y dirección. Implementa `Registerable.showSummary()`. |
| `Employee` | model | Hereda de `Person`. Incorpora cargo y sueldo base (validado > 0). Sobrescribe `showSummary()`. |
| `TouristGuide` | model | Hereda de `Employee`. Agrega lengua materna y segunda lengua del guía. Sobrescribe `showSummary()`. |
| `TourService` | model | Superclase abstracta con `id`, `name` y `durationHours`. Implementa `Registerable` declarando `showSummary()` como abstracta. Define métodos abstractos `getPrice()`, `getGuide()`, `getServiceType()`. |
| `GastronomicRoute` | model | Hereda de `TourService`. Agrega `numberOfStops`, `price` y `guide` (composición). `showSummary()` muestra nombre, paradas y precio. |
| `LakeCruise` | model | Hereda de `TourService`. Agrega `boatType`, `price` y `guide`. `showSummary()` muestra nombre, embarcación y precio. |
| `CulturalExcursion` | model | Hereda de `TourService`. Agrega `historicalPlace`, `price` y `guide`. `showSummary()` muestra nombre, lugar histórico y precio. |
| `DataManager` | data | Utilidad estática que lee `resources/tours.csv`, construye objetos de la jerarquía `TourService` según la columna `type`, filtra por precio o lengua materna del guía, y persiste nuevos servicios. Opera con `List<Registerable>` usando `instanceof TourService` para resolver el tipo concreto en los filtros. |
| `InvalidRutException` | util | Excepción personalizada para RUT inválido. |
| `RutValidator` | util | Implementa el algoritmo de validación de RUT chileno (módulo 11). |
| `GestionGUI` | ui | Interfaz gráfica con `JFrame` que ofrece botones para mostrar resumen (vía `Registerable.showSummary()`), listar todos los registros, filtrar por precio, filtrar por lengua materna, agregar un nuevo servicio (con `JDialog` de formulario) y salir. |
| `Main` | ui | Punto de entrada. Crea y muestra la ventana `GestionGUI`. |

## Interfaz gráfica (GUI)

La aplicación cuenta con una interfaz gráfica basada en `JFrame` con un panel de botones y un área de texto de salida.

### Botones

| Botón | Acción |
|---|---|
| **Mostrar Resumen** | Recorre la lista como `Registerable` e invoca `showSummary()` en cada entidad, demostrando el uso de la interfaz. |
| **Listar Todos** | Muestra todos los registros cargados desde `tours.csv` separados por `---`. Usa `instanceof TourService` para acceder a `toString()`. |
| **Filtrar por Precio** | Abre un `JOptionPane` para ingresar un precio máximo; aplica `DataManager.filterByPrice()` y muestra los resultados. |
| **Filtrar por Lengua Materna** | Abre un `JOptionPane` para ingresar un código ISO de idioma; aplica `DataManager.filterByMotherTongue()` y muestra los resultados. |
| **Agregar Registro** | Abre un `JDialog` modal con todos los campos del servicio y guía, combo Box para tipo e idiomas, y botón Guardar. Al guardar persiste vía `DataManager.appendService()`. |
| **Salir** | Cierra la aplicación. |

### Validaciones incluidas

- Los filtros trabajan sobre `List<Registerable>` y resuelven el tipo concreto con `instanceof TourService`.
- El diálogo de agregar valida campos obligatorios, formato numérico y RUT (módulo 11).

## Formato del archivo de datos

`resources/tours.csv` contiene registros separados por `;` (19 campos por línea, codificación UTF-8):

```
id;type;name;durationHours;numberOfStops;boatType;historicalPlace;price;rut;firstName;lastName;street;number;city;region;position;baseSalary;motherTongue;secondLanguage
```

Los campos específicos de cada tipo (`numberOfStops`, `boatType`, `historicalPlace`) se dejan vacíos cuando no corresponden al tipo de servicio.

### Datos de ejemplo (6 registros, 2 por subclase)

| # | Tipo | Nombre | Precio |
|---|---|---|
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
