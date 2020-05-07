package net.zdsoft.savedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


import net.zdsoft.framework.entity.BaseEntity;

/**
 * 其他公司和我们基础表的主键id 关联表
 * @author yangsj  2018年8月2日上午9:22:36
 */
@Entity
@Table(name = "base_syn_relation")
public class RelationBase extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "relationBase";
	}
	private String baseId;  //我们基础表的主键id
	private String relationId; //和第三方的主键id一致
	private String type;  // 基础表的类型
	private String area; //地区标志 或 公司标志 唯一性
	public String getBaseId() {
		return baseId;
	}
	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
}
