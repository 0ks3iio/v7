package net.zdsoft.gkelective.data.action.optaplanner.domain;

import java.util.List;
import java.util.Set;

import net.zdsoft.gkelective.data.action.optaplanner.solver.StudentDifficultyWeightFactory;
import net.zdsoft.gkelective.data.dto.StudentSubjectDto;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;


@PlanningEntity(difficultyWeightFactoryClass = StudentDifficultyWeightFactory.class)
public class ArrangeStudent extends AbstractPersistable {
	private static final long serialVersionUID = 1L;
	
    // -----以下必填-----
    /* 选考科目 */
    private Set<String> chooseSubjectIds;
    /* 所有科目 */
    private List<String> allSubjectList;
    
 // -----以下不用填-----
    /* subjectId + type */
    private List<String> allSubjectIdTypeList;
    private Set<String> allSubjectIdTypeSet;
    private ArrangeSubjectBatch subjectBatch;
    private List<ArrangeSubjectBatch> availableBatchIndexsList;
    
    // -----以下选填-----
    private StudentSubjectDto studentSubjectDto;
    // 学生id
	private String studentId;
    // 原行政班id
	private String classId;
    //0男 1女
    private int sex;
    // 新组建行政班级
    private String newClassId;

	/**
	 * 获取allSubjectList
	 * @return allSubjectList
	 */
	public List<String> getAllSubjectList() {
	    return allSubjectList;
	}

	/**
	 * 设置allSubjectList
	 * @param allSubjectList allSubjectList
	 */
	public void setAllSubjectList(List<String> allSubjectList) {
	    this.allSubjectList = allSubjectList;
	}

	/**
	 * 获取allSubjectIdTypeList
	 * @return allSubjectIdTypeList
	 */
	public List<String> getAllSubjectIdTypeList() {
	    return allSubjectIdTypeList;
	}

	/**
	 * 设置allSubjectIdTypeList
	 * @param allSubjectIdTypeList allSubjectIdTypeList
	 */
	public void setAllSubjectIdTypeList(List<String> allSubjectIdTypeList) {
	    this.allSubjectIdTypeList = allSubjectIdTypeList;
	}

	/**
	 * 获取subjectBatch
	 * @return subjectBatch
	 */
	@PlanningVariable(valueRangeProviderRefs = { "availableBatchIndexsList" }, nullable = false)
	public ArrangeSubjectBatch getSubjectBatch() {
	    return subjectBatch;
	}

	
	/**
	 * 设置subjectBatch
	 * @param subjectBatch subjectBatch
	 */
	public void setSubjectBatch(ArrangeSubjectBatch subjectBatch) {
	    this.subjectBatch = subjectBatch;
	}

	/**
	 * 获取availableBatchIndexsList
	 * @return availableBatchIndexsList
	 */
	@ValueRangeProvider(id="availableBatchIndexsList")
	public List<ArrangeSubjectBatch> getAvailableBatchIndexsList() {
	    return availableBatchIndexsList;
	}

	/**
	 * 设置availableBatchIndexsList
	 * @param availableBatchIndexsList availableBatchIndexsList
	 */
	public void setAvailableBatchIndexsList(List<ArrangeSubjectBatch> availableBatchIndexsList) {
	    this.availableBatchIndexsList = availableBatchIndexsList;
	}

	/**
     * 获取studentSubjectDto
     * @return studentSubjectDto
     */
    public StudentSubjectDto getStudentSubjectDto() {
        return studentSubjectDto;
    }

    /**
     * 设置studentSubjectDto
     * @param studentSubjectDto studentSubjectDto
     */
    public void setStudentSubjectDto(StudentSubjectDto studentSubjectDto) {
        this.studentSubjectDto = studentSubjectDto;
    }

    /**
	 * 获取studentId
	 * @return studentId
	 */
	public String getStudentId() {
	    return studentId;
	}

	/**
	 * 设置studentId
	 * @param studentId studentId
	 */
	public void setStudentId(String studentId) {
	    this.studentId = studentId;
	}

	/**
	 * 获取chooseSubjectIds
	 * @return chooseSubjectIds
	 */
	public Set<String> getChooseSubjectIds() {
	    return chooseSubjectIds;
	}

	/**
	 * 设置chooseSubjectIds
	 * @param chooseSubjectIds chooseSubjectIds
	 */
	public void setChooseSubjectIds(Set<String> chooseSubjectIds) {
	    this.chooseSubjectIds = chooseSubjectIds;
	}

	/**
	 * 获取classId
	 * @return classId
	 */
	public String getClassId() {
	    return classId;
	}

	/**
	 * 设置classId
	 * @param classId classId
	 */
	public void setClassId(String classId) {
	    this.classId = classId;
	}
	
	/**
	 * 获取sex
	 * @return sex
	 */
	public int getSex() {
	    return sex;
	}
	
	/**
	 * 设置sex
	 * @param sex sex
	 */
	public void setSex(int sex) {
	    this.sex = sex;
	}

	/**
	 * 获取allSubjectIdTypeSet
	 * @return allSubjectIdTypeSet
	 */
	public Set<String> getAllSubjectIdTypeSet() {
	    return allSubjectIdTypeSet;
	}

	/**
	 * 设置allSubjectIdTypeSet
	 * @param allSubjectIdTypeSet allSubjectIdTypeSet
	 */
	public void setAllSubjectIdTypeSet(Set<String> allSubjectIdTypeSet) {
	    this.allSubjectIdTypeSet = allSubjectIdTypeSet;
	}

    /**
     * 获取newClassId
     * @return newClassId
     */
    public String getNewClassId() {
        return newClassId;
    }

    /**
     * 设置newClassId
     * @param newClassId newClassId
     */
    public void setNewClassId(String newClassId) {
        this.newClassId = newClassId;
    }


}
