package uq.sistemagestionsolicitudes.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uq.sistemagestionsolicitudes.dto.CrearSolicitudRequest;
import uq.sistemagestionsolicitudes.dto.SolicitudResponse;
import uq.sistemagestionsolicitudes.model.Prioridad;
import uq.sistemagestionsolicitudes.repository.UsuarioRepository;
import uq.sistemagestionsolicitudes.service.SolicitudService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping
    public SolicitudResponse crearSolicitudRequest(@RequestBody CrearSolicitudRequest request){
        return solicitudService.crearSolicitud(request);
    }

    @GetMapping
    public List<SolicitudResponse> obtenerSolicitudes(){
        return solicitudService.obtenerSolicitudes();
    }

//    @PostMapping("/{id}/clasificar")
//    @PreAuthorize("hasAuthority('ROL_ADMINISTRATIVO')")
//    public SolicitudResponse clasificarSolicitud(UUID id, @PathVariable ){
//
//    }

    @GetMapping("/{id}")
    public SolicitudResponse obtenerSolicitudDetallada(@PathVariable UUID id){
        return solicitudService.obtenerSolicitudId(id);
    }


    @PatchMapping("/{id}/prioridad")
    @PreAuthorize("hasAuthority('ROL_ADMINISTRATIVO')")
    public SolicitudResponse priorizarSolicitudRequest(@PathVariable UUID id,@RequestBody Prioridad prioridad){
        return solicitudService.priorizarSolicitud(id,prioridad);
    }
}
