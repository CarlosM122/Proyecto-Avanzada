package uq.sistemagestionsolicitudes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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

    private String justificacion_prioridad;

    @Enumerated(EnumType.STRING)
    private TipoSolicitud tipoSolicitud;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Enumerated(EnumType.STRING)
    private OrigenSolicitud origen;

    private LocalDateTime fecha;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Usuario responsable;

    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private Usuario solicitante;

    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado=Estado.REGISTRADA;
        }
    }

}