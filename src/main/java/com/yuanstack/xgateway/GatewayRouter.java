package com.yuanstack.xgateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author Sylvan
 * @date 2024/06/02  15:52
 */
@Component
public class GatewayRouter {

    @Autowired
    private HelloHandler HelloHandler;
    @Autowired
    private GatewayHandler gatewayHandler;

    @Bean
    public RouterFunction<?> helloRouterFunction() {
        return route(GET("/hello"), request -> HelloHandler.handle(request));
    }

    @Bean
    public RouterFunction<?> gatewayRouterFunction() {
        return route(GET("/gw").or(POST("/gw/**")), request -> gatewayHandler.handle(request));
    }
}
