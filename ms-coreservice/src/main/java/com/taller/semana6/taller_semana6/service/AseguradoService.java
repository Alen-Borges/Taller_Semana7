package com.taller.semana6.taller_semana6.service;

import com.taller.semana6.taller_semana6.dto.AseguradoDTO;
import com.taller.semana6.taller_semana6.entity.Asegurado;
import com.taller.semana6.taller_semana6.exception.BadRequestException;
import com.taller.semana6.taller_semana6.exception.ResourceNotFoundException;
import com.taller.semana6.taller_semana6.repository.AseguradoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AseguradoService {

    private final AseguradoRepository aseguradoRepository;

    @Transactional
    public AseguradoDTO registrarAsegurado(AseguradoDTO dto) {
        if (aseguradoRepository.existsByNumeroIdentificacion(dto.getNumeroIdentificacion())) {
            throw new BadRequestException("El numero de identificacion ya se encuentra registrado");
        }
        if (aseguradoRepository.existsByCorreoElectronico(dto.getCorreoElectronico())) {
            throw new BadRequestException("El correo electronico ya se encuentra registrado");
        }

        Asegurado asegurado = Asegurado.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .numeroIdentificacion(dto.getNumeroIdentificacion())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .correoElectronico(dto.getCorreoElectronico())
                .build();

        Asegurado guardado = aseguradoRepository.save(asegurado);
        return mapToDTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<AseguradoDTO> consultarAsegurados() {
        return aseguradoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AseguradoDTO consultarAseguradoPorId(Long id) {
        Asegurado asegurado = aseguradoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asegurado no encontrado con ID: " + id));
        return mapToDTO(asegurado);
    }

    private AseguradoDTO mapToDTO(Asegurado asegurado) {
        return AseguradoDTO.builder()
                .id(asegurado.getId())
                .nombre(asegurado.getNombre())
                .apellido(asegurado.getApellido())
                .numeroIdentificacion(asegurado.getNumeroIdentificacion())
                .direccion(asegurado.getDireccion())
                .telefono(asegurado.getTelefono())
                .correoElectronico(asegurado.getCorreoElectronico())
                .build();
    }
}
