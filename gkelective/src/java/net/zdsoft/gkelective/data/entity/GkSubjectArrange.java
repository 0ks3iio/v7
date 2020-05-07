package net.zdsoft.gkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 选课课程安排
 * @author zhouyz
 *
 */
@Entity
@Table(name="gkelective_subject_arrange")
public class GkSubjectArrange extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String gradeId;
	private Integer subjectNum;//选课数
	private Date startTime;//开始时间
	private Date limitedTime;//截止时间
	private String notice;//公告
	private Integer isLock; //只锁选课结果及公告课程调整和基础模块
	private Integer isUsing;//是否启用指学生端可以查看到
	private Integer isDeleted;
	private Date creationTime;
	private Date modifyTime;

	@Transient
	private String arrangeName;

	public String getArrangeName() {
		return arrangeName;
	}

	public void setArrangeName(String arrangeName) {
		this.arrangeName = arrangeName;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getSubjectNum() {
		return subjectNum;
	}

	public void setSubjectNum(Integer subjectNum) {
		this.subjectNum = subjectNum;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public Integer getIsLock() {
		return isLock;
	}

	public void setIsLock(Integer isLock) {
		this.isLock = isLock;
	}

	public Integer getIsUsing() {
		return isUsing;
	}

	public void setIsUsing(Integer isUsing) {
		this.isUsing = isUsing;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

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
	@Override
	public String fetchCacheEntitName() {
		return "gkSubjectArrange";
	}

	public Date getLimitedTime() {
		return limitedTime;
	}

	public void setLimitedTime(Date limitedTime) {
		this.limitedTime = limitedTime;
	}

}
