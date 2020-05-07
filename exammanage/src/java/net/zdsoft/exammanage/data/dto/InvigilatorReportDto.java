package net.zdsoft.exammanage.data.dto;

import java.util.List;

public class InvigilatorReportDto {
    private String teacherId;
    private List<InvigilateTeacherDto> teachReportList;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public List<InvigilateTeacherDto> getTeachReportList() {
        return teachReportList;
    }

    public void setTeachReportList(List<InvigilateTeacherDto> teachReportList) {
        this.teachReportList = teachReportList;
    }


}
