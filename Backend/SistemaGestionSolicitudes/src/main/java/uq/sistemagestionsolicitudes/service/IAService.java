package uq.sistemagestionsolicitudes.service;

import uq.sistemagestionsolicitudes.dto.ResumenIAResponse;
import uq.sistemagestionsolicitudes.dto.SugerenciaIAResponse;

public interface IAService {

    ResumenIAResponse generarResumen(Long solicitudId);

    SugerenciaIAResponse sugerirClasificacion(String descripcion);
}