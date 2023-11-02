package com.hmall.api.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-02  19:31
 * @Description: TODO
 */
public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
