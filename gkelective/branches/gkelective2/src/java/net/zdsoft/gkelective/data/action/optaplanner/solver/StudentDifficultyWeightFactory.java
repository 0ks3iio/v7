package net.zdsoft.gkelective.data.action.optaplanner.solver;

import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeStudent;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

public class StudentDifficultyWeightFactory implements SelectionSorterWeightFactory<SelectionScheduleSolution, ArrangeStudent> {

    public Comparable createSorterWeight(SelectionScheduleSolution selectionSchedule, ArrangeStudent student) {
        return new StudentDifficultyWeight(student);
    }

    public static class StudentDifficultyWeight implements Comparable<StudentDifficultyWeight> {

        private final ArrangeStudent student;

        public StudentDifficultyWeight(ArrangeStudent student) {
            this.student = student;
        }

        public int compareTo(StudentDifficultyWeight other) {
            return new CompareToBuilder()
                    .append(other.student.getAvailableBatchIndexsList().size(), student.getAvailableBatchIndexsList().size())
                    .append(student.getId(), other.student.getId())
                    .toComparison();
        }

    }

}
