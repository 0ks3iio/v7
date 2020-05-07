package net.zdsoft.szxy.operation.inner.permission.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author shenke
 * @since 2019/3/29 下午5:34
 */
@Data
@Entity
@Table(name = "op_group_module_relation")
public class GroupModuleRelation {

    @Id
    private String id;
    /**
     * 分组ID
     */
    private String groupId;
    /**
     * 模块ID
     */
    private String moduleId;
    /**
     * 功能点ID
     */
    private String operateId;
}