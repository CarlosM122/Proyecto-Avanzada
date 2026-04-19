package uq.sistemagestionsolicitudes.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uq.sistemagestionsolicitudes.dto.LoginRequest;
import uq.sistemagestionsolicitudes.dto.LoginResponse;
import uq.sistemagestionsolicitudes.dto.RegisterRequest;
import uq.sistemagestionsolicitudes.dto.RegisterResponse;
import uq.sistemagestionsolicitudes.model.Administrativo;
import uq.sistemagestionsolicitudes.model.Docente;
import uq.sistemagestionsolicitudes.model.Estudiante;
import uq.sistemagestionsolicitudes.model.Usuario;
import uq.sistemagestionsolicitudes.repository.UsuarioRepository;
import uq.sistemagestionsolicitudes.security.JwtService;

@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
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
    public RegisterResponse register(RegisterRequest request) {

        if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        Usuario usuario;

        switch (request.getRole()) {

            case "ESTUDIANTE":
                usuario = crearEstudiante(request);
                break;

            case "ADMINISTRATIVO":
                usuario = crearAdministrativo(request);
                break;

            case "DOCENTE":
                usuario = crearDocente(request);;
                break;

            default:
                throw new RuntimeException("Rol no válido");
        }

        usuario.setCorreo(request.getCorreo());
        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        String token = jwtService.generateToken(request.getCorreo());

        usuarioRepository.save(usuario);
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setContrato(request.getTipoContrato());
        registerResponse.setCorreo(request.getCorreo());
        registerResponse.setToken(token);
        return registerResponse;
    }

    private Usuario crearDocente(RegisterRequest request) {
        Usuario usuario = new Docente(request.getTipoContrato());
        usuario.setCorreo(request.getCorreo());
        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        return usuario;
    }

    private Usuario crearAdministrativo(RegisterRequest request) {
        Usuario usuario = new Administrativo(request.getAreaEncargada(),request.getTipoContrato());
        usuario.setCorreo(request.getCorreo());
        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        return usuario;

    }

    private Usuario crearEstudiante(RegisterRequest request) {
        Usuario usuario = new Estudiante(request.getSemestre());
        usuario.setCorreo(request.getCorreo());
        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        return usuario;
    }
}
