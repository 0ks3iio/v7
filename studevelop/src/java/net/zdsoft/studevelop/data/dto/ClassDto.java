package net.zdsoft.studevelop.data.dto;

import java.io.Serializable;

public class ClassDto implements Serializable {
    String id;
    String classType;
    String className;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}

