package com.yuanstack.xgateway.plugin;

import com.yuanstack.xgateway.plugin.chain.GatewayPluginChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway plugin.
 *
 * @author Sylvan
 * @date 2024/06/02  22:24
 */
public interface GatewayPlugin {
    String GATEWAY_PREFIX = "/gw";

    void start();

    void stop();

    String getName();

    boolean support(ServerWebExchange exchange);

    Mono<Void> handle(ServerWebExchange exchange, GatewayPluginChain chain);
}
