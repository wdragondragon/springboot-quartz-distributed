package org.example.quartz.master.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.quartz.master.entity.EngineGroup;
import org.example.quartz.master.mapper.EngineGroupMapper;
import org.example.quartz.parent.utils.QuartzUtil;
import org.example.quartz.parent.utils.SchedulerManager;
import org.example.quartz.parent.utils.SpringContextHolder;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author JDragon
 * @Date 2021.12.14 下午 10:24
 * @Email 1061917196@qq.com
 * @Des:
 */

@Slf4j
@EnableScheduling
@Configuration
public class QuartzConfig {

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }


    @Bean("bmScheduler")
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
        return schedulerFactoryBean.getScheduler();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.quartz.properties")
    public Properties quartzProperties() {
        return new Properties();
    }

    @Bean
    public ApplicationRunner process(QuartzUtil quartzUtil, SchedulerRegister schedulerRegister) {
        return args -> {
            quartzUtil.addJob("check", "master", "0 * * * * ?", SchedulerSyncJob.class, new HashMap<>());
            log.info("启动master定时检查作业调度");
            schedulerRegister.load();
        };
    }
}

