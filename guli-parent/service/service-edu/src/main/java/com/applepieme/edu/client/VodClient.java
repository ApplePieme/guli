package com.applepieme.edu.client;

import com.applepieme.edu.client.impl.VodClientImpl;
import com.applepieme.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author applepieme
 * @date 2021/10/4 22:35
 */
@Component
@FeignClient(name = "service-vod", fallback = VodClientImpl.class)
public interface VodClient {
    /**
     * 根据 ID 删除视频
     *
     * @param id 视频 ID
     * @return web 结果对象
     */
    @DeleteMapping("/vod/video/{id}")
    R deleteVideoById(@PathVariable("id") String id);

    /**
     * 根据 ID 列表批量删除视频
     *
     * @param videoIdList 视频 ID 列表
     * @return web 结果对象
     */
    @DeleteMapping(value = "/vod/video/delete_batch")
    R removeVideoList(@RequestParam("videoIdList") List<String> videoIdList);
}
