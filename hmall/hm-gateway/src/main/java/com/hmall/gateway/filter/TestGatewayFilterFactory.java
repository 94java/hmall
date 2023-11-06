package com.hmall.gateway.filter;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-03  15:09
 * @Description: GatewayFilter 测试
 */
@Component
public class TestGatewayFilterFactory extends AbstractGatewayFilterFactory<TestGatewayFilterFactory.Config> {


    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter(new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                System.out.println("Gateway Filter doing....");
                System.out.println(config.getA());
                System.out.println(config.getB());
                System.out.println(config.getC());
                return chain.filter(exchange);
            }
        },1);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("a","b","c");
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    @Data
    public static class Config{
        int a;
        int b;
        int c;
    }
}
