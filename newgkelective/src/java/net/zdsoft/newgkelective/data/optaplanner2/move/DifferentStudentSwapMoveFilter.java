package net.zdsoft.newgkelective.data.optaplanner2.move;

import net.zdsoft.newgkelective.data.optaplanner2.domain.ArrangeStudent;
import net.zdsoft.newgkelective.data.optaplanner2.solver.SelectionScheduleSolution;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class DifferentStudentSwapMoveFilter implements SelectionFilter<SelectionScheduleSolution,SwapMove> {

    public boolean accept(ScoreDirector scoreDirector, SwapMove move) {
    	ArrangeStudent leftArrangeStudent = (ArrangeStudent) move.getLeftEntity();
    	ArrangeStudent rightArrangeStudent = (ArrangeStudent) move.getRightEntity();
    	return !leftArrangeStudent.getAllSubjectIdTypeSet().equals(rightArrangeStudent.getAllSubjectIdTypeSet());
    }

}
