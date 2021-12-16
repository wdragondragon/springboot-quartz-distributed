package org.example.quartz.master.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.quartz.master.entity.EngineGroup;
import org.example.quartz.master.mapper.EngineGroupMapper;
import org.example.quartz.parent.utils.QuartzUtil;
import org.example.quartz.parent.utils.SchedulerManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author JDragon
 * @Date 2021.12.16 下午 2:12
 * @Email 1061917196@qq.com
 * @Des:
 */
@Slf4j
@Component
public class SchedulerRegister {

    @Autowired
    private Properties quartzProperties;

    @Autowired
    private EngineGroupMapper engineGroupMapper;

    @SneakyThrows
    public void load() {
        log.info("加载scheduler......");
        List<EngineGroup> engines = engineGroupMapper.selectList(
                new LambdaQueryWrapper<EngineGroup>().select(EngineGroup::getGroupName));
        Set<String> engineGroups = engines.stream()
                .map(EngineGroup::getGroupName)
                .collect(Collectors.toSet());
        Set<String> allScheduler = SchedulerManager.allScheduler();
        for (String group : engineGroups) {
            if (allScheduler.contains(group)) {
                continue;
            }
            Properties properties = (Properties) quartzProperties.clone();
            properties.put("org.quartz.scheduler.instanceName", group);
            SchedulerFactory schedulerFactory = new StdSchedulerFactory(properties);
            Scheduler scheduler = schedulerFactory.getScheduler();
            SchedulerManager.register(group, new QuartzUtil(scheduler));
        }
        log.info("加载完成，已存在scheduler名单：[{}]", StringUtils.join(SchedulerManager.allScheduler(), ','));
    }
}
