package net.zdsoft.newgkelective.data.optaplanner2.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.zdsoft.newgkelective.data.optaplanner2.domain.AbstractPersistable;
import net.zdsoft.newgkelective.data.optaplanner2.domain.ArrangeConstantInfo;
import net.zdsoft.newgkelective.data.optaplanner2.domain.ArrangeStudent;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

@PlanningSolution
public class SelectionScheduleSolution extends AbstractPersistable implements Solution<HardMediumSoftScore> {
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private ArrangeConstantInfo constantInfo;
//    private List<ArrangeClassRoom> classRoomList;
    
    private List<ArrangeStudent> studentList;
    
    private HardMediumSoftScore score;
    
    @Override
    public Collection<? extends Object> getProblemFacts() {
        List<Object> facts = new ArrayList<Object>();
//        facts.addAll(classRoomList);
        facts.addAll(studentList);
        facts.add(constantInfo);
        return facts;
    }
    
    /**
	 * 获取name
	 * @return name
	 */
	public String getName() {
	    return name;
	}

	/**
	 * 设置name
	 * @param name name
	 */
	public void setName(String name) {
	    this.name = name;
	}


	/**
	 * 获取constantInfo
	 * @return constantInfo
	 */
	public ArrangeConstantInfo getConstantInfo() {
	    return constantInfo;
	}

	/**
	 * 设置constantInfo
	 * @param constantInfo constantInfo
	 */
	public void setConstantInfo(ArrangeConstantInfo constantInfo) {
	    this.constantInfo = constantInfo;
	}

	/**
	 * 获取studentList
	 * @return studentList
	 */
	@PlanningEntityCollectionProperty
	public List<ArrangeStudent> getStudentList() {
	    return studentList;
	}

	/**
	 * 设置studentList
	 * @param studentList studentList
	 */
	public void setStudentList(List<ArrangeStudent> studentList) {
	    this.studentList = studentList;
	}
	
	/**
	 * 获取score
	 * @return score
	 */
	@Override
	public HardMediumSoftScore getScore() {
	    return score;
	}

	/**
	 * 设置score
	 * @param score score
	 */
	@Override
	public void setScore(HardMediumSoftScore score) {
	    this.score = score;
	}
}
