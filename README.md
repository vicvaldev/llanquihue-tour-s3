# Llanquihue Tour

Sistema de digitalización de procesos para la agencia de turismo Llanquihue Tour, ubicada en la comuna de Llanquihue, Región de Los Lagos. El proyecto implementa un modelo orientado a objetos que representa las entidades clave del negocio, facilitando la coordinación con operadores locales, guías turísticos, proveedores de alojamiento y transporte.

## Estructura del proyecto

```
src/
├── app/
│   └── Main.java                 — Punto de entrada del sistema
├── model/
│   ├── Address.java              — Dirección física (composición)
│   ├── Person.java               — Persona genérica (clase base)
│   └── Employee.java             — Empleado de la agencia (hereda de Person)
└── util/
    ├── InvalidRutException.java  — Excepción personalizada para RUT inválido
    └── RutValidator.java         — Validador de RUT chileno (módulo 11)
```

### Clases implementadas

| Clase                  | Paquete | Descripción |
|------------------------|---------|-------------|
| `Address`              | model   | Representa una dirección con calle, número, ciudad y región. Utilizada como atributo de composición en `Person`. |
| `Person`               | model   | Clase base con atributos RUT, nombre, apellido y una dirección asociada. Valida el RUT en el constructor y setter. |
| `Employee`             | model   | Hereda de `Person` e incorpora cargo y sueldo base propios de un empleado. |
| `InvalidRutException`  | util    | Excepción personalizada que extiende `Exception` para RUT inválido. |
| `RutValidator`         | util    | Utilidad estática que implementa el algoritmo de validación de RUT chileno (módulo 11). |
| `Main`                 | app     | Demuestra la creación de objetos con validación de RUT y salida en formato JSON. |

## Requisitos

- Java JDK 17 o superior.

## Compilar y ejecutar

```bash
# Compilar todas las clases
javac -d out src/util/*.java src/model/*.java src/app/*.java

# Ejecutar
java -cp out app.Main
```

O en una línea:

```bash
javac -d out src/util/*.java src/model/*.java src/app/*.java && java -cp out app.Main
```
