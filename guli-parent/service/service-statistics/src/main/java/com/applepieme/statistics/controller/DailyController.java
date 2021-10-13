package com.applepieme.statistics.controller;


import com.applepieme.statistics.service.DailyService;
import com.applepieme.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author applepieme
 * @since 2021-10-10
 */
@RestController
@RequestMapping("/statistics/daily")
public class DailyController {
    @Resource
    private DailyService dailyService;

    @ApiOperation(value = "获取指定类型的图表数据")
    @GetMapping("/{type}/{begin}/{end}")
    public R getChartData(@PathVariable("type") String type,
                          @PathVariable("begin") String begin,
                          @PathVariable("end") String end) {
        Map<String, Object> data = dailyService.getChartData(type, begin, end);
        return R.ok().data(data);
    }

    @ApiOperation(value = "生成统计数据")
    @PostMapping("/{day}")
    public R createStatistics(@PathVariable("day") String day) {
        if (dailyService.createStatisticsByDay(day)) {
            return R.ok().message("生成数据成功");
        }
        return R.error().message("生成数据失败");
    }
}

