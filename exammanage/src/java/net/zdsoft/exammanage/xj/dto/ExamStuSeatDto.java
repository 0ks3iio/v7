package net.zdsoft.exammanage.xj.dto;

import java.util.List;


/**
 * @author yangsj  2017年10月12日下午5:38:52
 */
public class ExamStuSeatDto {

    private String stuName;
    private String admission;
    private String className;
    private String gradeName;
    private String carefulThings;
    private List<ExamStuSeatInfo> listess;


    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getAdmission() {
        return admission;
    }

    public void setAdmission(String admission) {
        this.admission = admission;
    }

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

    public String getCarefulThings() {
        return carefulThings;
    }

    public void setCarefulThings(String carefulThings) {
        this.carefulThings = carefulThings;
    }

    public List<ExamStuSeatInfo> getListess() {
        return listess;
    }

    public void setListess(List<ExamStuSeatInfo> listess) {
        this.listess = listess;
    }


}
