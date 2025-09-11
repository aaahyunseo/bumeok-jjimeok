package com.example.bjjm.configuration;

import com.example.bjjm.authentication.AuthenticatedUserArgumentResolver;
import com.example.bjjm.authentication.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthenticatedUserArgumentResolver authenticatedUserArgumentResolver;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/users/**",
                                "/themes/**",
                                "/place-reviews",
                                "/puzzles/mission/**", "/puzzles/progress", "/puzzles/my-record",
                                "/home")
                .excludePathPatterns("/test", "/home/top3", "/home/theme/**", "/map", "/place-reviews/list", "/api/tour/**",
                        "/puzzles/ranking",
                        "/themes/list/**", "/themes/keyword/**", "/themes/*/comment/list" , "/themes/*/review/list", "/themes/today");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserArgumentResolver);
    }
}