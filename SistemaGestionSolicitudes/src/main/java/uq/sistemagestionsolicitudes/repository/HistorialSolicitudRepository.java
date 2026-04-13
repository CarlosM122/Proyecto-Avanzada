package uq.sistemagestionsolicitudes.repository;

import uq.sistemagestionsolicitudes.model.HistorialSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialSolicitudRepository extends JpaRepository<HistorialSolicitud, Long> {

    List<HistorialSolicitud> findBySolicitudIdOrderByFechaAsc(Long solicitudId);}