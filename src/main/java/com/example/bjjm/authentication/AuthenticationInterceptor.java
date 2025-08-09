package com.example.bjjm.authentication;

import com.example.bjjm.entity.User;
import com.example.bjjm.exception.NotFoundException;
import com.example.bjjm.exception.errorcode.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.example.bjjm.repository.UserRepository;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;
    private final AccessTokenProvider accessTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return true;
        }

        String accessToken = authorizationHeader.substring(7);

        try {
            UUID userId = UUID.fromString(accessTokenProvider.getPayload(accessToken));
            User user = findExistingUser(userId);
            authenticationContext.setPrincipal(user);
        } catch (Exception e) {
            log.warn("Invalid token during authentication: {}", e.getMessage());
            throw new NotFoundException(ErrorCode.INVALID_TOKEN);
        }
        return true;
    }

    private User findExistingUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
