package uq.sistemagestionsolicitudes.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import uq.sistemagestionsolicitudes.dto.CrearSolicitudRequest;
import uq.sistemagestionsolicitudes.model.Solicitud;
import uq.sistemagestionsolicitudes.model.Usuario;
import uq.sistemagestionsolicitudes.service.SolicitudService;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;
    @PostMapping
    public Solicitud crearSolicitud(@RequestBody CrearSolicitudRequest request){
        Usuario solicitante = obtenerUsuarioAutenticado();
        return solicitudService.crearSolicitud(request,solicitante);
    }

    @GetMapping
    public List<Solicitud> obtenerSolicitudes(){
        Usuario usuario = obtenerUsuarioAutenticado();
        return solicitudService.obtenerSolicitudes(usuario);
    }

    private Usuario obtenerUsuarioAutenticado() {
        return null;
    }
}
