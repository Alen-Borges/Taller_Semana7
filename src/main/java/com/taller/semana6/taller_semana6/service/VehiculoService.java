package com.taller.semana6.taller_semana6.service;

import com.taller.semana6.taller_semana6.dto.VehiculoRequestDTO;
import com.taller.semana6.taller_semana6.dto.VehiculoResponseDTO;
import com.taller.semana6.taller_semana6.entity.Asegurado;
import com.taller.semana6.taller_semana6.entity.Vehiculo;
import com.taller.semana6.taller_semana6.exception.BadRequestException;
import com.taller.semana6.taller_semana6.exception.ResourceNotFoundException;
import com.taller.semana6.taller_semana6.repository.AseguradoRepository;
import com.taller.semana6.taller_semana6.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final AseguradoRepository aseguradoRepository;

    @Transactional
    public VehiculoResponseDTO registrarVehiculo(VehiculoRequestDTO dto) {
        if (vehiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new BadRequestException("La placa " + dto.getPlaca() + " ya se encuentra registrada");
        }

        Asegurado asegurado = aseguradoRepository.findById(dto.getAseguradoId())
                .orElseThrow(() -> new ResourceNotFoundException("Asegurado no encontrado con ID: " + dto.getAseguradoId()));

        Vehiculo vehiculo = Vehiculo.builder()
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .anio(dto.getAnio())
                .placa(dto.getPlaca())
                .asegurado(asegurado)
                .build();

        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        return mapToDTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> consultarVehiculos() {
        return vehiculoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VehiculoResponseDTO consultarVehiculoPorId(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con ID: " + id));
        return mapToDTO(vehiculo);
    }

    private VehiculoResponseDTO mapToDTO(Vehiculo vehiculo) {
        String aseguradoNombreCompleto = "";
        if (vehiculo.getAsegurado() != null) {
            aseguradoNombreCompleto = vehiculo.getAsegurado().getNombre() + " " + vehiculo.getAsegurado().getApellido();
        }

        return VehiculoResponseDTO.builder()
                .id(vehiculo.getId())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .anio(vehiculo.getAnio())
                .placa(vehiculo.getPlaca())
                .aseguradoId(vehiculo.getAsegurado() != null ? vehiculo.getAsegurado().getId() : null)
                .aseguradoNombreCompleto(aseguradoNombreCompleto)
                .build();
    }
}
