package net.zdsoft.newgkelective.data.entity;

import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 排课表
 */
@Entity
@Table(name = "newgkelective_array")
public class NewGkArray extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private String unitId;
	private String gradeId;
	private Integer times; // 次数
	private Integer isDeleted;
	private Date creationTime;
	private Date modifyTime;
	private String divideId;
	private String placeArrangeId; // 场地安排
//	private String teacherArrangeId;// 教师安排
//	private String subjectArrangeId;// 科目周课时安排丢弃
	private String lessonArrangeId;// 上课时间安排  课程特征设置
	private String arrayName;
	private Integer isDefault;
	private String remark;
	/**
	 * 0:未排课 1:全部完成排课(老师安排完成) 2:算法排课完成 3:解决完整冲突
	 */
	private String stat;
	@Transient
	private boolean isNow;//是否正在排
	@Transient
	private int errornum;//冲突数量
	@Transient
	private String errorMess;//上一次失败消息
	@Transient
	private Map<String,Map<Integer,Integer>> courseNameCountMap;
	@Transient
	private Map<String,Integer> subNamesNumMap;
	@Transient
	private Map<String,Integer> onePercentMap;
	@Transient
	private Map<String,Integer> twoPercentMap;
	@Transient
	private Map<String,Integer> threePercentMap;
	@Transient
	private String chooseName;//选课
	
	private String arrangeType;//01：7选3排课  02:行政班排课 
	@Transient
	private String openType;//页面显示判断用的

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	
	public String getArrangeType() {
		return arrangeType;
	}

	public void setArrangeType(String arrangeType) {
		this.arrangeType = arrangeType;
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


	public Map<String, Integer> getSubNamesNumMap() {
		return subNamesNumMap;
	}

	public void setSubNamesNumMap(Map<String, Integer> subNamesNumMap) {
		this.subNamesNumMap = subNamesNumMap;
	}

	public Map<String, Map<Integer, Integer>> getCourseNameCountMap() {
		return courseNameCountMap;
	}

	public void setCourseNameCountMap(
			Map<String, Map<Integer, Integer>> courseNameCountMap) {
		this.courseNameCountMap = courseNameCountMap;
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

	public String getDivideId() {
		return divideId;
	}

	public void setDivideId(String divideId) {
		this.divideId = divideId;
	}

	public String getPlaceArrangeId() {
		return placeArrangeId;
	}

	public void setPlaceArrangeId(String placeArrangeId) {
		this.placeArrangeId = placeArrangeId;
	}

	public String getLessonArrangeId() {
		return lessonArrangeId;
	}

	public void setLessonArrangeId(String lessonArrangeId) {
		this.lessonArrangeId = lessonArrangeId;
	}

	public String getArrayName() {
		return arrayName;
	}

	public void setArrayName(String arrayName) {
		this.arrayName = arrayName;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkArray";
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public boolean isNow() {
		return isNow;
	}

	public void setNow(boolean isNow) {
		this.isNow = isNow;
	}

	public int getErrornum() {
		return errornum;
	}

	public void setErrornum(int errornum) {
		this.errornum = errornum;
	}

	public String getErrorMess() {
		return errorMess;
	}

	public void setErrorMess(String errorMess) {
		this.errorMess = errorMess;
	}

	public String getChooseName() {
		return chooseName;
	}

	public void setChooseName(String chooseName) {
		this.chooseName = chooseName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
