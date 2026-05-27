package uq.sistemagestionsolicitudes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uq.sistemagestionsolicitudes.dto.UsuarioResumenResponse;
import uq.sistemagestionsolicitudes.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarionController {

    private final UsuarioService usuarioService;

    @GetMapping("/responsables")
    @PreAuthorize("hasAuthority('ROL_ADMINISTRATIVO')")
    public List<UsuarioResumenResponse> getResponsables() {
        return usuarioService.encontrarResponsable();
    }
}
