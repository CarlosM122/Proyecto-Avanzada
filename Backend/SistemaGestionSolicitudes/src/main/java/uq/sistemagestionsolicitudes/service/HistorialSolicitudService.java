package uq.sistemagestionsolicitudes.service;

import uq.sistemagestionsolicitudes.model.AccionSolicitud;

public interface HistorialSolicitudService {

    void registrarCambio(Long solicitudId, AccionSolicitud accion,Long solicitanteId, String anotacion);
}
