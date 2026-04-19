package uq.sistemagestionsolicitudes.dto;

import lombok.Data;
import uq.sistemagestionsolicitudes.model.Prioridad;

@Data
public class ClasificacionRequest {

    private Prioridad prioridad;
    private String justificacion;
}
