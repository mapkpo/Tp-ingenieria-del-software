# Pong

Trabajo Practico Ingenieria de Software - FCEFyN

## Integrantes

- Bevilacqua, Francisco
- Gil Cernich, Manuel
- Cisneros, Tomás
- Potinski, Mijail

---

## Requisitos

- Java Development Kit (JDK) 8 o superior.
- IDE de Java para compilar y ejecutar el proyecto. (Opcional)

## Ejecución
Para ejecutar el juego, sigue los siguientes pasos:

1. Clona el repositorio.
2. Abre el proyecto en tu IDE.
3. Ejecuta el archivo `Game.java` para iniciar el juego.

Otro metodo es ejecutar el archivo `Pong.jar` que se encuentra en la seccion de releases.

```bash
java -jar pong-1.0.jar
```
---

## Descripcion
Pong es un juego de arcade clásico en el que dos jugadores controlan paletas en los extremos de la pantalla y tratan de golpear una pelota hacia el lado opuesto para anotar puntos. 

- Utiliza las teclas de flecha izquierda y derecha para mover la paleta.
- La paleta del enemigo se mueve automáticamente con un nivel de dificultad determinado.

<p align="center">
  <img src="https://github.com/mapkpo/Tp-ingenieria-del-software/assets/20894332/8bb91774-8861-4ac2-a2a0-46efc961f816"><br>
  <em>Fig 1. Juego en ejecucion.</em>
</p>

### Documentacion
Para la documentacion del juego se utilizo la herramienta Doxygen, la cual permite generar documentacion a partir de comentarios en el codigo fuente.

El archivo de configuracion de Doxygen se encuentra en la raiz del proyecto bajo el nombre `Doxyfile`, y para poder visualizarlo se proporcionan diferentes `.html` para navegar la documentacion.

<p align="center">
  <img src="https://github.com/mapkpo/Tp-ingenieria-del-software/assets/20894332/77c34ef9-7211-4af9-b8ca-25e7cf091460"><br>
  <em>Fig 2. Demostracion de la documentacion.</em>
</p>

### Coverage Report
Para la generacion de un reporte de cobertura de codigo se utilizo la herramienta JaCoCo, la cual permite analizar el codigo fuente y generar un reporte en formato `.html`.

Actualmente el proyecto cuenta con un 50% de cobertura de codigo, el cual se puede ver en la siguiente imagen.

<p align="center">
  <img src="https://github.com/mapkpo/Tp-ingenieria-del-software/assets/20894332/e1ebccf9-55de-4247-a58d-06881c8bc4b5"><br>
  <em>Fig 3. Reporte de coverage.</em>
</p>

---

#### Nota
En las issues del repositorio se encuentran los diagramas de clases y de secuencia correspondientes al proyecto.

---

## Anexo

- [Drive del grupo](https://drive.google.com/drive/folders/1aoHKunj3fMuHrh10rXfOI_m_sZGkhvHD?usp=sharing)
- [Repositorio idea original](https://github.com/diegolrs/Pong)
