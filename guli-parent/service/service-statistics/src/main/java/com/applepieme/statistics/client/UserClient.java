package com.applepieme.statistics.client;

import com.applepieme.statistics.client.impl.UserClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author applepieme
 * @date 2021/10/10 19:17
 */
@Component
@FeignClient(name = "service-user", fallback = UserClientImpl.class)
public interface UserClient {
    /**
     * 获取某一天的注册人数
     *
     * @param day 日期
     * @return 注册人数
     */
    @GetMapping("/user/member/register_num/{day}")
    Integer getRegisterNumByDay(@PathVariable("day") String day);
}
