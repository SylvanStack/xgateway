package com.yuanstack.xgateway.config;

import com.yuanstack.xrpc.core.api.RegistryCenter;
import com.yuanstack.xrpc.core.registry.xregistry.XRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
