package com.usil.simulador.microcredito;

import java.math.BigDecimal;

/* Clasifica una solicitud según la capacidad de pago (HU-02). */
public class EvaluadorSolicitud {

    static final int PUNTAJE_MINIMO = 500;
    static final BigDecimal LIMITE_APROBACION = new BigDecimal("0.40");
    static final BigDecimal LIMITE_REVISION = new BigDecimal("0.50");

    private final CalculadoraCuota calculadora;

    public EvaluadorSolicitud(CalculadoraCuota calculadora) {
        this.calculadora = calculadora;
    }

    public Resultado evaluar(Solicitud solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud es obligatoria");
        }

        BigDecimal cuota = calculadora.calcularCuota(
                solicitud.getMonto(), 
                solicitud.getPlazoMeses(), 
                solicitud.getSegmento()
        );

        return clasificar(cuota, solicitud.getIngresoMensual(), 
                          solicitud.getPuntajeCrediticio(), solicitud.isMoraVigente());
    }

    private Resultado clasificar(BigDecimal cuota, BigDecimal ingresoMensual, int puntaje, boolean moraVigente) {
        
        if (puntaje < PUNTAJE_MINIMO || moraVigente) {
            return Resultado.RECHAZADA;
        }

        // RN-06: Evaluar los umbrales del 40% y 50%
        BigDecimal cuota40 = ingresoMensual.multiply(LIMITE_APROBACION);
        BigDecimal cuota50 = ingresoMensual.multiply(LIMITE_REVISION);

        if (cuota.compareTo(cuota40) <= 0) {
            return Resultado.APROBADA;
        }
        if (cuota.compareTo(cuota50) <= 0) {
            return Resultado.REVISION_MANUAL;
        }
        return Resultado.RECHAZADA;
    }
}