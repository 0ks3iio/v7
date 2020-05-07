package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;

import com.google.common.collect.Sets;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@PlanningSolution
@XStreamAlias("CGStudentCourseSchedule")
public class CGStudentCourseSchedule implements Serializable{
	
	private List<CGStudent> studentList;
	private List<CGCourse>  courseList;
	private List<CGPeriod>  periodList;
	private List<CGRoom>    roomList;
	private List<CGTeacher> teacherList;
	
	private List<CGCourseSection> 	courseSectionList;
	
	private List<CGSectionLecture> 	lectureList;
	
	private List<CGRoomConstraint>  			roomConstraintList; //算法中通过限定domain的方式实现                     
//	@XStreamOmitField
//	private List<CGCurriculum> 				curriculumList;
	@XStreamOmitField
	private List<CGSectionConflict>			sectionConflictList;
	private List<CGUnavailablePeriodPenaltySoft> 	softUnavailableList;
	private List<CGUnavailablePeriodPenaltyHard> 	hardUnavailableList;
	
	private List<CGLectureConflict> lectureConflictList;
	private List<CGCourseIntervalConstraint> intervalConstrainList;
	// 同时排课 和 合班的对象
	private List<CGMeanwhileClassGroup> meanwhileClassGroup;
	private List<CGNoMeanwhileClassGroup> noMeanwhileClassGroup;
	// 记录排课 id
	private String arrayId;
	private boolean isWL;
	
	private CGConstants cGConstants;
	
	@PlanningScore(bendableHardLevelsSize = 4, bendableSoftLevelsSize = 3)
	private BendableScore score;
	
	private boolean changeLess;

	
	public void printAll(String outFileName) throws FileNotFoundException {
		String outDirFileName = System.getProperty("user.dir") + "\\StudentCourseScheduling\\target\\data\\" + outFileName;
		System.out.println("INFO: About to create file: " + outDirFileName);
		OutputStream output = new FileOutputStream(outDirFileName);
		PrintWriter myout = new PrintWriter(output);
		Collections.sort(courseSectionList);
		
		for (CGCourseSection cs : courseSectionList) {
			myout.println(cs.getName() + "：" + cs.getStudentList().size());
			Collections.sort(cs.getStudentList());
			for (CGStudent student : cs.getStudentList()) {
				myout.println("\t" + student.getStudentId());
			}
			
			myout.println("------------------");
			Collections.sort(cs.getLectureList());
			for (CGSectionLecture lecture : cs.getLectureList()) {
				myout.println("\t" + lecture.getCourseSection().getCourseSectionCode() + "-ID-" + lecture.getId());
			}
		}
		
		
		
		Collections.sort(lectureList);
		for (CGSectionLecture lecture : lectureList) {
			myout.print(lecture.getCourseSection().getCourseSectionCode() + "-ID-" + lecture.getId() + "  ");
			myout.print("Period(" + lecture.getPeriod().getDay().getDayIndex() + ", " + lecture.getPeriod().getTimeslot().getTimeslotIndex() + ")  ");
//			myout.println("Room(" + lecture.getRoom().getCode() + ")");
		}
		myout.close();
	}
	
	public BendableScore getScore() {
		return score;
	}

	public void setScore(BendableScore score) {
		this.score = score;
	}

	private boolean checkSectionsConflict(CGCourseSection section1, CGCourseSection section2) {
	
		Set<CGStudent> studentSet1 = new HashSet<>(section1.getStudentList());
		Set<CGStudent> studentSet2 = new HashSet<>(section2.getStudentList());
		
		return Sets.intersection(studentSet1, studentSet2).size() > 0;
	}
	
	public int countCourseSectionConflict() {
		int count = 0;
		for (CGCourseSection cs1 : courseSectionList) {
			for (CGCourseSection cs2 : courseSectionList) {
				if (cs2.getId() <= cs1.getId()) 
					continue;
				if (checkSectionsConflict(cs1, cs2))
					count ++;
			}
		}
		return count;
	}
	
	public void printStudentsByMajorCourses() {
		int i = 0;
		for (CGStudent student : studentList) {
			System.out.println((i ++) + " " + student.getStudentId() + ": " + student.getGroupId());
		}
	}

	public int countRooms () {
		return roomList.size();
	}
	
	public int countLectures () {
		return lectureList.size();
	}
	
	public int countPeriods () {
		return periodList.size();
	}
	
	
    @ProblemFactCollectionProperty
    public List<CGSectionConflict> getSectionConflictList (){
    	//System.out.println("INFO: ProblemFactCollectionProperty List<CGSectionConflict>");
    	return sectionConflictList;
    }
    
    
    public void printCourseSectionList () {
    	courseSectionList.stream().forEach(e -> System.out.println(e.getCourseSectionCode() + ": " + e.getStudentList().size()));
    }
    
    public List<CGSectionConflict> calculateCourseConflictList() {
        sectionConflictList = new ArrayList<CGSectionConflict>();
        for (CGCourseSection leftSection : courseSectionList) {
            for (CGCourseSection rightSection : courseSectionList) {
                if (leftSection.getId() < rightSection.getId()) {
//                    int conflictCount = 0;
//                    if (leftSection.getTeacher() != null && rightSection.getTeacher() != null) {
//                        if (leftSection.getTeacher().equals(rightSection.getTeacher())) {
//                            conflictCount++;
//                        }
//                    }
//                    for (CGCurriculum curriculum : leftSection.getCurriculumList()) {
//                        if (rightSection.getCurriculumList().contains(curriculum)) {
//                            conflictCount++;
//                        }
//                    }
//                    
//                    if (conflictCount > 0) {
//                        sectionConflictList.add(new CGSectionConflict(leftSection, rightSection, conflictCount));
//                    }
                	if (checkSectionsConflict(leftSection, rightSection)) 
                		sectionConflictList.add(new CGSectionConflict(leftSection, rightSection, 1));
                }
            }
        }
        
//        System.out.println("Scheduling: sectionConflictList.size() = " + sectionConflictList.size());
        return sectionConflictList;
    }
    
    
	@ProblemFactCollectionProperty
	public List<CGStudent> getStudentList() {
		//System.out.println("INFO: ProblemFactCollectionProperty List<CGStudent>");
		return studentList;
	}

