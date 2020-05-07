package net.zdsoft.newgkelective.data.optaplanner.shuff.domain;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;

import net.zdsoft.newgkelective.data.optaplanner.shuff.api.ShuffleResult;

@PlanningSolution
public class ShuffleSolution extends AbstractPersistable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String appName;
	private List<Group> groupList;
	private List<GroupClass> groupClassList;
	private List<Subject> subjectList;
	
	private List<GroupSubject> groupSubjectList;
	private List<TimeSlot> timeSlotList;
	
	//hard1:一种组合的各科目批次点不能重复 hard2:2+x或者3+0 学生公用科目批次点必须一样 hard1 混合同种组合的批次点必须一样
	//soft1:每个批次下各科目人数尽量平均（后面用开设班级数）
	@PlanningScore(bendableHardLevelsSize = 2, bendableSoftLevelsSize = 2)
	private BendableScore score;

	public ShuffleSolution() {
		
	}
	
	
	
	public BendableScore getScore() {
		return this.score;
	}
	public void setScore(BendableScore score) {
		this.score = score;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public List<Group> getGroupList() {
		return groupList;
	}


	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	//@ProblemFactCollectionProperty 规则文件中可以直接获取该对象的集合
	@ProblemFactCollectionProperty
	public List<GroupClass> getGroupClassList() {
		return groupClassList;
	}


	public void setGroupClassList(List<GroupClass> groupClassList) {
		this.groupClassList = groupClassList;
	}

	
	public List<Subject> getSubjectList() {
		return subjectList;
	}


	public void setSubjectList(List<Subject> subjectList) {
		this.subjectList = subjectList;
	}
	
	 @PlanningEntityCollectionProperty
	public List<GroupSubject> getGroupSubjectList() {
		return groupSubjectList;
	}


	public void setGroupSubjectList(List<GroupSubject> groupSubjectList) {
		this.groupSubjectList = groupSubjectList;
	}


	@PlanningEntityCollectionProperty
	public List<TimeSlot> getTimeSlotList() {
		return this.timeSlotList;
	}

	public void setTimeSlotList(List<TimeSlot> timeSlotList) {
		this.timeSlotList = timeSlotList;
	}
	/**
	 * 班级id 组合ids 科目 批次点(1,2,3)
	 */
	public List<ShuffleResult> composeResult() {
		List<ShuffleResult> result=new ArrayList<>();
		for(GroupSubject g:groupSubjectList) {
			ShuffleResult re=new ShuffleResult();
			re.setClassId(g.getGroup().getGroupClass().getClassId());
			re.setNameSubjectIds(g.getGroup().getNameSubIds());
			re.setSubjectId(g.getSubject().getSubjectId());
			re.setBath(g.getTimeSlot().getId());
			result.add(re);
		}
		return result;
	}

}
