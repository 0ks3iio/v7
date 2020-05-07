package net.zdsoft.newgkelective.data.optaplanner.domain.batch;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("BatchScheduleSolution")
@PlanningSolution
public class BatchScheduleSolution implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer workdayCount = 5;
	private String arrayItemId;
	
	private List<BatchEntity> batchEntityList;
	
	private List<BatchLecture> batchLectureList;
	
	private List<BatchPeriod> periodList;
	
	@XStreamOmitField
	@PlanningScore(bendableHardLevelsSize=2,bendableSoftLevelsSize=2)
	private BendableScore score;

	@ProblemFactCollectionProperty
	public List<BatchEntity> getBatchEntityList() {
		return batchEntityList;
	}

	public void setBatchEntityList(List<BatchEntity> batchEntityList) {
		this.batchEntityList = batchEntityList;
	}

	@PlanningEntityCollectionProperty
	public List<BatchLecture> getBatchLectureList() {
		return batchLectureList;
	}

	public void setBatchLectureList(List<BatchLecture> batchLectureList) {
		this.batchLectureList = batchLectureList;
	}

	public List<BatchPeriod> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(List<BatchPeriod> periodList) {
		this.periodList = periodList;
	}

	public BendableScore getScore() {
		return score;
	}

	public void setScore(BendableScore score) {
		this.score = score;
	}

	public String getArrayItemId() {
		return arrayItemId;
	}

	public void setArrayItemId(String arrayItemId) {
		this.arrayItemId = arrayItemId;
	}
	
	public void printResult() {
		System.out.println("\n.........Result start........");
		Map<BatchEntity, List<BatchLecture>> collect = batchLectureList.stream().collect(Collectors.groupingBy(BatchLecture::getBatchEntity));
		for (BatchEntity en : collect.keySet()) {
			System.out.println(en.getBatchStr());
			collect.get(en).forEach(e->System.out.println("\t"+e.getPeriod().getPeriodCode()));
		}
		System.out.println("End..");
	}

	@ProblemFactProperty
	public Integer getWorkdayCount() {
		return workdayCount;
	}

	public void setWorkdayCount(Integer workdayCount) {
		this.workdayCount = workdayCount;
	}
}
