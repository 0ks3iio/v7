package net.zdsoft.szxy.operation.record.entity;

import lombok.Data;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @description: 服务任务记录表
 * @author: Fubicheng
 * @create: 2019-04-02 16:09
 **/
@Data
@Entity
@Table(name = "op_service_task")
public class ServiceTask {
    @Id
    private String id;

    private Integer taskType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completionTime;

    private String ownerUserId;

    private String ownerUserName;

    private String createUserId;

    private String content;

    private Integer state;

    private Integer isEmail;
}
