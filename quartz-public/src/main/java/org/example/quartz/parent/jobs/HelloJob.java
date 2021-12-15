package org.example.quartz.parent.jobs;

import lombok.Setter;
import lombok.SneakyThrows;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.15 16:51
 * @Description:
 */
@DisallowConcurrentExecution
public class HelloJob extends QuartzJobBean {
    @Setter
    private String name;

    public HelloJob() {
    }

    @SneakyThrows
    public void executeInternal(JobExecutionContext context) {
        TriggerKey key = context.getTrigger().getKey();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        JobKey key1 = context.getJobDetail().getKey();

        String doSomething = jobDataMap.getString("do");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.err.println(Thread.currentThread().getId() + "-exec start time:" + simpleDateFormat.format(new Date()));
        System.err.println(Thread.currentThread().getId() + "-" + name + " Hello! " + doSomething);
        System.out.println(Thread.currentThread().getId() + "-" + "triggerName:" + key.getName() + " jobName:" + key1.getName());
        Thread.sleep(180 * 1000L);
        System.err.println(Thread.currentThread().getId() + "-exec end time:" + simpleDateFormat.format(new Date()));
    }
}
