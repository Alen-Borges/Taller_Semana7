package com.taller.semana6.taller_semana6.service;

import com.taller.semana6.taller_semana6.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

/**
 * Gestiona el almacenamiento de fotografías de los reclamos.
 * Valida formato (JPG/PNG), tamaño máximo y guarda en el filesystem.
 * Compatible con Docker mediante volumen montado en storage.location.
 */
@Service
@Slf4j
public class FileStorageService {

    private static final List<String> TIPOS_PERMITIDOS = List.of("image/jpeg", "image/png");
    private static final long MAX_BYTES = 10 * 1024 * 1024; // 10 MB

    @Value("${app.storage.location:./uploads}")
    private String storageLocation;

    @PostConstruct
    public void inicializar() {
        try {
            Files.createDirectories(Paths.get(storageLocation));
            log.info("[FileStorage] Directorio de almacenamiento: {}", storageLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de almacenamiento: " + storageLocation, e);
        }
    }

    /**
     * Valida y guarda un archivo.
     * @return ruta relativa del archivo guardado (URL pública)
     */
    public String guardar(MultipartFile archivo, String numeroSeguimiento) {
        validar(archivo);

        String extension = obtenerExtension(archivo.getOriginalFilename());
        String nombreUnico = numeroSeguimiento + "_" + UUID.randomUUID() + "." + extension;
        Path destino = Paths.get(storageLocation).resolve(nombreUnico);

        try {
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            log.info("[FileStorage] Guardado: {}", destino);
            return "/uploads/" + nombreUnico;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + archivo.getOriginalFilename(), e);
        }
    }

    private void validar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new BadRequestException("No se puede procesar un archivo vacío.");
        }
        if (archivo.getSize() > MAX_BYTES) {
            throw new BadRequestException("El archivo supera el tamaño máximo permitido (10 MB): " + archivo.getOriginalFilename());
        }
        String contentType = archivo.getContentType();
        if (contentType == null || !TIPOS_PERMITIDOS.contains(contentType.toLowerCase())) {
            throw new BadRequestException(
                    "Formato de archivo no permitido: '" + contentType + "'. Solo se aceptan JPG y PNG.");
        }
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) return "jpg";
        return nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1).toLowerCase();
    }
}
