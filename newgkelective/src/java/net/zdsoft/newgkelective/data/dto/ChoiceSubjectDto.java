package net.zdsoft.newgkelective.data.dto;

import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;

import java.util.List;
import java.util.Set;

public class ChoiceSubjectDto {
	private String ids;
	private String shortNames;
	private String state;// 0代表已经选择 1代表未选择

	// 3+1+2分班模式扩充
	private int totalNum;
	private int unSettledNum;
	private Set<String> studentIds;
	private List<NewGkDivideClass> classList;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getShortNames() {
		return shortNames;
	}

	public void setShortNames(String shortNames) {
		this.shortNames = shortNames;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getUnSettledNum() {
		return unSettledNum;
	}

	public void setUnSettledNum(int unSettledNum) {
		this.unSettledNum = unSettledNum;
	}

	public Set<String> getStudentIds() {
		return studentIds;
	}

	public void setStudentIds(Set<String> studentIds) {
		this.studentIds = studentIds;
	}

	public List<NewGkDivideClass> getClassList() {
		return classList;
	}

	public void setClassList(List<NewGkDivideClass> classList) {
		this.classList = classList;
	}

	@Override
	public String toString() {
		return "ChoiceSubjectDto{" +
				"ids='" + ids + '\'' +
				", shortNames='" + shortNames + '\'' +
				", state='" + state + '\'' +
				", totalNum=" + totalNum +
				", unSettledNum=" + unSettledNum +
				", studentIds=" + studentIds +
				", classList=" + classList +
				'}';
	}

}
