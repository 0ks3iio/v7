package net.zdsoft.szxy.operation.record.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shenke
 * @since 2019/1/22 下午4:08
 */
@Getter
@Setter
public class OperationRecordQueryDto {

    private String unitId;
    private Date start;
    private Date end;
    private Integer type;
}
