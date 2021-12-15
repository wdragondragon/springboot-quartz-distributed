package org.example.quartz.parent.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author JDragon
 * @Date 2021.12.15 下午 5:57
 * @Email 1061917196@qq.com
 * @Des:
 */
public class SchedulerManager {

    private static final Map<String, QuartzUtil> schedulerMap = new HashMap<>();

    public static void register(String name, QuartzUtil scheduler) {
        if (schedulerMap.containsKey(name)) {
            throw new RuntimeException("scheduler name is exist");
        }
        schedulerMap.put(name, scheduler);
    }

    public static QuartzUtil get(String name) {
        QuartzUtil quartzUtil = schedulerMap.get(name);
        if (quartzUtil == null) {
            quartzUtil = SpringContextHolder.getBean(QuartzUtil.class);
        }
        return quartzUtil;
    }

    public static Set<String> allScheduler() {
        return schedulerMap.keySet();
    }
}
