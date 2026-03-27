package uq.sistemagestionsolicitudes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uq.sistemagestionsolicitudes.dto.CrearSolicitudRequest;
import uq.sistemagestionsolicitudes.dto.SolicitudResponse;
import uq.sistemagestionsolicitudes.exception.AccessDeniedException;
import uq.sistemagestionsolicitudes.exception.InvalidStateException;
import uq.sistemagestionsolicitudes.exception.ResourceNotFoundException;
import uq.sistemagestionsolicitudes.model.*;
import uq.sistemagestionsolicitudes.repository.SolicitudRepository;
import uq.sistemagestionsolicitudes.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    public SolicitudResponse crearSolicitud(CrearSolicitudRequest request) {
        Usuario solicitante = obtenerUsuarioAutenticado();
        Solicitud solicitud = new Solicitud();

        solicitud.setDescripcion(request.getDescripcion());
        solicitud.setTipoSolicitud(request.getTipoSolicitud());
        solicitud.setOrigen(request.getOrigen());
        solicitud.setFecha(LocalDate.now());

        solicitud.setSolicitante(solicitante);

        solicitudRepository.save(solicitud);
        return convertirDTO(solicitud);

    }

    public List<SolicitudResponse> obtenerSolicitudes() {
        Usuario solicitante = obtenerUsuarioAutenticado();
        List<Solicitud> solicitudList = solicitudRepository.findBySolicitanteId(solicitante.getId());
        return solicitudList.stream()
                .map(this::convertirDTO)
                .toList();
    }

    private SolicitudResponse convertirDTO(Solicitud solicitud) {
        return new SolicitudResponse(
                solicitud.getId(), solicitud.getDescripcion(), solicitud.getEstado(), solicitud.getPrioridad(),
                solicitud.getTipoSolicitud(), solicitud.getFecha()
        );
    }

    public SolicitudResponse obtenerSolicitudId(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrado"));
        return convertirDTO(solicitud);
    }

    public SolicitudResponse priorizarSolicitud(UUID id, Prioridad prioridad) {
        return null;
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Usuario no autenticado");
        }
        String correo = authentication.getName();
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
}