	public void setStudentList(List<CGStudent> studentList) {
		this.studentList = studentList;
	}

	@ProblemFactCollectionProperty
	public List<CGCourse> getCourseList() {
		//System.out.println("INFO: ProblemFactCollectionProperty List<CGCourse>");
		return courseList;
	}

	public void setCourseList(List<CGCourse> courseList) {
		this.courseList = courseList;
	}

//	@ProblemFactCollectionProperty
	@PlanningEntityCollectionProperty
	public List<CGPeriod> getPeriodList() {
		//System.out.println("INFO: ProblemFactCollectionProperty List<CGPeriod>");
		return periodList;
	}

	public void setPeriodList(List<CGPeriod> periodList) {
		this.periodList = periodList;
	}

	@ProblemFactCollectionProperty
//	@PlanningEntityCollectionProperty
	public List<CGCourseSection> getCourseSectionList() {
		//System.out.println("INFO: ProblemFactCollectionProperty List<CGCourseSection>");
		return courseSectionList;
	}

	public void setCourseSectionList(List<CGCourseSection> courseSectionList) {
		this.courseSectionList = courseSectionList;
	}

	@PlanningEntityCollectionProperty
	public List<CGSectionLecture> getLectureList() {
		//System.out.println("INFO: PlanningEntityCollectionProperty List<CGSectionLecture>");
		return lectureList;
	}

	public void setLectureList(List<CGSectionLecture> lectureList) {
		this.lectureList = lectureList;
	}

	@ProblemFactCollectionProperty
	public List<CGRoom> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<CGRoom> roomList) {
		this.roomList = roomList;
	}

	@ProblemFactCollectionProperty
	public List<CGTeacher> getTeacherList() {
		return teacherList;
	}

	public void setTeacherList(List<CGTeacher> teacherList) {
		this.teacherList = teacherList;
	}

	public List<CGRoomConstraint> getRoomConstraintList() {
		return roomConstraintList;
	}

	public void setRoomConstraintList(List<CGRoomConstraint> roomConstraintList) {
		this.roomConstraintList = roomConstraintList;
	}


	public void setSectionConflictList(List<CGSectionConflict> sectionConflictList) {
		this.sectionConflictList = sectionConflictList;
	}

	@ProblemFactCollectionProperty
	public List<CGUnavailablePeriodPenaltySoft> getSoftUnavailableList() {
		return softUnavailableList;
	}

	public void setSoftUnavailableList(
			List<CGUnavailablePeriodPenaltySoft> softUnavailableList) {
		this.softUnavailableList = softUnavailableList;
	}

	@ProblemFactCollectionProperty
	public List<CGUnavailablePeriodPenaltyHard> getHardUnavailableList() {
		return hardUnavailableList;
	}

	public void setHardUnavailableList(
			List<CGUnavailablePeriodPenaltyHard> hardUnavailableList) {
		this.hardUnavailableList = hardUnavailableList;
	}

	public String getArrayId() {
		return arrayId;
	}

	public void setArrayId(String arrayId) {
		this.arrayId = arrayId;
	}

	
	@ProblemFactProperty
	public CGConstants getcGConstants() {
		return cGConstants;
	}

	public void setcGConstants(CGConstants cGConstants) {
		this.cGConstants = cGConstants;
	}

	@ProblemFactCollectionProperty
	public List<CGLectureConflict> getLectureConflictList() {
		return lectureConflictList;
	}

	public void setLectureConflictList(List<CGLectureConflict> lectureConflictList) {
		this.lectureConflictList = lectureConflictList;
	}

	@ProblemFactCollectionProperty
	public List<CGCourseIntervalConstraint> getIntervalConstrainList() {
		return intervalConstrainList;
	}

	public void setIntervalConstrainList(List<CGCourseIntervalConstraint> intervalConstrainList) {
		this.intervalConstrainList = intervalConstrainList;
	}

	public boolean isWL() {
		return isWL;
	}

	public void setWL(boolean isWL) {
		this.isWL = isWL;
	}

	@ProblemFactCollectionProperty
	public List<CGMeanwhileClassGroup> getMeanwhileClassGroup() {
		return meanwhileClassGroup;
	}

	public void setMeanwhileClassGroup(List<CGMeanwhileClassGroup> meanwhileClassGroup) {
		this.meanwhileClassGroup = meanwhileClassGroup;
	}

	public boolean isChangeLess() {
		return changeLess;
	}

	public void setChangeLess(boolean changeLess) {
		this.changeLess = changeLess;
	}

	public List<CGNoMeanwhileClassGroup> getNoMeanwhileClassGroup() {
		return noMeanwhileClassGroup;
	}

	public void setNoMeanwhileClassGroup(List<CGNoMeanwhileClassGroup> noMeanwhileClassGroup) {
		this.noMeanwhileClassGroup = noMeanwhileClassGroup;
	}

	
}
