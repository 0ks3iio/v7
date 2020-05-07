package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author shenke
 * @since 2019/3/20 下午2:40
 */
@Data
@Entity
@Table(name = "sys_option")
public class SystemIni implements Serializable {

    @Id
    private String id;

    @Column(name = "iniid")
    private String iniId;
    private String name;
    @Column(name = "dvalue")
    private String defaultValue;
    private String description;
    @Column(name = "nowvalue")
    private String nowValue;
    private Integer viewable;
    @Column(name = "validatejs")
    private String validateJs;
    @Column(name = "orderid")
    private Integer orderId;
    @Column(name = "subsystemid")
    private String subsystemId;
    private Integer coercive;
    private Integer valueType;
}
