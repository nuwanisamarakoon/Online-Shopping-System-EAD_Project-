# API Gateway

This is the API Gateway for the Online Shopping System. It acts as a single entry point for all client requests, routing them to the appropriate backend services.

## Features

- **Routing**: Directs incoming requests to the appropriate microservices.
- **Load Balancing**: Distributes incoming traffic across multiple instances of microservices.
- **Security**: Provides authentication and authorization mechanisms.
- **Rate Limiting**: Controls the rate of incoming requests to prevent abuse.
- **Monitoring**: Tracks and logs requests for monitoring and debugging purposes.

## Prerequisites

- Java Development Kit (JDK)
- Apache Maven

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/OnlineShoppingSystem.git
    ```
2. Navigate to the API Gateway directory:
    ```bash
    cd OnlineShoppingSystem/backend/api-gateway
    ```
3. Build the project:
    ```bash
    mvn clean install
    ```

## Usage

1. Start the API Gateway:
    ```bash
    mvn spring-boot:run
    ```
2. The API Gateway will be running on `http://localhost:8080`.


## Docker

### Build Docker Image

To build the Docker image for the API Gateway, run the following command in the project root directory:

```bash
docker build -t api-gateway .
```

### Run Docker Container

To run the Docker container, use the following command:

```bash
docker run -p 8080:8080 api-gateway
```

This will start the API Gateway in a Docker container and map port 8080 of the container to port 8080 on your host machine.

## Configuration

Configuration options can be found in the `src/main/resources` directory. Modify these files to change settings such as port numbers, service endpoints, and security options.

## Contributing

Contributions are welcome! Please read the [contributing guidelines](../CONTRIBUTING.md) first.

## License

This project is licensed under the MIT License. See the [LICENSE](../LICENSE) file for details.

## Contact

For any questions or issues, please open an issue on GitHub or contact the maintainers.

