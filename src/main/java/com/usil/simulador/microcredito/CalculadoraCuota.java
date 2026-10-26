package com.usil.simulador.microcredito;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class CalculadoraCuota {

    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_UP);

    public BigDecimal calcularCuota(BigDecimal monto, int plazo, Segmento segmento) {
        if (monto == null || segmento == null) {
            throw new IllegalArgumentException("Monto y segmento no pueden ser nulos");
        }
        if (monto.compareTo(new BigDecimal("300")) < 0
                || monto.compareTo(new BigDecimal("20000")) > 0) {
            throw new IllegalArgumentException("Monto fuera de rango: " + monto);
        }
        if (plazo < 6 || plazo > 36) {
            throw new IllegalArgumentException("Plazo fuera de rango: " + plazo);
        }
        BigDecimal tasa = tasaAplicada(plazo, segmento);
        return cuotaFrancesa(monto, tasa, plazo);
    }

    public BigDecimal tasaAplicada(int plazo, Segmento segmento) {
        if (segmento == null) throw new IllegalArgumentException("Segmento nulo");
        if (plazo < 6 || plazo > 36) throw new IllegalArgumentException("Plazo fuera de rango");
        BigDecimal tasa = segmento.tasaBase();
        if (plazo > 24) {
            tasa = tasa.add(new BigDecimal("0.002"));
        }
        return tasa;
    }

    private BigDecimal cuotaFrancesa(BigDecimal monto, BigDecimal tasa, int n) {
        BigDecimal uno = BigDecimal.ONE;
        BigDecimal factor = uno.add(tasa).pow(-n, MC); // (1+i)^-n
        BigDecimal denominador = uno.subtract(factor);
        return monto.multiply(tasa)
                .divide(denominador, 10, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }
}