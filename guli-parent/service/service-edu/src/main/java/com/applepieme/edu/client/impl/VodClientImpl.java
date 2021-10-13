package com.applepieme.edu.client.impl;

import com.applepieme.edu.client.VodClient;
import com.applepieme.util.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author applepieme
 * @date 2021/10/5 11:05
 */
@Component
public class VodClientImpl implements VodClient {
    @Override
    public R deleteVideoById(String id) {
        return R.error().message("视频服务异常，删除失败");
    }

    @Override
    public R removeVideoList(List<String> videoIdList) {
        return R.error().message("视频服务异常，删除失败");
    }
}
