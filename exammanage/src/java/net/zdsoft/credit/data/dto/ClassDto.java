package net.zdsoft.credit.data.dto;

import java.io.Serializable;

public class ClassDto implements Serializable {

    private static final long serialVersionUID = 2517550534344187235L;

    private String classId;
    private String name;
    private String classType;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

}
