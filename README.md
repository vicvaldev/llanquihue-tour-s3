# Llanquihue Tour

Sistema de digitalización de procesos para la agencia de turismo Llanquihue Tour, ubicada en la comuna de Llanquihue, Región de Los Lagos. El proyecto implementa un modelo orientado a objetos con herencia y composición que representa los servicios turísticos ofrecidos, los guías asociados y las entidades clave del negocio.

## Estructura del proyecto

```
src/
├── data/
│   ├── DataManager.java               — Carga, guardado y filtros sobre CSV (servicios)
│   └── OrderDataManager.java          — Carga y guardado de órdenes de compra
├── model/
│   ├── Registerable.java              — Interfaz con método showSummary()
│   ├── IOrder.java                    — Interfaz para órdenes de compra
│   ├── Address.java                   — Dirección física (composición)
│   ├── Person.java                    — Persona genérica (clase base, implementa Registerable)
│   ├── Employee.java                  — Empleado de la agencia (hereda de Person)
│   ├── TouristGuide.java              — Guía turístico (hereda de Employee)
│   ├── TourService.java               — Servicio turístico (superclase abstracta, implementa Registerable)
│   ├── GastronomicRoute.java          — Ruta gastronómica (hereda de TourService)
│   ├── LakeCruise.java                — Paseo lacustre (hereda de TourService)
│   ├── CulturalExcursion.java         — Excursión cultural (hereda de TourService)
│   └── PurchaseOrder.java             — Orden de compra de un tour
├── ui/
│   ├── Main.java                      — Punto de entrada (lanza la GUI)
│   ├── GestionGUI.java               — Interfaz gráfica principal: solo orquesta botones y delega a los managers
│   ├── TourServiceManager.java        — Lógica de negocio de servicios turísticos (CRUD, filtros, diálogo de alta)
│   └── OrderManager.java             — Lógica de negocio de órdenes de compra (CRUD, diálogo de alta)
└── util/
    ├── ExceededCapacityException.java — Excepción para capacidad excedida en órdenes
    ├── InvalidRutException.java       — Excepción para RUT inválido
    └── RutValidator.java              — Validador de RUT chileno (módulo 11)

resources/
├── tours.csv                          — Datos de ejemplo (servicios, 20 campos con ;)
└── orders.csv                         — Órdenes de compra (8 campos con ;)
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

Cada subclase concreta implementa `showSummary()` con un mensaje personalizado según el tipo de entidad. `TourService` declara además los métodos abstractos `getPrice()`, `getGuide()` y `getServiceType()`. La colección principal del sistema utiliza `List<Registerable>`, resolviendo el tipo concreto mediante `instanceof` cuando es necesario.

## Clases implementadas

| Clase / Interfaz | Paquete | Descripción |
|---|---|---|
| `Registerable` | model | Interfaz que define el contrato `showSummary()` para todas las entidades gestionables del sistema. |
| `IOrder` | model | Interfaz que define el contrato para órdenes de compra: `toCsvLine()`. |
| `Address` | model | Dirección con calle, número, ciudad y región. Composición en `Person`. Solo lectura (getters). |
| `Person` | model | Clase base con RUT, nombre, apellido y dirección. Implementa `Registerable.showSummary()`. |
| `Employee` | model | Hereda de `Person`. Incorpora cargo y sueldo base (validado > 0). Sobrescribe `showSummary()`. |
| `TouristGuide` | model | Hereda de `Employee`. Agrega lengua materna y segunda lengua del guía. Sobrescribe `showSummary()`. |
| `TourService` | model | Superclase abstracta con `id`, `name`, `durationHours` y `maxCapacity`. Implementa `Registerable` declarando `showSummary()` como abstracta. Define métodos abstractos `getPrice()`, `getGuide()`, `getServiceType()`. |
| `GastronomicRoute` | model | Hereda de `TourService`. Agrega `numberOfStops`, `price` y `guide` (composición). `showSummary()` muestra nombre, paradas y precio. |
| `LakeCruise` | model | Hereda de `TourService`. Agrega `boatType`, `price` y `guide`. `showSummary()` muestra nombre, embarcación y precio. |
| `CulturalExcursion` | model | Hereda de `TourService`. Agrega `historicalPlace`, `price` y `guide`. `showSummary()` muestra nombre, lugar histórico y precio. |
| `PurchaseOrder` | model | Orden de compra con `customerName`, `tour`, `peopleCount` y `total` calculado. Implementa `IOrder`. Valida capacidad máxima lanzando `ExceededCapacityException`. |
| `DataManager` | data | Utilidad estática que lee `resources/tours.csv` (20 campos), construye objetos de la jerarquía `TourService` según la columna `type`, filtra por precio o lengua materna del guía, y persiste nuevos servicios. Opera con `List<Registerable>` usando `instanceof TourService` para resolver el tipo concreto en los filtros. |
| `OrderDataManager` | data | Utilidad estática que lee y agrega órdenes de compra en `resources/orders.csv` (8 campos). Trabaja con la interfaz `IOrder`. |
| `ExceededCapacityException` | util | Excepción controlada que se lanza al superar la capacidad máxima de un tour en una orden de compra. |
| `InvalidRutException` | util | Excepción personalizada para RUT inválido. |
| `RutValidator` | util | Implementa el algoritmo de validación de RUT chileno (módulo 11). |
| `GestionGUI` | ui | Interfaz gráfica principal con `JFrame`. Crea los botones de acción y los organiza en el layout. Delega cada operación a `TourServiceManager` u `OrderManager`. |
| `TourServiceManager` | ui | Gestiona la lógica de servicios turísticos: carga desde CSV, `showSummary()`, `listAll()`, filtros por precio y lengua materna, y diálogo de alta (`AddServiceDialog` como inner class). |
| `OrderManager` | ui | Gestiona la lógica de órdenes de compra: `listOrders()`, diálogo de alta (`AddOrderDialog` como inner class). Trabaja con `List<IOrder>`. |
| `Main` | ui | Punto de entrada. Crea y muestra la ventana `GestionGUI`. |

## Interfaz gráfica (GUI)

La aplicación cuenta con una interfaz gráfica basada en `JFrame` con un panel de botones y un área de texto de salida.

### Botones

| Botón | Acción |
|---|---|---|
| **Mostrar Resumen** | Recorre la lista como `Registerable` e invoca `showSummary()` en cada entidad, demostrando el uso de la interfaz. |
| **Listar Todos** | Muestra todos los registros cargados desde `tours.csv` separados por `---`. Usa `instanceof TourService` para acceder a `toString()`. |
| **Filtrar por Precio** | Abre un `JOptionPane` para ingresar un precio máximo; aplica `DataManager.filterByPrice()` y muestra los resultados. |
| **Filtrar por Lengua Materna** | Abre un `JOptionPane` para ingresar un código ISO de idioma; aplica `DataManager.filterByMotherTongue()` y muestra los resultados. |
| **Agregar Registro** | Abre un `JDialog` modal con todos los campos del servicio y guía, combo Box para tipo e idiomas, campo de capacidad máxima, y botón Guardar. Al guardar persiste vía `DataManager.appendService()`. |
| **Agregar Orden de Compra** | Abre un `JDialog` modal para seleccionar un servicio, ingresar cliente y cantidad de personas; valida capacidad máxima y persiste vía `OrderDataManager.appendOrder()`. |
| **Listar Órdenes** | Muestra las órdenes de compra registradas en `orders.csv`. |
| **Salir** | Cierra la aplicación. |

### Validaciones incluidas

- Los filtros trabajan sobre `List<Registerable>` y resuelven el tipo concreto con `instanceof TourService`.
- El diálogo de agregar servicio valida campos obligatorios, formato numérico, RUT (módulo 11) y capacidad máxima positiva.
- El diálogo de orden de compra valida que la cantidad de personas no supere la capacidad máxima del tour, lanzando `ExceededCapacityException`.

## Formato de archivos de datos

### tours.csv

`resources/tours.csv` contiene registros separados por `;` (20 campos por línea, codificación UTF-8):

```
id;type;name;durationHours;numberOfStops;boatType;historicalPlace;price;rut;firstName;lastName;street;number;city;region;position;baseSalary;motherTongue;secondLanguage;maxCapacity
```

Los campos específicos de cada tipo (`numberOfStops`, `boatType`, `historicalPlace`) se dejan vacíos cuando no corresponden al tipo de servicio. El campo 20 (`maxCapacity`) define la capacidad máxima de personas del tour.

### orders.csv

`resources/orders.csv` contiene registros separados por `;` (8 campos por línea, codificación UTF-8):

```
orderId;customerName;tourId;tourType;tourName;peopleCount;unitPrice;total
```

### Datos de ejemplo (8 registros en tours.csv)

| # | Tipo | Nombre | Precio | Capacidad |
|---|---|---|---|---|
| 1 | GastronomicRoute | Ruta de los Quesos Artesanales | $45.000 | 15 |
| 2 | GastronomicRoute | Ruta de la Cerveza Artesanal | $35.000 | 20 |
| 3 | LakeCruise | Navegación Lago Todos los Santos | $65.000 | 40 |
| 4 | LakeCruise | Travesía por el Canal de Chacao | $55.000 | 30 |
| 5 | CulturalExcursion | Visita Iglesia de Achao | $35.000 | 25 |
| 6 | CulturalExcursion | Recorrido por el Fuerte de Niebla | $40.000 | 25 |
| 7 | GastronomicRoute | La ruta del boqueron | $1.890.000 | 12 |
| 8 | CulturalExcursion | Descubriendo Valdivia | $70.000 | 20 |

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
