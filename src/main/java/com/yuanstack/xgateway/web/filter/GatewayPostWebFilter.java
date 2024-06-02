package com.yuanstack.xgateway.web.filter;

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
public class GatewayPostWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).doFinally(signal -> {
            System.out.println("GatewayPostWebFilter.filter");
            System.out.println(exchange.getRequest().getPath());
            System.out.println(exchange.getResponse().getHeaders());
            System.out.println(exchange.getResponse().getStatusCode());
            System.out.println(exchange.getAttributes());

        });
    }
}
