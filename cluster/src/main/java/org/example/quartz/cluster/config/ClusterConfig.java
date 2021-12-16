package org.example.quartz.cluster.config;

import lombok.extern.slf4j.Slf4j;
import org.example.quartz.parent.utils.SpringContextHolder;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @Author JDragon
 * @Date 2021.12.15 下午 9:25
 * @Email 1061917196@qq.com
 * @Des:
 */
@Slf4j
@EnableScheduling
@Configuration
public class ClusterConfig {
    @Bean("bmScheduler")
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
        return schedulerFactoryBean.getScheduler();
    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

}
