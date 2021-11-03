package com.systems.integrated.wineshopbackend.web.rest;

import com.systems.integrated.wineshopbackend.models.users.AuthToken;
import com.systems.integrated.wineshopbackend.models.users.DTO.JwtResponseDTO;
import com.systems.integrated.wineshopbackend.models.users.DTO.LoginDTO;
import com.systems.integrated.wineshopbackend.models.users.DTO.UserDTO;
import com.systems.integrated.wineshopbackend.security.JwtUtils;
import com.systems.integrated.wineshopbackend.service.impl.UserDetailsImpl;
import com.systems.integrated.wineshopbackend.service.intef.AuthTokenService;
import com.systems.integrated.wineshopbackend.service.intef.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthTokenService authTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String role = roles.get(0);

        return ResponseEntity.ok(new JwtResponseDTO(userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                jwt,
                role));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) throws MessagingException {
        userService.createUser(userDTO);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("authenticateToken/{token}")
    public ResponseEntity<?> authenticateToken(@PathVariable("token") String token) {
        boolean validateToken = authTokenService.validateToken(token);

        if (validateToken) {
            AuthToken authToken = authTokenService.findByToken(token);
            Long userId = authToken.getUser().getId();

            return new ResponseEntity<>(userId, HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
