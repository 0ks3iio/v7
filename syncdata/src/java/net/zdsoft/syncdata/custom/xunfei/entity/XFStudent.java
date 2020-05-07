package net.zdsoft.syncdata.custom.xunfei.entity;

import net.zdsoft.basedata.entity.Student;

public class XFStudent extends Student {
    
    private static final long serialVersionUID = 1L;

    private String className;
    
    private String gradeName;
    
    private String updatestamp;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getUpdatestamp() {
        return updatestamp;
    }

    public void setUpdatestamp(String updatestamp) {
        this.updatestamp = updatestamp;
    }

}
