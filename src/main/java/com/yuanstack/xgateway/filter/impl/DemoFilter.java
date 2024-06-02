package com.yuanstack.xgateway.filter.impl;

import com.yuanstack.xgateway.filter.GatewayFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Demo filter.
 *
 * @author Sylvan
 * @date 2024/06/02  22:53
 */
@Component("demoFilter")
public class DemoFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange) {
        System.out.println(" ===>>> filters: demo filter ...");
        exchange.getRequest().getHeaders().toSingleValueMap()
                .forEach((k, v) -> System.out.println(k + ":" + v));
        return Mono.empty();
    }
}
