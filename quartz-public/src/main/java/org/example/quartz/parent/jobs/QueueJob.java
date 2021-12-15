package org.example.quartz.parent.jobs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.quartz.parent.utils.SpringContextHolder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author JDragon
 * @Date 2021.12.15 下午 2:42
 * @Email 1061917196@qq.com
 * @Des: 队列作业
 */
public class QueueJob extends QuartzJobBean {

    @SneakyThrows
    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        ObjectMapper objectMapper = SpringContextHolder.getBean(ObjectMapper.class);
        String json = objectMapper.writeValueAsString(jobDataMap);
        Map<String, Object> params = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
        });
        QueueJobInfo queueJobInfo = new QueueJobInfo();
        queueJobInfo.setParams(params);
        JobBlockingQueue.getInstance().add(queueJobInfo);
    }
}
