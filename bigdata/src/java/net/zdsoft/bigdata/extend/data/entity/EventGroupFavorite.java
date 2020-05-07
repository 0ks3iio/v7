package net.zdsoft.bigdata.extend.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "bg_event_group_favorite")
public class EventGroupFavorite extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	@Override
	public String fetchCacheEntitName() {
		return "eventGroupFavorite";
	}

	private String groupId;

	private String favoriteId;

	private int orderId;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getFavoriteId() {
		return favoriteId;
	}

	public void setFavoriteId(String favoriteId) {
		this.favoriteId = favoriteId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

}
