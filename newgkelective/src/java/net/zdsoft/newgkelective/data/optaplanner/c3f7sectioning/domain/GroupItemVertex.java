package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain;

import java.util.HashSet;
import java.util.Set;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.CalculateSections;

public class GroupItemVertex {
	Curriculum  baseCurriculum;			//fact
	private int groupCount;				//fact, based on the above curriculum only

	Set<Curriculum>	 curriculumGroup;   //baseCurriculum and some other curriculum that share 2A with baseCurriculum
	
	public GroupItemVertex (Curriculum c) {
		baseCurriculum = c;
		groupCount = CalculateSections.calculateSectioning(c.getStudentCount(), c.getSectionSizeMean(), c.getSectionSizeMargin()).size();
		
		curriculumGroup = new HashSet<Curriculum>();
		curriculumGroup.add(baseCurriculum);
	}
	
	public void addCurriculum(Curriculum c) {
		curriculumGroup.add(c);
	}
	
	public Set<Curriculum> getCurriculumGroup () {
		return curriculumGroup;
	}
}
