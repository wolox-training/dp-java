package wolox.training.security;

import java.util.Collections;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    public CustomAuthProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> user = userRepository.findByUsername(name);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        } else if (!user.get().validPassword(password)) {
            throw new BadCredentialsException("Provide password doesn't match user's password");
        } else {
            return new UsernamePasswordAuthenticationToken(name, password, Collections.emptyList());
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}