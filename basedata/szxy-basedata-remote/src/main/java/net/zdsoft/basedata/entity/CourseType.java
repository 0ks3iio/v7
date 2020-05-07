package net.zdsoft.basedata.entity;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="base_course_type")
public class CourseType extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ColumnInfo(displayName = "科目名称")
	private String name;
	@ColumnInfo(displayName = "科目编号")
	private String code;
	/**
	 *  1.课程模块 2.选修课类型,3:选修Ⅰ（A）、4:选修Ⅰ（B）、5:选修Ⅱ
	 */
	@ColumnInfo(displayName = "科目类型")
	private String type;
	@ColumnInfo(displayName = "是否删除")
	private Integer isDeleted;

	/** 非必须字段*/
	@Transient
	private String typeName;
	@Override
	public String fetchCacheEntitName() {
		return "courseType";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
