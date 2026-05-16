# DESIGN - Impostor Android Game

Este documento describe la arquitectura de diseño actual de la aplicación Impostor Android Game para servir como referencia en mejoras y rediseños.

## 1. Concepto General
La aplicación es un juego de mesa digital orientado a grupos. El diseño debe ser **divertido, intuitivo y moderno**, facilitando el juego rápido sin fricciones innecesarias. Se basa en **Material Design 3 (M3)** pero con toques personalizados para darle carácter lúdico.

## 2. Identidad Visual

### Paleta de Colores (Core UI)
El tema utiliza Material 3 con soporte para tres niveles de contraste (Bajo, Medio, Alto) y modo claro/oscuro.

*   **Primario:** Azul (`#1A5FB5` light / `#A0C8FF` dark) - Color principal de acciones e interacción.
*   **Secundario:** Verde (`#1C6B47` light / `#98D8AB` dark) - Confirmación, civiles, elementos positivos.
*   **Terciario:** Amarillo (`#785A00` light / `#FBC21F` dark) - Advertencias, tensión, énfasis especial.
*   **Error:** Rojo M3 estándar (`#BA1A1A`) - Errores y roles impostores.
*   **Fondos:**
    *   *Modo Claro:* Blanco frío neutro (`#F8F9FF`).
    *   *Modo Oscuro:* Gris oscuro frío (`#111318`) con surfaces en `#161A22`.

### Tipografía
Se utilizan Google Fonts para diferenciar las jerarquías de información:
*   **Títulos:** `Finger Paint` - Una fuente estilo "pintado a mano" que refuerza el tono informal del juego.
*   **Cuerpo:** `Gupter` - Para una lectura clara y profesional en bloques de texto.
*   **Display/Encabezados:** `Raleway` - Para un look moderno y limpio en etiquetas y botones.

## 3. Experiencia de Usuario (UX)

### Flujo de Navegación
1.  **Splash:** Pantalla de carga con logo animado.
2.  **Login:** Gestión de identidad de usuario.
3.  **Home (Dashboard):** Acceso central a jugar, perfil, ranking y ajustes.
4.  **Setup de Partida:**
    *   Selección de Categorías (Grilla de tarjetas con iconos).
    *   Gestión de Jugadores (Lista dinámica).
    *   Modo de Juego (Selección de variantes).
5.  **Ciclo de Juego:**
    *   **Reveal:** Fase crítica donde cada jugador descubre su palabra mediante una **tarjeta animada que rota (3D rotation)** al ser arrastrada o pulsada.
    *   **Discussion:** Pantalla con temporizador para debate.
    *   **Result:** Resumen visual de ganadores y perdedores con tarjetas de puntuación.

### Componentes Clave
*   **Reveal Card:** Tarjeta con sombra pronunciada y animación de rotación en el eje Y (0 a 180 grados).
*   **Categorías:** Cards interactivas con estados visuales claros para selección.
*   **Botones:** Uso extensivo de botones con esquinas redondeadas (M3) y colores de contenedor primarios.

## 4. Áreas de Mejora (Feedback deseado)
*   **Consistencia de Iconografía:** Mejorar los iconos de las categorías para que se sientan parte de un mismo pack.
*   **Feedback Visual:** Mejorar las transiciones entre fases de juego para que se sientan más "vivas".
*   **Jerarquía en Resultados:** Hacer que la pantalla de ganadores sea más impactante visualmente.
*   **Accesibilidad:** Asegurar que los niveles de contraste medio/alto realmente ayuden sin perder la estética del juego.

---
*Este archivo está diseñado para ser compartido con herramientas de diseño/IA para iterar sobre la estética de la app.*
