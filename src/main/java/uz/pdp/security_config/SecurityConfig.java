package uz.pdp.security_config;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;
import uz.pdp.dto.ErrorDto;
import uz.pdp.service.CustomUserDetailsService;

import java.io.IOException;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenGenerator jwtTokenGenerator;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
                        httpSecurityCsrfConfigurer.disable();
                    }
                })
                .cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
                    }
                })
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable()))
                .userDetailsService(customUserDetailsService)
                .authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<org.springframework.security.config.annotation.web.builders.HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
                    @Override
                    public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(
                                        "/api/auth/**",
                                        "/h2-console/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html")
                                .permitAll()
                                .anyRequest()
                                .authenticated();
                    }
                })
                .exceptionHandling(new Customizer<ExceptionHandlingConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(ExceptionHandlingConfigurer<HttpSecurity> httpSecurityExceptionHandlingConfigurer) {
                        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new AuthenticationEntryPoint() {
                            @Override
                            public void commence(@NonNull HttpServletRequest request,
                                                 @NonNull HttpServletResponse response,
                                                 @NonNull AuthenticationException exception) throws IOException, ServletException {
                                ErrorDto error = ErrorDto.builder()
                                        .error_message(exception.getMessage())
                                        .error_path(request.getRequestURI())
                                        .error_code(HttpStatus.UNAUTHORIZED.value())
                                        .build();
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                ServletOutputStream outputStream = response.getOutputStream();
                                objectMapper.writeValue(outputStream, error);
                                log.error("Bad credentials", exception);
                                System.out.println("-------------- UNAUTHORIZED ---------------");
                            }
                        });
                        httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(new AccessDeniedHandler() {
                            @Override
                            public void handle(@NonNull HttpServletRequest request,
                                               @NonNull HttpServletResponse response,
                                               @NonNull AccessDeniedException exception) throws IOException, ServletException {
                                ErrorDto error = ErrorDto.builder()
                                        .error_message(exception.getMessage())
                                        .error_path(request.getRequestURI())
                                        .error_code(403)
                                        .build();
                                response.setStatus(403);
                                ServletOutputStream outputStream = response.getOutputStream();
                                objectMapper.writeValue(outputStream, error);
                                log.error("Access denied", exception);
                                System.out.println("--------------ACCESS DENIED--------------");
                            }
                        });
                    }
                }).sessionManagement(new Customizer<SessionManagementConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(SessionManagementConfigurer<HttpSecurity> httpSecuritySessionManagementConfigurer) {
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    }
                })
                .addFilterBefore(new JwtTokenFilter(jwtTokenGenerator, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(
                List.of("http://localhost:8080",
                        "http://localhost:9090",
                        "http://localhost:9595")
        );
        configuration.setAllowedMethods(
                // List.of("*"); // har qandey method
                List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(
                List.of("*")); // har qandey header
//                List.of(
//                "Authorization",
//                "Cache-Control",
//                "Content-Type",
//                "Accept"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
//        source.registerCorsConfiguration("/api/version-1", configuration1);
//        source.registerCorsConfiguration("/api/version-2", configuration2);
        return source;
    }
}
