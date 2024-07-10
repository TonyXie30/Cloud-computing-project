package com.whitepoor.auth.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config> {

//    @Autowired
//    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            String username = null;
            String jwt = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtil.validateToken(jwt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//
//                if (jwtUtil.validateToken(jwt).equals(username)) {
//                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                            userDetails, null, userDetails.getAuthorities());
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                } else {
//                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                    return exchange.getResponse().setComplete();
//                }
            } else if (username == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
        // 配置类可以包含需要的参数
    }
}

