package uq.sistemagestionsolicitudes.service;

import uq.sistemagestionsolicitudes.dto.ResumenIAResponse;
import uq.sistemagestionsolicitudes.dto.SugerenciaIAResponse;
import uq.sistemagestionsolicitudes.model.HistorialSolicitud;
import uq.sistemagestionsolicitudes.model.AccionSolicitud;
import uq.sistemagestionsolicitudes.repository.HistorialSolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IAServiceImpl implements IAService {

    @Autowired
    private HistorialSolicitudRepository historialRepo;

    @Override
    public ResumenIAResponse generarResumen(Long solicitudId) {

        List<HistorialSolicitud> historial =
                historialRepo.findBySolicitudIdOrderByFechaAsc(solicitudId);

        StringBuilder resumen = new StringBuilder();
        resumen.append("Resumen de la solicitud:\n");

        for (HistorialSolicitud h : historial) {

            resumen.append("- ")
                    .append(h.getFecha())
                    .append(": ")
                    .append(h.getAccion())
                    .append(" por ")
                    .append(h.getUsuarioResponsable().getNombre())
                    .append("\n");

            if (h.getAccion() == AccionSolicitud.CIERRE) {
                resumen.append("Solicitud finalizada\n");
            }
        }

        ResumenIAResponse res = new ResumenIAResponse();
        res.setResumen(resumen.toString());

        return res;
    }

    @Override
    public SugerenciaIAResponse sugerirClasificacion(String descripcion) {

        SugerenciaIAResponse res = new SugerenciaIAResponse();

        String texto = descripcion.toLowerCase();

        double confianza = 0.5;
        String explicacion = "";

        if (texto.contains("error") || texto.contains("fallo")) {
            res.setCategoria("BUG");
            confianza += 0.2;
            explicacion += "Se detectó 'error'. ";
        }

        if (texto.contains("urgente")) {
            res.setPrioridad("ALTA");
            confianza += 0.2;
            explicacion += "Se detectó 'urgente'. ";
        } else {
            res.setPrioridad("MEDIA");
        }

        res.setConfianza(confianza);
        res.setExplicacion(explicacion);

        return res;
    }
}