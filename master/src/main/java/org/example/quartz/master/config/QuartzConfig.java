package org.example.quartz.master.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.quartz.master.entity.Engine;
import org.example.quartz.master.mapper.EngineMapper;
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


    @Bean
    @ConfigurationProperties(prefix = "spring.quartz.properties")
    public Properties quartzProperties() {
        return new Properties();
    }

    @Bean
    public ApplicationRunner process(Properties quartzProperties, EngineMapper engineMapper) {
        return args -> {
            String master = System.getProperty("quartz.master");
            if (!"true".equals(master)) {
                return;
            }
            List<Engine> engines = engineMapper.selectList(
                    new LambdaQueryWrapper<Engine>().select(Engine::getEngineGroup));
            Set<String> engineGroups = engines.stream()
                    .map(Engine::getEngineGroup)
                    .collect(Collectors.toSet());
            for (String group : engineGroups) {
                Properties properties = (Properties) quartzProperties.clone();
                properties.put("org.quartz.scheduler.instanceName", group);
                SchedulerFactory schedulerFactory = new StdSchedulerFactory(properties);
                Scheduler scheduler = schedulerFactory.getScheduler();
                SchedulerManager.register(group, new QuartzUtil(scheduler));
            }
            log.info("加载完成，scheduler名单：[{}]", StringUtils.join(SchedulerManager.allScheduler(), ','));
        };
    }

}

