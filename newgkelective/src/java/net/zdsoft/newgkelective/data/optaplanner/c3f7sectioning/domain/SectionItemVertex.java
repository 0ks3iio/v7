package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

public class SectionItemVertex {
	Set<CourseSection>	sectionGroup;
	
	public SectionItemVertex(CourseSection aSection) {
		sectionGroup = new HashSet<>();
		sectionGroup.add(aSection);
	}
	
	public SectionItemVertex(List<CourseSection> sectionList) {
		sectionGroup = new HashSet<>();
		sectionGroup.addAll(sectionList);
	}
	
	public CourseSection generateCombinedSection () {
		CourseSection resultSection = new CourseSection();
		Course c;
		for (CourseSection cs : sectionGroup) {
			c = cs.getCourse();
			resultSection.setCourse(c);
			resultSection.addStudentListAll(cs.getStudentList());
		}
		
		return resultSection;
	}
	
	public int getSquredScore (int maxSectionSize) {
		return (maxSectionSize - getSectionStudentCount()) * (maxSectionSize - getSectionStudentCount());
	}
	
	public void addSectionItem(CourseSection aSection) {
		sectionGroup.add(aSection);
	}
	
	public int getSectionStudentCount () {
		int cnt = 0;
		for (CourseSection s : sectionGroup) {
			cnt += s.getStudentCount();
		}
		
		return cnt;
	}
	
	public Set<CourseSection> getSectionGroup (){
		return sectionGroup;
	}
	
	public boolean isIntersected (SectionItemVertex otherVertex) {
		Set<CourseSection> sharedVertex = Sets.intersection(sectionGroup, otherVertex.getSectionGroup());
		
		if (sharedVertex.size() > 0)
			return true;
		else
			return false;
	}
}
