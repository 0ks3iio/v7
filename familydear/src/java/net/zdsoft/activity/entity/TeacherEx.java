package net.zdsoft.activity.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_teacher_ex")
public class TeacherEx extends BaseEntity<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4053769560254352871L;
	@ColumnInfo(displayName = "走访干部类型", vtype = ColumnInfo.VTYPE_SELECT, mcodeId = "DM-FQGBLB")
    @Column(length=10)
    private String cadreType;

	public String getCadreType() {
		return cadreType;
	}

	public void setCadreType(String cadreType) {
		this.cadreType = cadreType;
	}

	@Override
	public String fetchCacheEntitName() {
		return "teacherEx";
	}

}
