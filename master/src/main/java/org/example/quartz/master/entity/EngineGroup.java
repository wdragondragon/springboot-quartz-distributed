package org.example.quartz.master.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author JDragon
 * @Date 2021.12.16 上午 9:03
 * @Email 1061917196@qq.com
 * @Des:
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dc_collect_engine_group")
public class EngineGroup extends Model<EngineGroup> {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String groupName;

    private String remark;
}
