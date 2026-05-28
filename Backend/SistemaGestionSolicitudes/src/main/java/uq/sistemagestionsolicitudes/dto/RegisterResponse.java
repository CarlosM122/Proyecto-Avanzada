package uq.sistemagestionsolicitudes.dto;

import lombok.Data;
import uq.sistemagestionsolicitudes.model.TipoContrato;

@Data
public class RegisterResponse {

    private String correo;
    private TipoContrato contrato;
    private String token;
}
