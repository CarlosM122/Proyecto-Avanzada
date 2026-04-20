package uq.sistemagestionsolicitudes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uq.sistemagestionsolicitudes.exception.AccessDeniedException;
import uq.sistemagestionsolicitudes.model.Estudiante;
import uq.sistemagestionsolicitudes.model.Solicitud;
import uq.sistemagestionsolicitudes.repository.SolicitudRepository;
import uq.sistemagestionsolicitudes.repository.UsuarioRepository;
import uq.sistemagestionsolicitudes.service.HistorialSolicitudService;
import uq.sistemagestionsolicitudes.service.SolicitudService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HistorialSolicitudService historialService;

    @InjectMocks
    private SolicitudService solicitudService;

    @Test
    void estudianteNoPuedeVerSolicitudAjena() {

        // ARRANGE — preparamos los datos falsos

        // Creamos el estudiante que está autenticado (id=1)
        Estudiante estudianteAutenticado = new Estudiante();
        estudianteAutenticado.setId(1L);

        // Creamos otro estudiante dueño de la solicitud (id=2)
        Estudiante otroEstudiante = new Estudiante();
        otroEstudiante.setId(2L);

        // Creamos la solicitud que pertenece al otro estudiante
        Solicitud solicitud = new Solicitud();
        solicitud.setId(10L);
        solicitud.setSolicitante(otroEstudiante); // dueño es id=2

        // Le decimos al mock: cuando busquen la solicitud 10, devuelve esta
        when(solicitudRepository.findById(10L))
                .thenReturn(Optional.of(solicitud));

        // Simula que hay un usuario autenticado en el SecurityContext
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "estudiante@correo.com", null, List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Le decimos al mock: cuando busquen un usuario por correo, devuelve el autenticado (id=1)
        when(usuarioRepository.findByCorreo("estudiante@correo.com"))
                .thenReturn(Optional.of(estudianteAutenticado));

        // ACT y ASSERT — ejecutamos y verificamos que lance la excepción
        // El estudiante id=1 intenta ver la solicitud del estudiante id=2
        // debe lanzar AccessDeniedException porque no es suya
        assertThrows(AccessDeniedException.class, () -> {
            solicitudService.obtenerSolicitudId(10L);
        });

        SecurityContextHolder.clearContext();
    }
}
