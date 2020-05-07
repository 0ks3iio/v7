package net.zdsoft.gkelective.data.action.optaplanner.move;

import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeStudent;
import net.zdsoft.gkelective.data.action.optaplanner.solver.SelectionScheduleSolution;

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
