package uq.sistemagestionsolicitudes.model;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter @Setter
@DiscriminatorValue("ADMINISTRATIVO")
public class Administrativo extends Usuario {

    @Enumerated(EnumType.STRING)
    private Area areaEncargada;

    @Override
    public String getRole() {
        return "ROL_ADMINISTRATIVO";
    }
}
