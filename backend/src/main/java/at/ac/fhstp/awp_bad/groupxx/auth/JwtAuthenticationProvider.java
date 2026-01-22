package at.ac.fhstp.awp_bad.groupxx.auth;

import at.ac.fhstp.awp_bad.groupxx.entities.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final TokenUtil tokenUtil;

    public JwtAuthenticationProvider(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

        String jwt = jwtAuthenticationToken.getJwt();
        Optional<User> userOptional = tokenUtil.parseToken(jwt);

        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

        if(userOptional.isPresent()) {
            jwtAuthenticationToken = new JwtAuthenticationToken(grantedAuthorityList, userOptional.get(), jwt);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
        }

        return jwtAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }

}

