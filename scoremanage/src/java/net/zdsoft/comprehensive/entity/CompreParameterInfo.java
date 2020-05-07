package net.zdsoft.comprehensive.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "scoremanage_compre_param_info")
public class CompreParameterInfo extends BaseEntity<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String unitId;

	private String infoKey;

	private Integer mcPrefix;

	private Integer mcSuffix;

	private String gradeScore;

	private Float score;

	@Transient
	private List<Float> scoreList = new ArrayList<Float>();

	public List<Float> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<Float> scoreList) {
		this.scoreList = scoreList;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getInfoKey() {
		return infoKey;
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}

	public Integer getMcPrefix() {
		return mcPrefix;
	}

	public void setMcPrefix(Integer mcPrefix) {
		this.mcPrefix = mcPrefix;
	}

	public Integer getMcSuffix() {
		return mcSuffix;
	}

	public void setMcSuffix(Integer mcSuffix) {
		this.mcSuffix = mcSuffix;
	}

	public String getGradeScore() {
		return gradeScore;
	}

	public void setGradeScore(String gradeScore) {
		this.gradeScore = gradeScore;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	@Override
	public String fetchCacheEntitName() {
		return "getComParInfo";
	}

	@Override
	public String toString() {
		return "CompreParameterInfo [unitId=" + unitId + ", infoKey=" + infoKey
				+ ", mcPrefix=" + mcPrefix + ", mcSuffix=" + mcSuffix
				+ ", gradeScore=" + gradeScore + ", score=" + score + "]";
	}

}
