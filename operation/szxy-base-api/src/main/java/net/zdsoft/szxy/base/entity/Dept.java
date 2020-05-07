package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/3/20 下午2:21
 */
@Data
@Entity
@Table(name = "base_dept")
public class Dept implements Serializable {

    @Id
    private String id;

    /**
     * 单位ID
     */
    private String unitId;

    /**
     * 名称
     */
    private String deptName;

    /**
     * 编号
     */
    private String deptCode;

    /**
     * 部门简称
     */
    private String deptShortName;

    /**
     * 上级部门
     */
    private String parentId;

    /**
     * 办公电话
     */
    private String deptTel;

    /**
     * 排序号
     */
    private Integer displayOrder;

    /**
     * 类型
     */
    private Integer deptType;

    /**
     * 负责人
     */
    private String teacherId;

    /**
     * 分管领导
     */
    private String leaderId;

    /**
     * 分管校长
     */
    private String deputyHeadId;

    /**
     * 备注
     */
    private String remark;


    private Integer isDefault;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date creationTime;

    /**
     * 修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    private Integer eventSource;

    private String dingdingId;


    /**
     * 所属学院
     */
    private String instituteId;

    /**
     * 所属校区
     */
    private String areaId;

}