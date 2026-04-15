package uq.sistemagestionsolicitudes.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SugerenciaIAResponse {

    private String categoria;
    private String prioridad;
    private double confianza;
    private String explicacion;
}
