package com.example.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            logRequest(exchange.getRequest());
            return chain.filter(exchange).then(Mono.fromRunnable(() -> logResponse(exchange.getResponse())));
        };
    }

    private void logRequest(ServerHttpRequest request) {
        System.out.println("Request: " + request.getMethod() + " " + request.getURI());
    }

    private void logResponse(ServerHttpResponse response) {
        System.out.println("Response: " + response.getStatusCode());
    }

    public static class Config {
        // Configuration properties if needed
    }
}