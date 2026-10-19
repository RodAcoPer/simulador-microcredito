package com.usil.simulador.microcredito;

import java.math.BigDecimal;

public class Solicitud {
    private final BigDecimal monto;
    private final Integer plazoMeses;
    private final Segmento segmento;
    private final BigDecimal ingresoMensual;
    private final int puntajeCrediticio;
    private final boolean moraVigente;

    public Solicitud(BigDecimal monto, Integer plazoMeses, Segmento segmento, 
                     BigDecimal ingresoMensual, int puntajeCrediticio, boolean moraVigente) {
        this.monto = monto;
        this.plazoMeses = plazoMeses;
        this.segmento = segmento;
        this.ingresoMensual = ingresoMensual;
        this.puntajeCrediticio = puntajeCrediticio;
        this.moraVigente = moraVigente;
    }

    public BigDecimal getMonto() { return monto; }
    public Integer getPlazoMeses() { return plazoMeses; }
    public Segmento getSegmento() { return segmento; }
    public BigDecimal getIngresoMensual() { return ingresoMensual; }
    public int getPuntajeCrediticio() { return puntajeCrediticio; }
    public boolean isMoraVigente() { return moraVigente; }
}