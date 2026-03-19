package uq.sistemagestionsolicitudes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uq.sistemagestionsolicitudes.model.Estado;
import uq.sistemagestionsolicitudes.model.Prioridad;
import uq.sistemagestionsolicitudes.model.TipoSolicitud;

import java.time.LocalDate;
@Getter @Setter
@AllArgsConstructor
public class SolicitudResponse {

    private Long id;

    private String descripcion;

    private Estado estado;

    private Prioridad prioridad;

    private TipoSolicitud tipoSolicitud;

    private LocalDate fecha;
}
