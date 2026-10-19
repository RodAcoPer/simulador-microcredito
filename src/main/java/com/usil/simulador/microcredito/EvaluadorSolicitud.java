package com.usil.simulador.microcredito;

import java.math.BigDecimal;

public class EvaluadorSolicitud {
    private final CalculadoraCuota calculadora;

    public EvaluadorSolicitud(CalculadoraCuota calculadora) {
        this.calculadora = calculadora;
    }

    public Resultado evaluar(Solicitud solicitud) {
<<<<<<< HEAD
        if (solicitud == null) throw new IllegalArgumentException("Solicitud obligatoria");
        if (solicitud.getMonto() == null) throw new IllegalArgumentException("Monto obligatorio");
        if (solicitud.getPlazoMeses() == null) throw new IllegalArgumentException("Plazo obligatorio");
        if (solicitud.getSegmento() == null) throw new IllegalArgumentException("Segmento obligatorio");
        if (solicitud.getIngresoMensual() == null || solicitud.getIngresoMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Ingreso mensual debe ser mayor que 0");
        }
=======
        // AQUI ESTABA EL ERROR: Faltaba validar el ingreso y los nulos (RN-07)
        validarSolicitud(solicitud);
>>>>>>> c0e770700945b33bafdba7d78e193f8a8d7946a8

        BigDecimal cuota = calculadora.calcularCuota(solicitud.getMonto(), solicitud.getPlazoMeses(), solicitud.getSegmento());

<<<<<<< HEAD
        if (solicitud.getPuntajeCrediticio() < 500 || solicitud.isMoraVigente()) {
            return Resultado.RECHAZADA;
        }

        BigDecimal limite40 = solicitud.getIngresoMensual().multiply(new BigDecimal("0.40"));
        BigDecimal limite50 = solicitud.getIngresoMensual().multiply(new BigDecimal("0.50"));
=======
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
>>>>>>> c0e770700945b33bafdba7d78e193f8a8d7946a8

        if (cuota.compareTo(limite40) <= 0) {
            return Resultado.APROBADA;
        } else if (cuota.compareTo(limite50) <= 0) {
            return Resultado.REVISION_MANUAL;
        } else {
            return Resultado.RECHAZADA;
        }
    }
}
