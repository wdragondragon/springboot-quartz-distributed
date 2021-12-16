package org.example.quartz.parent.entity;

import lombok.Data;

/**
 * @Author JDragon
 * @Date 2021.12.16 上午 9:47
 * @Email 1061917196@qq.com
 * @Des:
 */

@Data
public class JobInfo {
    private Integer id;

    private String name;

    private String cron;

    private String engineGroupName;
}
