package com.taller.semana6.taller_semana6.service;

import com.taller.semana6.taller_semana6.client.EvaluacionServiceClient;
import com.taller.semana6.taller_semana6.dto.*;
import com.taller.semana6.taller_semana6.entity.*;
import com.taller.semana6.taller_semana6.exception.BadRequestException;
import com.taller.semana6.taller_semana6.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReclamoServiceTest {

    @Mock
    private ReclamoRepository reclamoRepository;
    @Mock
    private PolizaRepository polizaRepository;
    @Mock
    private ReclamoFotografiaRepository fotografiaRepository;
    @Mock
    private ReclamoBanderaRepository banderaRepository;
    @Mock
    private FileStorageService fileStorageService;
    @Mock
    private EvaluacionServiceClient evaluacionServiceClient;

    @InjectMocks
    private ReclamoService reclamoService;

    private ReclamoRequestDTO reclamoRequestDTO;
    private Poliza poliza;
    private Asegurado asegurado;
    private Vehiculo vehiculo;
    private MultipartFile mockFile;
    private EvaluacionResponseDTO evalResponseDTO;

    @BeforeEach
    void setUp() {
        asegurado = Asegurado.builder().id(1L).nombre("Juan").apellido("Perez").build();
        vehiculo = Vehiculo.builder().id(1L).marca("Chevrolet").modelo("Aveo").placa("PBA-1234").build();
        
        poliza = Poliza.builder()
                .id(1L)
                .numero("POL-2026-001")
                .asegurado(asegurado)
                .vehiculo(vehiculo)
                .valorAsegurado(new BigDecimal("25000.00"))
                .vigenciaInicio(LocalDate.now().minusMonths(6))
                .vigenciaFin(LocalDate.now().plusMonths(6))
                .estado("ACTIVA")
                .build();

        reclamoRequestDTO = ReclamoRequestDTO.builder()
                .polizaId(1L)
                .fechaIncidente(LocalDate.now())
                .descripcion("Choque leve")
                .montoEstimado(new BigDecimal("1500.00"))
                .ubicacion("Quito")
                .build();

        mockFile = mock(MultipartFile.class);
        
        evalResponseDTO = EvaluacionResponseDTO.builder()
                .estado("APROBADO")
                .deducibleAplicado(new BigDecimal("250.00"))
                .montoAprobado(new BigDecimal("1250.00"))
                .justificacion("Evaluación automática exitosa")
                .build();
    }

    @Test
    @DisplayName("CP001-HU-007: Registro exitoso de reclamo")
    void registrarReclamoExitoso() {
        // Arrange
        when(polizaRepository.findById(1L)).thenReturn(Optional.of(poliza));
        when(reclamoRepository.save(any(Reclamo.class))).thenAnswer(i -> {
            Reclamo r = i.getArgument(0);
            r.setId(100L);
            return r;
        });
        when(fotografiaRepository.save(any(ReclamoFotografia.class))).thenAnswer(i -> i.getArgument(0));
        when(fileStorageService.guardar(any(), anyString())).thenReturn("http://foto.url");
        when(evaluacionServiceClient.evaluar(any())).thenReturn(evalResponseDTO);

        // Act
        ReclamoResponseDTO resultado = reclamoService.registrarReclamo(reclamoRequestDTO, List.of(mockFile));

        // Assert
        assertNotNull(resultado);
        assertEquals(EstadoReclamo.APROBADO, resultado.getEstado());
        verify(reclamoRepository, atLeastOnce()).save(any(Reclamo.class));
        verify(evaluacionServiceClient, times(1)).evaluar(any());
    }

    @Test
    @DisplayName("CP008-HU-007: Error por falta de fotografías")
    void registrarReclamoSinFotos() {
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            reclamoService.registrarReclamo(reclamoRequestDTO, List.of());
        });

        assertEquals("Se requiere al menos una fotografía del incidente.", exception.getMessage());
        verify(reclamoRepository, never()).save(any(Reclamo.class));
    }

    @Test
    @DisplayName("CP002-HU-007: Error por póliza inactiva")
    void registrarReclamoPolizaInactiva() {
        // Arrange
        poliza.setEstado("INACTIVA");
        when(polizaRepository.findById(1L)).thenReturn(Optional.of(poliza));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            reclamoService.registrarReclamo(reclamoRequestDTO, List.of(mockFile));
        });

        assertTrue(exception.getMessage().contains("La póliza no está activa"));
    }

    @Test
    @DisplayName("CP005-HU-007: Error por fecha de incidente fuera de vigencia")
    void registrarReclamoFechaFueraDeRango() {
        // Arrange
        reclamoRequestDTO.setFechaIncidente(poliza.getVigenciaFin().plusDays(1));
        when(polizaRepository.findById(1L)).thenReturn(Optional.of(poliza));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            reclamoService.registrarReclamo(reclamoRequestDTO, List.of(mockFile));
        });

        assertTrue(exception.getMessage().contains("dentro del período de vigencia"));
    }
}
