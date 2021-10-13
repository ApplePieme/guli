package com.applepieme.statistics.service;

import com.applepieme.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-10
 */
public interface DailyService extends IService<Daily> {
    /**
     * 根据开始和结束日期返回时间区间内的对应类型的图表数据
     *
     * @param type 数据类型
     * @param begin 开始日期
     * @param end 结束日期
     * @return 图表数据
     */
    Map<String, Object> getChartData(String type, String begin, String end);

    /**
     * 创建某一天的数据报表记录
     *
     * @param day 日期
     * @return 是否创建成功
     */
    boolean createStatisticsByDay(String day);
}
