package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import net.zdsoft.newgkelective.data.optaplanner.util.AbstractPersistable;

public class CGCourseIntervalConstraint extends AbstractPersistable {
	private CGCourseSection leftSection;
	private CGCourseSection rightSection;

	private Float interval;

	public CGCourseIntervalConstraint(CGCourseSection leftSection, CGCourseSection rightSection, Float interval) {
		super();
		this.leftSection = leftSection;
		this.rightSection = rightSection;
		this.interval = interval;
	}

	public CGCourseIntervalConstraint() {
		super();
	}

	public CGCourseSection getLeftSection() {
		return leftSection;
	}

	public void setLeftSection(CGCourseSection leftSection) {
		this.leftSection = leftSection;
	}

	public CGCourseSection getRightSection() {
		return rightSection;
	}

	public void setRightSection(CGCourseSection rightSection) {
		this.rightSection = rightSection;
	}

	public Float getInterval() {
		return interval;
	}

	public void setInterval(Float interval) {
		this.interval = interval;
	}
}
