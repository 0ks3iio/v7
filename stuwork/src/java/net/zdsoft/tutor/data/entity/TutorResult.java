package net.zdsoft.tutor.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2017年9月11日下午7:37:50
 * 导师结果表
 */
@Entity
@Table(name="tutor_result")
public class TutorResult extends BaseEntity<String> {
	/**
	 * 状态 未毕业
	 */
	public static final Integer STATE_NORMAL = 0;
	/**
	 * 状态 毕业
	 */
	public static final Integer STATE_LEAVE = 1;
	private static final long serialVersionUID = 1L;
    private String unitId;
    private String studentId;
    private String teacherId;
    private String roundId;
    private Integer state;  //学生是否毕业  1--毕业  0--未毕业
//    private Integer section; //1--小学，2--初中，3--高中
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    
	@Override
	public String fetchCacheEntitName() {
		return "tutorResult";
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
	public String getRoundId() {
		return roundId;
	}
	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
//	public Integer getSection() {
//		return section;
//	}
//	public void setSection(Integer section) {
//		this.section = section;
//	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}
