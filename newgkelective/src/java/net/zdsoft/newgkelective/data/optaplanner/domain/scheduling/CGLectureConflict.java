package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import net.zdsoft.newgkelective.data.optaplanner.util.AbstractPersistable;

public class CGLectureConflict extends AbstractPersistable{
	private int leftLectureId;
	private int rightLectureId;
	
	// 1,两个lecture必须在同一天 或者 隔一天;  -1，两个lecture不能在同一时间上课  ; 2.必须同一时间
	private int constraint;
	
	public int getLeftLectureId() {
		return leftLectureId;
	}
	public void setLeftLectureId(int leftLectureId) {
		this.leftLectureId = leftLectureId;
	}
	public int getRightLectureId() {
		return rightLectureId;
	}
	public void setRightLectureId(int rightLectureId) {
		this.rightLectureId = rightLectureId;
	}
	public CGLectureConflict(int leftLectureId, int rightLectureId) {
		super();
		this.leftLectureId = leftLectureId;
		this.rightLectureId = rightLectureId;
	}
	public CGLectureConflict(int leftLectureId, int rightLectureId, int constraint) {
		super();
		this.leftLectureId = leftLectureId;
		this.rightLectureId = rightLectureId;
		this.constraint = constraint;
	}
	public int getConstraint() {
		return constraint;
	}
	public void setConstraint(int constraint) {
		this.constraint = constraint;
	}
}
