package net.zdsoft.stutotality.data.entity;


import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "stutotality_good_stat")
public class StutotalityGoodStat extends BaseEntity<String> {
  private String unitId;
  private String acadyear;
  private String semester;
//  private String classId;
  private String studentId;
  private String studentName;
  private String studentCode;
  private Float standardSocre;
  private Float allScore;
  private int rank;
  private int haveGood;
  private Date creationTime;


  @Override
  public String fetchCacheEntitName() {
    return "StutotalityGoodStat";
  }


  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
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


  public Float getStandardSocre() {
    return standardSocre;
  }

  public void setStandardSocre(Float standardSocre) {
    this.standardSocre = standardSocre;
  }


  public Float getAllScore() {
    return allScore;
  }

  public void setAllScore(Float allScore) {
    this.allScore = allScore;
  }


  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }


  public int getHaveGood() {
    return haveGood;
  }

  public void setHaveGood(int haveGood) {
    this.haveGood = haveGood;
  }


  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

//  public String getClassId() {
//    return classId;
//  }
//
//  public void setClassId(String classId) {
//    this.classId = classId;
//  }
}
