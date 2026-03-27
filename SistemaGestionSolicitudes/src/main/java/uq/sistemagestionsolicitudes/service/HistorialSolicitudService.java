package uq.sistemagestionsolicitudes.service;

import uq.sistemagestionsolicitudes.model.HistorialSolicitud;
import uq.sistemagestionsolicitudes.model.Solicitud;
import uq.sistemagestionsolicitudes.repository.HistorialSolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorialSolicitudService {

    @Autowired
    private HistorialSolicitudRepository historialRepository;

    public HistorialSolicitud registrarAccion(HistorialSolicitud historial) {
        historial.setFecha(LocalDateTime.now());
        return historialRepository.save(historial);
    }

    public List<HistorialSolicitud> obtenerHistorial(Solicitud solicitud) {
        return historialRepository.findBySolicitudOrderByFechaAsc(solicitud);
    }
}
