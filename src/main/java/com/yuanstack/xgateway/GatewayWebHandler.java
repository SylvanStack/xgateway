package com.yuanstack.xgateway;

import com.yuanstack.xrpc.core.api.Loadbalancer;
import com.yuanstack.xrpc.core.api.RegistryCenter;
import com.yuanstack.xrpc.core.cluster.RoundRibonLoadbalancer;
import com.yuanstack.xrpc.core.meta.InstanceMeta;
import com.yuanstack.xrpc.core.meta.ServiceMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Flux;
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
    RegistryCenter registryCenter;
    Loadbalancer<InstanceMeta> loadbalancer = new RoundRibonLoadbalancer<>();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        System.out.println("GatewayWebHandler handle");

        // 1. 通过请求路径或者服务名
        String service = exchange.getRequest().getPath().value().substring(4);
        ServiceMeta serviceMeta = ServiceMeta.builder()
                .name(service)
                .app("app1")
                .env("dev")
                .namespace("public").build();

        // 2. 通过注册中心拿到活着的获取服务实例
        List<InstanceMeta> instanceMetas = registryCenter.fetchAll(serviceMeta);
        InstanceMeta instanceMeta = loadbalancer.choose(instanceMetas);
        // 3. 先简化处理，通过实例信息拿到服务地址
        String url = instanceMeta.toUrl();
        // 4. 通过请求体拿到请求参数
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();
        // 5. 通过WebClient发送请求
        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .body(requestBody, DataBuffer.class)
                .retrieve()
                .toEntity(String.class);
        Mono<String> responseBody = entity.map(ResponseEntity::getBody);
        // 6. 返回响应
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("XGATEWAY_VERSION", "v1.0.0");
        return responseBody.flatMap(x ->
                exchange.getResponse().writeWith(
                        Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))));
    }
}
