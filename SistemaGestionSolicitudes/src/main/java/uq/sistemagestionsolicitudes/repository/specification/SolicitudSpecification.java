package uq.sistemagestionsolicitudes.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import uq.sistemagestionsolicitudes.model.Estado;
import uq.sistemagestionsolicitudes.model.Prioridad;
import uq.sistemagestionsolicitudes.model.Solicitud;
import uq.sistemagestionsolicitudes.model.TipoSolicitud;

public class SolicitudSpecification {

    public static Specification<Solicitud> conFiltros(
            Estado estado,
            TipoSolicitud tipo,
            Prioridad prioridad,
            Long responsableId
    ) {
        return (root, query, cb) -> {

            var predicates = cb.conjunction();

            if (estado != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("estado"), estado));
            }

            if (tipo != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("tipoSolicitud"), tipo));
            }

            if (prioridad != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("prioridad"), prioridad));
            }

            if (responsableId != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("responsable").get("id"), responsableId));
            }

            return predicates;
        };
    }
}