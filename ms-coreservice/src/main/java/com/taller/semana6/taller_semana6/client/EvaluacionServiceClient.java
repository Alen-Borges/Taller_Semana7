package com.taller.semana6.taller_semana6.client;

import com.taller.semana6.taller_semana6.dto.EvaluacionRequestDTO;
import com.taller.semana6.taller_semana6.dto.EvaluacionResponseDTO;

public interface EvaluacionServiceClient {
    EvaluacionResponseDTO evaluar(EvaluacionRequestDTO request);
}
