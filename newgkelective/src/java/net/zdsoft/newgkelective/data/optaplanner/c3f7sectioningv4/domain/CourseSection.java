package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

import com.google.common.collect.Sets;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common.AbstractPersistable;

@PlanningEntity
public class CourseSection extends AbstractPersistable{
	V4Course  	course;
	int			indexInCourse;
	
	int			avgCourseScore;		//正常的平均成绩 * 100， 相当于保留小数点后2位。转化为整数，计算快一些。

	boolean 	isBalancingOnScore; //一个控制开关，如果是true，表示要求按平均成绩来衡量平行班的分班质量
	
	int			avgSectionSize;
	int			maxSectionSize;
	
	List<CurriculumCourse>	curriculumCourseList = new ArrayList<>(); //算法给定
	
	@InverseRelationShadowVariable(sourceVariableName = "courseSection")
	public List<CurriculumCourse> getCurriculumCourseList () {
		return curriculumCourseList;
	}

	public CourseSection() {
		super();
	}
	
	public CourseSection(V4Course course, int indexInCourse, int avgCourseScore, boolean isBalancingOnScore,
			int avgSectionSize, int maxSectionSize) {
		super();
		this.course = course;
		this.indexInCourse = indexInCourse;
		this.avgCourseScore = avgCourseScore;
		this.isBalancingOnScore = isBalancingOnScore;
		this.avgSectionSize = avgSectionSize;
		this.maxSectionSize = maxSectionSize;
	}
	
	public void printCourseSection () {
		System.out.print(course.getCourseCode() + "-" + indexInCourse + ": (avg, max, current) = " + "(" + avgSectionSize + ", " + maxSectionSize + ", " + getCurrentStudentCount() + "): ");
		curriculumCourseList.stream().forEach(e -> System.out.print(e.getStudentCurriculum().getCurriculum().getCurriculumCode() + ": " + e.getStudentCount() + ", "));
		System.out.println("");
	}
	
	public V4Course getCourse() {
		return course;
	}

	public void setCourse(V4Course course) {
		this.course = course;
	}

	public int getIndexInCourse() {
		return indexInCourse;
	}

	public void setIndexInCourse(int indexInCourse) {
		this.indexInCourse = indexInCourse;
	}

	public int getAvgCourseScore() {
		return avgCourseScore;
	}

	public void setAvgCourseScore(int avgScore) {
		this.avgCourseScore = avgScore;
	}

	public boolean isBalancingOnScore() {
		return isBalancingOnScore;
	}

	public void setBalancingOnScore(boolean isBalancingOnScore) {
		this.isBalancingOnScore = isBalancingOnScore;
	}

	public int getAvgSectionSize() {
		return avgSectionSize;
	}

	public void setAvgSectionSize(int avgSectionSize) {
		this.avgSectionSize = avgSectionSize;
	}

	public void setCurriculumCourseList(List<CurriculumCourse> curriculumCourseList) {
		this.curriculumCourseList = curriculumCourseList;
	}
	
	public int getCurrentStudentCount () {
		return curriculumCourseList.stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x + y);
	}
	
	public int getCurrentAvgCourseScore () {
		int totalCourseScore = 0;
		for (CurriculumCourse cc : curriculumCourseList) {
			totalCourseScore += cc.getTotalCourseScore();
		}
		return totalCourseScore / getCurrentStudentCount();
		
	}
	
	public Set<V4Student> getCurrentStudentSet () {
		Set<V4Student> studentSet = new HashSet<>();
		
		curriculumCourseList.stream().forEach(e -> studentSet.addAll(e.getStudentList()));
		
		return studentSet;
		
	}
	public Set<String> getCurrentStuIds(){
		return getCurrentStudentSet().stream().map(e->e.getStudentId()+"").collect(Collectors.toSet());
	}
	public BitSet getCurrentStudentBitSet () {
		BitSet studentBitSet = new BitSet();
		
		for (CurriculumCourse cc : curriculumCourseList) {
			cc.getStudentList().forEach(e -> studentBitSet.set(e.getId()));
		}
		
		return studentBitSet;
	}
	
	public static int getCourseSectionConflictCount (CourseSection section1, CourseSection section2) {
		Set<V4Student> studentSet1 = section1.getCurrentStudentSet();
		Set<V4Student> studentSet2 = section2.getCurrentStudentSet();

		return Sets.intersection(studentSet1, studentSet2).size();
	}

	public int getMaxSectionSize() {
		return maxSectionSize;
	}

	public void setMaxSectionSize(int maxSectionSize) {
		this.maxSectionSize = maxSectionSize;
	}

	public void addCurriculumCourse(CurriculumCourse curriCourse) {
		if(curriculumCourseList == null) {
			curriculumCourseList = new ArrayList<>();
		}
		curriculumCourseList.add(curriCourse);
	}
	
	
}
