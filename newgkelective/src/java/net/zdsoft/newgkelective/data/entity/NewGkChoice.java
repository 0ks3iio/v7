package net.zdsoft.newgkelective.data.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;

/**
 * 选课
 */
@Entity
@Table(name = "newgkelective_choice")
public class NewGkChoice extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private String unitId;
	private String gradeId;
	private String choiceName;
	private Integer chooseNum; // 选课数量
	private Date startTime; // 开始时间
	private Date endTime; // 结束时间
	private String notice; // 公告
	private Integer times; // 次数
	private Integer isDeleted;
	private Date creationTime;
	private Date modifyTime;
	// referScoreId已从表中删除
	// referScoreId又加回来了 @2019/08/26
	private String referScoreId;
	private Integer showSamesele;
	private int isDefault;
	private Integer statShow;//选课结果是否开放
	private Integer showNum;//人以下的选课组合进行提示
	private Date showTime;//提示开始时间
	private String hintContent;//提示文字

	@Transient
	private Map<String,Map<String,Integer>> courseNameCountMap;
	@Transient
	List<NewGkConditionDto> newConditionList3;
	@Transient
	private Map<String,Integer> onePercentMap;
	@Transient
	private Map<String,Integer> twoPercentMap;
	@Transient
	private Map<String,Integer> threePercentMap;
	@Transient
	private List<String> courseNames;
	@Transient
	private List<String> coursePercentNames;
	@Transient
	private String[] whsOfsdzPercent;
	

	public String getHintContent() {
		return hintContent;
	}

	public void setHintContent(String hintContent) {
		this.hintContent = hintContent;
	}

	public Integer getStatShow() {
		return statShow;
	}

	public void setStatShow(Integer statShow) {
		this.statShow = statShow;
	}

	public Integer getShowNum() {
		return showNum;
	}

	public void setShowNum(Integer showNum) {
		this.showNum = showNum;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public String[] getWhsOfsdzPercent() {
		return whsOfsdzPercent;
	}

	public void setWhsOfsdzPercent(String[] whsOfsdzPercent) {
		this.whsOfsdzPercent = whsOfsdzPercent;
	}

	public List<String> getCoursePercentNames() {
		return coursePercentNames;
	}

	public void setCoursePercentNames(List<String> coursePercentNames) {
		this.coursePercentNames = coursePercentNames;
	}

	public List<String> getCourseNames() {
		return courseNames;
	}

	public void setCourseNames(List<String> courseNames) {
		this.courseNames = courseNames;
	}

	public Map<String, Map<String, Integer>> getCourseNameCountMap() {
		return courseNameCountMap;
	}

	public void setCourseNameCountMap(
			Map<String, Map<String, Integer>> courseNameCountMap) {
		this.courseNameCountMap = courseNameCountMap;
	}

	public List<NewGkConditionDto> getNewConditionList3() {
		return newConditionList3;
	}

	public void setNewConditionList3(List<NewGkConditionDto> newConditionList3) {
		this.newConditionList3 = newConditionList3;
	}

	public Map<String, Integer> getOnePercentMap() {
		return onePercentMap;
	}

	public void setOnePercentMap(Map<String, Integer> onePercentMap) {
		this.onePercentMap = onePercentMap;
	}

	public Map<String, Integer> getTwoPercentMap() {
		return twoPercentMap;
	}

	public void setTwoPercentMap(Map<String, Integer> twoPercentMap) {
		this.twoPercentMap = twoPercentMap;
	}

	public Map<String, Integer> getThreePercentMap() {
		return threePercentMap;
	}

	public void setThreePercentMap(Map<String, Integer> threePercentMap) {
		this.threePercentMap = threePercentMap;
	}

	public String getChoiceName() {
		return choiceName;
	}

	public void setChoiceName(String choiceName) {
		this.choiceName = choiceName;
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

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
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

	public Integer getChooseNum() {
		return chooseNum;
	}

	public void setChooseNum(Integer chooseNum) {
		this.chooseNum = chooseNum;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getShowSamesele() {
		return showSamesele;
	}

	public void setShowSamesele(Integer showSamesele) {
		this.showSamesele = showSamesele;
	}

	public String getReferScoreId() {
		return referScoreId;
	}

	public void setReferScoreId(String referScoreId) {
		this.referScoreId = referScoreId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkChoice";
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

}
