package uq.sistemagestionsolicitudes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uq.sistemagestionsolicitudes.dto.LoginRequest;
import uq.sistemagestionsolicitudes.dto.LoginResponse;
import uq.sistemagestionsolicitudes.dto.RegisterRequest;
import uq.sistemagestionsolicitudes.dto.RegisterResponse;
import uq.sistemagestionsolicitudes.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {

        return authService.register(request);
    }
}
