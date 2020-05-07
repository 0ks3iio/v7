package net.zdsoft.szxy.operation.inner.permission.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 用户和模块关系表
 * @author shenke
 * @since 2019/3/29 下午4:36
 */
@Data
@Entity
@Table(name = "op_user_module_relation")
public class UserModuleRelation {

    @Id
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 模块ID
     */
    private String moduleId;
    /**
     * 功能点ID
     */
    @Column(name = "module_operate_id")
    private String operateId;
    ///**
    // * 行政区划
    // */
    //@Transient
    //private String regionCode;
}