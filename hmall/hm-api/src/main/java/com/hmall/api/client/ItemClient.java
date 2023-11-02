package com.hmall.api.client;

import com.hmall.api.config.DefaultFeignConfig;
import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-02  18:41
 * @Description: TODO
 */
@FeignClient(value = "item-service")
public interface ItemClient {
    @GetMapping("/items")
    List<ItemDTO> queryItemByIds(@RequestParam("ids") Collection<Long> ids);

    @PutMapping("/stock/deduct")
    public void deductStock(@RequestBody List<OrderDetailDTO> items);
}
