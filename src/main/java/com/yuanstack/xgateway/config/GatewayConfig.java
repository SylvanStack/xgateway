package com.yuanstack.xgateway.config;

import com.yuanstack.xrpc.core.api.RegistryCenter;
import com.yuanstack.xrpc.core.registry.xregistry.XRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

/**
 * gateway config.
 *
 * @author Sylvan
 * @date 2024/06/02  16:31
 */
@Configuration
public class GatewayConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RegistryCenter consumerRegistryCenter() {
        return new XRegistryCenter();
    }

    @Bean
    ApplicationRunner runner(@Autowired ApplicationContext context) {
        return args -> {
            SimpleUrlHandlerMapping handlerMapping = context.getBean(SimpleUrlHandlerMapping.class);
            Properties mappings = new Properties();
            mappings.put("/gw/**", "gatewayWebHandler");
            handlerMapping.setMappings(mappings);
            handlerMapping.initApplicationContext();
            System.out.println("xrpc gateway start");
        };
    }
}
