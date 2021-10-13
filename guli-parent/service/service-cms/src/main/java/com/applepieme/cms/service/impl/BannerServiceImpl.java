package com.applepieme.cms.service.impl;

import com.applepieme.cms.entity.Banner;
import com.applepieme.cms.mapper.BannerMapper;
import com.applepieme.cms.service.BannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-05
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
    @Cacheable(cacheNames = {"index"}, key = "'bannerList'")
    @Override
    public List<Banner> findAll() {
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort");
        queryWrapper.last("limit 2");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void queryPage(Page<Banner> page) {
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort");
        baseMapper.selectPage(page, queryWrapper);
    }
}
