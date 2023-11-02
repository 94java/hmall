package com.hmall.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-02  18:41
 * @Description: TODO
 */
@FeignClient(value = "user-service")
public interface UserClient {

    @PutMapping("/money/deduct")
    public void deductMoney(@RequestParam("pw") String pw,@RequestParam("amount") Integer amount);
}
