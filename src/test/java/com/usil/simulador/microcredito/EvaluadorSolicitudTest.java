package com.usil.simulador.microcredito;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author Rodrigo Acosta
 */
public class EvaluadorSolicitudTest {

    private EvaluadorSolicitud evaluador;

    private static final BigDecimal MONTO = new BigDecimal("1000");
    private static final int PLAZO = 12;
    private static final Segmento SEGMENTO = Segmento.PREFERENTE;

    public EvaluadorSolicitudTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        evaluador = new EvaluadorSolicitud(new CalculadoraCuota());
    }

    @AfterEach
    public void tearDown() {
        evaluador = null;
    }

    // CA-06 / CA-07 / CA-08: clasificación por ratio cuota/ingreso (RN-06)
    @ParameterizedTest(name = "CA-06/07/08: ingreso={0} -> {1}")
    @MethodSource("casosRatio")
    public void clasificaPorRatio(BigDecimal ingreso, Resultado esperado) {
        // Arrange
        Solicitud solicitud = new Solicitud(
                MONTO, PLAZO, SEGMENTO, ingreso, 700, false);

        // Act
        Resultado obtenido = evaluador.evaluar(solicitud);

        // Assert
        assertEquals(esperado, obtenido,
                "El ratio cuota/ingreso debe clasificar como " + esperado);
    }

    static Stream<Arguments> casosRatio() {
        return Stream.of(
                // Cuota CA-01 = 94.56; 94.56 es exactamente el 40% de 236.40
                Arguments.of(new BigDecimal("236.40"), Resultado.APROBADA),
                // 94.56 es exactamente el 50% de 189.12
                Arguments.of(new BigDecimal("189.12"), Resultado.REVISION_MANUAL),
                // 94.56 / 180.00 = 52.53% > 50%
                Arguments.of(new BigDecimal("180.00"), Resultado.RECHAZADA)
        );
    }

    // CA-09: puntaje 499 rechaza aunque la cuota sea baja (RN-06)
    @Test
    public void rechazaPuntajeMenor500() {
        Solicitud solicitud = new Solicitud(
                MONTO, PLAZO, SEGMENTO, new BigDecimal("1000"), 499, false);

        assertEquals(Resultado.RECHAZADA, evaluador.evaluar(solicitud),
                "Puntaje menor a 500 debe rechazar sin importar la cuota");
    }

    // CA-09: mora vigente rechaza aunque la cuota sea baja (RN-06)
    @Test
    public void rechazaMoraVigente() {
        Solicitud solicitud = new Solicitud(
                MONTO, PLAZO, SEGMENTO, new BigDecimal("1000"), 700, true);

        assertEquals(Resultado.RECHAZADA, evaluador.evaluar(solicitud),
                "Mora vigente debe rechazar sin importar la cuota");
    }

    // CA-10: ingreso menor o igual a 0 lanza excepción (RN-07)
    @ParameterizedTest(name = "CA-10: ingreso={0} lanza excepción")
    @ValueSource(strings = {"0", "-1"})
    public void rechazaIngresoNoPositivo(String ingreso) {
        Solicitud solicitud = new Solicitud(
                MONTO, PLAZO, SEGMENTO, new BigDecimal(ingreso), 700, false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> evaluador.evaluar(solicitud),
                "Ingreso <= 0 debe lanzar IllegalArgumentException");

        assertEquals("Ingreso mensual debe ser mayor que 0", ex.getMessage(),
                "El mensaje debe explicar la causa del rechazo");
    }

    // RN-07: monto, plazo o segmento nulos lanzan excepción
    @Test
    public void rechazaDatosObligatoriosNulos() {
        assertAll("Datos obligatorios nulos deben lanzar excepción",
                () -> assertThrows(IllegalArgumentException.class,
                        () -> evaluador.evaluar(new Solicitud(
                                null, PLAZO, SEGMENTO, BigDecimal.TEN, 700, false))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> evaluador.evaluar(new Solicitud(
                                MONTO, null, SEGMENTO, BigDecimal.TEN, 700, false))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> evaluador.evaluar(new Solicitud(
                                MONTO, PLAZO, null, BigDecimal.TEN, 700, false)))
        );
    }
}