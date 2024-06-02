package com.yuanstack.xgateway.plugin.chain;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Gateway Plugin Chain
 *
 * @author Sylvan
 * @date 2024/06/02  22:24
 */
public interface GatewayPluginChain {

    Mono<Void> handle(ServerWebExchange exchange);
}
