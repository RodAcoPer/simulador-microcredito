package com.usil.simulador.microcredito;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CalculadoraCuota {

    private static final BigDecimal MONTO_MIN = new BigDecimal("300");
    private static final BigDecimal MONTO_MAX = new BigDecimal("20000");
    private static final int PLAZO_MIN = 6;
    private static final int PLAZO_MAX = 36;
    private static final BigDecimal RECARGO_PLAZO_MAYOR_24 = new BigDecimal("0.002");
    private static final MathContext MC = MathContext.DECIMAL128;

    public BigDecimal calcularCuota(BigDecimal monto, Integer plazoMeses, Segmento segmento) {
        validarMonto(monto);
        BigDecimal tasaMensual = tasaMensual(segmento, plazoMeses);
        return calcularCuotaConTasa(monto, plazoMeses, tasaMensual);
    }

    /**
     * Sobrecarga útil para la demostración del eje 2: calcularCuota(monto, plazo, tasa).
     */
    public BigDecimal calcularCuotaConTasa(BigDecimal monto, Integer plazoMeses, BigDecimal tasaMensual) {
        validarMonto(monto);
        validarPlazo(plazoMeses);
        if (tasaMensual == null) {
            throw new IllegalArgumentException("Tasa mensual obligatoria");
        }

        BigDecimal unoMasI = BigDecimal.ONE.add(tasaMensual, MC);
        BigDecimal potencia = unoMasI.pow(plazoMeses, MC);
        BigDecimal denominador = BigDecimal.ONE.subtract(
                BigDecimal.ONE.divide(potencia, MC), MC);

        BigDecimal cuota = monto.multiply(tasaMensual, MC)
                .divide(denominador, MC);

        return cuota.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal tasaMensual(Segmento segmento, Integer plazoMeses) {
        if (segmento == null) {
            throw new IllegalArgumentException("Segmento obligatorio");
        }
        validarPlazo(plazoMeses);

        BigDecimal tasa = segmento.getTasaBaseMensual();
        if (plazoMeses > 24) {
            tasa = tasa.add(RECARGO_PLAZO_MAYOR_24);
        }
        return tasa;
    }

    private void validarMonto(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("Monto obligatorio");
        }
        if (monto.compareTo(MONTO_MIN) < 0 || monto.compareTo(MONTO_MAX) > 0) {
            throw new IllegalArgumentException("Monto fuera de rango: 300 a 20000");
        }
    }

    private void validarPlazo(Integer plazoMeses) {
        if (plazoMeses == null) {
            throw new IllegalArgumentException("Plazo obligatorio");
        }
        if (plazoMeses < PLAZO_MIN || plazoMeses > PLAZO_MAX) {
            throw new IllegalArgumentException("Plazo fuera de rango: 6 a 36 meses");
        }
    }
}