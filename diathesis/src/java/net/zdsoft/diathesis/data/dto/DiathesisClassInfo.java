package net.zdsoft.diathesis.data.dto;

import javax.validation.constraints.NotBlank;

/**
 * @Author: panlf
 * @Date: 2019/6/4 10:33
 */
public class DiathesisClassInfo {
    @NotBlank(message = "classId不能为空")
    private String classId;
    @NotBlank(message = "学年信息不能为空")
    private String acadyear;
    @NotBlank(message = "学期信息不能为空")
    private Integer semeter;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getAcadyear() {
        return acadyear;
    }

    public void setAcadyear(String acadyear) {
        this.acadyear = acadyear;
    }

    public Integer getSemeter() {
        return semeter;
    }

    public void setSemeter(Integer semeter) {
        this.semeter = semeter;
    }

}
