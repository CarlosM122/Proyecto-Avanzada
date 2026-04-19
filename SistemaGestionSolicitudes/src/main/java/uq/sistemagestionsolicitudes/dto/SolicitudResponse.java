package uq.sistemagestionsolicitudes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uq.sistemagestionsolicitudes.model.Estado;
import uq.sistemagestionsolicitudes.model.Prioridad;
import uq.sistemagestionsolicitudes.model.TipoSolicitud;
import uq.sistemagestionsolicitudes.model.Usuario;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class SolicitudResponse {

    private Long id;

    private String descripcion;

    private Estado estado;

    private Prioridad prioridad;

    private TipoSolicitud tipoSolicitud;

    private LocalDateTime fecha;

    private String justificacionPrioridad;

    private Usuario responsable;
}
