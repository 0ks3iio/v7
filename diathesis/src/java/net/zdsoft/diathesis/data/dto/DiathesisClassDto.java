package net.zdsoft.diathesis.data.dto;

import net.zdsoft.basedata.dto.BaseDto;

import java.util.List;

/**
 * @DATE: 2019/04/01
 * 对应年级下班级列表
 *
 */
public class DiathesisClassDto extends BaseDto {
    private String classId;
    private String className;
    private List<DiathesisStudentDto> studentList;

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

    public List<DiathesisStudentDto> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<DiathesisStudentDto> studentList) {
        this.studentList = studentList;
    }
}
