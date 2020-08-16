package cn.aliberter.boot.common.job.quartz.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Date;

/**
 * 定时任务全局监听器
 *
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
public class GlobalTriggerListener implements TriggerListener {

    private final String name;

    public GlobalTriggerListener(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        //ignore
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        return false;
    }

    /**
     * 捕捉错误的触发器
     *
     * @param trigger trigger
     */
    @Override
    public void triggerMisfired(Trigger trigger) {
        JobKey jobKey = trigger.getJobKey();
        String jobName = jobKey.getName();
        String jobGroup = jobKey.getGroup();
        JobDataMap jobDataMap = trigger.getJobDataMap();
        Date previousFireTime = trigger.getPreviousFireTime();
        log.error("[定时任务意外终止]|任务名{}|任务分组{}|任务参数{}|最后执行时间{}",
                jobName, jobGroup, JSONUtil.toJsonStr(jobDataMap), DateUtil.formatDateTime(previousFireTime));
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        //ignore
    }
}
