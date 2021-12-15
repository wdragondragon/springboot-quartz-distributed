package org.example.quartz.master.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
* <p>
* 
* </p>
* @author hjs
* @since 2019-10-12
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dc_collect_engine")
@ApiModel(value="Engine对象", description="")
public class Engine extends Model<Engine> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer projectId;

    private String name;

    private Integer core;

    private Integer editionId;

    private String editionName;

    private String deployPath;

    private Integer serverId;

    private Integer serverStatus;

    private Integer status;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deployTime;

    @ApiModelProperty("作业运行数阈值")
    private Integer threshold;

    @ApiModelProperty(value = "备注")
    private String remarks;

    private String engineGroup;
}