package net.zdsoft.diathesis.data.dto;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/8/13 10:28
 */
public class DiathesisCourseDto {
    private String courseId;
    private String courseName;
    List<DiathesisFieldDto> fieldList;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<DiathesisFieldDto> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<DiathesisFieldDto> fieldList) {
        this.fieldList = fieldList;
    }
}
