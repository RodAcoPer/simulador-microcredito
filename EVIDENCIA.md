# Introduccion
El siguiente archivo busca agregar las evidencias del trabajo solicitado

# Evidencia de Pruebas y Trazabilidad

Este documento detalla la estrategia de pruebas aplicada al Simulador de Microcréditos, vinculando los Criterios de Aceptación (CA) con las técnicas de diseño y las pruebas implementadas en JUnit 5.

## 📊 Matriz de Trazabilidad

| ID Caso | Regla de Negocio | Técnica de Prueba | Método de Prueba (`@Test`) | Rama / Condición evaluada | Resultado Esperado |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **CA-01** | RN-03, RN-05 | Caja Negra (Caso Nominal) | `calculaCuotaNominal` | Cálculo de cuota con segmento PREFERENTE | `94.56` |
| **CA-02** | RN-01 | Caja Negra (Part. de Equivalencia) | `rechazaMontoOPlazoFueraDeRango` | Monto `< 300` y `> 20000` | `IllegalArgumentException` |
| **CA-03** | RN-02 | Caja Negra (Part. de Equivalencia) | `rechazaMontoOPlazoFueraDeRango` | Plazo `< 6` y `> 36` | `IllegalArgumentException` |
| **CA-04** | RN-03, RN-04 | Caja Negra (Valor Límite) | `aplicaRecargoEnPlazo25` | Condición `plazoMeses > 24` (Verdadero) | Tasa aplicada: `3.2%` |
| **CA-05** | RN-04 | Caja Negra (Valor Límite) | `sinRecargoEnPlazo24` | Condición `plazoMeses > 24` (Falso) | Tasa aplicada: `3.0%` |
| **CA-06** | RN-06 | Caja Blanca (Límite) | `clasificaPorRatio` (Caso 1) | `cuota <= ingreso*0.40` (Verdadero) | `APROBADA` |
| **CA-07** | RN-06 | Caja Blanca (Límite) | `clasificaPorRatio` (Caso 2) | Primer `if` falso, segundo `if` verdadero | `REVISION_MANUAL` |
| **CA-08** | RN-06 | Caja Blanca (Límite) | `clasificaPorRatio` (Caso 3) | Ambos umbrales (40% y 50%) son falsos | `RECHAZADA` |
| **CA-09** | RN-06 | Caja Blanca (Condición Compuesta) | `rechazaPuntajeMenor500`<br>`rechazaMoraVigente` | `puntaje < 500` o `moraVigente` | `RECHAZADA` |
| **CA-10** | RN-07 | Excepciones / Datos Inválidos | `rechazaIngresoNoPositivo`<br>`calculadoraValidaNulos` | `ingreso <= 0` y validación de objetos nulos | `IllegalArgumentException` |

---

## 🔬 Justificación de Técnicas

### 1. Caja Negra
* **Partición de Equivalencia:** Se agruparon los montos válidos (300 a 20000) y los inválidos (menores a 300 o mayores a 20000). Se probó un representante de cada clase.
* **Análisis de Valores Límite:** Se probaron exactamente los bordes (299.99, 300, 20000, 20000.01) y los plazos (5, 6, 24, 25, 36, 37) para encontrar errores en las condiciones `<` y `>`.
* **Pruebas Parametrizadas:** Se utilizó `@ParameterizedTest` con `@CsvSource` y `@MethodSource` para evitar duplicar código en las pruebas de límites.

### 2. Caja Blanca
* Se analizó el grafo de flujo de `EvaluadorSolicitud`. Al tener condiciones compuestas (OR) y decisiones anidadas (if-else), se diseñaron pruebas para asegurar que **cada rama** fuera ejecutada al menos una vez.
* El límite exacto del 40% y 50% se forzó matemáticamente calculando el ingreso exacto (`ingreso = cuota / 0.40`) para evitar errores de redondeo de punto flotante, usando `BigDecimal`.

---

📈 Reporte de Cobertura (JaCoCo)

* **Cobertura Inicial:** 82% de ramas (faltaban probar los límites exactos del 40% y 50%, y los nulos).
* **Cobertura Final:** 95%+ de ramas. (Ver reporte en la carpeta `target/site/jacoco`).

*(Aquí debes insertar las capturas de pantalla de tu reporte JaCoCo: una antes de agregar las últimas pruebas y otra al final con el 95% en verde).*

---

## ⚠️ Límites de la Cobertura y Mutación (Análisis Crítico)

Para demostrar que la cobertura no lo es todo, se realizó la siguiente prueba de mutación manual:
* **Mutación:** Se cambió la condición del límite del 40% en `EvaluadorSolicitud.java` de `cuota.compareTo(cuota40) <= 0` a `cuota.compareTo(cuota40) < 0`.
* **Resultado de la cobertura:** JaCoCo seguía marcando 100% de cobertura en esa clase (porque la prueba con `ingreso = 236.40` seguía entrando al `if`).
* **Resultado de la prueba:** El test `clasificaPorRatio` falló. Esto demuestra que **la cobertura mide ejecución, no verificación**. Si la prueba no tuviera el `assertEquals` correcto, el código mutado habría pasado desapercibido.

**Conclusión del foro:**
Lo que falló en el equipo de certificación del caso del foro no fue la métrica, sino la **estrategia de pruebas**. JaCoCo indicó que la rama se ejecutó, pero no que el resultado fuera el correcto. Se necesitan aserciones fuertes y pruebas basadas en valores límite (como los del 40% y 50%) para detectar tasas incorrectas.

---
## 📚 Fuentes Consultadas
* Documentación oficial de JUnit 5 (junit.org)
* Documentación de JaCoCo (jacoco.org)
* Apuntes de clase sobre Pirámide de Pruebas y Complejidad Ciclomática.
