package uq.sistemagestionsolicitudes.dto;

import lombok.Getter;
import lombok.Setter;
import uq.sistemagestionsolicitudes.model.OrigenSolicitud;
import uq.sistemagestionsolicitudes.model.TipoSolicitud;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class CrearSolicitudRequest {

    private String descripcion;

    private TipoSolicitud tipoSolicitud;

    private OrigenSolicitud origen;

    private Date fechaRegistro;

    private UUID uuidSolicitante;
}
