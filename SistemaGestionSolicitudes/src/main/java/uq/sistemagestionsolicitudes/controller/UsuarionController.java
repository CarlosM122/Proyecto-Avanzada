package uq.sistemagestionsolicitudes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uq.sistemagestionsolicitudes.dto.RegisterRequest;
import uq.sistemagestionsolicitudes.dto.UsuarioResumenResponse;
import uq.sistemagestionsolicitudes.service.AuthService;
import uq.sistemagestionsolicitudes.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarionController {

    private final UsuarioService usuarioService;
    private final AuthService authService;

    @GetMapping("/responsables")
    @PreAuthorize("hasAuthority('ROL_ADMINISTRATIVO')")
    public List<UsuarioResumenResponse> getResponsables() {
        return usuarioService.encontrarResponsable();
    }

    @PostMapping("/crear")
    @PreAuthorize("hasAuthority('ROL_ADMINISTRATIVO')")
    public ResponseEntity<String> crearUsuario(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Usuario creado exitosamente");
    }
}
