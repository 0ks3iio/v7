package net.zdsoft.szxy.operation.record.controller.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description:
 * @author: Fubicheng
 * @create: 2019-04-10 16:34
 **/
@Data
public class ServiceTaskInsertDto {

    @NotNull
    private String ownerUserId;

    @Min(value = 0,message = "不合法的任务类型")
    @Max(value = 2,message = "不合法的任务类型")
    private Integer taskType;

    @NotNull
    @Min(value = 0,message = "不合法的任务类型")
    @Max(value = 1,message = "不合法的任务类型")
    private Integer isEmail;

    @NotNull
    private Date completionTime;

    @NotNull
    private String content;

}
