package uz.pdp.service;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import uz.pdp.dto.auth_dto.AuthUserRequestDto;
import uz.pdp.dto.auth_dto.TokenRequestDto;
import uz.pdp.entity.AuthUser;
import uz.pdp.entity.Role;
import uz.pdp.enums.RoleName;
import uz.pdp.exception_handling.ItemNotFoundException;
import uz.pdp.mapper.CustomMapper;
import uz.pdp.repository.AuthUserRepository;
import uz.pdp.repository.RoleRepository;
import uz.pdp.security_config.JwtTokenGenerator;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final CustomMapper customMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenGenerator jwtTokenGenerator;
    //private final Logger log = LoggerFactory.getLogger(AuthUserService.class);


    public ResponseEntity<@NonNull String> register(AuthUserRequestDto dto) {

        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ItemNotFoundException("Role not found"));
        AuthUser authUser = customMapper.toAuthUser(dto);
        authUser.setRoles(Set.of(role));

        authUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        authUserRepository.save(authUser);
        return ResponseEntity.ok().body("You have successfully registered");
    }

    public ResponseEntity<@NonNull String> login(@RequestBody @Valid TokenRequestDto dto) {
        String password = dto.getPassword();
        String username = dto.getUsername();

        try {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, password);

            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtTokenGenerator.generateJwtToken(username);
        log.info("{} named user got token", username);
        return ResponseEntity.ok().body(token);
    }
}
