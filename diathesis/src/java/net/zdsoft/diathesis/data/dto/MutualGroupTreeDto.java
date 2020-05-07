package net.zdsoft.diathesis.data.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/5/31 16:37
 */
public class MutualGroupTreeDto {
    private String gradeId;
    private String gradeName;
    List<DiathesisIdAndNameDto> classList=new ArrayList<>();

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }


    public List<DiathesisIdAndNameDto> getClassList() {
        return classList;
    }

    public void setClassList(List<DiathesisIdAndNameDto> classList) {
        this.classList = classList;
    }
}
