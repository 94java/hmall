package com.hmall.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-03  19:06
 * @Description: cart 配置类
 */
@Component
@ConfigurationProperties(prefix = "hm.cart")
@Data
public class CartProperties {
    private int maxSize;
}
