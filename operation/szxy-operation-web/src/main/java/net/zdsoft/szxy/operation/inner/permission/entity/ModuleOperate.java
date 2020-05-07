package net.zdsoft.szxy.operation.inner.permission.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模块操作明细表
 * @author shenke
 * @since 2019/3/28 下午6:52
 */
@Data
@Entity
@Table(name = "op_module_operate")
public class ModuleOperate {

    @Id
    private String id;
    /**
     * 模块ID
     */
    private String moduleId;
    /**
     * 操作名称
     */
    private String operateName;
    /**
     * 操作码，用于权限控制
     */
    private String operateCode;

}
