package uq.sistemagestionsolicitudes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uq.sistemagestionsolicitudes.dto.ResumenIAResponse;
import uq.sistemagestionsolicitudes.dto.SugerenciaIAResponse;
import uq.sistemagestionsolicitudes.model.AccionSolicitud;
import uq.sistemagestionsolicitudes.model.HistorialSolicitud;
import uq.sistemagestionsolicitudes.repository.HistorialSolicitudRepository;
import uq.sistemagestionsolicitudes.service.IAService;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class IAServiceFallbackImpl implements IAService {

    private final HistorialSolicitudRepository historialRepo;

    private static final Map<String, String> KEYWORDS_CATEGORIA = Map.ofEntries(
            Map.entry("error", "ERROR_ACADEMICO"),
            Map.entry("matricula", "MATRICULA"),
            Map.entry("cancelacion", "CANCELACION"),
            Map.entry("homologacion", "HOMOLOGACION"),
            Map.entry("certificado", "CERTIFICADO")
    );

    private static final Map<String, String> KEYWORDS_PRIORIDAD = Map.of(
            "urgente", "ALTA",
            "inmediato", "ALTA",
            "importante", "MEDIA"
    );

    @Override
    public SugerenciaIAResponse sugerirClasificacion(String descripcion) {

        String texto = descripcion.toLowerCase();

        String categoria = "GENERAL";
        String prioridad = "MEDIA";
        double confianza = 0.4;
        StringBuilder explicacion = new StringBuilder();

        for (var entry : KEYWORDS_CATEGORIA.entrySet()) {
            if (texto.contains(entry.getKey())) {
                categoria = entry.getValue();
                confianza += 0.3;
                explicacion.append("Se detectó palabra clave: ")
                        .append(entry.getKey()).append(". ");
                break;
            }
        }

        for (var entry : KEYWORDS_PRIORIDAD.entrySet()) {
            if (texto.contains(entry.getKey())) {
                prioridad = entry.getValue();
                confianza += 0.2;
                explicacion.append("Prioridad sugerida por palabra: ")
                        .append(entry.getKey()).append(". ");
                break;
            }
        }

        SugerenciaIAResponse res = new SugerenciaIAResponse();
        res.setCategoria(categoria);
        res.setPrioridad(prioridad);
        res.setConfianza(confianza);
        res.setExplicacion("[Fallback] " + explicacion);

        return res;
    }

    @Override
    public ResumenIAResponse generarResumen(Long solicitudId) {

        List<HistorialSolicitud> historial =
                historialRepo.findBySolicitudIdOrderByFechaAsc(solicitudId);

        StringBuilder resumen = new StringBuilder();
        resumen.append("Resumen de la solicitud académica:\n");

        for (HistorialSolicitud h : historial) {

            resumen.append("- ")
                    .append(h.getFecha())
                    .append(": ")
                    .append(h.getAccion())
                    .append(" por ")
                    .append(h.getUsuarioResponsable().getNombre())
                    .append("\n");

            if (h.getAccion() == AccionSolicitud.CIERRE) {
                resumen.append("La solicitud fue finalizada.\n");
            }
        }

        ResumenIAResponse res = new ResumenIAResponse();
        res.setResumen(resumen.toString());
        res.setGeneradoPor("Fallback");

        return res;
    }
}