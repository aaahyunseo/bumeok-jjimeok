package authentication;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class AuthenticationExtractor {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TYPE = "Bearer ";

    public static Optional<String> extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(BEARER_TYPE)) {
            return Optional.of(header.substring(BEARER_TYPE.length()));
        }
        return Optional.empty();
    }
}

