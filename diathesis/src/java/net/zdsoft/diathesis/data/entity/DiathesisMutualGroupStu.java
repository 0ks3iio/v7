package net.zdsoft.diathesis.data.entity;


import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 学生和互评小组的关联表
 * @Author: panlf
 * @Date: 2019/6/3 17:07
 */
@Entity
@Table(name="newdiathesis_mutual_group_stu")
public class DiathesisMutualGroupStu extends BaseEntity<String> {
    private String unitId;
    private String mutualGroupId;
    private String studentId;

    @Override
    public DiathesisMutualGroupStu clone(){
        DiathesisMutualGroupStu x = new DiathesisMutualGroupStu();
        x.setId(this.getId());
        x.setUnitId(this.getUnitId());
        x.setMutualGroupId(this.getMutualGroupId());
        x.setStudentId(this.getStudentId());
        return x;
    }


    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }


    public String getMutualGroupId() {
        return mutualGroupId;
    }

    public void setMutualGroupId(String mutualGroupId) {
        this.mutualGroupId = mutualGroupId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


    @Override
    public String fetchCacheEntitName() {
        return null;
    }
}
