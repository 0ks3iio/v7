package net.zdsoft.basedata.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.utils.SUtils;
@Entity
@Table(name="base_teach_class")
public class TeachClass extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	@Column(updatable=false)
	private String unitId;
	private String acadyear;
	private String semester;
	private String name;
	private String classType;
	private String teacherId;
	private String isUsing;
	private String gradeId;
	private int punchCard;//是否需要电子班排打卡
	private String isUsingMerge;//是否用于合并教学班
	private String isMerge;//班级是否是合并的教学班
	private String parentId;//是用于合并的教学班，其合成后的教学班Id
	
	private String relaCourseId;// 此教学班绑定的 虚拟课程-----0901修改为base_class_hour的id
	private String placeId; //教学班 场地
	
	@ColumnInfo(displayName = "学分")
	private Integer credit;
	@ColumnInfo(displayName = "满分")
	private Integer fullMark;
	@ColumnInfo(displayName = "及格分")
	private Integer passMark;	
	/**
	 * 课程id
	 */
	private String courseId;
	/**
	 * 辅助教师多个用逗号分隔
	 */
	private String assistantTeachers;
	//用于页面多选下拉框传值 （辅助教师）
	@Transient
	private String[] ateschids;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private Integer isDeleted;
	
	/**
	 * 1:必修
	 */
	private String subjectType;
	
	@Transient
	private String xnCourseId;
	
	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	@Transient
	private List<TeachClassEx> exList;//上课时间场地
	
	@Transient
	private String teachClassIds;//用于合并教学班的教学班Id
	
	/** 7选3 常规科目*/
	public static final String CLASS_TYPE_SEVEN = "3";
	
	/**
	 * 杭外定制  1:必修 2:选修 
	 */
	public static final String CLASS_TYPE_REQUIRED = "1";
	/**
	 * 2:选修
	 */
	public static final String CLASS_TYPE_ELECTIVE = "2";
	
	@Transient
	private List<String> stuIdList;//学生ids
	@Transient
	private String courseName;//教学班对应的科目名称
	@Transient
	private String fromCourseType;//必修课有所属学科 语文必修一所属语文  选修课：选修类型
	@Transient
	private String teacherNames;
	
	@Transient
	private String[] gradeIds;//可以有多个年级
	//人数
	@Transient
	private int studentNum;
	
	@Override
	public String fetchCacheEntitName() {
		return "teachclass";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getAssistantTeachers() {
		return assistantTeachers;
	}

	public void setAssistantTeachers(String assistantTeachers) {
		this.assistantTeachers = assistantTeachers;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	//使用到页面多选操作
	public Map<String, String> getLkxzSelectMap() {
		Map<String, String> lkxzSelectMap =new HashMap<String, String>();
		if(this.assistantTeachers!=null){
			String[] strs = assistantTeachers.split(",");
			for(String teaid :strs){
				lkxzSelectMap.put(teaid, teaid);
			}
		}
		return lkxzSelectMap;
	}

	public String[] getAteschids() {
		return ateschids;
	}

	public void setAteschids(String[] ateschids) {
		this.ateschids = ateschids;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public List<String> getStuIdList() {
		return stuIdList;
	}

	public void setStuIdList(List<String> stuIdList) {
		this.stuIdList = stuIdList;
	}

	public List<TeachClassEx> getExList() {
		return exList;
	}

	public void setExList(List<TeachClassEx> exList) {
		this.exList = exList;
	}
	public int getPunchCard() {
		return punchCard;
	}

	public void setPunchCard(int punchCard) {
		this.punchCard = punchCard;
	}

	public String getIsUsing() {
		return isUsing;
	}

	public void setIsUsing(String isUsing) {
		this.isUsing = isUsing;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getFromCourseType() {
		return fromCourseType;
	}

	public void setFromCourseType(String fromCourseType) {
		this.fromCourseType = fromCourseType;
	}

	public String getTeacherNames() {
		return teacherNames;
	}

	public void setTeacherNames(String teacherNames) {
		this.teacherNames = teacherNames;
	}

	public String[] getGradeIds() {
		return gradeIds;
	}

	public void setGradeIds(String[] gradeIds) {
		this.gradeIds = gradeIds;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public Integer getFullMark() {
		return fullMark;
	}

	public void setFullMark(Integer fullMark) {
		this.fullMark = fullMark;
	}

	public Integer getPassMark() {
		return passMark;
	}

	public void setPassMark(Integer passMark) {
		this.passMark = passMark;
	}

	public int getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(int studentNum) {
		this.studentNum = studentNum;
	}

	public String getIsUsingMerge() {
		return isUsingMerge;
	}

	public void setIsUsingMerge(String isUsingMerge) {
		this.isUsingMerge = isUsingMerge;
	}

	public String getIsMerge() {
		return isMerge;
	}

	public void setIsMerge(String isMerge) {
		this.isMerge = isMerge;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTeachClassIds() {
		return teachClassIds;
	}

	public void setTeachClassIds(String teachClassIds) {
		this.teachClassIds = teachClassIds;
	}
	
	public static List<TeachClass> dt(String data) {
		List<TeachClass> ts = SUtils.dt(data, new TypeReference<List<TeachClass>>() {
		});
		if (ts == null)
			ts = new ArrayList<TeachClass>();
		return ts;

	}

	public String getRelaCourseId() {
		return relaCourseId;
	}

	public void setRelaCourseId(String relaCourseId) {
		this.relaCourseId = relaCourseId;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getXnCourseId() {
		return xnCourseId;
	}

	public void setXnCourseId(String xnCourseId) {
		this.xnCourseId = xnCourseId;
	}
	
}
