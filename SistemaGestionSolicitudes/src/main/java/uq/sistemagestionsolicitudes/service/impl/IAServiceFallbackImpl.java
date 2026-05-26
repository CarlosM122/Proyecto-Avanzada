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

@Slf4j
@Service
@RequiredArgsConstructor
public class IAServiceFallbackImpl implements IAService {

    private final HistorialSolicitudRepository historialRepo;

    @Override
    public SugerenciaIAResponse sugerirClasificacion(String descripcion) {

        String texto = descripcion.toLowerCase();

        if(texto.contains("login")
                || texto.contains("sesión")
                || texto.contains("autenticar")
                || texto.contains("contraseña")
                || texto.contains("usuario")) {

            SugerenciaIAResponse response = new SugerenciaIAResponse();

            response.setCategoria("AUTENTICACION");
            response.setPrioridad("ALTA");
            response.setConfianza(0.95);

            response.setExplicacion("""
                    La solicitud fue clasificada como AUTENTICACION
                    debido a que se detectaron términos relacionados
                    con acceso de usuarios al sistema.

                    Posibles causas:
                    
                    credenciales inválidas,
                     sesión expirada,
                     bloqueo de acceso,
                     error de autenticación

                    Se recomienda validar el servicio
                    de autenticación y verificar
                    las credenciales del usuario.
                    """);

            return response;
        }


        if(texto.contains("documento")
                || texto.contains("certificado")
                || texto.contains("archivo")
                || texto.contains("descargar")) {

            SugerenciaIAResponse response = new SugerenciaIAResponse();

            response.setCategoria("DOCUMENTACION");
            response.setPrioridad("MEDIA");
            response.setConfianza(0.85);

            response.setExplicacion("""
                    La solicitud fue clasificada como DOCUMENTACION
                    porque se encontraron referencias relacionadas
                    con archivos o documentos académicos.

                    Posibles causas:
                     documento no disponible,
                     error al descargar archivos,
                     problemas de generación documental

                    Se recomienda verificar
                    la disponibilidad de los documentos
                    y el servicio de generación de archivos.
                    """);

            return response;
        }


        if(texto.contains("lento")
                || texto.contains("servidor")
                || texto.contains("demora")
                || texto.contains("caido")) {

            SugerenciaIAResponse response = new SugerenciaIAResponse();

            response.setCategoria("INFRAESTRUCTURA");
            response.setPrioridad("MEDIA");
            response.setConfianza(0.80);

            response.setExplicacion("""
                    La solicitud fue clasificada como INFRAESTRUCTURA
                    porque se detectaron términos asociados
                    al rendimiento del sistema.

                    Posibles causas:
                     sobrecarga del servidor,
                     tiempos altos de respuesta,
                     problemas de red,
                     consumo elevado de recursos.

                     Se recomienda intentar nuevamente
                     en unos minutos. Si el problema continúa,
                     comuníquese con soporte técnico.
                    """);

            return response;
        }

        if(texto.contains("error")
                || texto.contains("falla")
                || texto.contains("bug")
                || texto.contains("crash")
                || texto.contains("exception")) {

            SugerenciaIAResponse response = new SugerenciaIAResponse();

            response.setCategoria("SOPORTE_TECNICO");
            response.setPrioridad("ALTA");
            response.setConfianza(0.90);

            response.setExplicacion("""
                    La solicitud fue clasificada como SOPORTE_TECNICO
                    porque se detectaron términos asociados
                    a errores técnicos del sistema.

                    Posibles causas:
                     fallo interno del sistema,
                     error de procesamiento,
                     comportamiento inesperado,
                     interrupción del servicio.

                    Se recomienda revisar los logs
                    y validar el funcionamiento
                    general de la plataforma.
                    """);

            return response;
        }


        SugerenciaIAResponse response = new SugerenciaIAResponse();

        response.setCategoria("GENERAL");
        response.setPrioridad("MEDIA");
        response.setConfianza(0.40);

        response.setExplicacion("""
                No fue posible identificar una categoría específica
                para la solicitud utilizando las reglas actuales
                del sistema fallback.

                La descripción no contiene suficientes palabras clave
                para determinar automáticamente el tipo de incidente.

                Se recomienda realizar una revisión manual
                por parte del equipo de soporte.
                """);

        return response;
    }

    @Override
    public ResumenIAResponse generarResumen(Long solicitudId) {

        List<HistorialSolicitud> historial =
                historialRepo.findBySolicitudIdOrderByFechaAsc(solicitudId);

        StringBuilder resumen = new StringBuilder();

        resumen.append("Resumen de la solicitud:\n\n");

        if(historial.isEmpty()) {

            resumen.append("""
                    No se encontraron registros asociados
                    a la solicitud consultada.
                    """);

        } else {

            for(HistorialSolicitud h : historial) {

                resumen.append("- ")
                        .append(h.getFecha())
                        .append(" | ")
                        .append(h.getAccion())
                        .append(" por ");

                if(h.getUsuarioResponsable() != null) {

                    resumen.append(
                            h.getUsuarioResponsable().getNombre()
                    );

                } else {

                    resumen.append("Sistema");
                }

                resumen.append(".\n");

                if(h.getAccion() == AccionSolicitud.CIERRE) {

                    resumen.append("""
                            
                            La solicitud fue finalizada
                            correctamente por el sistema.
                            
                            """);

                } else {

                    resumen.append("""
                            
                            Se registró una actualización
                            dentro del flujo de atención.
                            
                            """);
                }
            }
        }

        ResumenIAResponse response = new ResumenIAResponse();

        response.setResumen(resumen.toString());
        response.setGeneradoPor("Fallback");

        return response;
    }
}