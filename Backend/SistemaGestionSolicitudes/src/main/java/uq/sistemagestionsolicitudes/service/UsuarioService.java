package uq.sistemagestionsolicitudes.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uq.sistemagestionsolicitudes.dto.UsuarioResumenResponse;
import uq.sistemagestionsolicitudes.model.Usuario;
import uq.sistemagestionsolicitudes.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public List<UsuarioResumenResponse> encontrarResponsable() {
        return usuarioRepository.findResponsables()
                .stream()
                .map(u -> new UsuarioResumenResponse(u.getId(), u.getNombre(), u.getCorreo(), u.getRole()))
                .collect(Collectors.toList());
    }
}
