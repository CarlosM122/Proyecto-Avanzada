package uq.sistemagestionsolicitudes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsuarioResumenResponse {
    private Long id;
    private String nombre;
    private String correo;
    private String role;
}
