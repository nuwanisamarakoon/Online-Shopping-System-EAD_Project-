package com.example.apigateway.filter;

import com.example.apigateway.Dto.TokenVerificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Component
public class TokenVerificationFilter extends AbstractGatewayFilterFactory<TokenVerificationFilter.Config> {

    @Autowired
    private WebClient.Builder webClientBuilder;

    // Endpoint for verifying the token
    @Value("${token.verification.url}")
    private String VERIFY_TOKEN;

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/auth/.*", "/public/.*", "/users/forgot-password",
            "/users/forgot-password/verify", "/users/verify-email"
    );

    public TokenVerificationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            // Skip excluded paths
            if (EXCLUDED_PATHS.stream().anyMatch(path::matches)) {
                return chain.filter(exchange);
            }

            // Check for Authorization header
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer " prefix
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Validate the token and append user details to the query parameters
            return webClientBuilder.build()
                    .post()
                    .uri(VERIFY_TOKEN)
                    .bodyValue(Collections.singletonMap("token", token))
                    .retrieve()
                    .bodyToMono(TokenVerificationResponse.class)
                    .flatMap(response -> {
                        if (response != null && response.getStatusCode() == 200) {
                            // Modify the request URI with additional query parameters
                            URI originalUri = exchange.getRequest().getURI();
                            URI modifiedUri = UriComponentsBuilder.fromUri(originalUri)
                                    .queryParam("userId", response.getUserId())
                                    .queryParam("role", response.getRole())
                                    .queryParam("accStatus", response.getAccStatus())
                                    .build(true)
                                    .toUri();

                            // Create a new request with the modified URI
                            var mutatedRequest = exchange.getRequest().mutate()
                                    .uri(modifiedUri)
                                    .build();

                            // Continue with the filter chain using the mutated request
                            return chain.filter(exchange.mutate().request(mutatedRequest).build());
                        } else {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                    })
                    .onErrorResume(e -> {
                        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                        return exchange.getResponse().setComplete();
                    });
        };
    }

    public static class Config {
        // Configuration properties if needed
    }



}