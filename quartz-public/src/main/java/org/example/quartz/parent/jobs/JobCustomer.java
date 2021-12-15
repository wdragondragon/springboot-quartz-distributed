package org.example.quartz.parent.jobs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author JDragon
 * @Date 2021.12.15 下午 4:28
 * @Email 1061917196@qq.com
 * @Des:
 */
@Slf4j
public class JobCustomer implements Runnable {

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            try {
                QueueJobInfo jobInfo = JobBlockingQueue.getInstance().take();
                log.info("{}", jobInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }
}
