package uq.sistemagestionsolicitudes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uq.sistemagestionsolicitudes.dto.CrearSolicitudRequest;
import uq.sistemagestionsolicitudes.exception.InvalidStateException;
import uq.sistemagestionsolicitudes.exception.ResourceNotFoundException;
import uq.sistemagestionsolicitudes.model.Estado;
import uq.sistemagestionsolicitudes.model.Estudiante;
import uq.sistemagestionsolicitudes.model.Solicitud;
import uq.sistemagestionsolicitudes.model.Usuario;
import uq.sistemagestionsolicitudes.repository.SolicitudRepository;
import uq.sistemagestionsolicitudes.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    public Solicitud crearSolicitud(CrearSolicitudRequest  request, Usuario solicitante) {
        Usuario usuario = usuarioRepository.findById(solicitante.getId()).orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado"));
        if (usuario instanceof Estudiante){
            Solicitud solicitud = new Solicitud();

            solicitud.setDescripcion(request.getDescripcion());
            solicitud.setTipoSolicitud(request.getTipoSolicitud());
            solicitud.setPrioridad(request.getPrioridad());
            solicitud.setOrigen(request.getOrigen());

            solicitud.setFecha(LocalDate.now());
            solicitud.setEstado(Estado.REGISTRADA);

            solicitud.setSolicitante(solicitante);

            return solicitudRepository.save(solicitud);
        }else {
            throw new InvalidStateException("Solo los estudiantes pueden crear solicitudes");
        }
    }

    public List<Solicitud> obtenerSolicitudes(Usuario usuario) {
        Usuario solicitante = usuarioRepository.findById(usuario.getId()).orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado"));
        return solicitudRepository.findBySolicitanteId(solicitante.getId());
    }
}
