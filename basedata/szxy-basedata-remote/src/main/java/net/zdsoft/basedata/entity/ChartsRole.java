package net.zdsoft.basedata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="base_charts_role")
public class ChartsRole extends BaseEntity<Integer>{
	
	private static final long serialVersionUID = 1L;

    private String otherId;
    
    @Column(updatable = false,nullable = false)
    private String appid;
    @Column(nullable = false)
    private String appkey;
    private String name;
    private Integer isUsing;

	public String getOtherId() {
		return otherId;
	}


	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}


	public String getAppid() {
		return appid;
	}


	public void setAppid(String appid) {
		this.appid = appid;
	}


	public String getAppkey() {
		return appkey;
	}


	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Integer getIsUsing() {
		return isUsing;
	}


	public void setIsUsing(Integer isUsing) {
		this.isUsing = isUsing;
	}


	@Override
	public String fetchCacheEntitName() {
		return "chartsRole";
	}

}
