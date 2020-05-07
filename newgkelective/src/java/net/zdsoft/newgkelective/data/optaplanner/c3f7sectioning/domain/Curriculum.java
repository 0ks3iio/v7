package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.AbstractPersistable;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.CalculateSections;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain.Student;

public class Curriculum extends AbstractPersistable {
	private String  code;  						//3 A course names glued together
	private List<Student> 		studentList;			//all the students in this curriculum

	private Set<String>	aCourseNameSet;
	private Set<String>	bCourseNameSet;
	private Set<String>	allCourseNameSet;
	
	private int sectionSizeMean;
	private int sectionSizeMargin;
	

	public Curriculum(String curriculumCode, List<Student> students) {
		this.code = curriculumCode;
		studentList = students;

		aCourseNameSet = students.get(0).getACourseNameSet();
		bCourseNameSet = students.get(0).getBCourseNameSet();
		allCourseNameSet = students.get(0).getAllCourseNameSet();
	}
	
	public int sharedCourseCount (Curriculum other) {
		return Sets.intersection(this.getAllCourseNameSet(), other.getAllCourseNameSet()).size();
	}
	
	public int getSectionCount (int maxSectionSize) {
		return CalculateSections.calculateSectioning(studentList.size(), maxSectionSize, 0).size();
	}
	
    @Override
    public String toString() {
        return code;
    }

    public int getMarginCountAsBigCurriculum () {
		int studentCount = getStudentCount();
		int sectionGroupCount = CalculateSections.calculateSectioning(studentCount, sectionSizeMean, sectionSizeMargin).size();
		int marginCount = (sectionSizeMean + sectionSizeMargin) * sectionGroupCount - studentCount;    
		
		return marginCount;
    }

    public int getSurplusCountAsSmallCurriculum () {
		int marginCount = getMarginCountAsBigCurriculum();
		int surplusCount = (sectionSizeMean + sectionSizeMargin) - marginCount;
		
		if (marginCount == 0) {
			surplusCount = 0;
		}
		
		return surplusCount;
    }
    
    public boolean isMatched2X (Curriculum c2) {
    	return Sets.intersection(aCourseNameSet, c2.getaCourseNameSet()).size() == 2;
    }
    
	public boolean containsCourseByName(String coursename) {
		if (allCourseNameSet.contains(coursename)) {
			return true;
		}
		else {
			return false;
		}
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public int getStudentCount() {
		return studentList.size();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<String> getaCourseNameSet() {
		return aCourseNameSet;
	}

	public void setaCourseNameSet(Set<String> aCourseNameSet) {
		this.aCourseNameSet = aCourseNameSet;
	}

	public Set<String> getbCourseNameSet() {
		return bCourseNameSet;
	}

	public void setbCourseNameSet(Set<String> bCourseNameSet) {
		this.bCourseNameSet = bCourseNameSet;
	}

	public Set<String> getAllCourseNameSet() {
		return allCourseNameSet;
	}

	public void setAllCourseNameSet(Set<String> allCourseNameSet) {
		this.allCourseNameSet = allCourseNameSet;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}
	
	public int getSectionSizeMean() {
		return sectionSizeMean;
	}

	public void setSectionSizeMean(int sectionSizeMean) {
		this.sectionSizeMean = sectionSizeMean;
	}

	public int getSectionSizeMargin() {
		return sectionSizeMargin;
	}

	public void setSectionSizeMargin(int sectionSizeMargin) {
		this.sectionSizeMargin = sectionSizeMargin;
	}
	
	public int getSectionSizeMax() {
		return sectionSizeMean + sectionSizeMargin;
	}
}
