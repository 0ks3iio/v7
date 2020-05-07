package net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequiredParm {
	
	private int xzbNum;//剩余的学生中开设班级数量,基本保持xzbNum数量
	
	private int singleNum;//单科最大开设班级数量(包括已经手动安排2+x剩余的x)
	
//	private int minReguired;//下限
//	
//	private int canAddReguired;//最大可调整人数
	
	private int bestClassStuNum;//班级平均人数
	//key:subjectId    已经手动安排2+x剩余的x的学生
	private Map<String,Set<ChooseStudent>> otherChooseStudentMap=new HashMap<String,Set<ChooseStudent>>();
	public int getXzbNum() {
		return xzbNum;
	}
	public void setXzbNum(int xzbNum) {
		this.xzbNum = xzbNum;
	}
	public int getSingleNum() {
		return singleNum;
	}
	public void setSingleNum(int singleNum) {
		this.singleNum = singleNum;
	}
//	public int getMinReguired() {
//		return minReguired;
//	}
//	public void setMinReguired(int minReguired) {
//		this.minReguired = minReguired;
//	}
//	public int getCanAddReguired() {
//		return canAddReguired;
//	}
//	public void setCanAddReguired(int canAddReguired) {
//		this.canAddReguired = canAddReguired;
//	}
	public Map<String, Set<ChooseStudent>> getOtherChooseStudentMap() {
		return otherChooseStudentMap;
	}
	public void setOtherChooseStudentMap(
			Map<String, Set<ChooseStudent>> otherChooseStudentMap) {
		this.otherChooseStudentMap = otherChooseStudentMap;
	}
	public int getBestClassStuNum() {
		return bestClassStuNum;
	}
	public void setBestClassStuNum(int bestClassStuNum) {
		this.bestClassStuNum = bestClassStuNum;
	}
	
	
}
