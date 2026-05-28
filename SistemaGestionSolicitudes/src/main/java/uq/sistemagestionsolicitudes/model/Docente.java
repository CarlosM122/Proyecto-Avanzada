package uq.sistemagestionsolicitudes.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@DiscriminatorValue("DOCENTE")
public class Docente extends Usuario {

    @Enumerated(EnumType.STRING)
    private TipoContrato tipoContrato;

    public Docente(TipoContrato tipoContrato) {
        this.tipoContrato=tipoContrato;
    }

    public Docente() {

    }

    @Override
    public String getRole() {
        return "ROL_DOCENTE";
    }
}