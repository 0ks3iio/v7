package net.zdsoft.newgkelective.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 上课时间安排
 */
@Entity
@Table(name = "newgkelective_lesson_time")
public class NewGkLessonTime extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	/**
	 * newgkelective_array_item.id
	 */
	private String arrayItemId;
	
	/**
	 * objectType:
	 * 		0:年级 objectId:gradeId 年级不排课时间
	 * 		9:科目  objectId:subjectId 与 levelType结合 表示 选考、学考科目的不排课时间
	 */
	private String objectId;
	/**
	 *  0:年级 2:老师 9:科目 7:总课表 5:班级科目  6:教师组
	 */
	private String objectType;
	/**
	 * 是否参与排课
	 */
	private Integer isJoin;
	private Date creationTime;
	private Date modifyTime;
	/**
	 * 1:7选3或者6选3(物化生历地政技)  2:理科(物化生)3:文科(历地政)4:语数英 
	 * 5:选考（divide.openType=01/05，年级总课表时选考批次，或者单科选考课目的课表时间）
	 * 6:学考（divide.openType=01/05，年级总课表时学考批次，或者单科学考课目的课表时间）
	 */
	private String groupType;
	/**
	 * A选考B学考 可为空
	 * divide.openType=01/05时有值：单科课表为A或B，年级总课表为批次数1/2/3/...
	 */
	private String levelType;	
	
	@Transient
	private List<NewGkLessonTimeEx> timesList=new ArrayList<>();

	public String getArrayItemId() {
		return arrayItemId;
	}

	public void setArrayItemId(String arrayItemId) {
		this.arrayItemId = arrayItemId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Integer getIsJoin() {
		return isJoin;
	}

	public void setIsJoin(Integer isJoin) {
		this.isJoin = isJoin;
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
		return "newGkLessonTime";
	}

	public List<NewGkLessonTimeEx> getTimesList() {
		return timesList;
	}

	public void setTimesList(List<NewGkLessonTimeEx> timesList) {
		this.timesList = timesList;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getLevelType() {
		return levelType;
	}

	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}

}
