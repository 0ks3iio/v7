package net.zdsoft.newgkelective.data.optaplanner.util;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudentCourseSchedule;

public class CGMoveableFilter implements SelectionFilter<CGStudentCourseSchedule, CGSectionLecture>{

	@Override
	public boolean accept(ScoreDirector<CGStudentCourseSchedule> scoreDirector, CGSectionLecture selection) {
		return selection.isMoveable();
	}

}
