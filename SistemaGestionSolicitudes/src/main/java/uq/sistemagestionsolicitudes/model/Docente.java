package uq.sistemagestionsolicitudes.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@DiscriminatorValue("DOCENTE")
public class Docente extends Usuario {

    private String tipoContrato;

    public Docente(String tipoContrato) {
        this.tipoContrato=tipoContrato;
    }

    public Docente() {

    }

    @Override
    public String getRole() {
        return "ROL_DOCENTE";
    }
}
