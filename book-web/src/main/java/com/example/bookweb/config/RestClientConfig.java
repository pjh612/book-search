package com.example.bookweb.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient restClient(RestClient.Builder builder, @Value("${client.book-api.url}") String baseUrl) {
        ClientHttpRequestInterceptor authInterceptor = (request, body, execution) -> {
            try {
                ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attrs != null) {
                    HttpServletRequest httpReq = attrs.getRequest();
                    String incomingAuth = httpReq.getHeader(HttpHeaders.AUTHORIZATION);
                    if (incomingAuth != null && !incomingAuth.isBlank()) {
                        request.getHeaders().set(HttpHeaders.AUTHORIZATION, incomingAuth);
                    }
                }
            } catch (Exception ignored) { }
            return execution.execute(request, body);
        };

        return builder
                .baseUrl(baseUrl)
                .requestInterceptor(authInterceptor)
                .build();
    }
}
