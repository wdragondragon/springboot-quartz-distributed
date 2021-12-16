package org.example.quartz.parent.utils;


import lombok.extern.slf4j.Slf4j;
import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CalendarIntervalTrigger;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 定时任务util
 *
 * @author js0014
 */

@Component
@Slf4j
@DependsOn({"quartzInstanceIdGenerator"})
public class QuartzUtil {

    /**
     * 声明注入调度器
     */
    private final Scheduler scheduler;

    public QuartzUtil(Scheduler bmScheduler) {
        this.scheduler = bmScheduler;
    }

    public void addJob(String jobName, String group,
                       String cronExpress, Class<? extends Job> jobClass, Map<String, Object> params) throws ObjectAlreadyExistsException {
        this.addJob(jobName, group, null, null, cronExpress, jobClass, params);
    }

    /**
     * 添加cron表达式作业
     *
     * @param jobName     作业名称
     * @param cronExpress 时间表达式
     * @param jobClass    集成 job 类 的实现类
     * @param params      所需参数
     */
    public void addJob(String jobName, String group,
                       Date startTime, Date endTime,
                       String cronExpress, Class<? extends Job> jobClass, Map<String, Object> params) throws ObjectAlreadyExistsException {
        try {
            if (existJob(jobName, group)) {
                return;
            }
            //添加触发调度名称
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, group);
            //判断时间表达式是否有效
            if (!CronExpression.isValidExpression(cronExpress)) {
                throw new Exception("时间表达式格式错误：" + cronExpress);
            }
            //设置触发时间
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpress);
            cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
            //触发建立
            TriggerBuilder<CronTrigger> cronTriggerTriggerBuilder = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder);

            if (startTime == null) {
                cronTriggerTriggerBuilder.startAt(new Date());
            } else {
                cronTriggerTriggerBuilder.startAt(startTime);
            }
            if (endTime != null) {
                cronTriggerTriggerBuilder.endAt(endTime);
            }
            CronTrigger trigger = cronTriggerTriggerBuilder.build();
            //添加作业名称
            JobKey jobKey = JobKey.jobKey(jobName, group);
            //建立作业
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();
            //传入调度的数据，在QuartzFactory中需要使用
            jobDetail.getJobDataMap().putAll(params);
            //调度作业
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (ObjectAlreadyExistsException oe) {
            throw oe;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用固定间隔添加一个作业
     *
     * @param jobName   作业名称
     * @param group     作业组
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param interval  频率单位数量
     * @param unit      频率单位
     * @param jobClass  集成 job 类 的实现类
     * @param params    所需参数
     */
    public void addJob(String jobName, String group,
                       Date startTime, Date endTime, Integer interval, DateBuilder.IntervalUnit unit,
                       Class<? extends Job> jobClass, Map<String, Object> params) throws ObjectAlreadyExistsException {
        try {
            if (existJob(jobName, group)) {
                return;
            }
            //添加触发调度名称
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, group);

            CalendarIntervalScheduleBuilder calendarIntervalScheduleBuilder =
                    CalendarIntervalScheduleBuilder.calendarIntervalSchedule();
            calendarIntervalScheduleBuilder.withInterval(interval, unit);
            calendarIntervalScheduleBuilder.withMisfireHandlingInstructionDoNothing();


            TriggerBuilder<CalendarIntervalTrigger> calendarIntervalTriggerTriggerBuilder = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey).withSchedule(calendarIntervalScheduleBuilder);

            if (startTime == null) {
                calendarIntervalTriggerTriggerBuilder.startAt(new Date());
            } else {
                calendarIntervalTriggerTriggerBuilder.startAt(startTime);
            }
            if (endTime != null) {
                calendarIntervalTriggerTriggerBuilder.endAt(endTime);
            }
            CalendarIntervalTrigger trigger = calendarIntervalTriggerTriggerBuilder.build();
            //添加作业名称
            JobKey jobKey = JobKey.jobKey(jobName, group);
            //建立作业
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();
            //传入调度的数据，在QuartzFactory中需要使用
            jobDetail.getJobDataMap().putAll(params);
            //调度作业
            scheduler.scheduleJob(jobDetail, trigger);

//            scheduler.getListenerManager().addTriggerListener();
        } catch (ObjectAlreadyExistsException oe) {
            throw oe;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取Job状态
     */
    public String getJobState(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
        return scheduler.getTriggerState(triggerKey).name();
    }

    /**
     * 暂停所有job
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 根据group ，jobName 暂停 job
     */
    public boolean pauseJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            log.info("jobDetail is null");
            return false;
        } else {
            scheduler.pauseJob(jobKey);
            return true;
        }

    }

    /**
     * 恢复被pauseJob方法暂停的job
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 单独恢复某个job
     */
    public boolean resumeJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            log.info("jobDetail is null");
            return false;
        } else {
            scheduler.resumeJob(jobKey);
            return true;
        }
    }

    /**
     * 删除某个job
     *
     * @param jobName  jobName
     * @param jobGroup group
     */
    public boolean deleteJob(String jobName, String jobGroup) throws SchedulerException {
        if (!existJob(jobName, jobGroup)) {
            return true;
        }
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            log.info("jobDetail is null");
            return false;
        } else if (!scheduler.checkExists(jobKey)) {
            log.info("jobKey is not exists");
            return false;
        } else {
            scheduler.deleteJob(jobKey);
            return true;
        }

    }

    /**
     * 修改定时任务
     *
     * @param jobName     任务名称
     * @param group       任务group
     * @param cronExpress 时间表达式
     * @param jobClass    执行方法类
     * @param params      传入参数
     */
    public boolean modifyJob(String jobName, String group, String cronExpress, Class<? extends Job> jobClass, Map<String, Object> params) throws Exception {
        if (!CronExpression.isValidExpression(cronExpress)) {
            throw new Exception("时间表达式格式错误：" + cronExpress);
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, group);
        JobKey jobKey = new JobKey(jobName, group);
        if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //表达式调度构建器,不立即执行
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpress).withMisfireHandlingInstructionDoNothing();
            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(scheduleBuilder).build();
            //修改参数
            trigger.getJobDataMap().putAll(params);
            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            return true;
        } else {
            log.info("job or trigger not exists");
            return false;
        }

    }

    public void startNowWithOne(String jobName, String group, Class<? extends Job> jobClass, Map<String, Object> params) throws ObjectAlreadyExistsException {
        try {
            //添加触发调度名称
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, group);
            //判断时间表达式是否有效
//            if (!CronExpression.isValidExpression(cronExpress)) {
//                throw new Exception("时间表达式格式错误：" + cronExpress);
//            }
            //设置触发时间
            //CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpress);
            //触发建立
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startNow().build();
            //CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
            //添加作业名称
            JobKey jobKey = JobKey.jobKey(jobName, group);
            //建立作业
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();
            //传入调度的数据，在QuartzFactory中需要使用
            jobDetail.getJobDataMap().putAll(params);
            //调度作业
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (ObjectAlreadyExistsException oe) {
            throw oe;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<String>> getAllJobs() {
        Map<String, List<String>> map = new LinkedHashMap<>();
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();
                    if (!map.containsKey(jobGroup)) {
                        map.put(jobGroup, new LinkedList<>());
                    }
                    map.get(jobGroup).add(jobName);
                }
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllJobsByGroup(String groupName) {
        try {
            List<String> jobNameList = new ArrayList<>();
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                String jobName = jobKey.getName();
                jobNameList.add(jobName);
            }
            return jobNameList;
        } catch (SchedulerException e) {
            log.error("根据组获取quartz任务列表失败", e);
            throw new RuntimeException("根据组获取quartz任务列表失败");
        }
    }

    public boolean existJob(String jobName, String group) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobName, group));
            if (jobDetail == null) {
                log.info("不存在作业[{}.{}]", group, jobName);
                return false;
            } else {
                log.info("作业存在[{}.{}]", group, jobName);
                return true;
            }
        } catch (SchedulerException e) {
            log.error("", e);
            return false;
        }
    }


}
