package com.taller.semana6.taller_semana6.service;

import com.taller.semana6.taller_semana6.dto.AseguradoDTO;
import com.taller.semana6.taller_semana6.entity.Asegurado;
import com.taller.semana6.taller_semana6.exception.BadRequestException;
import com.taller.semana6.taller_semana6.exception.ResourceNotFoundException;
import com.taller.semana6.taller_semana6.repository.AseguradoRepository;
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
class AseguradoServiceTest {

    @Mock
    private AseguradoRepository aseguradoRepository;

    @InjectMocks
    private AseguradoService aseguradoService;

    private AseguradoDTO aseguradoDTO;
    private Asegurado asegurado;

    @BeforeEach
    void setUp() {
        aseguradoDTO = AseguradoDTO.builder()
                .nombre("Juan")
                .apellido("Perez")
                .numeroIdentificacion("1712345678")
                .direccion("Av. Amazonas")
                .telefono("0991234567")
                .correoElectronico("juan.perez@correo.com")
                .build();

        asegurado = Asegurado.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Perez")
                .numeroIdentificacion("1712345678")
                .direccion("Av. Amazonas")
                .telefono("0991234567")
                .correoElectronico("juan.perez@correo.com")
                .build();
    }

    @Test
    @DisplayName("CP001-HU-001: Registro exitoso de asegurado")
    void registrarAseguradoExitoso() {
        // Arrange
        when(aseguradoRepository.existsByNumeroIdentificacion(anyString())).thenReturn(false);
        when(aseguradoRepository.existsByCorreoElectronico(anyString())).thenReturn(false);
        when(aseguradoRepository.save(any(Asegurado.class))).thenReturn(asegurado);

        // Act
        AseguradoDTO resultado = aseguradoService.registrarAsegurado(aseguradoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(aseguradoDTO.getNumeroIdentificacion(), resultado.getNumeroIdentificacion());
        verify(aseguradoRepository, times(1)).save(any(Asegurado.class));
    }

    @Test
    @DisplayName("CP004-HU-001: Error por identificación duplicada")
    void registrarAseguradoIdentificacionDuplicada() {
        // Arrange
        when(aseguradoRepository.existsByNumeroIdentificacion(anyString())).thenReturn(true);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            aseguradoService.registrarAsegurado(aseguradoDTO);
        });

        assertEquals("El numero de identificacion ya se encuentra registrado", exception.getMessage());
        verify(aseguradoRepository, never()).save(any(Asegurado.class));
    }

    @Test
    @DisplayName("HU-002: Consulta de detalle de asegurado existente")
    void consultarAseguradoPorIdExitoso() {
        // Arrange
        when(aseguradoRepository.findById(1L)).thenReturn(Optional.of(asegurado));

        // Act
        AseguradoDTO resultado = aseguradoService.consultarAseguradoPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(aseguradoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("HU-002: Error al consultar asegurado inexistente")
    void consultarAseguradoInexistente() {
        // Arrange
        when(aseguradoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            aseguradoService.consultarAseguradoPorId(99L);
        });

        assertTrue(exception.getMessage().contains("Asegurado no encontrado"));
    }
}
