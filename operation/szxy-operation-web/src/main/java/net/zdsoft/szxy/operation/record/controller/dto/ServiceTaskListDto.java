package net.zdsoft.szxy.operation.record.controller.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description: 服务任务列表展示参数
 * @author: Fubicheng
 * @create: 2019-04-15 10:47
 **/
@Data
public class ServiceTaskListDto {
    private String id;
    private Integer taskType;
    private Date creationTime;
    private Date completionTime;
    private String ownerUserId;
    private String ownerUserName;
    private String realName;
    private String content;
    private Integer state;
    private Integer isEmail;
}
