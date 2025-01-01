package com.example.apigateway.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient  //added for testing
public class EurekaClientConfig {
    // Additional Eureka client configurations can be added here if
}