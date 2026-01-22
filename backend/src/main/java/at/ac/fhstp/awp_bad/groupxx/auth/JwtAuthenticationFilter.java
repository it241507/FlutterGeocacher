package at.ac.fhstp.awp_bad.groupxx.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenBlacklist tokenBlacklist;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TokenBlacklist tokenBlacklist) {
        this.authenticationManager = authenticationManager;
        this.tokenBlacklist = tokenBlacklist;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;

        if(authHeader != null && authHeader.split(" ").length >= 2){
            token = (authHeader.split(" ")[1]).trim();
        }

        if(token != null && !token.isEmpty() && !token.equalsIgnoreCase("undefined")){
            if (tokenBlacklist.isBlacklisted(token)){
                token="";
            }
            authenticationManager.authenticate(new JwtAuthenticationToken(token));
        }

        filterChain.doFilter(request, response);

    }
}

