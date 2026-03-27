package uq.sistemagestionsolicitudes.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import uq.sistemagestionsolicitudes.dto.LoginRequest;
import uq.sistemagestionsolicitudes.dto.LoginResponse;
import uq.sistemagestionsolicitudes.security.JwtService;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken(request.getCorreo());

        return new LoginResponse(token);
    }
}
