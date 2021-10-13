package com.applepieme.statistics.schedule;

import com.applepieme.statistics.service.DailyService;
import com.applepieme.statistics.util.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 定时任务
 * 每天凌晨 1 点生成前一天的统计数据
 *
 * @author applepieme
 * @date 2021/10/10 20:54
 */
@Component
public class ScheduledTask {
    @Resource
    private DailyService dailyService;

    /**
     * 每天凌晨1点执行定时
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void createStatistics() {
        dailyService.createStatisticsByDay(DateUtils.formatDate(DateUtils.addDays(new Date(), -1)));
    }
}
