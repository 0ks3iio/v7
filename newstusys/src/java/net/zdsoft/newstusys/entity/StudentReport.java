package net.zdsoft.newstusys.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_student")
public class StudentReport extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2871758860929972916L;

	@Override
	public String fetchCacheEntitName() {
		return "studentReport";
	}

}
