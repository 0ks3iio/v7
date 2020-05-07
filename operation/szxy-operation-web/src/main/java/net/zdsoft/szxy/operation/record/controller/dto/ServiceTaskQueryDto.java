package net.zdsoft.szxy.operation.record.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 服务任务查询条件参数
 * @author: Fubicheng
 * @create: 2019-04-09 17:08
 **/

@Data
public class ServiceTaskQueryDto {

    private Integer state;

    private Integer taskType;

    private String ownerUserName;
}
