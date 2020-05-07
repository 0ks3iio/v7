package net.zdsoft.basedata.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 教研组扩展表
 */
@Entity
@Table(name = "base_teach_group_ex")
public class TeachGroupEx extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	private String teachGroupId;// 教研组Id
	/**
	 * 1:负责人,0:成员
	 */
	private Integer type;// 类型
	private String teacherId;// 老师Id

	public String getTeachGroupId() {
		return teachGroupId;
	}

	public void setTeachGroupId(String teachGroupId) {
		this.teachGroupId = teachGroupId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "baseTeachGroupEx";
	}

}
