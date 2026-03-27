package uq.sistemagestionsolicitudes.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@DiscriminatorValue("DOCENTE")
public class Docente extends Usuario {

    private String tipoContrato;

    @Override
    public String getRole() {
        return "ROL_DOCENTE";
    }
}
