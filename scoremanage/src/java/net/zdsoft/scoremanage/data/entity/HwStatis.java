package net.zdsoft.scoremanage.data.entity;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * 杭外定制成绩统计表
 * @author niuchao
 * @date 2019/11/5 9:54
 */
@Entity
@Table(name = "scoremanage_hw_statis")
public class HwStatis extends BaseEntity<String> {
    @ColumnInfo(displayName = "单位id")
    private String unitId;
    @ColumnInfo(displayName = "方案id")
    private String hwPlanId;
    @ColumnInfo(displayName = "01统计,02保送生汇总,03个人成绩汇总")
    private String planType;
    @ColumnInfo(displayName = "学生id")
    private String studentId;
    @ColumnInfo(displayName = "学生姓名")
    private String stuName;
    @ColumnInfo(displayName = "学号")
    private String stuCode;
    @ColumnInfo(displayName = "班级id")
    private String classId;
    @ColumnInfo(displayName = "班级编号")
    private String claCode;
    @ColumnInfo(displayName = "班级名称")
    private String claName;
    @ColumnInfo(displayName = "年级id")
    private String gradeId;
    @ColumnInfo(displayName = "年级名称")
    private String gradeName;

    @Transient
    List<HwStatisEx> exList1 = new ArrayList<>();//科目
    @Transient
    List<HwStatisEx> exList2 = new ArrayList<>();//总分或排名

    public List<HwStatisEx> getExList1() {
        return exList1;
    }

    public void setExList1(List<HwStatisEx> exList1) {
        this.exList1 = exList1;
    }

    public List<HwStatisEx> getExList2() {
        return exList2;
    }

    public void setExList2(List<HwStatisEx> exList2) {
        this.exList2 = exList2;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getHwPlanId() {
        return hwPlanId;
    }

    public void setHwPlanId(String hwPlanId) {
        this.hwPlanId = hwPlanId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuCode() {
        return stuCode;
    }

    public void setStuCode(String stuCode) {
        this.stuCode = stuCode;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClaCode() {
        return claCode;
    }

    public void setClaCode(String claCode) {
        this.claCode = claCode;
    }

    public String getClaName() {
        return claName;
    }

    public void setClaName(String claName) {
        this.claName = claName;
    }

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

    @Override
    public String fetchCacheEntitName() {
        return "hwStatis";
    }
}
