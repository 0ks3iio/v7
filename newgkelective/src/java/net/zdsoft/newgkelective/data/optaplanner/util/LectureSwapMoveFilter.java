package net.zdsoft.newgkelective.data.optaplanner.util;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudentCourseSchedule;

public class LectureSwapMoveFilter implements SelectionFilter<CGStudentCourseSchedule, SwapMove<CGStudentCourseSchedule>> {

	@Override
	public boolean accept(ScoreDirector<CGStudentCourseSchedule> scoreDirector,
			SwapMove<CGStudentCourseSchedule> selection) {
		CGSectionLecture leftEntity = (CGSectionLecture)selection.getLeftEntity();
		CGSectionLecture rightEntity = (CGSectionLecture)selection.getRightEntity();
		
		if(leftEntity.getCourseSection().equals(rightEntity.getCourseSection()) && leftEntity.getTimeSlotCount() == rightEntity.getTimeSlotCount()) {
			return false;
		}
		return true;
	}


}
