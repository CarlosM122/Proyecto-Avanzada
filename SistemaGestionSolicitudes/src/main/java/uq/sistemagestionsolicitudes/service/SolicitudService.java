package uq.sistemagestionsolicitudes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uq.sistemagestionsolicitudes.dto.ClasificacionRequest;
import uq.sistemagestionsolicitudes.dto.SolicitudRequest;
import uq.sistemagestionsolicitudes.dto.SolicitudResponse;
import uq.sistemagestionsolicitudes.exception.AccessDeniedException;
import uq.sistemagestionsolicitudes.exception.InvalidStateException;
import uq.sistemagestionsolicitudes.exception.ResourceNotFoundException;
import uq.sistemagestionsolicitudes.model.AccionSolicitud;
import uq.sistemagestionsolicitudes.model.*;
import uq.sistemagestionsolicitudes.repository.SolicitudRepository;
import uq.sistemagestionsolicitudes.repository.UsuarioRepository;
import uq.sistemagestionsolicitudes.repository.specification.SolicitudSpecification;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistorialSolicitudService historialService;

    public SolicitudResponse crearSolicitud(SolicitudRequest request) {
        request.setFechaRegistro(LocalDate.now());
        String [] prioridad = calcularPrioridad(request);
        Solicitud solicitud = new Solicitud();


        solicitud.setDescripcion(request.getDescripcion());
        solicitud.setTipoSolicitud(request.getTipoSolicitud());
        solicitud.setOrigen(request.getOrigen());
        solicitud.setFecha(request.getFechaRegistro().atStartOfDay());
        solicitud.setPrioridad(Prioridad.valueOf(prioridad[0]));
        solicitud.setJustificacion_prioridad(prioridad[1]);

        solicitud.setSolicitante(obtenerUsuarioAutenticado());

        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);

        historialService.registrarCambio(
                solicitudGuardada.getId(),
                AccionSolicitud.REGISTRO,
                obtenerUsuarioAutenticado().getId(),
                null
                );

        return convertirDTO(solicitud);

    }

    private String [] calcularPrioridad(SolicitudRequest request) {
        String [] prioridad = new String[2];
        LocalDate fecha = request.getFechaRegistro();
        String tipoSolicitud= request.getTipoSolicitud().toString();
        if (tipoSolicitud.equalsIgnoreCase("REGISTRO_ASIGNATURAS")||
                tipoSolicitud.equalsIgnoreCase("SOLICITUD_CUPOS")){
            prioridad[0] = "ALTA";
            prioridad[1] = "Se clasifica con prioridad alta, ya que se debe resolver en un plazo no mayor a 5 dias desde el" +
                    "registro de la solicitud. La fecha maxima para resolver esta solicitud es: " + fecha.plusDays(5);
        } else if (tipoSolicitud.equalsIgnoreCase("CANCELACION_ASIGNATURAS")||
                tipoSolicitud.equalsIgnoreCase("HOMOLOGACION")) {
            prioridad[0] = "MEDIA";
            prioridad[1] = "Se clasifica con prioridad media, ya que se debe resolver en un plazo no mayor a 10 dias desde el" +
                    "registro de la solicitud. La fecha maxima para resolver esta solicitud es: " + fecha.plusDays(10);
        } else {
            prioridad[0] = "BAJA";
            prioridad[1] = "Se clasifica con prioridad baja, ya que se debe resolver en un plazo no mayor a 15 dias desde el" +
                    "registro de la solicitud. La fecha maxima para resolver esta solicitud es: " + fecha.plusDays(15);
        }
        return prioridad;
    }

    public List<SolicitudResponse> obtenerSolicitudes() {
        Usuario solicitante = obtenerUsuarioAutenticado();
        List<Solicitud> solicitudList = solicitudRepository.findBySolicitanteId(solicitante.getId());
        return solicitudList.stream()
                .map(this::convertirDTO)
                .toList();
    }

    public SolicitudResponse clasificarSolicitud(Long id, ClasificacionRequest request) {
        Solicitud solicitud = encontrarSolicitud(id);
        solicitud.setPrioridad(request.getPrioridad());
        solicitud.setJustificacion_prioridad(request.getJustificacion());
        solicitud.setEstado(cambiarEstado(Estado.CLASIFICADA, solicitud.getEstado()));
        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);

        historialService.registrarCambio(
                solicitudGuardada.getId(),
                AccionSolicitud.CLASIFICACION,
                obtenerUsuarioAutenticado().getId(),
                null
        );
        return convertirDTO(solicitud);
    }

    public SolicitudResponse aignarResponsable(Long id, Long responsableId) {
        Solicitud solicitud = encontrarSolicitud(id);
        Usuario responsable = usuarioRepository.findById(responsableId).
                orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado"));
        solicitud.setResponsable(responsable);
        solicitud.setEstado(cambiarEstado(Estado.EN_ATENCION, solicitud.getEstado()));
        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);

        historialService.registrarCambio(
                solicitudGuardada.getId(),
                AccionSolicitud.ASIGNACION,
                obtenerUsuarioAutenticado().getId(),
                null
        );
        return convertirDTO(solicitud);
    }

    public SolicitudResponse atenderSolcitud(Long id, String anotacion) {
        Solicitud solicitud = encontrarSolicitud(id);
        solicitud.setEstado(cambiarEstado(Estado.ATENDIDA, solicitud.getEstado()));
        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);
        historialService.registrarCambio(
                solicitudGuardada.getId(),
                AccionSolicitud.CAMBIO_ESTADO,
                obtenerUsuarioAutenticado().getId(),
                anotacion
        );
        return convertirDTO(solicitud);
    }

    public SolicitudResponse cerrarSolicitud(Long id, String anotacion) {
        Solicitud solicitud = encontrarSolicitud(id);
        solicitud.setEstado(cambiarEstado(Estado.CERRADA, solicitud.getEstado()));
        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);

        historialService.registrarCambio(
                solicitudGuardada.getId(),
                AccionSolicitud.CIERRE,
                obtenerUsuarioAutenticado().getId(),
                anotacion
        );
        return convertirDTO(solicitud);
    }

    public List<SolicitudResponse> buscarSolicitudes(Estado estado, TipoSolicitud tipo, Prioridad prioridad, Long responsableId) {

        var spec = SolicitudSpecification.conFiltros(
                estado, tipo, prioridad, responsableId
        );

        return solicitudRepository.findAll(spec)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    private SolicitudResponse convertirDTO(Solicitud solicitud) {
        return new SolicitudResponse(
                solicitud.getId(), solicitud.getDescripcion(), solicitud.getEstado(), solicitud.getPrioridad(),
                solicitud.getTipoSolicitud(), solicitud.getFecha(), solicitud.getJustificacion_prioridad(), solicitud.getResponsable()
        );
    }

    public SolicitudResponse obtenerSolicitudId(Long id) {
        return convertirDTO(encontrarSolicitud(id));
    }

    private Solicitud encontrarSolicitud(Long solicitudId) {
         return solicitudRepository.findById(solicitudId).orElseThrow(() ->
                 new ResourceNotFoundException("Solicitud no encontrado"));

    }

    private Estado cambiarEstado(Estado estado, Estado solicitudEstado) {
        Estado nuevoEstado = null;
        if (estado == Estado.CLASIFICADA) {
            if (solicitudEstado == Estado.REGISTRADA){
                nuevoEstado = Estado.CLASIFICADA;
            }
        }else if (estado == Estado.EN_ATENCION){
            if (solicitudEstado == Estado.CLASIFICADA){
                nuevoEstado = Estado.EN_ATENCION;
            }
        } else if (estado == Estado.ATENDIDA) {
            if (solicitudEstado == Estado.EN_ATENCION){
                nuevoEstado = Estado.ATENDIDA;
            }
        } else if (estado == Estado.CERRADA){
            if (solicitudEstado == Estado.ATENDIDA){
                nuevoEstado = Estado.CERRADA;
            }
        }
        if (estado == null){
            throw new InvalidStateException("La acción no corresponde a el estado de la solicitud, estado actual:"
                    + solicitudEstado);
        }
        return nuevoEstado;
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Usuario no autenticado");
        }
        String correo = authentication.getName();
        assert usuarioRepository != null;
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
}