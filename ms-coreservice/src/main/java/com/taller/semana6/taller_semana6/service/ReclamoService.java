package com.taller.semana6.taller_semana6.service;

import com.taller.semana6.taller_semana6.client.EvaluacionServiceClient;
import com.taller.semana6.taller_semana6.dto.*;
import com.taller.semana6.taller_semana6.entity.*;
import com.taller.semana6.taller_semana6.exception.BadRequestException;
import com.taller.semana6.taller_semana6.exception.ResourceNotFoundException;
import com.taller.semana6.taller_semana6.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReclamoService {

    private final ReclamoRepository reclamoRepository;
    private final PolizaRepository polizaRepository;
    private final ReclamoFotografiaRepository fotografiaRepository;
    private final ReclamoBanderaRepository banderaRepository;
    private final FileStorageService fileStorageService;
    private final EvaluacionServiceClient evaluacionServiceClient;

    // ══════════════════════════════════════════════════════════════════════
    //  HU-007: Registrar Reclamo
    //  Flujo (spec §8.1):
    //  1. Validar póliza existe, ACTIVA y fecha dentro de vigencia
    //  2. Validar mínimo 1 fotografía
    //  3. Generar número de seguimiento REC-YYYY-NNN
    //  4. Guardar reclamo como REGISTRADO
    //  5. Guardar fotografías
    //  6. Llamar evaluacion-service vía REST (POST /api/v1/evaluar)
    //  7. Actualizar estado con resultado
    //  8. Retornar HTTP 201
    // ══════════════════════════════════════════════════════════════════════

    @Transactional
    public ReclamoResponseDTO registrarReclamo(ReclamoRequestDTO dto,
                                               List<MultipartFile> fotografias) {

        // 1a. Validar mínimo 1 fotografía (spec §8.1 Tabla 16)
        if (CollectionUtils.isEmpty(fotografias)) {
            throw new BadRequestException("Se requiere al menos una fotografía del incidente.");
        }

        // 1b. Obtener póliza
        Poliza poliza = polizaRepository.findById(dto.getPolizaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Póliza no encontrada con ID: " + dto.getPolizaId()));

        // 1c. Validar póliza ACTIVA (spec §8.1 paso 2)
        if (!"ACTIVA".equalsIgnoreCase(poliza.getEstado())) {
            throw new BadRequestException(
                    "La póliza no está activa. Estado actual: " + poliza.getEstado());
        }

        // 1d. Validar fecha incidente dentro de vigencia (spec §8.1 paso 2)
        LocalDate fechaIncidente = dto.getFechaIncidente();
        if (fechaIncidente.isBefore(poliza.getVigenciaInicio()) ||
                fechaIncidente.isAfter(poliza.getVigenciaFin())) {
            throw new BadRequestException(
                    "La fecha del incidente debe estar dentro del período de vigencia de la póliza ("
                    + poliza.getVigenciaInicio() + " — " + poliza.getVigenciaFin() + ").");
        }

        // 2. Generar número de seguimiento
        String numeroSeguimiento = generarNumeroSeguimiento();

        // 3. Persistir reclamo en estado REGISTRADO (spec §5.2.4)
        Reclamo reclamo = Reclamo.builder()
                .numeroSeguimiento(numeroSeguimiento)
                .poliza(poliza)
                .fechaIncidente(fechaIncidente)
                .descripcion(dto.getDescripcion())
                .montoEstimado(dto.getMontoEstimado())
                .ubicacion(dto.getUbicacion())
                .estado(EstadoReclamo.REGISTRADO)
                .fechaCreacion(LocalDateTime.now())
                .build();

        reclamo = reclamoRepository.save(reclamo);
        log.info("[core-service] Reclamo {} guardado — estado: REGISTRADO", numeroSeguimiento);

        // 4. Guardar fotografías
        List<ReclamoFotografia> fotos = new ArrayList<>();
        for (MultipartFile archivo : fotografias) {
            String url = fileStorageService.guardar(archivo, numeroSeguimiento);
            fotos.add(fotografiaRepository.save(
                    ReclamoFotografia.builder()
                            .reclamo(reclamo)
                            .urlFotografia(url)
                            .nombreArchivo(archivo.getOriginalFilename())
                            .build()));
        }
        reclamo.setFotografias(fotos);

        // 5. Llamar evaluacion-service de forma síncrona (spec §2.2)
        EvaluacionRequestDTO evalRequest = EvaluacionRequestDTO.builder()
                .reclamoId(reclamo.getId())
                .montoEstimado(dto.getMontoEstimado())
                .valorAsegurado(poliza.getValorAsegurado())
                .numeroPoliza(poliza.getNumero())
                .build();

        EvaluacionResponseDTO evalResponse = evaluacionServiceClient.evaluar(evalRequest);

        // 6. Actualizar estado con el resultado de evaluacion-service
        if (evalResponse != null) {
            reclamo.setEstado(EstadoReclamo.valueOf(evalResponse.getEstado()));
            reclamo.setDeducibleCalculado(evalResponse.getDeducibleAplicado());
            reclamo.setMontoAprobado(evalResponse.getMontoAprobado());
            reclamo.setMotivoDecision(evalResponse.getJustificacion());
            reclamo.setFechaResolucion(LocalDateTime.now());

            if (!CollectionUtils.isEmpty(evalResponse.getBanderas())) {
                for (String desc : evalResponse.getBanderas()) {
                    banderaRepository.save(ReclamoBandera.builder()
                            .reclamo(reclamo)
                            .descripcionBandera(desc)
                            .reglaOrigen("ms-evaluacion")
                            .build());
                }
            }
            reclamo = reclamoRepository.save(reclamo);
            log.info("[core-service] Reclamo {} evaluado → estado: {}", numeroSeguimiento, reclamo.getEstado());
        } else {
            log.warn("[core-service] Reclamo {} queda en REGISTRADO (evaluacion-service no disponible).", numeroSeguimiento);
        }

        return mapToResponseDTO(reclamo);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  HU-011: Panel del Gestor
    // ══════════════════════════════════════════════════════════════════════

    @Transactional(readOnly = true)
    public List<ReclamoEscaladoDTO> consultarEscalados() {
        return reclamoRepository.findByEstado(EstadoReclamo.EN_REVISION_MANUAL)
                .stream().map(this::mapToEscaladoDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReclamoDetalleDTO consultarDetalle(String numeroSeguimiento) {
        return mapToDetalleDTO(buscarPorNumeroSeguimiento(numeroSeguimiento));
    }

    // ══════════════════════════════════════════════════════════════════════
    //  HU-012: Resolución manual del gestor
    // ══════════════════════════════════════════════════════════════════════

    @Transactional
    public ReclamoResponseDTO resolverManualmente(String numeroSeguimiento,
                                                  ResolucionManualRequestDTO dto) {
        Reclamo reclamo = buscarPorNumeroSeguimiento(numeroSeguimiento);

        if (reclamo.getEstado() != EstadoReclamo.EN_REVISION_MANUAL) {
            throw new BadRequestException(
                    "Solo se pueden resolver reclamos en estado EN_REVISION_MANUAL. Estado actual: " + reclamo.getEstado());
        }

        if (dto.getDecision() == ResolucionManualRequestDTO.DecisionManual.APROBADO) {
            reclamo.setEstado(EstadoReclamo.APROBADO);
            if (reclamo.getDeducibleCalculado() != null) {
                BigDecimal monto = reclamo.getMontoEstimado()
                        .subtract(reclamo.getDeducibleCalculado())
                        .setScale(2, RoundingMode.HALF_UP);
                reclamo.setMontoAprobado(monto.max(BigDecimal.ZERO));
            }
        } else {
            reclamo.setEstado(EstadoReclamo.RECHAZADO);
        }

        reclamo.setMotivoDecision("[GESTOR] " + dto.getJustificacion());
        reclamo.setFechaResolucion(LocalDateTime.now());
        log.info("[core-service] Resolución manual #{} → {}", numeroSeguimiento, reclamo.getEstado());
        return mapToResponseDTO(reclamoRepository.save(reclamo));
    }

    // ══════════════════════════════════════════════════════════════════════
    //  HU-013: Estado para asegurado
    // ══════════════════════════════════════════════════════════════════════

    @Transactional(readOnly = true)
    public EstadoReclamoDTO consultarEstado(String numeroSeguimiento) {
        Reclamo r = buscarPorNumeroSeguimiento(numeroSeguimiento);
        return EstadoReclamoDTO.builder()
                .numeroSeguimiento(r.getNumeroSeguimiento())
                .estado(r.getEstado())
                .mensajeEstado(generarMensajeAmigable(r))
                .montoEstimado(r.getMontoEstimado())
                .deducibleCalculado(r.getDeducibleCalculado())
                .montoAprobado(r.getMontoAprobado())
                .fechaCreacion(r.getFechaCreacion())
                .fechaResolucion(r.getFechaResolucion())
                .build();
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private String generarNumeroSeguimiento() {
        int anio = Year.now().getValue();
        String prefix = "REC-" + anio + "-";
        long total = reclamoRepository.contarPorAnio(prefix + "%");
        String candidato = prefix + String.format("%03d", total + 1);
        while (reclamoRepository.existsByNumeroSeguimiento(candidato)) {
            total++;
            candidato = prefix + String.format("%03d", total + 1);
        }
        return candidato;
    }

    private Reclamo buscarPorNumeroSeguimiento(String numSeg) {
        return reclamoRepository.findByNumeroSeguimiento(numSeg)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reclamo no encontrado: " + numSeg));
    }

    private String generarMensajeAmigable(Reclamo r) {
        return switch (r.getEstado()) {
            case REGISTRADO         -> "Su reclamo fue recibido y está siendo evaluado.";
            case APROBADO           -> "¡Reclamo aprobado! Monto: $" + r.getMontoAprobado() + " (deducible: $" + r.getDeducibleCalculado() + ").";
            case RECHAZADO          -> "Reclamo rechazado. Motivo: " + r.getMotivoDecision();
            case EN_REVISION_MANUAL -> "Su reclamo está siendo revisado por un gestor.";
            case DESCARTADO         -> "Reclamo descartado. " + r.getMotivoDecision();
            default                 -> "Contacte a soporte para más información.";
        };
    }

    // ── Mappers ───────────────────────────────────────────────────────────

    private ReclamoResponseDTO mapToResponseDTO(Reclamo r) {
        Poliza p = r.getPoliza();
        return ReclamoResponseDTO.builder()
                .id(r.getId()).numeroSeguimiento(r.getNumeroSeguimiento()).estado(r.getEstado())
                .fechaIncidente(r.getFechaIncidente()).descripcion(r.getDescripcion()).ubicacion(r.getUbicacion())
                .montoEstimado(r.getMontoEstimado()).deducibleCalculado(r.getDeducibleCalculado())
                .montoAprobado(r.getMontoAprobado()).motivoDecision(r.getMotivoDecision())
                .fechaResolucion(r.getFechaResolucion()).polizaId(p.getId()).polizaNumero(p.getNumero())
                .aseguradoId(p.getAsegurado().getId())
                .aseguradoNombreCompleto(p.getAsegurado().getNombre() + " " + p.getAsegurado().getApellido())
                .vehiculoMarcaModeloPlaca(p.getVehiculo().getMarca() + " " + p.getVehiculo().getModelo() + " [" + p.getVehiculo().getPlaca() + "]")
                .fechaCreacion(r.getFechaCreacion())
                .urlsFotografias(r.getFotografias() == null ? List.of() :
                        r.getFotografias().stream().map(ReclamoFotografia::getUrlFotografia).collect(Collectors.toList()))
                .banderas(r.getBanderas() == null ? List.of() :
                        r.getBanderas().stream().map(ReclamoBandera::getDescripcionBandera).collect(Collectors.toList()))
                .build();
    }

    private ReclamoEscaladoDTO mapToEscaladoDTO(Reclamo r) {
        Poliza p = r.getPoliza();
        return ReclamoEscaladoDTO.builder()
                .numeroSeguimiento(r.getNumeroSeguimiento()).estado(r.getEstado())
                .fechaCreacion(r.getFechaCreacion()).montoEstimado(r.getMontoEstimado())
                .deducibleCalculado(r.getDeducibleCalculado()).aseguradoId(p.getAsegurado().getId())
                .aseguradoNombreCompleto(p.getAsegurado().getNombre() + " " + p.getAsegurado().getApellido())
                .aseguradoNumeroIdentificacion(p.getAsegurado().getNumeroIdentificacion())
                .polizaId(p.getId()).polizaNumero(p.getNumero())
                .banderas(r.getBanderas() == null ? List.of() :
                        r.getBanderas().stream().map(ReclamoBandera::getDescripcionBandera).collect(Collectors.toList()))
                .build();
    }

    private ReclamoDetalleDTO mapToDetalleDTO(Reclamo r) {
        Poliza p = r.getPoliza();
        return ReclamoDetalleDTO.builder()
                .id(r.getId()).numeroSeguimiento(r.getNumeroSeguimiento()).estado(r.getEstado())
                .motivoDecision(r.getMotivoDecision()).fechaCreacion(r.getFechaCreacion())
                .fechaResolucion(r.getFechaResolucion()).fechaIncidente(r.getFechaIncidente())
                .descripcion(r.getDescripcion()).ubicacion(r.getUbicacion())
                .montoEstimado(r.getMontoEstimado()).deducibleCalculado(r.getDeducibleCalculado())
                .montoAprobado(r.getMontoAprobado()).polizaId(p.getId()).polizaNumero(p.getNumero())
                .polizaEstado(p.getEstado()).vigenciaInicio(p.getVigenciaInicio()).vigenciaFin(p.getVigenciaFin())
                .valorAsegurado(p.getValorAsegurado()).aseguradoId(p.getAsegurado().getId())
                .aseguradoNombre(p.getAsegurado().getNombre()).aseguradoApellido(p.getAsegurado().getApellido())
                .aseguradoNumeroIdentificacion(p.getAsegurado().getNumeroIdentificacion())
                .aseguradoCorreo(p.getAsegurado().getCorreoElectronico()).aseguradoTelefono(p.getAsegurado().getTelefono())
                .vehiculoId(p.getVehiculo().getId()).vehiculoMarca(p.getVehiculo().getMarca())
                .vehiculoModelo(p.getVehiculo().getModelo()).vehiculoAnio(p.getVehiculo().getAnio())
                .vehiculoPlaca(p.getVehiculo().getPlaca())
                .urlsFotografias(r.getFotografias() == null ? List.of() :
                        r.getFotografias().stream().map(ReclamoFotografia::getUrlFotografia).collect(Collectors.toList()))
                .banderas(r.getBanderas() == null ? List.of() :
                        r.getBanderas().stream().map(ReclamoBandera::getDescripcionBandera).collect(Collectors.toList()))
                .build();
    }
}
