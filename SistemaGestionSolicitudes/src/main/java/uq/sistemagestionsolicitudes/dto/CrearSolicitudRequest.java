package uq.sistemagestionsolicitudes.dto;

import lombok.Getter;
import lombok.Setter;
import uq.sistemagestionsolicitudes.model.OrigenSolicitud;
import uq.sistemagestionsolicitudes.model.Prioridad;
import uq.sistemagestionsolicitudes.model.TipoSolicitud;

@Getter @Setter
public class CrearSolicitudRequest {

    private String descripcion;

    private TipoSolicitud tipoSolicitud;

    private Prioridad prioridad;

    private OrigenSolicitud origen;
}
