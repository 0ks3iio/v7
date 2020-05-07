package net.zdsoft.tutor.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2017年9月11日下午7:34:43
 * 导师轮次教师表
 */
@Entity
@Table(name="tutor_round_teacher")
public class TutorRoundTeacher extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;
    private String unitId;
    private String roundId;
    private String teacherId;
	@Override
	public String fetchCacheEntitName() {
		return "tutorRoundTeacher";
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getRoundId() {
		return roundId;
	}
	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
}
