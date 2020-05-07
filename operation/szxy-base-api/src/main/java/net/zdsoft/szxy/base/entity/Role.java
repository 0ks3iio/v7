package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author shenke
 * @since 2019/3/20 下午2:41
 */
@Data
@Entity
@Table(name = "sys_role")
public class Role implements Serializable {

    @Id
    private String id;

    private String identifier;
    /**
     * 所在单位
     */
    @Column(name = "unitid")
    private String unitId;
    /**
     * 名称
     */
    private String name;
    @Column(name = "modid")
    private String modId;
    @Column(name = "operid")
    private String operId;
    @Column(name = "dynamicdataset")
    private String dynamicDataSet;
    @Column(name = "isactive")
    private String isActive;
    private String description;
    @Column(name = "subsystem")
    private String subSystem;
    @Column(name = "refid")
    private Integer refId;
    @Column(name = "roletype")
    private Integer roleType;


}
