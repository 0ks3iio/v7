package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Objects;

/**
 * @Date: 2019/03/28
 *
 */
@Entity
@Table(name = "newdiathesis_score_info")
public class DiathesisScoreInfo extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	
	private String unitId;
    /**
     * 1:必修 2:选修
     * 1:选考
     * 1:学生自评 2:家长评价 3:学生互评 4:导师评价 5:班主任评价
     */
    private String type;
    private String scoreTypeId;
    private String objId;
    private String stuId;
    private String score;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    /**
     * 互评人
     */
    private String evaluateStuId;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEvaluateStuId() {
        return evaluateStuId;
    }

    public void setEvaluateStuId(String evaluateStuId) {
        this.evaluateStuId = evaluateStuId;
    }


    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScoreTypeId() {
        return scoreTypeId;
    }

    public void setScoreTypeId(String scoreTypeId) {
        this.scoreTypeId = scoreTypeId;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String fetchCacheEntitName() {
        return "newDiathesisScoreInfo";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiathesisScoreInfo that = (DiathesisScoreInfo) o;
        return stuId.equals(that.stuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stuId);
    }
}
