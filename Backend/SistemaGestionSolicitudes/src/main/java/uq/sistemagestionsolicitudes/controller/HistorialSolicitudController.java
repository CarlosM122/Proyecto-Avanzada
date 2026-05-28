package uq.sistemagestionsolicitudes.controller;

import uq.sistemagestionsolicitudes.model.HistorialSolicitud;
import uq.sistemagestionsolicitudes.repository.HistorialSolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historial")
public class HistorialSolicitudController {

    @Autowired
    private HistorialSolicitudRepository historialRepo;

    @GetMapping("/{solicitudId}")
    public List<HistorialSolicitud> obtenerHistorial(@PathVariable Long solicitudId) {
        return historialRepo.findBySolicitudIdOrderByFechaAsc(solicitudId);
    }
}