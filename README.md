# Simulador de Microcréditos (HU-01 y HU-02)

Este proyecto es un componente de lógica de negocio en Java 17 para calcular la cuota mensual de un microcrédito (HU-01) y evaluar solicitudes según la capacidad de pago (HU-02). Fue desarrollado como parte del curso de Pruebas de Software, aplicando estrategias de pruebas unitarias, caja negra, caja blanca y medición de cobertura.

## 🛠️ Tecnologías Utilizadas
* **Java 17** (OpenJDK / Temurin)
* **Maven** (Gestión de dependencias y construcción)
* **JUnit 5 (Jupiter)** (Pruebas unitarias y parametrizadas)
* **JaCoCo** (Medición de cobertura de código, configurado al 95% de ramas)
* **GitHub Actions** (Pipeline de Integración Continua - CI)

## 📂 Estructura del Proyecto
* `com.usil.simulador.microcredito.Segmento`: Enum con las tasas base (NUEVO, RECURRENTE, PREFERENTE).
* `com.usil.simulador.microcredito.Resultado`: Enum de salida (APROBADA, REVISION_MANUAL, RECHAZADA).
* `com.usil.simulador.microcredito.Solicitud`: Modelo de datos del cliente.
* `com.usil.simulador.microcredito.CalculadoraCuota`: Lógica de amortización francesa (RN-01 a RN-05).
* `com.usil.simulador.microcredito.EvaluadorSolicitud`: Lógica de clasificación (RN-06 y RN-07).

## 🚀 Cómo ejecutar y probar

Para compilar el proyecto y ejecutar las pruebas unitarias:
```bash
mvn -B clean test
