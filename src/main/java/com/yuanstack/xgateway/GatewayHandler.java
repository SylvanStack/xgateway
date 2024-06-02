package com.yuanstack.xgateway;

import com.yuanstack.xrpc.core.api.Loadbalancer;
import com.yuanstack.xrpc.core.api.RegistryCenter;
import com.yuanstack.xrpc.core.cluster.RoundRibonLoadbalancer;
import com.yuanstack.xrpc.core.meta.InstanceMeta;
import com.yuanstack.xrpc.core.meta.ServiceMeta;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Sylvan
 * @date 2024/06/02  15:58
 */
@Component
public class GatewayHandler {

    @Autowired
    RegistryCenter registryCenter;
    Loadbalancer<InstanceMeta> loadbalancer = new RoundRibonLoadbalancer<>();

    Mono<ServerResponse> handle(ServerRequest request) {
        // 1. 通过请求路径或者服务名
        String service = request.path().substring(4);
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
        Mono<String> requestMono = request.bodyToMono(String.class);
        return requestMono.flatMap(x -> invokeFromRegistry(x, url));
    }


    private static @NotNull Mono<ServerResponse> invokeFromRegistry(String x, String url) {
        // 5. 通过WebClient发送请求
        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .bodyValue(x)
                .retrieve()
                .toEntity(String.class);
        // 6. 返回响应
        return ServerResponse.ok()
                .header("Content-Type", "application/json")
                .header("XGATEWAY_VERSION", "1.0.0")
                .body(entity.map(ResponseEntity::getBody), String.class);
    }
}
