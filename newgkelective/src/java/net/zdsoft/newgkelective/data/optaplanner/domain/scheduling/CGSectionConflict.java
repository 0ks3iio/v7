package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CGSectionConflict")
public class CGSectionConflict implements Serializable{
	private CGCourseSection leftCourseSection;
	private CGCourseSection rightCourseSection;
	private int conflictCount;
	
	public CGSectionConflict() {
		super();
	}
	public CGSectionConflict(CGCourseSection left, CGCourseSection right, int count) {
		this.leftCourseSection = left;
		this.rightCourseSection = right;
		this.conflictCount = count;
	}
	
	public CGCourseSection getLeftCourseSection() {
		return leftCourseSection;
	}
	public void setLeftCourseSection(CGCourseSection leftCourseSection) {
		this.leftCourseSection = leftCourseSection;
	}
	public CGCourseSection getRightCourseSection() {
		return rightCourseSection;
	}
	public void setRightCourseSection(CGCourseSection rightCourseSection) {
		this.rightCourseSection = rightCourseSection;
	}
	public int getConflictCount() {
		return conflictCount;
	}
	public void setConflictCount(int conflictCount) {
		this.conflictCount = conflictCount;
	}

}
