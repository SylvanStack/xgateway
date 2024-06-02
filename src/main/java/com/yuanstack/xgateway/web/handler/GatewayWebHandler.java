package com.yuanstack.xgateway.web.handler;

import com.yuanstack.xgateway.filter.GatewayFilter;
import com.yuanstack.xgateway.plugin.chain.DefaultGatewayPluginChain;
import com.yuanstack.xgateway.plugin.GatewayPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Gateway Web Handler
 *
 * @author Sylvan
 * @date 2024/06/02  15:58
 */
@Component("gatewayWebHandler")
public class GatewayWebHandler implements WebHandler {

    @Autowired
    List<GatewayPlugin> plugins;

    @Autowired
    List<GatewayFilter> filters;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        System.out.println(" ====> XGateway web handler ... ");

        if (plugins == null || plugins.isEmpty()) {
            String mock = """
                    {"result":"no plugin"}
                    """;
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
        }

        for (GatewayFilter filter : filters) {
            filter.filter(exchange);
        }

        return new DefaultGatewayPluginChain(plugins).handle(exchange);
    }
}
