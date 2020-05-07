package net.zdsoft.exammanage.data.dto;

import java.util.List;

public class EmFilterSaveDto {
    private List<EmStudentDto> stuDtoList;
    private String examId;

    public List<EmStudentDto> getStuDtoList() {
        return stuDtoList;
    }

    public void setStuDtoList(List<EmStudentDto> stuDtoList) {
        this.stuDtoList = stuDtoList;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

}
