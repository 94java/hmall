package com.hmall.gateway.filter;

import cn.hutool.core.collection.CollUtil;
import com.hmall.common.exception.UnauthorizedException;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-03  15:34
 * @Description: 全局网关登录过滤器
 */
@Component
@RequiredArgsConstructor
public class LoginGlobalFilter implements GlobalFilter, Ordered {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final AuthProperties authProperties;
    private final JwtTool jwtTool;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取 Request
        ServerHttpRequest request = exchange.getRequest();
        // 判断当前请求是否需要被拦截
        if(isAllowPath(request)){
            // 无需拦截
            return chain.filter(exchange);
        }
        // 获取token
        String token = null;
        List<String> headers = request.getHeaders().get("authorization");
        if(CollUtil.isNotEmpty(headers)){
            token = headers.get(0);
        }
        // 解析token
        Long userId = null;
        try {
            userId = jwtTool.parseToken(token);
        } catch (Exception e) {
            ServerHttpResponse response = exchange.getResponse();
            // 设置状态为未授权
            response.setRawStatusCode(401);
            // 结束后续的请求传递
            return response.setComplete();
        }
        System.out.println("userId = " + userId);

        // 传递用户信息到下游服务
        String userInfo = userId.toString();
        ServerWebExchange exec = exchange.mutate()
                .request(builder -> builder.header("user-info", userInfo))
                .build();
        // 放行
        return chain.filter(exec);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * @author: HelloCode.
     * @date: 2023/11/3 15:36
     * @param: request
     * @return: boolean
     * @description: 判断是否是放行路径
     */
    private boolean isAllowPath(ServerHttpRequest request){
        boolean flag = false;
        // 请求路径
        String path = request.getPath().toString();
        // 判断是否要放行
        for (String excludePath : authProperties.getExcludePaths()) {
            boolean match = pathMatcher.match(excludePath, path);
            if(match){
                // 放行
                flag = true;
                break;
            }
        }
        return flag;
    }
}
