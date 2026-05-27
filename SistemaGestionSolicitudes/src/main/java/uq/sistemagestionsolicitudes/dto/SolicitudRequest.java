package uq.sistemagestionsolicitudes.dto;

import lombok.Getter;
import lombok.Setter;
import uq.sistemagestionsolicitudes.model.OrigenSolicitud;
import uq.sistemagestionsolicitudes.model.TipoSolicitud;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SolicitudRequest {

    private String descripcion;

    private TipoSolicitud tipoSolicitud;

    private OrigenSolicitud origen;

    private LocalDateTime fechaRegistro;

    private Long idSolicitante;
}