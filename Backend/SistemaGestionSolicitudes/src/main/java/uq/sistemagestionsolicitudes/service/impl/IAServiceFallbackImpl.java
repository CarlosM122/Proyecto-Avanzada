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


        if(texto.contains("registro")
                || texto.contains("matricula")
                || texto.contains("inscribir")
                || texto.contains("asignatura")
                || texto.contains("materia")
                || texto.contains("horario")) {

            SugerenciaIAResponse response = new SugerenciaIAResponse();

            response.setCategoria("REGISTRO_ASIGNATURAS");
            response.setPrioridad("MEDIA");
            response.setConfianza(0.95);

            response.setExplicacion("""
                    La solicitud fue clasificada como REGISTRO_ASIGNATURAS
                    porque se detectaron términos relacionados
                    con inscripción o matrícula académica.

                    Posibles causas:
                     problemas de inscripción,
                     asignaturas no disponibles,
                     conflictos de horario,
                     cupos agotados.

                    Se recomienda validar
                    la disponibilidad de materias
                    y revisar el estado académico del estudiante.
                    """);

            return response;
        }


        if(texto.contains("homologacion")
                || texto.contains("homologar")
                || texto.contains("equivalencia")
                || texto.contains("materias cursadas")
                || texto.contains("otra universidad")
                || texto.contains("plan de estudios")) {

            SugerenciaIAResponse response = new SugerenciaIAResponse();

            response.setCategoria("HOMOLOGACION");
            response.setPrioridad("MEDIA");
            response.setConfianza(0.92);

            response.setExplicacion("""
            La solicitud fue clasificada como HOMOLOGACION
            porque se detectaron términos relacionados
            con reconocimiento de asignaturas cursadas previamente.

            Posibles causas:
             solicitud de equivalencias académicas,
             homologación de materias,
             validación de créditos,
             revisión de contenidos programáticos,
             traslado desde otra institución.

            Se recomienda verificar
            los certificados de notas,
            contenidos programáticos
            y requisitos establecidos por la facultad.
            """);

            return response;
        }


        if(texto.contains("cancelar")
                || texto.contains("cancelación")
                || texto.contains("retirar")
                || texto.contains("retiro")
                || texto.contains("eliminar materia")) {

            SugerenciaIAResponse response = new SugerenciaIAResponse();

            response.setCategoria("CANCELACION_ASIGNATURAS");
            response.setPrioridad("ALTA");
            response.setConfianza(0.94);

            response.setExplicacion("""
                    La solicitud fue clasificada como CANCELACION_ASIGNATURAS
                    porque se identificaron términos asociados
                    con retiro o cancelación académica.

                    Posibles causas:
                     solicitud de retiro de materias,
                     cruce de horarios,
                     inconvenientes personales,
                     bajo rendimiento académico.

                    Se recomienda validar
                    las fechas establecidas
                    y las políticas institucionales vigentes.
                    """);

            return response;
        }


        if(texto.contains("cupo")
                || texto.contains("grupo lleno")
                || texto.contains("sin cupos")
                || texto.contains("disponibilidad")
                || texto.contains("abrir grupo")) {

            SugerenciaIAResponse response = new SugerenciaIAResponse();

            response.setCategoria("SOLICITUD_CUPOS");
            response.setPrioridad("ALTA");
            response.setConfianza(0.93);

            response.setExplicacion("""
                    La solicitud fue clasificada como SOLICITUD_CUPOS
                    porque se detectaron referencias
                    relacionadas con disponibilidad de grupos.

                    Posibles causas:
                     grupos académicos llenos,
                     falta de disponibilidad,
                     apertura de nuevos cupos,
                     alta demanda estudiantil.

                    Se recomienda consultar
                    la disponibilidad de grupos
                    y contactar coordinación académica.
                    """);

            return response;
        }


        if(texto.contains("consulta")
                || texto.contains("información")
                || texto.contains("academico")
                || texto.contains("promedio")
                || texto.contains("notas")
                || texto.contains("certificado")) {

            SugerenciaIAResponse response = new SugerenciaIAResponse();

            response.setCategoria("CONSULTA_ACADEMICA");
            response.setPrioridad("BAJA");
            response.setConfianza(0.88);

            response.setExplicacion("""
                    La solicitud fue clasificada como CONSULTA_ACADEMICA
                    porque contiene términos relacionados
                    con información académica general.

                    Posibles causas:
                     consultas sobre notas,
                     promedio académico,
                     certificados,
                     información institucional.

                    Se recomienda revisar
                    el historial académico
                    y los servicios disponibles para estudiantes.
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
                para determinar automáticamente el tipo de solicitud.

                Se recomienda realizar una revisión manual.
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