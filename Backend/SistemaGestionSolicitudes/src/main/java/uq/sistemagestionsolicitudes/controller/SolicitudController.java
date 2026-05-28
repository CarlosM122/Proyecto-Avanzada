package uq.sistemagestionsolicitudes.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uq.sistemagestionsolicitudes.dto.ClasificacionRequest;
import uq.sistemagestionsolicitudes.dto.SolicitudRequest;
import uq.sistemagestionsolicitudes.dto.SolicitudResponse;
import uq.sistemagestionsolicitudes.model.Estado;
import uq.sistemagestionsolicitudes.model.Prioridad;
import uq.sistemagestionsolicitudes.model.TipoSolicitud;
import uq.sistemagestionsolicitudes.service.SolicitudService;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROL_ESTUDIANTE')")
    public SolicitudResponse crearSolicitudRequest(@RequestBody SolicitudRequest request) {
        return solicitudService.crearSolicitud(request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROL_ESTUDIANTE', 'ROL_ADMINISTRATIVO', 'ROL_DOCENTE')")
    public SolicitudResponse obtenerSolicitudDetallada(@PathVariable Long id) {
        return solicitudService.obtenerSolicitudId(id);
    }

    @GetMapping
    public Page<SolicitudResponse> obtenerSolicitudes(Pageable pageable) {
        return solicitudService.obtenerSolicitudes(pageable);
    }

    @PatchMapping("/{id}/clasificar")
    @PreAuthorize("hasAuthority('ROL_ADMINISTRATIVO')")
    public SolicitudResponse clasificarSolicitudRequest(@PathVariable Long id, @RequestBody ClasificacionRequest request) {
        return solicitudService.clasificarSolicitud(id, request);
    }

    @GetMapping("/{id}/clasificar")
    @PreAuthorize("hasAuthority('ROL_ADMINISTRATIVO')")
    public SolicitudResponse obtenerSolicitudClasificaionRequest(@PathVariable Long id) {
        return obtenerSolicitudDetallada(id);
    }

    @PatchMapping("/{id}/asignacion")
    @PreAuthorize("hasAuthority('ROL_ADMINISTRATIVO')")
    public SolicitudResponse asignarResponsable(@PathVariable Long id, @RequestBody Long responsableId) {
        return solicitudService.asignarResponsable(id, responsableId);
    }

    @GetMapping("/{id}/asignacion")
    @PreAuthorize("hasAuthority('ROL_ADMINISTRATIVO')")
    public SolicitudResponse obtenerSolicitudAsigancionRequest(@PathVariable Long id) {
        return obtenerSolicitudDetallada(id);
    }

    @PatchMapping("/{id}/atender")
    @PreAuthorize("hasAnyAuthority('ROL_ADMINISTRATIVO','ROL_DOCENTE')")
    public SolicitudResponse atenderSolicitudRequest(@PathVariable Long id, @RequestBody String anotacion) {
        return solicitudService.atenderSolicitud(id, anotacion);
    }

    @GetMapping("/{id}/atender")
    @PreAuthorize("hasAnyAuthority('ROL_ADMINISTRATIVO','ROL_DOCENTE')")
    public SolicitudResponse obtenerSolicitudAtenderRequest(@PathVariable Long id) {
        return obtenerSolicitudDetallada(id);
    }

    @PostMapping("/{id}/cerrar")
    @PreAuthorize("hasAuthority('ROL_ADMINISTRATIVO')")
    public SolicitudResponse cerrarSolicitud(@PathVariable Long id, @RequestBody String anotacion) {
        return solicitudService.cerrarSolicitud(id,anotacion);
    }

    @GetMapping("/{id}/cerrar")
    @PreAuthorize("hasAuthority('ROL_ADMINISTRATIVO')")
    public SolicitudResponse obtenerSolicitudCerrarRequest(@PathVariable Long id) {
        return obtenerSolicitudDetallada(id);
    }

    @GetMapping("/buscar")
    public List<SolicitudResponse> buscar(
            @RequestParam(required = false) Estado estado,
            @RequestParam(required = false) TipoSolicitud tipo,
            @RequestParam(required = false) Prioridad prioridad,
            @RequestParam(required = false) Long responsableId
    ) {
        return solicitudService.buscarSolicitudes(
                estado, tipo, prioridad, responsableId
        );
    }
}