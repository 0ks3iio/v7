package net.zdsoft.szxy.operation.inner.permission.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模块表
 * @author shenke
 * @since 2019/3/28 下午6:46
 */
@Data
@Entity
@Table(name = "op_module")
public class Module {

    @Id
    private String id;
    /**
     * 模块名称
     */
    private String name;
    /**
     * 模块地址（全的，包含前缀）
     */
    private String url;
    /**
     * 模块地址前缀
     */
    private String urlPrefix;

}