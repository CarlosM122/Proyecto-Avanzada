package uq.sistemagestionsolicitudes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uq.sistemagestionsolicitudes.model.Estado;
import uq.sistemagestionsolicitudes.model.Solicitud;

import java.util.List;
import java.util.UUID;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long>, JpaSpecificationExecutor<Solicitud> {
    List<Solicitud> findByEstado (Estado estado);
    List<Solicitud> findBySolicitanteId(Long solicitanteId);
    Page<Solicitud> findBySolicitanteId(Long solicitanteId, Pageable pageable);
}