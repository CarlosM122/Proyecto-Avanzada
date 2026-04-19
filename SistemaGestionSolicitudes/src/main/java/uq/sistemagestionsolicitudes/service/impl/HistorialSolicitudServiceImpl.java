package uq.sistemagestionsolicitudes.service.impl;

import uq.sistemagestionsolicitudes.exception.ResourceNotFoundException;
import uq.sistemagestionsolicitudes.model.HistorialSolicitud;
import uq.sistemagestionsolicitudes.model.Solicitud;
import uq.sistemagestionsolicitudes.model.AccionSolicitud;
import uq.sistemagestionsolicitudes.model.Usuario;
import uq.sistemagestionsolicitudes.repository.HistorialSolicitudRepository;
import uq.sistemagestionsolicitudes.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uq.sistemagestionsolicitudes.repository.UsuarioRepository;
import uq.sistemagestionsolicitudes.service.HistorialSolicitudService;

import java.time.LocalDateTime;

@Service
public class HistorialSolicitudServiceImpl implements HistorialSolicitudService {

    @Autowired
    private HistorialSolicitudRepository repo;

    @Autowired
    private SolicitudRepository solicitudRepo;

    @Autowired
    private UsuarioRepository  usuarioRepo;

    @Override
    public void registrarCambio(Long solicitudId, AccionSolicitud accion, Long solicitanteId, String anotacion) {
        HistorialSolicitud h = new HistorialSolicitud();

        h.setFecha(LocalDateTime.now());
        h.setAccion(accion);
        if (anotacion != null) {
            h.setObservacion(anotacion);
        }

        Solicitud solicitud = solicitudRepo.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        Usuario usuario = usuarioRepo.findById(solicitanteId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        h.setUsuarioResponsable(usuario);
        h.setSolicitud(solicitud);

        repo.save(h);
    }
}