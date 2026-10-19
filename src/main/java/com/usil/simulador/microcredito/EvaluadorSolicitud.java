package com.usil.simulador.microcredito;

import java.math.BigDecimal;

public class EvaluadorSolicitud {
    private final CalculadoraCuota calculadora;

    public EvaluadorSolicitud(CalculadoraCuota calculadora) {
        this.calculadora = calculadora;
    }

    public Resultado evaluar(Solicitud solicitud) {
        if (solicitud == null) throw new IllegalArgumentException("Solicitud obligatoria");
        if (solicitud.getMonto() == null) throw new IllegalArgumentException("Monto obligatorio");
        if (solicitud.getPlazoMeses() == null) throw new IllegalArgumentException("Plazo obligatorio");
        if (solicitud.getSegmento() == null) throw new IllegalArgumentException("Segmento obligatorio");
        if (solicitud.getIngresoMensual() == null || solicitud.getIngresoMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Ingreso mensual debe ser mayor que 0");
        }

        BigDecimal cuota = calculadora.calcularCuota(solicitud.getMonto(), solicitud.getPlazoMeses(), solicitud.getSegmento());

        if (solicitud.getPuntajeCrediticio() < 500 || solicitud.isMoraVigente()) {
            return Resultado.RECHAZADA;
        }

        BigDecimal limite40 = solicitud.getIngresoMensual().multiply(new BigDecimal("0.40"));
        BigDecimal limite50 = solicitud.getIngresoMensual().multiply(new BigDecimal("0.50"));

        if (cuota.compareTo(limite40) <= 0) {
            return Resultado.APROBADA;
        } else if (cuota.compareTo(limite50) <= 0) {
            return Resultado.REVISION_MANUAL;
        } else {
            return Resultado.RECHAZADA;
        }
    }
}