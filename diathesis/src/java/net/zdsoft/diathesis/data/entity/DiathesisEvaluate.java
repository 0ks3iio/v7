package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 学生自述
 * @Author: panlf
 * @Date: 2019/5/15 10:29
 */
@Entity
@Table(name="newdiathesis_evaluate")
public class DiathesisEvaluate extends BaseEntity<String> {

    private String unitId;
    private String studentId;
    private String teacherId;
    //数据库是clob类型.自述内容
    @NotBlank(message = "自述内容不能为空")
    @Length(max = 1000,message = "自述内容不能超过1000字")
    private String contentTxt;
    private Date creationTime;
    private Date modifyTime;

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

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getContentTxt() {
        return contentTxt;
    }

    public void setContentTxt(String contentTxt) {
        this.contentTxt = contentTxt;
    }

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

}
