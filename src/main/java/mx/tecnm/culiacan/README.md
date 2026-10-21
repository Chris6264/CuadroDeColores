# Cuadro de Colores

Rompecabezas de consola en Java. Un tablero se llena de piezas de colores y el objetivo es **sacar todas las piezas** deslizándolas hacia las salidas del borde que tienen su mismo color. El programa genera un tablero, comprueba con una búsqueda **A\*** que tenga solución y luego muestra la solución paso a paso.

Paquete: `mx.tecnm.culiacan`

---

## Contenido

1. [Reglas del juego](#reglas-del-juego)
2. [Versiones](#versiones)
3. [Piezas](#piezas)
4. [Salidas](#salidas)
5. [Requisitos y ejecución](#requisitos-y-ejecución)
6. [Ejemplo de ejecución](#ejemplo-de-ejecución)
7. [Arquitectura](#arquitectura)
8. [Generación de tableros](#generación-de-tableros)
9. [Solucionador](#solucionador)
10. [Configuración](#configuración)
11. [Limitaciones conocidas](#limitaciones-conocidas)
12. [Créditos](#créditos)

---

## Reglas del juego

- El tablero es una cuadrícula de `filas x columnas` totalmente llena de piezas.
- Cada pieza tiene un **color** y una **forma** (ocupa una o varias casillas).
- En cada movimiento se desliza **una pieza una casilla** hacia arriba, abajo, izquierda o derecha, siempre que las casillas a las que llega estén libres.
- Alrededor del tablero hay **salidas** de colores. Una pieza **sale** cuando está pegada al borde, se mueve hacia afuera por una salida de **su mismo color** y la cara de la pieza coincide en ancho y posición con esa salida.
- Al salir, la pieza desaparece completa del tablero.
- **Se gana** cuando el tablero queda vacío.
- Todas las piezas de un mismo color tienen la misma forma, y por eso comparten salida.

---

## Versiones

La versión se elige en `Main.java` (`Version.AVANZADA` por defecto).

| Versión | Dimensiones | Piezas |
|---|---|---|
| `BASICA` | 2x2 fijo | 4 piezas individuales con 2 colores |
| `INTERMEDIA` | Cuadrado, lado de 3 a 7 (se pide al usuario) | Individuales, horizontales (2x1) y verticales (1x2) |
| `AVANZADA` | Filas y columnas de 5 a 14 (se piden por separado) | Individuales, horizontales, verticales, cuadrados 2x2, L de 3 casillas y L de 4 casillas |

El rango válido de cada versión está en `Version.java` y se valida con `ReglasCuadro`. Si el número está fuera del rango, se muestra el error y se vuelve a pedir.

---

## Piezas

Las formas están definidas en el enum `Forma` como desplazamientos `(fila, columna)` desde la esquina superior izquierda de la pieza.

| Forma | Casillas | Dibujo |
|---|---|---|
| `INDIVIDUAL` | 1 | `#` |
| `HORIZONTAL` | 2 | `##` |
| `VERTICAL` | 2 | `#` sobre `#` |
| `CUADRADO` | 4 | `##` sobre `##` |
| `L1` a `L4` | 3 | L de 3 casillas, 4 rotaciones |
| `LARGA1` a `LARGA4` | 4 | L de 4 casillas (3 en línea más 1), 4 rotaciones |

### L de 3 casillas (`L1` a `L4`)

```
L1      L2      L3      L4
#.      ##      ##      .#
##      #.      .#      ##
```

### L de 4 casillas (`LARGA1` a `LARGA4`)

```
LARGA1   LARGA2   LARGA3   LARGA4
#.       ###      ##       ..#
#.       #..      .#       ###
##                .#
```

### Varias L en el mismo tablero

En la versión avanzada cada tablero lleva **2 rotaciones distintas de L de 3** y **2 rotaciones distintas de L de 4** (constante `ROTACIONES_POR_L` en `TableroAvanzado`). Cada rotación recibe su propio color, porque su salida cambia de lado según la rotación.

---

## Salidas

Una `Salida` tiene un lado (`ARRIBA`, `ABAJO`, `IZQUIERDA`, `DERECHA`), un índice, un color y un tamaño de **1 o 2** casillas. El tamaño máximo es 2.

El lado y el ancho por el que puede salir cada forma:

| Forma | Lados permitidos | Ancho de salida |
|---|---|---|
| `INDIVIDUAL` | Los 4 | 1 |
| `HORIZONTAL` | Izquierda y derecha | 1 |
| `VERTICAL` | Arriba y abajo | 1 |
| `CUADRADO` | Los 4 | 2 |
| `L1` a `L4` | Los 2 lados de 2 casillas | 2 |
| `LARGA1` a `LARGA4` | El único lado de 2 casillas | 2 |

Lado de salida de cada L de 4 casillas:

| Forma | Sale por |
|---|---|
| `LARGA1` | Abajo |
| `LARGA2` | Izquierda |
| `LARGA3` | Arriba |
| `LARGA4` | Derecha |

El lado de 3 casillas de una L de 4 **no** admite salida, porque el ancho máximo es 2. El código usa `Forma.getTamanoSalida(lado)` para obtener el ancho y `Forma.permiteSalida(lado)` para saber si ese lado es válido.

---

## Requisitos y ejecución

- **Java 11 o superior** (se usa `String.repeat`).
- Una terminal con soporte de colores **ANSI** para ver las piezas y las salidas coloreadas.

Compilar y ejecutar desde la carpeta que contiene `mx/`:

```bash
javac -d out mx/tecnm/culiacan/*.java
java -cp out mx.tecnm.culiacan.Main
```

Para cambiar de versión, edita en `Main.java`:

```java
Version version = Version.AVANZADA; // BASICA, INTERMEDIA o AVANZADA
```

---

## Ejemplo de ejecución

```
Ingresa Numero De Filas: 6
Ingresa Numero De Columnas: 6

Intento 1
Resultado: SOLUCION_ENCONTRADA
Nodos explorados: ...
Nodos generados: ...
Tiempo: 0.140 segundos

Algoritmo utilizado: A* ponderado
Solución en N movimientos:

1) Mover ROJA (R) de [r,c] hacia ARRIBA
2) ...
N) Sacar AZUL (Z) de [r,c] hacia DERECHA

Historial de movimientos:
...
```

Después de cada movimiento se imprime el tablero. Los números son ilustrativos.

---

## Arquitectura

| Clase | Responsabilidad |
|---|---|
| `Main` | Punto de entrada: elige la versión y arma las dependencias |
| `CuadroDeColores` | Coordinador del flujo: dimensiones, generación, reproducción de la solución |
| `Vista` | Toda la entrada y salida por consola |
| `ImpresorTablero` | Dibuja el tablero con colores ANSI |
| `Keyboard` | Lectura de teclado |
| `Version` | Enum con los rangos de cada versión y fábrica de `Reglas` |
| `Reglas` / `ReglasCuadro` / `ReglasException` | `Reglas` declara las validaciones y `ReglasCuadro` las implementa. Es la **única clase que lanza excepciones** (`ReglasException`): valida el número de lados y las reglas internas del tablero, las piezas y las salidas. `Tablero`, `TableroAvanzado`, `Estado` y `SolucionadorTablero` reciben un `Reglas` |
| `TableroJuego` | Interfaz común de los tableros |
| `Tablero` | Tablero cuadrado (básico e intermedio) |
| `TableroAvanzado` | Tablero rectangular con L, cuadrados y salidas de tamaño 2 |
| `Pieza` / `Forma` / `Orientacion` | Pieza con color y forma; formas y sus caras |
| `Ficha` | Colores disponibles (10) con su símbolo y código ANSI |
| `Salida` / `LadoSalida` | Salidas del borde |
| `Estado` | Estado del tablero como arreglo de bytes con id de pieza por casilla |
| `Movimiento` / `Paso` | Un movimiento posible y un paso concreto de la solución |
| `SolucionadorTablero` | Búsqueda A\* |
| `Nodo` | Nodo de la búsqueda |
| `ResultadoBusqueda` | `SOLUCION_ENCONTRADA`, `SIN_SOLUCION` o `LIMITE_ALCANZADO` |

### Manejo de errores

Ninguna clase lanza excepciones por su cuenta. Cuando una regla se rompe (rango de lados, identificador de pieza, tamaño de salida, colores insuficientes, casillas vacías, etc.), la clase que lo detecta llama a un método de la interfaz `Reglas`, y su implementación `ReglasCuadro` lanza `ReglasException`. `CuadroDeColores` la captura: en la captura de dimensiones vuelve a pedir el dato, y en la generación de tableros cuenta el fallo y prueba con otro tablero.

### Flujo de `CuadroDeColores.iniciar()`

1. `pedirDimensiones()`: según la versión, pide lados, o filas y columnas.
2. `generarTableroResoluble()`: crea tableros y los resuelve hasta encontrar uno con solución, o agota los intentos.
3. `mostrarSolucion()`: muestra el tablero, el algoritmo, la cantidad de movimientos, repite cada paso en el tablero y al final imprime el historial.

`CuadroDeColores` no tiene ningún `System.out`: toda la salida pasa por `Vista`.

---

## Generación de tableros

**Intermedio (`Tablero`):** coloca una pieza horizontal, una vertical y una individual fijas, rellena el resto en orden aleatorio (65 % de probabilidad de pieza de 2 casillas cuando cabe) y asigna colores sin que dos vecinas compartan color. Los colores se reparten por orientación: 4 para individuales, 3 para horizontales y 3 para verticales.

**Avanzado (`TableroAvanzado`):**

1. Coloca una pieza individual en `(0,0)`, un cuadrado, las 4 L elegidas (2 de 3 casillas y 2 de 4), una horizontal y una vertical.
2. Rellena las casillas restantes en orden aleatorio con formas candidatas (20 % de probabilidad de dejar una individual).
3. Colorea con un algoritmo tipo **DSATUR**: las piezas vecinas nunca comparten color y todas las piezas de una misma forma comparten color.
4. Coloca una salida por color. La primera salida es de la pieza `1`, arriba en el índice 0. Para las demás prefiere salidas alineadas con alguna pieza de ese color.
5. Valida que no haya casillas vacías y que haya entre 3 y 10 colores.

Si la generación falla, se reintenta hasta 100 veces dentro del propio constructor.

---

## Solucionador

`SolucionadorTablero` implementa **A\*** sobre estados de tablero:

- **Sucesores:** mover cada pieza una casilla en las 4 direcciones, o sacarla si está en el borde y su cara coincide con una salida de su color.
- **Costo:** 1 por movimiento.
- **Heurística:** suma, por pieza, de la distancia al borde de la mejor salida compatible más el desajuste de posición más 1.
- **Peso:** 1.0 (A\* clásico) para tableros de hasta 25 casillas y 1.5 (A\* ponderado) para los mayores. Con peso 1.5 la solución puede no ser la más corta.
- **Límites de búsqueda:**

| Casillas | Peso | Nodos explorados máx. | Estados máx. |
|---|---|---|---|
| hasta 25 | 1.0 | 100 000 | 150 000 |
| hasta 49 | 1.5 | 150 000 | 200 000 |
| más de 49 | 1.5 | 300 000 | 500 000 |

Si se supera un límite, el resultado es `LIMITE_ALCANZADO` y `CuadroDeColores` genera otro tablero. Si la búsqueda termina sin solución, el resultado es `SIN_SOLUCION`.

---

## Configuración

Constantes en `CuadroDeColores`:

| Constante | Valor | Significado |
|---|---|---|
| `INTENTOS_NORMAL` | 30 | Tableros a probar en básica e intermedia |
| `INTENTOS_AVANZADA` | 100 | Tableros a probar en avanzada |
| `UMBRAL_A_ESTRELLA` | 25 | Hasta esas casillas se muestra "A\*"; arriba, "A\* ponderado" |

Constante en `TableroAvanzado`:

| Constante | Valor | Significado |
|---|---|---|
| `ROTACIONES_POR_L` | 2 | Rotaciones distintas de cada tipo de L por tablero |

Con 10 colores disponibles, subir `ROTACIONES_POR_L` aumenta los reintentos de generación, porque cada rotación necesita su propio color.

---

## Limitaciones conocidas

- **Tableros de 5x5 en la versión avanzada:** casi todos los tableros llegan a `LIMITE_ALCANZADO` con el límite actual de 100 000 nodos. Para esos tamaños conviene subir el límite o usar el peso 1.5.
- **Tableros grandes (8x8 o más):** cada búsqueda tarda segundos y una parte de los tableros llega al límite.
- El mensaje "Algoritmo utilizado" se calcula en `CuadroDeColores` con la misma regla de 25 casillas que usa `SolucionadorTablero`. Si cambias una, cambia la otra, o expón el peso con `getPeso()`.
- La clase `Vista` imprime por consola, así que cambiar a una interfaz gráfica implica reemplazar `Vista` e `ImpresorTablero`.

---

## Créditos

- `Keyboard.java` es de Lewis y Loftus, de su libro de texto de Java, y se incluye sin cambios.
- Proyecto del TecNM Culiacán (`mx.tecnm.culiacan`).
