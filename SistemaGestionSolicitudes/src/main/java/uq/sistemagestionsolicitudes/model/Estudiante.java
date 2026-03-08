package uq.sistemagestionsolicitudes.model;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter @Setter
@DiscriminatorValue("ESTUDIANTE")
public class Estudiante extends Usuario {

    private Integer semestre;

}
