package uq.sistemagestionsolicitudes.controller;

import uq.sistemagestionsolicitudes.model.Solicitud;
import uq.sistemagestionsolicitudes.model.HistorialSolicitud;
import uq.sistemagestionsolicitudes.service.HistorialSolicitudService;
import uq.sistemagestionsolicitudes.repository.SolicitudRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/solicitudes")
public class HistorialSolicitudController {

    @Autowired
    private HistorialSolicitudService historialService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @PostMapping("/historial")
    public HistorialSolicitud crearHistorial(@RequestBody HistorialSolicitud historial) {
        return historialService.registrarAccion(historial);
    }

    @GetMapping("/{solicitudId}/historial")
    public List<HistorialSolicitud> obtenerHistorial(@PathVariable Long solicitudId) {

        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        return historialService.obtenerHistorial(solicitud);
    }
}