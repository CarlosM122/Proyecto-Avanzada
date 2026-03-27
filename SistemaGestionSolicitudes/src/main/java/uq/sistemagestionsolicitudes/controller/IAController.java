package uq.sistemagestionsolicitudes.controller;

import uq.sistemagestionsolicitudes.model.Solicitud;
import uq.sistemagestionsolicitudes.repository.SolicitudRepository;
import uq.sistemagestionsolicitudes.service.HistorialSolicitudService;
import uq.sistemagestionsolicitudes.service.IAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/ia")
public class IAController {

    @Autowired
    private HistorialSolicitudService historialService;

    @Autowired
    private IAService iaService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @GetMapping("/resumen/{solicitudId}")
    public String generarResumen(@PathVariable Long solicitudId) {

        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        return iaService.generarResumen(
                historialService.obtenerHistorial(solicitud)
        );
    }
}