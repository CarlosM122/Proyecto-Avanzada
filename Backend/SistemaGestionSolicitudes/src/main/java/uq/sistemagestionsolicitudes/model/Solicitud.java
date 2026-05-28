package uq.sistemagestionsolicitudes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @Column(length = 2000)
    private String justificacionPrioridad;

    @Enumerated(EnumType.STRING)
    private TipoSolicitud tipoSolicitud;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Enumerated(EnumType.STRING)
    private OrigenSolicitud origen;

    private LocalDateTime fecha;

    @Column(length = 2000)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Usuario responsable;

    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private Usuario solicitante;
}