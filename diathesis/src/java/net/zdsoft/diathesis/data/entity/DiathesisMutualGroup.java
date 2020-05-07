package net.zdsoft.diathesis.data.entity;


import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/3 17:10
 */
@Entity
@Table(name="newdiathesis_mutual_group")
public class DiathesisMutualGroup extends BaseEntity<String> {
    private String unitId;
    @NotBlank(message = "组名不能为空")
    private String mutualGroupName;
    private String classId;
    private String acadyear;
    private Integer semester;
    private String leaderId;
    private Integer sortNum;
    private String operator;
    private Date modifyTime;
    private Date creationTime;
    @Transient
    @Size(message = "不能成立空组")
    private List<String> studentIds;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public DiathesisMutualGroup clone() {
        DiathesisMutualGroup x = new DiathesisMutualGroup();
        x.setId(this.getId());
        x.setUnitId(this.getUnitId());
        x.setMutualGroupName(this.getMutualGroupName());
        x.setClassId(this.getClassId());
        x.setAcadyear(this.getAcadyear());
        x.setSemester(this.getSemester());
        x.setLeaderId(this.getLeaderId());
        x.setSortNum(this.getSortNum());
        x.setModifyTime(this.getModifyTime());
        x.setCreationTime(this.getCreationTime());
        x.setOperator(this.getOperator());
        return x;
    }

    public String getMutualGroupName() {
        return mutualGroupName;
    }

    public void setMutualGroupName(String mutualGroupName) {
        this.mutualGroupName = mutualGroupName;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }



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

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    @Override
    public String fetchCacheEntitName() {
        return null;
    }
}
