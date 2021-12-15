package org.example.quartz.parent.jobs;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public final class JobBlockingQueue {

    private final BlockingQueue<QueueJobInfo> queue = new LinkedBlockingDeque<>();

    /**
     * 添加一个元素, 如果队列已满 则阻塞
     */
    public void put(QueueJobInfo e) throws InterruptedException {
        queue.put(e);
    }

    /**
     * 删除并返回队列头部元素 如果队列为空 则阻塞
     */
    public QueueJobInfo take() throws InterruptedException {
        return queue.take();
    }


    /**
     * 添加一个元素 如果队列已满 抛出异常
     */
    public void add(QueueJobInfo e) {
        queue.add(e);
    }

    /**
     * 删除并返回队列头部元素 如果队列为空 则返回null
     */
    public QueueJobInfo remove() {
        return queue.remove();
    }


    /**
     * 添加一个元素并返回true 如果队列已满 则返回false
     */
    public boolean offer(QueueJobInfo e) {
        return queue.offer(e);
    }

    public static JobBlockingQueue getInstance() {
        return Singleton.INSTANCE.get();
    }

    private enum Singleton {
        INSTANCE;
        private final JobBlockingQueue jobBlockingQueue;

        Singleton() {
            jobBlockingQueue = new JobBlockingQueue();
        }

        JobBlockingQueue get() {
            return jobBlockingQueue;
        }
    }

}
