package com.usil.simulador.microcredito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class EvaluadorSolicitudTest {
    
    private EvaluadorSolicitud evaluador;
    private final BigDecimal MONTO = new BigDecimal("1000");
    private final int PLAZO = 12;
    private final Segmento SEGMENTO = Segmento.PREFERENTE; // Cuota = 94.56

    @BeforeEach
    public void setUp() {
        evaluador = new EvaluadorSolicitud(new CalculadoraCuota());
    }

    @Test
    public void apruebaSiCuotaEsExactamente40PorCiento() {
        // 94.56 es el 40% exacto de 236.40
        Solicitud solicitud = new Solicitud(MONTO, PLAZO, SEGMENTO, new BigDecimal("236.40"), 700, false);
        assertEquals(Resultado.APROBADA, evaluador.evaluar(solicitud));
    }

    @Test
    public void revisionSiCuotaEsExactamente50PorCiento() {
        // 94.56 es el 50% exacto de 189.12
        Solicitud solicitud = new Solicitud(MONTO, PLAZO, SEGMENTO, new BigDecimal("189.12"), 700, false);
        assertEquals(Resultado.REVISION_MANUAL, evaluador.evaluar(solicitud));
    }

    @Test
    public void rechazaSiCuotaSupera50PorCiento() {
        // 94.56 sobre 180.00 = 52.5%
        Solicitud solicitud = new Solicitud(MONTO, PLAZO, SEGMENTO, new BigDecimal("180.00"), 700, false);
        assertEquals(Resultado.RECHAZADA, evaluador.evaluar(solicitud));
    }

    @Test
    public void rechazaPorPuntajeMenorA500() {
        Solicitud solicitud = new Solicitud(MONTO, PLAZO, SEGMENTO, new BigDecimal("1000.00"), 499, false);
        assertEquals(Resultado.RECHAZADA, evaluador.evaluar(solicitud));
    }

    @Test
    public void rechazaPorMoraVigente() {
        Solicitud solicitud = new Solicitud(MONTO, PLAZO, SEGMENTO, new BigDecimal("1000.00"), 700, true);
        assertEquals(Resultado.RECHAZADA, evaluador.evaluar(solicitud));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1"})
    public void rechazaIngresoNoPositivo(String ingresoStr) {
        Solicitud solicitud = new Solicitud(MONTO, PLAZO, SEGMENTO, new BigDecimal(ingresoStr), 700, false);
        assertThrows(IllegalArgumentException.class, () -> evaluador.evaluar(solicitud));
    }
}