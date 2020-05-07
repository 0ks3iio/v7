package net.zdsoft.szxy.operation.record.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 服务记录实体类
 *
 * @author panlf   2019年1月15日
 */
@EqualsAndHashCode
@Data
@Entity
@Table(name = "op_operation_record")
public class OperationRecord  {

    @Id
    private String id;

    private String operatorId;
    private String operateUnitId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date operateTime;
    private Integer operateType;
    private String operateDetail;

    @Transient
    private String operatorName;
    @Transient
    private String operatorTypeName;
    @Transient
    private String unitName;

}



