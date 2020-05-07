package net.zdsoft.tutor.data.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2017年9月11日下午5:51:12
 *  导师轮次表
 */
@Entity
@Table(name="tutor_round")
public class TutorRound extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
    private String unitId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    private String roundName;   //轮次名称
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Transient
    private String gradeId;
    @Transient
    private String gradeCode;
    @Transient
    private String roundTitleName;
	@Override
	public String fetchCacheEntitName() {
		return "tutorRound";
	}
	public String getRoundTitleName() {
		return roundTitleName;
	}
	public void setRoundTitleName(String roundTitleName) {
		this.roundTitleName = roundTitleName;
	}
	public String getGradeCode() {
		return gradeCode;
	}
	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getRoundName() {
		return roundName;
	}
	public void setRoundName(String roundName) {
		this.roundName = roundName;
	}
	public Date getCreatTime() {
		return creationTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creationTime = creatTime;
	}
}
