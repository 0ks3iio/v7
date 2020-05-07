package net.zdsoft.newgkelective.data.optaplanner.domain.biweekly;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningEntityProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;

import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;

@PlanningSolution
public class WeeklySolution {
	
	private List<Section> sectionList;
	
	private List<Integer> weekTypeRange;

	private Constants constants;
	
	@PlanningScore(bendableHardLevelsSize=1,bendableSoftLevelsSize=1)
	private BendableScore score;
	
	@PlanningEntityCollectionProperty
	public List<Section> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<Section> sectionList) {
		this.sectionList = sectionList;
	}

	@ProblemFactCollectionProperty
	@ValueRangeProvider(id="weekTypeProvider")
	public List<Integer> getWeekTypeRange() {
		return weekTypeRange;
	}

	public void setWeekTypeRange(List<Integer> weekTypeRange) {
		this.weekTypeRange = weekTypeRange;
	}

	public BendableScore getScore() {
		return score;
	}

	public void setScore(BendableScore score) {
		this.score = score;
	}

	public void printResult() {
		System.out.println("教师单双周情况");
		Map<String, List<Section>> collect = sectionList.stream().filter(s->s.getTeacherCode()!=null).collect(Collectors.groupingBy(Section::getTeacherCode));
		for (List<Section> ss : collect.values()) {
			int s = (int)ss.stream().filter(e->e.getIsBiweekly() == CGCourse.WEEK_TYPE_EVEN).count();
			int d = (int)ss.stream().filter(e->e.getIsBiweekly() == CGCourse.WEEK_TYPE_ODD).count();
			
			System.out.println("单： "+d+"  双 ： "+s);
		}
		
		System.out.println("班级单双周情况");
		Map<String, List<Section>> collect2 = sectionList.stream().collect(Collectors.groupingBy(Section::getOldId));
		for (List<Section> ss : collect2.values()) {
			int s = (int)ss.stream().filter(e->e.getIsBiweekly() == CGCourse.WEEK_TYPE_EVEN).count();
			int d = (int)ss.stream().filter(e->e.getIsBiweekly() == CGCourse.WEEK_TYPE_ODD).count();
			
			System.out.println("单： "+d+"  双 ： "+s);
		}
		
	}

	@ProblemFactProperty
	public Constants getConstants() {
		return constants;
	}

	public void setConstants(Constants constants) {
		this.constants = constants;
	}
}
