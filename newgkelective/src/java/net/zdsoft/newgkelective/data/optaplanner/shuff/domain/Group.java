package net.zdsoft.newgkelective.data.optaplanner.shuff.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 某个班级下的组合
 *
 */
public class Group extends AbstractPersistable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GroupClass groupClass;
	String nameSubIds;//只用于选课组合显示
	List<String> subjectIds=new ArrayList<>();//需要开设的科目
	public GroupClass getGroupClass() {
		return groupClass;
	}
	public void setGroupClass(GroupClass groupClass) {
		this.groupClass = groupClass;
	}
	public List<String> getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(List<String> subjectIds) {
		this.subjectIds = subjectIds;
	}
	
	public String getNameSubIds() {
		return nameSubIds;
	}
	public void setNameSubIds(String nameSubIds) {
		this.nameSubIds = nameSubIds;
	}
}
