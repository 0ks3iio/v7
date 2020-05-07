package net.zdsoft.syncdata.entity;

import net.zdsoft.basedata.entity.Teacher;

public class XFTeacher extends Teacher {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private Integer teacherIntId;

    public Integer getTeacherIntId() {
        return teacherIntId;
    }

    public void setTeacherIntId(Integer teacherIntId) {
        this.teacherIntId = teacherIntId;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
