package net.zdsoft.szxy.operation.record.controller.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description: 服务任务更新参数
 * @author: Fubicheng
 * @create: 2019-04-04 14:40
 **/
@Data
public class ServiceTaskUpdateDto {
    /**
     * id
     */
    @NotNull
    private String id;

    /**
     * 任务类型
     */
    @NotNull
    @Min(value = 0,message = "不合法的任务类型")
    @Max(value = 2,message = "不合法的任务类型")
    private Integer taskType;

    /**
     * 任务内容
     */
    @NotNull
    private String taskContent;

    /**
     * 任务邮件
     */
    @NotNull
    @Min(value = 0,message = "不合法的任务类型")
    @Max(value = 1,message = "不合法的任务类型")
    private Integer isEmail;
}
