package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author shenke
 * @since 2019/3/19 下午6:08
 */
@Data
@Entity
@Table(name = "base_sys_option")
public class SysOption implements Serializable {

    @Id
    private String id;

    private String optionCode;
    /**
     * 名称
     */
    private String name;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 描述
     */
    private String description;
    /**
     * 在用值
     */
    private String nowValue;
    /**
     * 系统验证（遗留）
     */
    private String validateJs;
    /**
     * 是否可见
     */
    private Integer viewable;
    /**
     *
     */
    private Integer eventSource;
    private Integer isDeleted;
    /**
     * 数据类型
     */
    private Integer valueType;
}
