package com.usil.simulador.microcredito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class CalculadoraCuotaTest {
    private final CalculadoraCuota calculadora = new CalculadoraCuota();

    @Test
    public void calculaCuotaNominal() {
        BigDecimal cuota = calculadora.calcularCuota(new BigDecimal("1000"), 12, Segmento.PREFERENTE);
        assertEquals(new BigDecimal("94.56"), cuota, "La cuota debe ser 94.56");
    }

    @ParameterizedTest
    @CsvSource({
        "299.99, 12",
        "20000.01, 12",
        "1000, 5",
        "1000, 37"
    })
    public void rechazaMontoOPlazoFueraDeRango(String monto, int plazo) {
        assertThrows(IllegalArgumentException.class, () -> {
            calculadora.calcularCuota(new BigDecimal(monto), plazo, Segmento.NUEVO);
        }, "Debe lanzar excepción por monto o plazo inválido");
    }

        @Test
    public void aplicaRecargoEnPlazoMayorA24() {
        // 25 meses NUEVO: 3.0% + 0.2% = 3.2%
        // Verificamos indirectamente calculando la cuota (1000 a 25 meses con 3.2%)
        BigDecimal cuotaConRecargo = calculadora.calcularCuota(new BigDecimal("1000"), 25, Segmento.NUEVO);
        
        // El valor correcto con 3.2% es 58.72 (el 47.83 era con 2%, error mío)
        assertEquals(new BigDecimal("58.72"), cuotaConRecargo, "Debe aplicar el recargo de 0.2%");
    }
}