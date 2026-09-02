package com.backing.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.account.dto.CuentaRequest;
import com.banking.account.dto.CuentaResponse;
import com.banking.account.dto.MontoRequest;
import com.banking.account.entity.Cuenta;
import com.banking.account.entity.TipoCuenta;
import com.banking.account.exception.CuentaNotFoundException;
import com.banking.account.exception.MontoInvalidoException;
import com.banking.account.repository.CuentaRepository;
import com.banking.account.service.CuentaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceTest {

    CuentaServiceImpl cuentaService;

    @Mock
    CuentaRepository cuentaRepository;

    @BeforeEach
    void setUp() {
        cuentaService = new CuentaServiceImpl(cuentaRepository);
    }

    @DisplayName("crear cuenta correctamente")
    @Test
    void testCrearCuentaCorrectamente() {

        // Arrange
        CuentaRequest request = new CuentaRequest(
                "ASDF1234",
                "Sergio Test",
                TipoCuenta.AHORRO,
                new BigDecimal("100"));

        Cuenta cuentaGuardada = new Cuenta();
        cuentaGuardada.setId(1L);
        cuentaGuardada.setNumeroCuenta("ASDF1234");
        cuentaGuardada.setTitular("Sergio Test");
        cuentaGuardada.setTipoCuenta(TipoCuenta.AHORRO);
        cuentaGuardada.setSaldo(new BigDecimal("100"));

        when(cuentaRepository.existsByNumeroCuenta(request.numeroCuenta()))
                .thenReturn(false);

        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaGuardada);

        // Act
        CuentaResponse resultado = cuentaService.crear(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(cuentaGuardada.getId(), resultado.id());
        assertEquals(cuentaGuardada.getNumeroCuenta(), resultado.numeroCuenta());
        assertEquals(cuentaGuardada.getTitular(), resultado.titular());
        assertEquals(cuentaGuardada.getTipoCuenta(), resultado.tipoCuenta());
        assertEquals(cuentaGuardada.getSaldo(), resultado.saldo());

        // Verify
        verify(cuentaRepository, times(1)).existsByNumeroCuenta(request.numeroCuenta());
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @DisplayName("buscar cuenta existente")
    @Test
    void testFindByNumeroCuentaBuscarCuentaExistente() {
        // Arrange
        String numeroCuenta = "ASDF1234";

        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("ASDF1234");
        cuenta.setTitular("Sergio Test");
        cuenta.setTipoCuenta(TipoCuenta.AHORRO);
        cuenta.setSaldo(new BigDecimal("100"));

        when(cuentaRepository.findByNumeroCuenta(numeroCuenta))
                .thenReturn(Optional.of(cuenta));

        // Act
        Cuenta resultado = cuentaService.findByNumeroCuenta(numeroCuenta);

        // Assert
        assertNotNull(resultado);
        assertEquals(any(Cuenta.class), resultado);

    }

    @DisplayName("buscar cuenta inexistente")
    @Test
    void testFindByNumeroCuentaBuscarCuentaInexistente() {
        // Arrange
        String numeroCuenta = "ASDF1234";

        when(cuentaRepository.findByNumeroCuenta(numeroCuenta))
                .thenReturn(Optional.empty());

        // Act
        CuentaNotFoundException exception = assertThrows(CuentaNotFoundException.class,
                () -> cuentaService.findByNumeroCuenta(numeroCuenta));

        // Assert
        String mensajeEsperado = "No se encontró la cuenta con número: " + numeroCuenta;
        assertEquals(mensajeEsperado, exception.getMessage());

        // Verify
        verify(cuentaRepository, times(1)).findByNumeroCuenta(numeroCuenta);

    }

    @DisplayName("listar cuentas")
    @Test
    void testFindAll() {
        // Arrange
        Cuenta cuenta1 = new Cuenta();
        cuenta1.setId(1L);
        cuenta1.setNumeroCuenta("ASDF1234");
        cuenta1.setTitular("Sergio Test");
        cuenta1.setTipoCuenta(TipoCuenta.AHORRO);
        cuenta1.setSaldo(new BigDecimal("100"));

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setId(2L);
        cuenta2.setNumeroCuenta("QWER5678");
        cuenta2.setTitular("Maria Test");
        cuenta2.setTipoCuenta(TipoCuenta.CORRIENTE);
        cuenta2.setSaldo(new BigDecimal("200"));

        when(cuentaRepository.findAll()).thenReturn(List.of(cuenta1, cuenta2));

        // Act
        List<CuentaResponse> resultado = cuentaService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        assertEquals(1L, resultado.get(0).id());
        assertEquals("ASDF1234", resultado.get(0).numeroCuenta());

        assertEquals(2L, resultado.get(1).id());
        assertEquals("QWER5678", resultado.get(1).numeroCuenta());

        // Verify
        verify(cuentaRepository, times(1)).findAll();
    }

    @DisplayName("depositar correctamente")
    @Test
    void testDepositarCorrectamente() {
        // Arrange
        Long id = 1L;
        MontoRequest montoRequest = new MontoRequest(new BigDecimal("50"));
        String numeroCuenta = "ASDF1234";

        Cuenta cuentaDepositada = new Cuenta();
        cuentaDepositada.setId(1L);
        cuentaDepositada.setNumeroCuenta("ASDF1234");
        cuentaDepositada.setTitular("Sergio Test");
        cuentaDepositada.setTipoCuenta(TipoCuenta.AHORRO);
        cuentaDepositada.setSaldo(new BigDecimal("100"));

        Cuenta cuentaActualizada = cuentaDepositada;
        cuentaActualizada.setSaldo(cuentaActualizada.getSaldo().add(montoRequest.monto()));

        when(cuentaRepository.findById(id))
                .thenReturn(Optional.of(cuentaDepositada));

        when(cuentaRepository.existsByNumeroCuenta(numeroCuenta))
                .thenReturn(true);

        when(cuentaRepository.save(cuentaActualizada)).thenReturn(cuentaActualizada);

        // Act
        CuentaResponse resultado = cuentaService.depositar(id, montoRequest);

        // Assert
        assertNotNull(resultado);
        assertEquals(cuentaActualizada.getSaldo(), resultado.saldo());
        assertEquals(id, resultado.id());

        // Verifu
        verify(cuentaRepository, times(1)).findById(id);
        verify(cuentaRepository, times(1)).existsByNumeroCuenta(numeroCuenta);
        verify(cuentaRepository, times(1)).save(cuentaActualizada);

    }

    @DisplayName("depósito con monto inválido")
    @Test
    void testDepositarMontoInvalido() {
        // Arrange
        Long id = 1L;
        MontoRequest montoRequest = new MontoRequest(new BigDecimal("0"));

        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("ASDF1234");
        cuenta.setTitular("Sergio Test");
        cuenta.setTipoCuenta(TipoCuenta.AHORRO);
        cuenta.setSaldo(new BigDecimal("100"));

        when(cuentaRepository.findById(id))
                .thenReturn(Optional.of(cuenta));

        // Act
        MontoInvalidoException exception = assertThrows(MontoInvalidoException.class,
                () -> cuentaService.depositar(id, montoRequest));

        // Assert
        String mensajeEsperado = "El monto debe ser mayor que 0";
        assertEquals(mensajeEsperado, exception.getMessage());

        // Verify
        verify(cuentaRepository, times(1)).findById(id);
    }

    @DisplayName("retirar correctamente")
    @Test
    void testRetirarCorrectamente() {
        // Arrange
        Long id = 1L;
        MontoRequest montoRequest = new MontoRequest(new BigDecimal("50"));

        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("ASDF1234");
        cuenta.setTitular("Sergio Test");
        cuenta.setTipoCuenta(TipoCuenta.AHORRO);
        cuenta.setSaldo(new BigDecimal("100"));

        // Create a separate object for the updated account
        Cuenta cuentaActualizada = new Cuenta();
        cuentaActualizada.setId(1L);
        cuentaActualizada.setNumeroCuenta("ASDF1234");
        cuentaActualizada.setTitular("Sergio Test");
        cuentaActualizada.setTipoCuenta(TipoCuenta.AHORRO);
        cuentaActualizada.setSaldo(new BigDecimal("50")); // 100 - 50

        when(cuentaRepository.findById(id))
                .thenReturn(Optional.of(cuenta));

        when(cuentaRepository.existsByNumeroCuenta(cuenta.getNumeroCuenta()))
                .thenReturn(true);

        // Mock save to return the updated account
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaActualizada);

        // Act
        CuentaResponse resultado = cuentaService.retirar(id, montoRequest);

        // Assert
        assertNotNull(resultado);
        assertEquals(new BigDecimal("50"), resultado.saldo()); // Expected balance: 50
        assertEquals(id, resultado.id());

        // Verify
        verify(cuentaRepository, times(1)).findById(id);
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    // @DisplayName("retirar con saldo insuficiente")
    // @DisplayName("retirar de cuenta inactiva")
    // @DisplayName("eliminar cuenta")
    // @DisplayName("número de cuenta duplicado")

}