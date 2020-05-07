package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/3/22 上午11:43
 */
@Data
@Entity
@Table(name = "base_unit_extension")
public class UnitExtension implements Serializable {

    @Id
    private String id;
    /**
     * 单位ID
     */
    private String unitId;
    /**
     * 过期类型
     */
    private Integer expireTimeType;
    /**
     * 过期时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime;
    /**
     * 服务过期时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceExpireTime;
    /**
     * 使用性质
     */
    private Integer usingNature;
    /**
     * 使用状态
     */
    private Integer usingState;
    /**
     * 合同编号
     */
    private String contractNumber;
    /**
     * 合作关系
     */
    private Integer starLevel;
}
