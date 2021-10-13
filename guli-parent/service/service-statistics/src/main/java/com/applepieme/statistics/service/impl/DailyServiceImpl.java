package com.applepieme.statistics.service.impl;

import com.applepieme.statistics.client.UserClient;
import com.applepieme.statistics.entity.Daily;
import com.applepieme.statistics.mapper.DailyMapper;
import com.applepieme.statistics.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-10
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {
    @Resource
    private UserClient userClient;

    @Override
    public Map<String, Object> getChartData(String type, String begin, String end) {
        /* 根据查询条件查询数据 */
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("date_calculated");
        queryWrapper.between("date_calculated", begin, end);
        List<Daily> dailyList = baseMapper.selectList(queryWrapper);

        /* 封装图表的 x 轴和 y 轴数据*/
        List<String> dateList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        for (Daily daily : dailyList) {
            dateList.add(daily.getDateCalculated());
            switch (type) {
                case "register_num":
                    numberList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numberList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numberList.add(daily.getCourseNum());
                    break;
                default:
                    numberList.add(daily.getLoginNum());
                    break;
            }
        }

        /* 把图表数据封装到 map 中返回 */
        Map<String, Object> map = new HashMap<>();
        map.put("xAxisData", dateList);
        map.put("seriesData", numberList);
        return map;
    }

    @Override
    public boolean createStatisticsByDay(String day) {
        /* 添加记录之前先删除之前的记录，保证一天只有一条记录 */
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated", day);
        baseMapper.delete(queryWrapper);

        /* 获取统计信息 */
        Integer registerNum = userClient.getRegisterNumByDay(day);
        Integer loginNum = RandomUtils.nextInt(100, 200);
        Integer videoViewNum = RandomUtils.nextInt(100, 200);
        Integer courseNum = RandomUtils.nextInt(100, 200);

        /* 创建统计对象 */
        Daily daily = new Daily();
        daily.setDateCalculated(day);
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        return baseMapper.insert(daily) > 0;
    }
}
