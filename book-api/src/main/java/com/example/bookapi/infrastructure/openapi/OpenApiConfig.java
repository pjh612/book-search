package com.example.bookapi.infrastructure.openapi;

import com.example.bookapi.common.exception.converter.ExceptionCodeConverter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI bookApi() {
        return new OpenAPI()
                .info(new Info().title("Book API")
                        .description("Trevari Book Search - REST API")
                        .version("v1")
                        .contact(new Contact().name("Book API Team").email("api@example.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addServersItem(new Server().url("http://localhost:8082").description("Local"))
                .externalDocs(new ExternalDocumentation()
                        .description("API Docs")
                        .url("/docs/api.md"));
    }

    @Bean
    public OperationCustomizer customize(ExceptionCodeConverter exceptionCodeConverter) {
        return new ErrorResponseOperationCustomizer(exceptionCodeConverter);
    }
}
