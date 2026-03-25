package com.taller.semana6.taller_semana6.service;

import com.taller.semana6.taller_semana6.dto.VehiculoRequestDTO;
import com.taller.semana6.taller_semana6.dto.VehiculoResponseDTO;
import com.taller.semana6.taller_semana6.entity.Vehiculo;
import com.taller.semana6.taller_semana6.exception.BadRequestException;
import com.taller.semana6.taller_semana6.exception.ResourceNotFoundException;
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

    @Transactional
    public VehiculoResponseDTO registrarVehiculo(VehiculoRequestDTO dto) {
        if (vehiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new BadRequestException("La placa " + dto.getPlaca() + " ya se encuentra registrada");
        }

        Vehiculo vehiculo = Vehiculo.builder()
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .anio(dto.getAnio())
                .placa(dto.getPlaca())
                .build();

        return mapToDTO(vehiculoRepository.save(vehiculo));
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
        return VehiculoResponseDTO.builder()
                .id(vehiculo.getId())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .anio(vehiculo.getAnio())
                .placa(vehiculo.getPlaca())
                .build();
    }
}
