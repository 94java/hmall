package com.hmall.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-02  18:41
 * @Description: TODO
 */
@FeignClient(value = "trade-service")
public interface TradeClient {

    @PutMapping("/orders/{orderId}")
    void markOrderPaySuccess(@PathVariable("orderId") Long orderId);
}
