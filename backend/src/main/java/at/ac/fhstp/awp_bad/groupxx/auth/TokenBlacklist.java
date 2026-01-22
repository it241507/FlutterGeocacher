package at.ac.fhstp.awp_bad.groupxx.auth;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {

    private final List<String> blacklist = new ArrayList<>() {
    };

    public void blacklistTokens(String token) {
        this.blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return this.blacklist.contains(token);
    }
}
