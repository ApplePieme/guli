package com.applepieme.cms.controller;


import com.applepieme.cms.entity.Banner;
import com.applepieme.cms.service.BannerService;
import com.applepieme.util.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author applepieme
 * @since 2021-10-05
 */
@RestController
@RequestMapping("/cms/banner")
public class BannerController {
    @Resource
    private BannerService bannerService;

    @ApiOperation(value = "首页查询所有")
    @GetMapping("")
    public R findAll() {
        List<Banner> bannerList = bannerService.findAll();
        return R.ok().data("items", bannerList);
    }

    @CacheEvict(cacheNames = {"index"}, allEntries = true)
    @ApiOperation(value = "添加 Banner")
    @PostMapping("/save")
    public R saveBanner(@RequestBody Banner banner) {
        if (bannerService.save(banner)) {
            return R.ok().message("添加成功");
        }
        return R.error().message("添加失败");
    }

    @ApiOperation(value = "管理员分页查询")
    @GetMapping("/query/{current}/{limit}")
    public R queryPage(@ApiParam(name = "current", value = "当前页码", required = true)
                           @PathVariable("current") Long current,
                       @ApiParam(name = "limit", value = "每页记录数", required = true)
                           @PathVariable("limit") Long limit) {
        Page<Banner> page = new Page<>(current, limit);
        bannerService.queryPage(page);
        long total = page.getTotal();
        List<Banner> bannerList = page.getRecords();
        return R.ok().data("total", total).data("items", bannerList);
    }

    @ApiOperation(value = "根据 ID 获取")
    @GetMapping("/{id}")
    public R getBannerById(@ApiParam(name = "id", value = "Banner ID", required = true) @PathVariable("id") String id) {
        Banner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }

    @CacheEvict(cacheNames = {"index"}, allEntries = true)
    @ApiOperation(value = "根据 ID 修改")
    @PutMapping("/{id}")
    public R updateBannerById(@ApiParam(name = "id", value = "Banner ID", required = true) @PathVariable("id") String id,
                              @RequestBody Banner banner) {
        banner.setId(id);
        if (bannerService.updateById(banner)) {
            return R.ok().message("修改成功");
        }
        return R.error().message("修改失败");
    }

    @CacheEvict(cacheNames = {"index"}, allEntries = true)
    @ApiOperation(value = "根据 ID 删除")
    @DeleteMapping("/{id}")
    public R removeBannerById(@ApiParam(name = "id", value = "Banner ID", required = true) @PathVariable("id") String id) {
        if (bannerService.removeById(id)) {
            return R.ok().message("删除成功");
        }
        return R.ok().message("删除失败");
    }
}

