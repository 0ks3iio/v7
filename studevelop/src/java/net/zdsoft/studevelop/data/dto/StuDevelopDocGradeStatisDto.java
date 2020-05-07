package net.zdsoft.studevelop.data.dto;

import java.util.List;

/**
 * Created by luf on 2018/10/16.
 */
public class StuDevelopDocGradeStatisDto {

    private String classId;
    private String className;

    private List<String> monthsSum;

    private String classActivity;

    private String themeActivity;

    private String classHonor;

    private String parentJoin;

    private String parentSubmitPic;

    private String parentSubmitPicAve;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getMonthsSum() {
        return monthsSum;
    }

    public void setMonthsSum(List<String> monthsSum) {
        this.monthsSum = monthsSum;
    }

    public String getClassActivity() {
        return classActivity;
    }

    public void setClassActivity(String classActivity) {
        this.classActivity = classActivity;
    }

    public String getThemeActivity() {
        return themeActivity;
    }

    public void setThemeActivity(String themeActivity) {
        this.themeActivity = themeActivity;
    }

    public String getClassHonor() {
        return classHonor;
    }

    public void setClassHonor(String classHonor) {
        this.classHonor = classHonor;
    }

    public String getParentJoin() {
        return parentJoin;
    }

    public void setParentJoin(String parentJoin) {
        this.parentJoin = parentJoin;
    }

    public String getParentSubmitPic() {
        return parentSubmitPic;
    }

    public void setParentSubmitPic(String parentSubmitPic) {
        this.parentSubmitPic = parentSubmitPic;
    }

    public String getParentSubmitPicAve() {
        return parentSubmitPicAve;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setParentSubmitPicAve(String parentSubmitPicAve) {
        this.parentSubmitPicAve = parentSubmitPicAve;
    }
}
