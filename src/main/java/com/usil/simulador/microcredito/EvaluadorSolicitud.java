package com.usil.simulador.microcredito;

import java.math.BigDecimal;

public class EvaluadorSolicitud {

    static final int PUNTAJE_MINIMO = 500;
    static final BigDecimal LIMITE_APROBACION = new BigDecimal("0.40");
    static final BigDecimal LIMITE_REVISION = new BigDecimal("0.50");

    private final CalculadoraCuota calculadora;

    public EvaluadorSolicitud(CalculadoraCuota calculadora) {
        this.calculadora = calculadora;
    }

    public Resultado evaluar(Solicitud solicitud) {
        // AQUI ESTABA EL ERROR: Faltaba validar el ingreso y los nulos (RN-07)
        validarSolicitud(solicitud);

        BigDecimal cuota = calculadora.calcularCuota(
                solicitud.getMonto(), 
                solicitud.getPlazoMeses(), 
                solicitud.getSegmento()
        );

        return clasificar(cuota, solicitud.getIngresoMensual(), 
                          solicitud.getPuntajeCrediticio(), solicitud.isMoraVigente());
    }

    private void validarSolicitud(Solicitud solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud es obligatoria");
        }
        if (solicitud.getMonto() == null) {
            throw new IllegalArgumentException("El monto es obligatorio");
        }
        if (solicitud.getPlazoMeses() == null) {
            throw new IllegalArgumentException("El plazo es obligatorio");
        }
        if (solicitud.getSegmento() == null) {
            throw new IllegalArgumentException("El segmento es obligatorio");
        }
        if (solicitud.getIngresoMensual() == null || 
            solicitud.getIngresoMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El ingreso mensual debe ser mayor a 0");
        }
    }

    private Resultado clasificar(BigDecimal cuota, BigDecimal ingresoMensual, int puntaje, boolean moraVigente) {
        if (puntaje < PUNTAJE_MINIMO || moraVigente) {
            return Resultado.RECHAZADA;
        }

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
