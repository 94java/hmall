package com.hmall.gateway.routes;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-04  11:33
 * @Description: 动态路由配置
 */
@Component
@RequiredArgsConstructor
public class RouteConfigLoader {
    private final NacosConfigManager configManager;

    private final RouteDefinitionWriter writer;

    private final Set<String> routeIds = new HashSet<>();

    // 网关路由不支持 yaml，只支持 json
    private final static String DATA_ID = "gateway-routes.json";
    private final static String GROUP = "DEFAULT_GROUP";

    @PostConstruct
    public void initRouteConfiguration() throws NacosException {
        // 第一次启动时，拉取路由表，并添加监听器
        String configInfo = configManager.getConfigService().getConfigAndSignListener(DATA_ID, GROUP, 1000, new Listener() {
            @Override
            public Executor getExecutor() {
                return Executors.newSingleThreadExecutor();
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                // 监听到路由变更，更新路由表
                updateRouteConfigInfo(configInfo);
            }
        });
        // 写入路由表
        updateRouteConfigInfo(configInfo);
    }

    private void updateRouteConfigInfo(String configInfo) {
        // 解析路由信息
        List<RouteDefinition> routeDefinitions = JSONUtil.toList(configInfo, RouteDefinition.class);
        // 删除旧的路由（为了拿到路由 id，在每次更新的时候将这些id保存起来）
        for (String routeId : routeIds) {
            writer.delete(Mono.just(routeId)).subscribe();
        }
        routeIds.clear();

        // 判断是否有新路由
        if(CollUtil.isEmpty(routeDefinitions)){
            // 无新路由
            return;
        }

        // 更新路由表
        for (RouteDefinition routeDefinition : routeDefinitions) {
            // 保存本次路由id（便于下次删除）
            routeIds.add(routeDefinition.getId());
            // 写入路由表
            writer.save(Mono.just(routeDefinition)).subscribe();
        }
    }
}
