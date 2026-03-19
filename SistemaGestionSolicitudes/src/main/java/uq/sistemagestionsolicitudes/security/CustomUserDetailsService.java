package uq.sistemagestionsolicitudes.security;

import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uq.sistemagestionsolicitudes.exception.ResourceNotFoundException;
import uq.sistemagestionsolicitudes.model.Usuario;
import uq.sistemagestionsolicitudes.repository.UsuarioRepository;

@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        String rol = usuario.getRole();

        return User.builder()
                .username(usuario.getNombre())
                .password(usuario.getPassword())
                .authorities(new SimpleGrantedAuthority(rol)).build();
    }
}
