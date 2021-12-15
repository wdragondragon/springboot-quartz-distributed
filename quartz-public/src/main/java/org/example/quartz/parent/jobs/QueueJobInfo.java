package org.example.quartz.parent.jobs;

import lombok.Data;

import java.util.Map;

/**
 * @Author JDragon
 * @Date 2021.12.15 下午 2:50
 * @Email 1061917196@qq.com
 * @Des:
 */
@Data
public class QueueJobInfo {

    private Map<String, Object> params;

}
