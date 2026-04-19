package uq.sistemagestionsolicitudes.dto;

import lombok.Getter;
import lombok.Setter;
import uq.sistemagestionsolicitudes.model.Area;
import uq.sistemagestionsolicitudes.model.TipoContrato;

@Getter
@Setter
public class RegisterRequest {
    private String correo;
    private String password;
    private String nombre;
    private String role;
    private TipoContrato tipoContrato;
    private String telefono;
    private Integer semestre;
    private Area areaEncargada;
}
