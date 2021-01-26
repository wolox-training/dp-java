package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/security")
@Api(tags = "Security")
public class SecurityController {

    private final UserRepository userRepository;

    public SecurityController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method return current user authenticated
     *
     * @return {@link User}
     */
    @GetMapping("/username")
    @ApiOperation(value = "Authenticated user's username", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Not Authorized"),
            @ApiResponse(code = 403, message = "Access forbidden")
    })
    public User currentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("User not logged");
        }

        return userRepository.findByUsername(authentication.getName()).orElseThrow(
                UserNotFoundException::new);
    }
}
