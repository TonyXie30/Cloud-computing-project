package com.whitepoor.auth.config;

import com.whitepoor.auth.utils.JwtAuthGatewayFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthGatewayFilter jwtAuthGatewayFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_service", r -> r.path("/auth/**")
                        .uri("http://auth-service:8080"))
                .route("login_service", r -> r.path("/login/**")
                        .uri("http://login-service:8080"))
                .route("main_service", r -> r.path("/main/**")
                        .filters(f -> f.filter(jwtAuthGatewayFilter))
                        .uri("http://main-service:8080"))
                .build();
    }
}

