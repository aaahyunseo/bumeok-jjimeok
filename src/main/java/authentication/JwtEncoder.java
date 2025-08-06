package authentication;

import exception.UnauthorizedException;
import exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class JwtEncoder {
    public static final String TOKEN_TYPE = "Bearer_";

    public static String encode(String token) {
        return TOKEN_TYPE + token;
    }

    public static String decode(String cookieValue) {
        if (cookieValue.startsWith(TOKEN_TYPE)) {
            return cookieValue.substring(TOKEN_TYPE.length());
        }
        throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
    }
}
