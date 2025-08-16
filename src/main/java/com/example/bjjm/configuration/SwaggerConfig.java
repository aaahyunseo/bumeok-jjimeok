package com.example.bjjm.configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String API_NAME = "전통시장 API";
    private static final String API_VERSION = "v1.0.0";
    private static final String API_DESCRIPTION = "2025 문화 데이터 활용 공모전 API 문서입니다.";

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title(API_NAME)
                        .version(API_VERSION)
                        .description(API_DESCRIPTION)
                );
    }
}
