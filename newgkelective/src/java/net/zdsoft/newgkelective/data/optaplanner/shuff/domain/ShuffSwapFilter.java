package net.zdsoft.newgkelective.data.optaplanner.shuff.domain;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class ShuffSwapFilter implements SelectionFilter<ShuffleSolution, SwapMove> {
	public boolean accept(ScoreDirector<ShuffleSolution> scoreDirector, SwapMove move) {
		GroupSubject leftStudentCourse = (GroupSubject) move.getLeftEntity();
		GroupSubject rightStudentCourse = (GroupSubject) move.getRightEntity();
		return leftStudentCourse.getGroup().getId() == rightStudentCourse.getGroup().getId();
	}
}
