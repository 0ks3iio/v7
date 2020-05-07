package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkTimetable;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;

public class NewGkTimetableSaveDto {
	private String arrayId;
	private List<NewGkTimetable> insertTimeTableList=new ArrayList<NewGkTimetable>();
	private List<NewGkTimetableOther> insertOtherList=new ArrayList<NewGkTimetableOther>();
	private List<NewGkTimetableTeacher> insertTeacherList=new ArrayList<NewGkTimetableTeacher>();
	
	//旧数据删除
	private String[] timeTableIds;

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
	
}
