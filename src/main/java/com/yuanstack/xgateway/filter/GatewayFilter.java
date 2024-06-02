package com.yuanstack.xgateway.filter;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway filter.
 *
 * @author Sylvan
 * @date 2024/06/02  22:46
 */
public interface GatewayFilter {

    Mono<Void> filter(ServerWebExchange exchange);
}
