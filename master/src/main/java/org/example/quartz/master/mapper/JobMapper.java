package org.example.quartz.master.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.quartz.master.entity.EngineGroup;
import org.example.quartz.master.entity.Job;
import org.example.quartz.parent.entity.JobInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author JDragon
 * @Date 2021.12.15 下午 8:25
 * @Email 1061917196@qq.com
 * @Des:
 */

@Mapper
@Repository
public interface JobMapper extends BaseMapper<Job> {
    List<JobInfo> getRunningJob();
}
