package net.zdsoft.desktop.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2017-6-12下午4:43:51
 */
@Entity
@Table(name = "desktop_user_subscribe")
public class UserSubscribe extends BaseEntity<String> {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 状态：启用
     */
    public static final int STATUS_TURNON = 1;
    // 状态：关闭
    public static final int STATUS_TURNOFF = 0;
    
    
	@Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return "userSubscribe";
	}
    
	private String userId;
	private Integer status;// 内部应用状态（停用（0）、上线（1））第三方应用状态（停用（0）、上线（1）、下线（2）、未提交（3）、审核中（4）、未通过（5）)
	private Integer serverId; //对应的应用id
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getServerId() {
		return serverId;
	}
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	
    
	
	
}
