package com.applepieme.cms.service;

import com.applepieme.cms.entity.Banner;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-05
 */
public interface BannerService extends IService<Banner> {
    /**
     * 查询所有 banner 根据排序返回前 2 条记录
     *
     * @return banner 列表
     */
    List<Banner> findAll();

    /**
     * 分页查询 banner
     *
     * @param page 分页对象
     */
    void queryPage(Page<Banner> page);
}
