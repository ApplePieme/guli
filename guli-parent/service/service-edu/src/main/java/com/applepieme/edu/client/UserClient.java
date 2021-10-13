package com.applepieme.edu.client;

import com.applepieme.edu.client.impl.UserClientImpl;
import com.applepieme.util.R;
import com.applepieme.vo.MemberVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author applepieme
 * @date 2021/10/8 16:11
 */
@Component
@FeignClient(name = "service-user", fallback = UserClientImpl.class)
public interface UserClient {
    /**
     * 根据用户id获取用户信息
     *
     * @param id 用户id
     * @return 封装用户信息的结果对象
     */
    @GetMapping("/user/member/{id}")
    MemberVo getMemberVoById(@PathVariable("id") String id);
}
