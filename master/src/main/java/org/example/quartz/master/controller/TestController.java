package org.example.quartz.master.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.quartz.parent.jobs.QueueJob;
import org.example.quartz.parent.utils.SchedulerManager;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author JDragon
 * @Date 2021.12.14 下午 10:23
 * @Email 1061917196@qq.com
 * @Des:
 */
@Api(tags = "quartz测试")
@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${engine-schedule.job-group:engine-group}")
    private String jobGroup;

    @Value("${engine-schedule.job-prefix:engine-job-id-}")
    private String jobNamePrefix;

    @GetMapping("/startJob")
    @ApiOperation("测试启动作业")
    public String startJob(@RequestParam(defaultValue = "engine-default") String schedulerName,
                           @RequestParam(defaultValue = "") String jobName,
                           @RequestParam(defaultValue = "0 * * * * ?") String cron) throws ObjectAlreadyExistsException {
        Map<String, Object> param = new HashMap<>();
        param.put("name", "张三");
        param.put("do", "做些啥呢?");
        SchedulerManager.get(schedulerName).addJob(jobNamePrefix + jobName, jobGroup, cron, QueueJob.class, param);
        return "成功";
    }

    @GetMapping("/stopJob")
    @ApiOperation("测试停止作业")
    public String stopJob(@RequestParam(defaultValue = "engine-default") String schedulerName,
                          @RequestParam(defaultValue = "") String jobName) throws SchedulerException {
        SchedulerManager.get(schedulerName).deleteJob(jobNamePrefix + jobName, jobGroup);
        return "成功";
    }
}
