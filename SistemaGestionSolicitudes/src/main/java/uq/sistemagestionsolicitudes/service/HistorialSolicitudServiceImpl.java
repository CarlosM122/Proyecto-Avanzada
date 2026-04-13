package uq.sistemagestionsolicitudes.service;

import uq.sistemagestionsolicitudes.model.HistorialSolicitud;
import uq.sistemagestionsolicitudes.model.Solicitud;
import uq.sistemagestionsolicitudes.model.AccionSolicitud;
import uq.sistemagestionsolicitudes.repository.HistorialSolicitudRepository;
import uq.sistemagestionsolicitudes.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistorialSolicitudServiceImpl implements HistorialSolicitudService {

    @Autowired
    private HistorialSolicitudRepository repo;

    @Autowired
    private SolicitudRepository solicitudRepo;

    @Override
    public void registrarCambio(Long solicitudId, AccionSolicitud accion) {

        HistorialSolicitud h = new HistorialSolicitud();

        h.setFecha(LocalDateTime.now());
        h.setAccion(accion);

        Solicitud solicitud = solicitudRepo.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        h.setSolicitud(solicitud);

        repo.save(h);
    }
}