package com.example.order_management;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    void testThatPasses() {
        assertTrue(true);
    }

    @Test
    void getItemsInShoppingCartWithCorrectUserIdPasses() {
        String requestBody = """
                {
                    "customerId": 1
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/shoppingCart/1",
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getItemsInShoppingCartWithIncorrectUserIdFails() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/shoppingCart/2",
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getItemsInShoppingCartWithNoUserIdFails() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/shoppingCart",
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAllOrdersByAdminPasses() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/orders",
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllOrdersByUserFails() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/orders",
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void deleteOrderByIdByAdminPasses() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/orders/1",
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteOrderByIdByUserFails() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/orders/1",
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void deleteOrderByIdWithIncorrectIdFails() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/orders/2",
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteOrderByIdWithNoIdFails() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/orders",
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateOrderByIdByAdminPasses() {
        String requestBody = """
                {
                      "orderStatus": "Shipped",
                      "deliveryPerson": "Sarah Williams"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/orders/1",
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateOrderByIdByUserFails() {
        String requestBody = """
                {
                      "orderStatus": "Shipped",
                      "deliveryPerson": "Sarah Williams"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/orders/1",
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void updateOrderByIdWithIncorrectIdFails() {
        String requestBody = """
                {
                      "orderStatus": "Shipped",
                      "deliveryPerson": "Sarah Williams"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/orders/2",
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateOrderByIdWithNoIdFails() {
        String requestBody = """
                {
                      "orderStatus": "Shipped",
                      "deliveryPerson": "Sarah Williams"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/orders",
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
