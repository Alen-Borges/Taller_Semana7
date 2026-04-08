package com.taller.semana6.taller_semana6.service;

import com.taller.semana6.taller_semana6.dto.VehiculoRequestDTO;
import com.taller.semana6.taller_semana6.dto.VehiculoResponseDTO;
import com.taller.semana6.taller_semana6.entity.Vehiculo;
import com.taller.semana6.taller_semana6.exception.BadRequestException;
import com.taller.semana6.taller_semana6.exception.ResourceNotFoundException;
import com.taller.semana6.taller_semana6.repository.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehiculoServiceTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private VehiculoService vehiculoService;

    private VehiculoRequestDTO vehiculoRequestDTO;
    private Vehiculo vehiculo;

    @BeforeEach
    void setUp() {
        vehiculoRequestDTO = VehiculoRequestDTO.builder()
                .marca("Chevrolet")
                .modelo("Aveo")
                .anio(2022)
                .placa("PBA-1234")
                .build();

        vehiculo = Vehiculo.builder()
                .id(1L)
                .marca("Chevrolet")
                .modelo("Aveo")
                .anio(2022)
                .placa("PBA-1234")
                .build();
    }

    @Test
    @DisplayName("CP001-HU-003: Registro exitoso de vehículo")
    void registrarVehiculoExitoso() {
        // Arrange
        when(vehiculoRepository.existsByPlaca(anyString())).thenReturn(false);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);

        // Act
        VehiculoResponseDTO resultado = vehiculoService.registrarVehiculo(vehiculoRequestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(vehiculoRequestDTO.getPlaca(), resultado.getPlaca());
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("CP003-HU-003: Error por placa duplicada")
    void registrarVehiculoPlacaDuplicada() {
        // Arrange
        when(vehiculoRepository.existsByPlaca("PBA-1234")).thenReturn(true);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            vehiculoService.registrarVehiculo(vehiculoRequestDTO);
        });

        assertTrue(exception.getMessage().contains("ya se encuentra registrada"));
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("HU-004: Consulta de detalle de vehículo existente")
    void consultarVehiculoPorIdExitoso() {
        // Arrange
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        // Act
        VehiculoResponseDTO resultado = vehiculoService.consultarVehiculoPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Chevrolet", resultado.getMarca());
        verify(vehiculoRepository, times(1)).findById(1L);
    }
}
