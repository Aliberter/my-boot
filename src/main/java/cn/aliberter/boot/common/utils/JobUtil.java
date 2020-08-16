package cn.aliberter.boot.common.utils;

import cn.aliberter.boot.common.job.quartz.listener.GlobalTriggerListener;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.EverythingMatcher;

import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 定时任务工具类
 *
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
@SuppressWarnings("unused")
public class JobUtil {

    private static final Scheduler SCHEDULER = SpringUtil.getBean(Scheduler.class);

    /**
     * @param startAtTime 任务执行时刻
     * @param name        任务名称
     * @param group       任务组名称
     * @param description 任务描述
     * @param jobBean     具体任务
     */
    public static void createJobByStartAt(Date startAtTime, String name, String group, String description, Class<? extends Job> jobBean) {
        //创建任务触发器
        Trigger trigger = newTrigger().withIdentity(name, group).startAt(startAtTime).build();
        createJob(name, group, description, trigger, jobBean);
    }

    /**
     * @param startAtTime 任务执行时刻
     * @param name        任务名称
     * @param group       任务组名称
     * @param description 任务描述
     * @param jobDataMap  任务执行参数
     * @param jobBean     具体任务
     */
    public static void createJobByStartAt(Date startAtTime, String name, String group, String description, JobDataMap jobDataMap, Class<? extends Job> jobBean) {
        //创建任务触发器
        Trigger trigger = newTrigger().withIdentity(name, group).startAt(startAtTime).build();
        createJob(name, group, description, trigger, jobDataMap, jobBean);
    }

    /**
     * @param name        任务名称
     * @param group       任务组名称
     * @param description 任务描述
     * @param cron        cron表达式
     * @param jobBean     具体任务
     */
    public static void createJobByCron(String name, String group, String description, String cron, Class<? extends Job> jobBean) {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        //创建任务触发器
        Trigger trigger = newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
        createJob(name, group, description, trigger, jobBean);
    }

    /**
     * @param name        任务名称
     * @param group       任务组名称
     * @param description 任务描述
     * @param cron        cron表达式
     * @param jobDataMap  任务执行参数
     * @param jobBean     具体任务
     */
    public static void createJobByCron(String name, String group, String description, String cron, JobDataMap jobDataMap, Class<? extends Job> jobBean) {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        //创建任务触发器
        Trigger trigger = newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
        createJob(name, group, description, trigger, jobDataMap, jobBean);
    }

    /**
     * @param name        任务名称
     * @param group       任务组名称
     * @param description 任务描述
     * @param second      多少秒执行一次
     * @param repeatCount 重复执行多少次
     * @param jobDataMap  任务执行参数
     * @param jobBean     具体任务
     */
    public static void createSimpleJob(String name, String group, String description, int second, int repeatCount, JobDataMap jobDataMap, Class<? extends Job> jobBean) {
        //创建任务触发器
        TriggerBuilder<Trigger> triggerTriggerBuilder = newTrigger()
                .withIdentity(name, group)
                .startNow();
        if (0 == repeatCount) {
            triggerTriggerBuilder.withSchedule(simpleSchedule()
                    .withIntervalInSeconds(second)
                    .repeatForever());
        } else {
            triggerTriggerBuilder.withSchedule(simpleSchedule()
                    .withIntervalInSeconds(second)
                    .withRepeatCount(repeatCount));
        }
        Trigger trigger = triggerTriggerBuilder.forJob(name, group).build();
        createJob(name, group, description, trigger, jobDataMap, jobBean);
    }

    /**
     * 停止任务
     *
     * @param name  任务名称
     * @param group 任务组名称
     */
    public static void stopJob(String name, String group) {
        log.info("[开始停止定时任务] name:{} group:{}", name, group);
        TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
        JobKey jobKey = JobKey.jobKey(name, group);
        try {
            Trigger trigger = SCHEDULER.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            SCHEDULER.pauseTrigger(triggerKey);
            SCHEDULER.unscheduleJob(triggerKey);
            SCHEDULER.deleteJob(jobKey);
            log.info("[停止定时任务成功] name:{} group:{}", name, group);
        } catch (Exception e) {
            log.info("停止定时任务异常...name:{} group:{}", name, group);
        }
    }

    private static void createJob(String name, String group, String description, Trigger trigger, Class<? extends Job> jobBean) {
        try {
            //创建任务
            JobDetail jobDetail = JobBuilder.newJob(jobBean).withIdentity(name, group).withDescription(description).build();
            //将触发器与任务绑定到调度器内
            SCHEDULER.scheduleJob(jobDetail, trigger);
            // 创建并注册一个全局的Trigger Listener
            SCHEDULER.getListenerManager().addTriggerListener(new GlobalTriggerListener("GlobalTrigger"), EverythingMatcher.allTriggers());
            log.info("创建新的定时任务->name:{} group:{}", name, group);
        } catch (Exception e) {
            log.error("创建定时任务异常：", e);
        }
    }

    private static void createJob(String name, String group, String description, Trigger trigger, JobDataMap jobDataMap, Class<? extends Job> jobBean) {
        try {
            //创建任务
            JobDetail jobDetail = JobBuilder.newJob(jobBean).withIdentity(name, group).withDescription(description).setJobData(jobDataMap).build();
            //将触发器与任务绑定到调度器内
            SCHEDULER.scheduleJob(jobDetail, trigger);
            // 创建并注册一个全局的Trigger Listener
            SCHEDULER.getListenerManager().addTriggerListener(new GlobalTriggerListener("GlobalTrigger"), EverythingMatcher.allTriggers());
            log.info("创建新的定时任务->name:{} group:{}", name, group);
        } catch (Exception e) {
            log.error("创建定时任务异常：", e);
        }
    }

    private JobUtil() {

    }
}
