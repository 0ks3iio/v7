package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 
 *  场地安排
 */
@Entity
@Table(name="newgkelective_place_arrange")
public class NewGkplaceArrange extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String arrayItemId;

	private Integer orderId;
	
	private String placeId;
	
	private Date creationTime;
	
	private Date modifyTime;

    public Integer getOrderId() {
        return orderId;
    }



    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }



    public String getArrayItemId() {
		return arrayItemId;
	}



	public void setArrayItemId(String arrayItemId) {
		this.arrayItemId = arrayItemId;
	}



	public String getPlaceId() {
		return placeId;
	}



	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}



	public Date getCreationTime() {
		return creationTime;
	}



	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}



	public Date getModifyTime() {
		return modifyTime;
	}



	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}



	@Override
	public String fetchCacheEntitName() {
		return "getPlaceArrange";
	}

}
