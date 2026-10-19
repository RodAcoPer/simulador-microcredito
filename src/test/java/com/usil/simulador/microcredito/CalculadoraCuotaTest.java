/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.usil.simulador.microcredito;

// Imports por defecto del template NetBeans JUnit 5
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Imports adicionales necesarios para @ParameterizedTest (CA-02, CA-03, CA-04, CA-05)
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author Rodrigo Acosta
 */
public class CalculadoraCuotaTest {

    private CalculadoraCuota calculadora;

    public CalculadoraCuotaTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        calculadora = new CalculadoraCuota();
    }

    @AfterEach
    public void tearDown() {
        calculadora = null;
    }

    // CA-01: PREFERENTE, 1000 a 12 meses -> 94.56 (RN-03, RN-05)
    @Test
    public void calculaCuotaNominal() {
        // Arrange
        BigDecimal monto = new BigDecimal("1000");
        int plazo = 12;
        Segmento segmento = Segmento.PREFERENTE;
        BigDecimal esperado = new BigDecimal("94.56");

        // Act
        BigDecimal cuota = calculadora.calcularCuota(monto, plazo, segmento);

        // Assert
        assertEquals(esperado, cuota,
                "La cuota para 1000 a 12 meses con PREFERENTE debe ser 94.56");
    }

    // Eje 2: sobrecarga calcularCuotaConTasa con tasa explícita del 2% mensual
    @Test
    public void calculaCuotaConTasaExplicita() {
        BigDecimal cuota = calculadora.calcularCuotaConTasa(
                new BigDecimal("1000"), 12, new BigDecimal("0.02"));

        assertEquals(new BigDecimal("94.56"), cuota,
                "La fórmula francesa con 2% mensual y 12 meses debe dar 94.56");
    }

    // CA-02 / CA-03: montos y plazos fuera de rango lanzan excepción
    @ParameterizedTest(name = "CA-02/CA-03: monto={0}, plazo={1} lanza excepción")
    @CsvSource({
        "299.99, 12",
        "20000.01, 12",
        "1000, 5",
        "1000, 37"
    })
    public void rechazaMontoOPlazoFueraDeRango(String monto, int plazo) {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> calculadora.calcularCuota(new BigDecimal(monto), plazo, Segmento.NUEVO),
                "Debe lanzar IllegalArgumentException fuera de rango");

        assertTrue(
                ex.getMessage().contains("Monto") || ex.getMessage().contains("Plazo"),
                "El mensaje debe indicar monto o plazo fuera de rango");
    }

    // CA-02 / CA-03 / CA-04 / CA-05: valores límite con @MethodSource
    @ParameterizedTest(name = "Límite monto={0}, plazo={1}, válido={2}")
    @MethodSource("limites")
    public void validaValoresLimite(BigDecimal monto, int plazo, boolean valido) {
        if (valido) {
            assertDoesNotThrow(
                    () -> calculadora.calcularCuota(monto, plazo, Segmento.NUEVO),
                    "El límite inferior/superior debe ser aceptado");
        } else {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> calculadora.calcularCuota(monto, plazo, Segmento.NUEVO),
                    "El valor justo fuera del límite debe rechazarse");
        }
    }

    static Stream<Arguments> limites() {
        return Stream.of(
                Arguments.of(new BigDecimal("299.99"), 12, false),
                Arguments.of(new BigDecimal("300"), 12, true),
                Arguments.of(new BigDecimal("20000"), 12, true),
                Arguments.of(new BigDecimal("20000.01"), 12, false),
                Arguments.of(new BigDecimal("1000"), 5, false),
                Arguments.of(new BigDecimal("1000"), 6, true),
                Arguments.of(new BigDecimal("1000"), 36, true),
                Arguments.of(new BigDecimal("1000"), 37, false)
        );
    }

    // CA-05: plazo 24, NUEVO -> 3.0% sin recargo (RN-04)
    @Test
    public void sinRecargoEnPlazo24() {
        assertEquals(new BigDecimal("0.030"),
                calculadora.tasaMensual(Segmento.NUEVO, 24),
                "El plazo 24 no debe aplicar recargo");
    }

    // CA-04: plazo 25, NUEVO -> 3.2% con recargo (RN-04)
    @Test
    public void aplicaRecargoEnPlazo25() {
        assertEquals(new BigDecimal("0.032"),
                calculadora.tasaMensual(Segmento.NUEVO, 25),
                "El plazo mayor a 24 debe sumar 0.2 puntos porcentuales");
    }
}