package com.yuanstack.xgateway;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Sylvan
 * @date 2024/06/02  15:58
 */
@Component
public class HelloHandler {
    Mono<ServerResponse> handle(ServerRequest request) {

        String url = "http://localhost:8081/";
        String requestJson = """
                {
                    "service":"com.yuanstack.xrpc.demo.api.service.UserService",
                    "methodSign":"findById@1_java.lang.Integer",
                    "args":[100]
                }
                """;

        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .bodyValue(requestJson)
                .retrieve()
                .toEntity(String.class);

        return ServerResponse.ok()
                .header("Content-Type", "application/json")
                .header("XGATEWAY_VERSION", "1.0.0")
                .body(entity.map(ResponseEntity::getBody), String.class);
    }
}
