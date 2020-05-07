package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain;

import java.util.ArrayList;
import java.util.List;

public class SmallCurriculum {
	Curriculum curriculum;
	Processing2X processing2X;
	
	public SmallCurriculum(Curriculum c, Processing2X p2X) {
		this.curriculum = c;
		this.processing2X = p2X;
	}
	
	public void setCurriculum (Curriculum c) {
		this.curriculum = c;
	}

	public Curriculum getCurriculum () {
		return this.curriculum;
	}

	public int getStudentCount() {
		return curriculum.getStudentCount();
	}
	
	public List<CourseSection> generateSectionList () {
		List<CourseSection> sectionList = new ArrayList<>();
		curriculum.getAllCourseNameSet().stream().forEach(e -> {
				CourseSection cs = new CourseSection(processing2X.getCourseMap().get(e)); 
				cs.addStudentListAll(curriculum.getStudentList());
				sectionList.add(cs);
		    });
		return sectionList;
	}

	public Processing2X getProcessing2X() {
		return processing2X;
	}

	public void setProcessing2X(Processing2X processing2x) {
		processing2X = processing2x;
	}
	
	
}
