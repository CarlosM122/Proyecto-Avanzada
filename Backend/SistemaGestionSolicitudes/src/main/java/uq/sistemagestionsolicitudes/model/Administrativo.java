package uq.sistemagestionsolicitudes.model;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter @Setter
@DiscriminatorValue("ADMINISTRATIVO")
public class Administrativo extends Usuario {

    @Enumerated(EnumType.STRING)
    private Area areaEncargada;
    @Enumerated(EnumType.STRING)
    private TipoContrato tipoContrato;

    public Administrativo (Area areaEncargada, TipoContrato tipoContrato) {
        this.areaEncargada=areaEncargada;
        this.tipoContrato=tipoContrato;
    }

    public Administrativo() {

    }

    @Override
    public String getRole() {
        return "ROL_ADMINISTRATIVO";
    }
}