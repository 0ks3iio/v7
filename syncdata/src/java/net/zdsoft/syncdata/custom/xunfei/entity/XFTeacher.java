package net.zdsoft.syncdata.custom.xunfei.entity;

import net.zdsoft.basedata.entity.Teacher;

public class XFTeacher extends Teacher {
    
    private static final long serialVersionUID = 1L;
    private Integer teacherIntId;

    public Integer getTeacherIntId() {
        return teacherIntId;
    }

    public void setTeacherIntId(Integer teacherIntId) {
        this.teacherIntId = teacherIntId;
    }

}
