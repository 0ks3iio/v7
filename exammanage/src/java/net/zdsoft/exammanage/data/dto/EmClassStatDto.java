package net.zdsoft.exammanage.data.dto;

import java.util.ArrayList;
import java.util.List;

public class EmClassStatDto {
    private String classId;
    private String className;
    private int stuNum;
    private List<EmStudentStatDto> stuDtoList = new ArrayList<EmStudentStatDto>();

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getStuNum() {
        return stuNum;
    }

    public void setStuNum(int stuNum) {
        this.stuNum = stuNum;
    }

    public List<EmStudentStatDto> getStuDtoList() {
        return stuDtoList;
    }

    public void setStuDtoList(List<EmStudentStatDto> stuDtoList) {
        this.stuDtoList = stuDtoList;
    }


}
