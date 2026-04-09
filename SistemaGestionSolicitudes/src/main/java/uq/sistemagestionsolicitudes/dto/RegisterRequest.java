package uq.sistemagestionsolicitudes.dto;

import lombok.Getter;
import lombok.Setter;
import uq.sistemagestionsolicitudes.model.Area;

@Getter
@Setter
public class RegisterRequest {
    private String correo;
    private String password;
    private String nombre;
    private String role;
    private String tipoContrato;
    private String telefono;
    private Integer semestre;
    private Area areaEncargada;
}
