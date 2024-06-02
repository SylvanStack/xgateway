package com.yuanstack.xgateway;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Gateway Web Filter
 *
 * @author Sylvan
 * @date 2024/06/02  15:58
 */
@Component
public class GatewayWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("GatewayWebFilter");
        if (exchange.getRequest().getQueryParams().getFirst("mock") == null) {
            return chain.filter(exchange);
        }

        String mock = """
                {
                    "result": "mock"
                }
                """;
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
    }
}
