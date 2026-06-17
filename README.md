# Llanquihue Tour

Sistema de digitalización de procesos para la agencia de turismo Llanquihue Tour, ubicada en la comuna de Llanquihue, Región de Los Lagos. El proyecto implementa un modelo orientado a objetos que representa las entidades clave del negocio, facilitando la coordinación con operadores locales, guías turísticos, proveedores de alojamiento y transporte.

## Estructura del proyecto

```
src/
├── data/
│   └── DataManager.java           — Carga y guardado de datos desde CSV y filtros
├── model/
│   ├── Address.java               — Dirección física (composición)
│   ├── Person.java                — Persona genérica (clase base)
│   ├── Employee.java              — Empleado de la agencia (hereda de Person)
│   ├── TouristGuide.java          — Guía turístico (hereda de Employee)
│   ├── Products.java              — Producto turístico genérico (clase base)
│   └── Tours.java                 — Tour turístico (hereda de Products)
├── ui/
│   └── Main.java                  — Punto de entrada del sistema (menú interactivo)
└── util/
    ├── InvalidRutException.java   — Excepción personalizada para RUT inválido
    └── RutValidator.java          — Validador de RUT chileno (módulo 11)

resources/
└── tours.csv                   — Datos de ejemplo de tours (formato CSV con ;)
```

## Jerarquía de herencia

```
Person
  └── Employee
        └── TouristGuide

Products
  └── Tours
```

## Clases implementadas

| Clase                  | Paquete | Descripción |
|------------------------|---------|-------------|
| `Address`              | model   | Representa una dirección con calle, número, ciudad y región. Utilizada como atributo de composición en `Person`. |
| `Person`               | model   | Clase base con atributos RUT, nombre, apellido y una dirección asociada. Valida el RUT en el constructor y setter. |
| `Employee`             | model   | Hereda de `Person` e incorpora cargo y sueldo base propios de un empleado. |
| `TouristGuide`         | model   | Hereda de `Employee` y agrega lengua materna y segunda lengua del guía. |
| `Products`             | model   | Clase base para productos turísticos con id, nombre y precio. |
| `Tours`                | model   | Hereda de `Products` e incorpora ubicación, duración y el guía turístico asignado (composición con `TouristGuide`). |
| `DataManager`          | data    | Utilidad estática que lee `resources/tours.csv`, crea objetos `Tours` con su `TouristGuide` asociado, permite filtrar por precio o lengua materna del guía, y guarda nuevos tours. |
| `InvalidRutException`  | util    | Excepción personalizada que extiende `Exception` para RUT inválido. |
| `RutValidator`         | util    | Utilidad estática que implementa el algoritmo de validación de RUT chileno (módulo 11). |
| `Main`                 | ui      | Punto de entrada con menú interactivo: listar tours, buscar por precio, buscar por lengua materna, agregar un nuevo tour y salir. |

## Menú interactivo

Al ejecutar el sistema se muestra un menú con las siguientes opciones:

```
=== LLANQUIHUE TOUR ===
1. Listar todos los tours
2. Buscar por precio máximo
3. Buscar por lengua materna del guía
4. Agregar un nuevo tour
5. Salir
```

### Opciones

1. **Listar todos los tours** — Muestra todos los tours registrados en el sistema.
2. **Buscar por precio máximo** — Solicita un valor y muestra los tours con precio menor o igual al indicado.
3. **Buscar por lengua materna del guía** — Solicita un idioma y muestra los tours cuyo guía tenga esa lengua materna.
4. **Agregar un nuevo tour** — Formulario guiado con validaciones. Los campos obligatorios son: nombre del tour, precio, RUT del guía, nombre del guía, apellido del guía, calle, sueldo base y lengua materna. Los campos opcionales indican `(opcional, presione Enter para omitir)`. El RUT es validado con el algoritmo módulo 11.
5. **Salir** — Termina la ejecución del programa.

## Formato del archivo de datos

`resources/tours.csv` contiene registros separados por `;` (16 campos por línea):

```
id;productName;location;duration;price;rut;firstName;lastName;street;number;city;region;position;baseSalary;motherTongue;secondLanguage
```

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
