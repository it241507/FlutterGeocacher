package at.ac.fhstp.awp_bad.groupxx.auth;

import at.ac.fhstp.awp_bad.groupxx.entities.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final User user;
    private final String jwt;

    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities,
                                  User user,
                                  String jwt) {
        super(authorities);
        this.user = user;
        this.jwt = jwt;
        setAuthenticated(true);
    }

    public JwtAuthenticationToken(String jwt) {
        this(null, null, jwt);
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    public String getJwt(){
        return jwt;
    }
}
