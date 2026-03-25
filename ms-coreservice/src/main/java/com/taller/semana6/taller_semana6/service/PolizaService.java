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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolizaService {

    private final PolizaRepository polizaRepository;
    private final AseguradoRepository aseguradoRepository;
    private final VehiculoRepository vehiculoRepository;

    @Transactional
    public PolizaResponseDTO registrarPoliza(PolizaRequestDTO dto) {
        // Validar unicidad del número
        if (polizaRepository.existsByNumero(dto.getNumero())) {
            throw new BadRequestException("El numero de poliza " + dto.getNumero() + " ya se encuentra registrado");
        }

        // Validar existencia de asegurado
        Asegurado asegurado = aseguradoRepository.findById(dto.getAseguradoId())
                .orElseThrow(() -> new ResourceNotFoundException("Asegurado no encontrado con ID: " + dto.getAseguradoId()));

        // Validar existencia de vehículo
        Vehiculo vehiculo = vehiculoRepository.findById(dto.getVehiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo no encontrado con ID: " + dto.getVehiculoId()));

        // Validar rango de fechas
        if (dto.getVigenciaFin().isBefore(dto.getVigenciaInicio()) || dto.getVigenciaFin().isEqual(dto.getVigenciaInicio())) {
            throw new BadRequestException("La fecha de fin de vigencia debe ser posterior a la fecha de inicio");
        }

        Poliza poliza = Poliza.builder()
                .numero(dto.getNumero())
                .asegurado(asegurado)
                .vehiculo(vehiculo)
                .valorAsegurado(dto.getValorAsegurado())
                .vigenciaInicio(dto.getVigenciaInicio())
                .vigenciaFin(dto.getVigenciaFin())
                .estado("ACTIVA")
                .build();

        Poliza guardada = polizaRepository.save(poliza);
        return mapToDTO(guardada);
    }

    @Transactional(readOnly = true)
    public List<PolizaResponseDTO> consultarPolizas() {
        return polizaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PolizaResponseDTO consultarPolizaPorId(Long id) {
        Poliza poliza = polizaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poliza no encontrada con ID: " + id));
        return mapToDTO(poliza);
    }

    private PolizaResponseDTO mapToDTO(Poliza poliza) {
        String vehiculoInfo = poliza.getVehiculo().getMarca() + " " + poliza.getVehiculo().getModelo() + " [" + poliza.getVehiculo().getPlaca() + "]";
        
        return PolizaResponseDTO.builder()
                .id(poliza.getId())
                .numero(poliza.getNumero())
                .valorAsegurado(poliza.getValorAsegurado())
                .vigenciaInicio(poliza.getVigenciaInicio())
                .vigenciaFin(poliza.getVigenciaFin())
                .estado(poliza.getEstado())
                .aseguradoId(poliza.getAsegurado().getId())
                .aseguradoNombre(poliza.getAsegurado().getNombre() + " " + poliza.getAsegurado().getApellido())
                .vehiculoId(poliza.getVehiculo().getId())
                .vehiculoMarcaModeloPlaca(vehiculoInfo)
                .build();
    }
}
