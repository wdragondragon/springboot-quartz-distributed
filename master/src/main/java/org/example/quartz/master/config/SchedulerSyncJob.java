package org.example.quartz.master.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.quartz.master.mapper.JobMapper;
import org.example.quartz.parent.entity.JobInfo;
import org.example.quartz.parent.jobs.QueueJob;
import org.example.quartz.parent.utils.QuartzUtil;
import org.example.quartz.parent.utils.SchedulerManager;
import org.example.quartz.parent.utils.SpringContextHolder;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author JDragon
 * @Date 2021.12.16 下午 12:50
 * @Email 1061917196@qq.com
 * @Des:
 */
@Slf4j
@DisallowConcurrentExecution
public class SchedulerSyncJob extends QuartzJobBean {

    @Value("${engine-schedule.job-group:engine-group}")
    private String jobGroup;

    @Value("${engine-schedule.job-prefix:engine-job-id-}")
    private String jobNamePrefix;

    private final JobMapper jobMapper;

    private final SchedulerRegister schedulerRegister;

    public SchedulerSyncJob(JobMapper jobMapper, SchedulerRegister schedulerRegister) {
        this.jobMapper = jobMapper;
        this.schedulerRegister = schedulerRegister;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //刷新新加入的scheduler
        schedulerRegister.load();

        List<JobInfo> runningJobs = jobMapper.getRunningJob();
        Map<String, List<JobInfo>> engineRunJobMap =
                runningJobs.stream().collect(Collectors.groupingBy(JobInfo::getEngineGroupName));

        for (String schedulerName : SchedulerManager.allScheduler()) {
            QuartzUtil quartzUtil = SchedulerManager.get(schedulerName);
            List<JobInfo> jobInfos = engineRunJobMap.get(schedulerName);
            if (jobInfos == null) {
                jobInfos = new LinkedList<>();
            }
            //需要运行的作业名称
            List<String> runningJobName = jobInfos.stream().map(e -> jobNamePrefix + e.getId()).collect(Collectors.toList());
            //已在调度的作业名称
            List<String> allJobsByGroup = quartzUtil.getAllJobsByGroup(jobGroup);

            //启动作业为 需要运行作业中去除已在调度作业
            List<JobInfo> startJobs = jobInfos.stream()
                    .filter(e -> !allJobsByGroup.contains(jobNamePrefix + e.getId())).collect(Collectors.toList());
            //停止作业为 已在调度作业去除需要运行作业
            List<String> stopJobs = allJobsByGroup.stream().filter(e -> !runningJobName.contains(e)).collect(Collectors.toList());

            for (String stopJob : stopJobs) {
                try {
                    quartzUtil.deleteJob(stopJob, jobGroup);
                    log.info("停止调度：[{}.{}]", jobGroup, stopJob);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }

            ObjectMapper objectMapper = SpringContextHolder.getBean(ObjectMapper.class);
            for (JobInfo startJob : startJobs) {
                try {
                    String json = objectMapper.writeValueAsString(startJob);
                    Map<String, Object> params = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                    });
                    String jobName = jobNamePrefix + startJob.getId();
                    quartzUtil.addJob(jobName, jobGroup, startJob.getCron(), QueueJob.class, params);
                    log.info("加入调度：[{}.{}]", jobGroup, jobName);
                } catch (JsonProcessingException | ObjectAlreadyExistsException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
