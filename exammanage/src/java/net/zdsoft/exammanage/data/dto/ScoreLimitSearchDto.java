package net.zdsoft.exammanage.data.dto;

/**
 * 成绩录分权限的查询dto
 */
public class ScoreLimitSearchDto {

    private String acadyear;
    private String semester;
    private String unitId;
    private String examId;//必修课 有考试id 选修课用32个0
    //以上查询条件不能为空
    private String[] classIds;//1、查询条件 2、保存班级参数
    private String teacherId;//1:查询条件 2:删除参数

    private String subjectId;
    //如果查询中  subjectId subjectIds 优先subjectId
    private String[] subjectIds;//用于查询条件


    //以下页面来源
    private String classId;//1:页面来源 2:删除参数
    private String gradeId;
    private String type;//选修 必修
    private String gradeCode;

    private String hasOne;
    //用于保存
    private String[] teacherIds;//
    private String[] classTypes;//跟classIds一一对应

    public String getHasOne() {
        return hasOne;
    }

    public void setHasOne(String hasOne) {
        this.hasOne = hasOne;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getAcadyear() {
        return acadyear;
    }

    public void setAcadyear(String acadyear) {
        this.acadyear = acadyear;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String[] getClassIds() {
        return classIds;
    }

    public void setClassIds(String[] classIds) {
        this.classIds = classIds;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String[] getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String[] subjectIds) {
        this.subjectIds = subjectIds;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(String[] teacherIds) {
        this.teacherIds = teacherIds;
    }

    public String[] getClassTypes() {
        return classTypes;
    }

    public void setClassTypes(String[] classTypes) {
        this.classTypes = classTypes;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }


}
