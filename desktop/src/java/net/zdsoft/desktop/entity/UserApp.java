package net.zdsoft.desktop.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 常用操作 (继承自6.0)
 * @author shenke
 * @since 2017.05.08
 */
@Entity
@Table(name = "sys_user_app")
public class UserApp extends BaseEntity<String> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public String fetchCacheEntitName() {
        return "userApp";
    }

    /**
     * 用户id
     */
    private String userId;
    /**
     * 模块id
     */
    @Column(name = "module_id")
    private Integer modelId;
    /**
     * 子系统
     */
    private Integer subsystem;
    /**
     * 排序号
     */
    @Column(name = "order_no")
    private Integer displayOrder;
    
    private Date creationTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(Integer subsystem) {
        this.subsystem = subsystem;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Date getcreationTime() {
        return creationTime;
    }

    public void setcreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
