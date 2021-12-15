package org.example.quartz.parent.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author JDragon
 * @Date 2021.12.15 下午 4:32
 * @Email 1061917196@qq.com
 * @Des:
 */
@Slf4j
@Component
public class JobManager {

    @PostConstruct
    public void start() {
        for (int i = 0; i < 3; i++) {
            log.info("init the thread file-in [{}]", i);
            JobCustomer jobCustomer = new JobCustomer();
            new Thread(jobCustomer).start();
        }
    }
}
