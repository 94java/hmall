package com.hmall.api.interceptor;

import com.hmall.common.utils.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-03  17:59
 * @Description: OpenFeign 用户信息拦截器
 */
public class UserInfoInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long userId = UserContext.getUser();
        if(userId != null){
            requestTemplate.header("user-info",userId.toString());
        }
    }

}
