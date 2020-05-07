package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.entity.NewGkArray;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkTimetable;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;

public class NewGkArrayResultSaveDto {
	private String arrayId;
	private List<NewGkTimetable> insertTimeTableList=new ArrayList<NewGkTimetable>();
	private List<NewGkTimetableOther> insertOtherList=new ArrayList<NewGkTimetableOther>();
	private List<NewGkTimetableTeacher> insertTeacherList=new ArrayList<NewGkTimetableTeacher>();
	private List<NewGkDivideClass> insertClassList=new ArrayList<>();
	private List<NewGkClassStudent> insertStudentList=new ArrayList<>();
	
	// 手动调课增删 课程时 时可以使用,
	private List<NewGkTimetableTeacher> updateTeacherList=new ArrayList<NewGkTimetableTeacher>();
	
	//旧数据删除
	private String[] timeTableIds;
	// 指定保存后的排课 状态
	private String stat;
	//  新的排课方案。 复制 排课方案时使用
	private NewGkArray newGkArray;
	
	// 手动调整课表时使用
	private String[] delTimeTableOtherIds = new String[0];

	public List<NewGkTimetable> getInsertTimeTableList() {
		return insertTimeTableList;
	}

	public void setInsertTimeTableList(List<NewGkTimetable> insertTimeTableList) {
		this.insertTimeTableList = insertTimeTableList;
	}

	public List<NewGkTimetableOther> getInsertOtherList() {
		return insertOtherList;
	}

	public void setInsertOtherList(List<NewGkTimetableOther> insertOtherList) {
		this.insertOtherList = insertOtherList;
	}

	public List<NewGkTimetableTeacher> getInsertTeacherList() {
		return insertTeacherList;
	}

	public void setInsertTeacherList(List<NewGkTimetableTeacher> insertTeacherList) {
		this.insertTeacherList = insertTeacherList;
	}

	public String[] getTimeTableIds() {
		return timeTableIds;
	}

	public void setTimeTableIds(String[] timeTableIds) {
		this.timeTableIds = timeTableIds;
	}

	public String getArrayId() {
		return arrayId;
	}

	public void setArrayId(String arrayId) {
		this.arrayId = arrayId;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public List<NewGkDivideClass> getInsertClassList() {
		return insertClassList;
	}

	public void setInsertClassList(List<NewGkDivideClass> insertClassList) {
		this.insertClassList = insertClassList;
	}

	public List<NewGkClassStudent> getInsertStudentList() {
		return insertStudentList;
	}

	public void setInsertStudentList(List<NewGkClassStudent> insertStudentList) {
		this.insertStudentList = insertStudentList;
	}

	public NewGkArray getNewGkArray() {
		return newGkArray;
	}

	public void setNewGkArray(NewGkArray newGkArray) {
		this.newGkArray = newGkArray;
	}

	public String[] getDelTimeTableOtherIds() {
		return delTimeTableOtherIds;
	}

	public void setDelTimeTableOtherIds(String[] delTimeTableOtherIds) {
		this.delTimeTableOtherIds = delTimeTableOtherIds;
	}

	public List<NewGkTimetableTeacher> getUpdateTeacherList() {
		return updateTeacherList;
	}

	public void setUpdateTeacherList(List<NewGkTimetableTeacher> updateTeacherList) {
		this.updateTeacherList = updateTeacherList;
	}
	
}
