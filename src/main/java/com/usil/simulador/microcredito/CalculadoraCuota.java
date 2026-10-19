package com.usil.simulador.microcredito;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CalculadoraCuota {
    private static final BigDecimal MONTO_MIN = new BigDecimal("300");
    private static final BigDecimal MONTO_MAX = new BigDecimal("20000");
    private static final int PLAZO_MIN = 6;
    private static final int PLAZO_MAX = 36;
    private static final BigDecimal RECARGO = new BigDecimal("0.002");
    private static final MathContext MC = MathContext.DECIMAL128;

    public BigDecimal calcularCuota(BigDecimal monto, Integer plazoMeses, Segmento segmento) {
        if (monto == null || monto.compareTo(MONTO_MIN) < 0 || monto.compareTo(MONTO_MAX) > 0) {
            throw new IllegalArgumentException("Monto fuera de rango: 300 a 20000");
        }
        if (plazoMeses == null || plazoMeses < PLAZO_MIN || plazoMeses > PLAZO_MAX) {
            throw new IllegalArgumentException("Plazo fuera de rango: 6 a 36 meses");
        }
        if (segmento == null) {
            throw new IllegalArgumentException("Segmento obligatorio");
        }

        BigDecimal tasa = segmento.getTasaBaseMensual();
        if (plazoMeses > 24) {
            tasa = tasa.add(RECARGO);
        }

        BigDecimal unoMasI = BigDecimal.ONE.add(tasa, MC);
        BigDecimal potencia = unoMasI.pow(plazoMeses, MC);
        BigDecimal denominador = BigDecimal.ONE.subtract(BigDecimal.ONE.divide(potencia, MC), MC);
        BigDecimal cuota = monto.multiply(tasa, MC).divide(denominador, MC);
        
        return cuota.setScale(2, RoundingMode.HALF_UP);
    }
}