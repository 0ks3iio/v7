package net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.moveFilter;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain.SectioningSolution;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain.StudentCourse;

public class SameStudentCourseSwapFilter implements SelectionFilter<SectioningSolution, SwapMove> {
	  
	@Override
	    public boolean accept(ScoreDirector<SectioningSolution> scoreDirector, SwapMove move) {
	        StudentCourse leftStudentCourse = (StudentCourse) move.getLeftEntity();
	        StudentCourse rightStudentCourse = (StudentCourse) move.getRightEntity();
	        if (leftStudentCourse.getStudent().getId() == rightStudentCourse.getStudent().getId())
	        	return true;
	        else
	        	return false;
	    }
}
