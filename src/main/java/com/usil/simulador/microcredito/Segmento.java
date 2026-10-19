package com.usil.simulador.microcredito;

import java.math.BigDecimal;

public enum Segmento {
    NUEVO(new BigDecimal("0.030")),
    RECURRENTE(new BigDecimal("0.025")),
    PREFERENTE(new BigDecimal("0.020"));

    private final BigDecimal tasaBaseMensual;

    Segmento(BigDecimal tasaBaseMensual) {
        this.tasaBaseMensual = tasaBaseMensual;
    }

    public BigDecimal getTasaBaseMensual() {
        return tasaBaseMensual;
    }
}
