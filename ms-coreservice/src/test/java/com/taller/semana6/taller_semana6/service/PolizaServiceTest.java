package com.taller.semana6.taller_semana6.service;

import com.taller.semana6.taller_semana6.dto.PolizaRequestDTO;
import com.taller.semana6.taller_semana6.dto.PolizaResponseDTO;
import com.taller.semana6.taller_semana6.entity.Asegurado;
import com.taller.semana6.taller_semana6.entity.Poliza;
import com.taller.semana6.taller_semana6.entity.Vehiculo;
import com.taller.semana6.taller_semana6.exception.BadRequestException;
import com.taller.semana6.taller_semana6.exception.ResourceNotFoundException;
import com.taller.semana6.taller_semana6.repository.AseguradoRepository;
import com.taller.semana6.taller_semana6.repository.PolizaRepository;
import com.taller.semana6.taller_semana6.repository.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolizaServiceTest {

    @Mock
    private PolizaRepository polizaRepository;
    @Mock
    private AseguradoRepository aseguradoRepository;
    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private PolizaService polizaService;

    private PolizaRequestDTO polizaRequestDTO;
    private Poliza poliza;
    private Asegurado asegurado;
    private Vehiculo vehiculo;

    @BeforeEach
    void setUp() {
        asegurado = Asegurado.builder().id(1L).nombre("Juan").apellido("Perez").build();
        vehiculo = Vehiculo.builder().id(1L).marca("Chevrolet").modelo("Aveo").placa("PBA-1234").build();

        polizaRequestDTO = PolizaRequestDTO.builder()
                .numero("POL-2026-001")
                .aseguradoId(1L)
                .vehiculoId(1L)
                .valorAsegurado(new BigDecimal("25000.00"))
                .vigenciaInicio(LocalDate.now())
                .vigenciaFin(LocalDate.now().plusYears(1))
                .build();

        poliza = Poliza.builder()
                .id(1L)
                .numero("POL-2026-001")
                .asegurado(asegurado)
                .vehiculo(vehiculo)
                .valorAsegurado(new BigDecimal("25000.00"))
                .vigenciaInicio(LocalDate.now())
                .vigenciaFin(LocalDate.now().plusYears(1))
                .estado("ACTIVA")
                .build();
    }

    @Test
    @DisplayName("CP001-HU-005: Registro exitoso de póliza")
    void registrarPolizaExitoso() {
        // Arrange
        when(polizaRepository.existsByNumero(anyString())).thenReturn(false);
        when(aseguradoRepository.findById(1L)).thenReturn(Optional.of(asegurado));
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(polizaRepository.save(any(Poliza.class))).thenReturn(poliza);

        // Act
        PolizaResponseDTO resultado = polizaService.registrarPoliza(polizaRequestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("ACTIVA", resultado.getEstado());
        verify(polizaRepository, times(1)).save(any(Poliza.class));
    }

    @Test
    @DisplayName("CP004-HU-005: Error por fechas de vigencia inválidas")
    void registrarPolizaFechasInvalidas() {
        // Arrange
        polizaRequestDTO.setVigenciaFin(polizaRequestDTO.getVigenciaInicio().minusDays(1));
        when(polizaRepository.existsByNumero(anyString())).thenReturn(false);
        when(aseguradoRepository.findById(1L)).thenReturn(Optional.of(asegurado));
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            polizaService.registrarPoliza(polizaRequestDTO);
        });

        assertEquals("La fecha de fin de vigencia debe ser posterior a la fecha de inicio", exception.getMessage());
    }

    @Test
    @DisplayName("CP003-HU-005: Error por número de póliza duplicado")
    void registrarPolizaNumeroDuplicado() {
        // Arrange
        when(polizaRepository.existsByNumero("POL-2026-001")).thenReturn(true);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            polizaService.registrarPoliza(polizaRequestDTO);
        });

        assertTrue(exception.getMessage().contains("ya se encuentra registrado"));
    }
}
