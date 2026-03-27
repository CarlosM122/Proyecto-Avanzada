package uq.sistemagestionsolicitudes.service;


import uq.sistemagestionsolicitudes.model.AccionSolicitud;
import uq.sistemagestionsolicitudes.model.HistorialSolicitud;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IAService {

    public String generarResumen(List<HistorialSolicitud> historial) {

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
                resumen.append(" Solicitud finalizada\n ");
            }
        }

        return resumen.toString();
    }
}