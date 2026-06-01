package uz.pdp.security_config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.service.CustomUserDetails;
import uz.pdp.service.CustomUserDetailsService;

import java.io.IOException;
import java.util.Enumeration;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

//        System.out.println("================================");
//        Enumeration<String> names = request.getHeaderNames();
//
//        while (names.hasMoreElements()) {
//            String name = names.nextElement();
//            System.out.println(name + " = " + request.getHeader(name));
//        }
//        System.out.println("================================");

        String authorization = request.getHeader("Authorization");
        System.out.println("authorization = " + authorization);
        if (authorization == null || authorization.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);
        if (!jwtTokenGenerator.isValidToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtTokenGenerator.username(token);
        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);
        authenticationToken.setDetails(details);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
