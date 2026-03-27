package uq.sistemagestionsolicitudes.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class HistorialSolicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private AccionSolicitud accion;

    private String observacion;

    @ManyToOne
    private Usuario usuarioResponsable;

    @ManyToOne
    private Solicitud solicitud;

}