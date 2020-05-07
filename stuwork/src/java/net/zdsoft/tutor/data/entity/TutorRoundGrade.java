package net.zdsoft.tutor.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2017年9月11日下午7:32:08
 * 导师轮次年级表
 */
@Entity
@Table(name="tutor_round_grade")
public class TutorRoundGrade extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;
    private String unitId;
    private String roundId;
    private String gradeId;
    @Override
	public String fetchCacheEntitName() {
		return "tutorRoundGrade";
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
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
}
