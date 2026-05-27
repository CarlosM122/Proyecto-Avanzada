package uq.sistemagestionsolicitudes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uq.sistemagestionsolicitudes.model.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    @Query("SELECT u FROM Usuario u WHERE TYPE(u) IN (Administrativo, Docente)")
    List<Usuario> findResponsables();
}
