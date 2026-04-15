package uq.sistemagestionsolicitudes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import uq.sistemagestionsolicitudes.dto.ResumenIAResponse;
import uq.sistemagestionsolicitudes.dto.SugerenciaIAResponse;
import uq.sistemagestionsolicitudes.model.HistorialSolicitud;
import uq.sistemagestionsolicitudes.repository.HistorialSolicitudRepository;
import uq.sistemagestionsolicitudes.service.IAService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class IAServiceOpenAIImpl implements IAService {

    private final ChatClient chatClient;
    private final HistorialSolicitudRepository historialRepo;
    private final IAServiceFallbackImpl fallback;

    @Override
    public SugerenciaIAResponse sugerirClasificacion(String descripcion) {
        try {

            String respuesta = chatClient.prompt()
                    .user("Clasifica esta solicitud académica: " + descripcion)
                    .call()
                    .content();

            SugerenciaIAResponse res = new SugerenciaIAResponse();
            res.setCategoria("IA_GENERADA");
            res.setPrioridad("MEDIA");
            res.setConfianza(0.9);
            res.setExplicacion(respuesta);

            return res;

        } catch (Exception e) {
            return fallback.sugerirClasificacion(descripcion);
        }
    }

    @Override
    public ResumenIAResponse generarResumen(Long solicitudId) {
        try {

            List<HistorialSolicitud> historial =
                    historialRepo.findBySolicitudIdOrderByFechaAsc(solicitudId);

            StringBuilder datos = new StringBuilder();

            for (HistorialSolicitud h : historial) {
                datos.append(h.getFecha())
                        .append(" - ")
                        .append(h.getAccion())
                        .append("\n");
            }

            String resumen = chatClient.prompt()
                    .user("Resume esta solicitud académica:\n" + datos)
                    .call()
                    .content();

            ResumenIAResponse res = new ResumenIAResponse();
            res.setResumen(resumen);
            res.setGeneradoPor("OpenAI");

            return res;

        } catch (Exception e) {
            return fallback.generarResumen(solicitudId);
        }
    }
}