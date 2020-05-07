package net.zdsoft.qulity.data.dto;

import java.util.List;

/**
 * @author niuchao
 * @date 2019/10/16 11:50
 */
public class StuPdfDto {

    private String studentId;
    private String studentName;
    private String studentCode;
    private Integer sex;
    private String className;
    private Float totalScore;
    private String[] xnxq;//学年学期
    private String[] acadyears;//学年
    private String[] xkcj;//学科成绩
    private String[] yybs;//英语笔试
    private String[] yyks;//英语口试
    private Float xkjsScore;//学科竞赛总分
    private List<String[]> xkjsList;//学科竞赛每学年分
    private Float xkScore;//选考成绩
    private String xkResult;//选考成绩详情
    private Float dyScore;//德育成绩
    private List<String[]> dyList;//德育每学年分
    private String[] tycj;//体育成绩
    private Float myScore;//美育成绩
    private List<String[]> myList;//美育美学年分

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
    }

    public String[] getXnxq() {
        return xnxq;
    }

    public void setXnxq(String[] xnxq) {
        this.xnxq = xnxq;
    }

    public String[] getAcadyears() {
        return acadyears;
    }

    public void setAcadyears(String[] acadyears) {
        this.acadyears = acadyears;
    }

    public String[] getXkcj() {
        return xkcj;
    }

    public void setXkcj(String[] xkcj) {
        this.xkcj = xkcj;
    }

    public String[] getYybs() {
        return yybs;
    }

    public void setYybs(String[] yybs) {
        this.yybs = yybs;
    }

    public String[] getYyks() {
        return yyks;
    }

    public void setYyks(String[] yyks) {
        this.yyks = yyks;
    }

    public Float getXkjsScore() {
        return xkjsScore;
    }

    public void setXkjsScore(Float xkjsScore) {
        this.xkjsScore = xkjsScore;
    }

    public List<String[]> getXkjsList() {
        return xkjsList;
    }

    public void setXkjsList(List<String[]> xkjsList) {
        this.xkjsList = xkjsList;
    }

    public Float getXkScore() {
        return xkScore;
    }

    public void setXkScore(Float xkScore) {
        this.xkScore = xkScore;
    }

    public String getXkResult() {
        return xkResult;
    }

    public void setXkResult(String xkResult) {
        this.xkResult = xkResult;
    }

    public Float getDyScore() {
        return dyScore;
    }

    public void setDyScore(Float dyScore) {
        this.dyScore = dyScore;
    }

    public List<String[]> getDyList() {
        return dyList;
    }

    public void setDyList(List<String[]> dyList) {
        this.dyList = dyList;
    }

    public String[] getTycj() {
        return tycj;
    }

    public void setTycj(String[] tycj) {
        this.tycj = tycj;
    }

    public Float getMyScore() {
        return myScore;
    }

    public void setMyScore(Float myScore) {
        this.myScore = myScore;
    }

    public List<String[]> getMyList() {
        return myList;
    }

    public void setMyList(List<String[]> myList) {
        this.myList = myList;
    }
}
