package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.AbstractPersistable;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.CalculateSections;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.StudentVO;

public class SectionSolution extends AbstractPersistable {
//	public static int roomCount;							//fact
//	public static int maxSectionSize;						//fact
//	
//	private int localMaxSectionSize;
//	private int localRoomCount;

	private int sectionSizeMean;
	private int sectionSizeMargin;
	private int roomCount;
	
    private String name;									//fact
    private List<StudentVO>		    rawStudentList;			//fact
    
    private List<StudentVO>		    rawStudentListPartial;	//fact, 手动分了一部分的情况
    private List<Student>			partialSectionStudentList;
    private List<CourseSection>		partialSectionList;
    private List<CourseSection>		fullSectionList;
    
    private List<Student> 			studentList;			//fact
    private Map<String, Curriculum>	curriculumMap;			//fact
    private List<Course>			courseList;				//fact
    Map<String, Integer> 			sectionCountByCourse; 	//fact
    
	private Set<String> allCourseNames;
	//{"物理A", "化学A", "生物A", "地理A", "政治A", "历史A", "技术A", 
    // "物理B", "化学B", "生物B", "地理B", "政治B", "历史B", "技术B"}
	
	private Set<String> aCourseNames;
	// {"物理A", "化学A", "生物A", "地理A", "政治A", "历史A", "技术A"};
	
	private Set<String> bCourseNames; 
	//{"物理B", "化学B", "生物B", "地理B", "政治B", "历史B", "技术B"};

	private Map<String, Course>  courseMap;    
    
    
    private Processing2X			processor2x;   			//v1
    
    private Processing2XOptimized	processor2xOptimized;	//v2
    
    private List<CourseSection>		sectionList;			//result

   
    public Map<String, Course> getCourseMap () {
    	return courseMap;
    }
    
	private void initCourseNames (List<String> courseCodeList) {
		this.aCourseNames = courseCodeList.stream().filter(e -> e.endsWith("A")).collect(Collectors.toSet());
		this.bCourseNames = courseCodeList.stream().filter(e -> e.endsWith("B")).collect(Collectors.toSet());
		this.allCourseNames = courseCodeList.stream().collect(Collectors.toSet());
		this.courseMap = new HashMap<String, Course>();
		this.allCourseNames.stream().forEach(e -> this.courseMap.put(e, new Course(e)));		
	}	
    
	/**
	 * populate the studentList, each student's curriculum will be initialized when curriculumMap is created from the studentList
	 */
	private void populateStudentList() {
		assert rawStudentList != null;
		int id = 1;
		studentList = rawStudentList.stream().map(e -> new Student(e, aCourseNames)).collect(Collectors.toList());
		for (Student s : studentList) {
			s.setId(id++);
		}
	}
	
	/**
	 * populate curriculumMap, a student's curriculum will be assigned on the fly
	 */
	private void populateCurriculumMap() {
		curriculumMap = new HashMap<String, Curriculum> ();
		int id = 1;
		Map<String, List<Student>> curriculumGroups = studentList.stream().collect(Collectors.groupingBy(Student::getCurriculumCode));
		curriculumGroups.entrySet().stream().forEach(e -> curriculumMap.put(e.getKey(), new Curriculum(e.getKey(), e.getValue())));
		for (Map.Entry<String, Curriculum> m : curriculumMap.entrySet()) {
			m.getValue().setId(id++);
		}
	}
	
	/**
	 * populate the courseList and their studentList
	 */
	private void populateCourseList() {
		courseList = new ArrayList<Course>();
		allCourseNames.stream().forEach(e -> courseList.add(new Course(e)));
		String coursename;
		int i = 1;
		for (Course c : courseList) {
			coursename = c.getCode();
			for (Map.Entry<String, Curriculum> m : curriculumMap.entrySet()) {
				if (m.getValue().containsCourseByName(coursename)) {
					c.addStudentList(m.getValue().getStudentList());
				}
			}
			c.setId(i++);
		}
	}
	
	private void processPartialSections () {
	    partialSectionStudentList = new ArrayList<>();
	    partialSectionList = new ArrayList<>();
	    fullSectionList = new ArrayList<>();
		
		if (rawStudentListPartial.size() == 0) {
			return;
		}
		
		Map<String, List<StudentVO>> partialSections = rawStudentListPartial.stream().collect(Collectors.groupingBy(StudentVO::getSectionID));
	    
		for (Map.Entry<String, List<StudentVO>> me : partialSections.entrySet()) {
			StudentVO currentStudentVO = me.getValue().get(0);
			String sectionCourseName1 = currentStudentVO.getSectionSubject1() + "A";
			String sectionCourseName2 = currentStudentVO.getSectionSubject2() + "A";
			Set<String> courseNameSet2A = new HashSet<>();
			courseNameSet2A.add(sectionCourseName1);
			courseNameSet2A.add(sectionCourseName2);
			
			CourseSection course1 = new CourseSection(courseMap.get(sectionCourseName1));
			CourseSection course2 = new CourseSection(courseMap.get(sectionCourseName2));
			
			Set<String> otherCourseNameSet = Sets.difference(allCourseNames, courseNameSet2A); 
			List<CourseSection> otherCourseSectionList = otherCourseNameSet.stream().map(e -> new CourseSection(courseMap.get(e))).collect(Collectors.toList());
			Map<String, CourseSection> otherCourseSectionMap = new HashMap<>();
			otherCourseSectionList.stream().forEach(e -> otherCourseSectionMap.put(e.getCourse().getCode(), e));
			
			for (StudentVO s : me.getValue()) {
				Set<String> courseNameSet3A = new HashSet<>();
				courseNameSet3A.add(s.getChooseSubject1() + "A");
				courseNameSet3A.add(s.getChooseSubject2() + "A");
				courseNameSet3A.add(s.getChooseSubject3() + "A");
				String chooseSubjectName3 = Sets.difference(courseNameSet3A, courseNameSet2A).stream().collect(Collectors.toList()).get(0);
				
				Student currentStudent = new Student(s, aCourseNames);
				
				partialSectionStudentList.add(currentStudent);
				course1.addStudent(currentStudent);
				course2.addStudent(currentStudent);
				otherCourseSectionMap.get(chooseSubjectName3).addStudent(currentStudent);
				
				for (String cn : currentStudent.getBCourseNameSet()) {
					otherCourseSectionMap.get(cn).addStudent(currentStudent);
				}
			}
			
			fullSectionList.add(course1);
			fullSectionList.add(course2);
			otherCourseSectionList = otherCourseSectionList.stream().filter(e -> e.getStudentCount() > 0).collect(Collectors.toList());
			partialSectionList.addAll(otherCourseSectionList);
			
		}
		
		System.out.println("processPartialSections(): fullSectionList.size = " + fullSectionList.size() + ", partialSectionList.size = " + partialSectionList.size() + ", partialSectionStudentList.size = " + partialSectionStudentList.size());
	}
	
	public void init(String solutionName, List<StudentVO> studentVOList, List<String> courseCodeList, int sectionSizeMean, int sectionSizeMargin, int roomCount) throws IOException{
		this.name = solutionName;
		this.rawStudentList = studentVOList.stream().filter(e -> e.getSectionID() == null).collect(Collectors.toList());
		this.rawStudentListPartial = studentVOList.stream().filter(e -> e.getSectionID() != null).collect(Collectors.toList());
		
		this.sectionSizeMean = sectionSizeMean;
		this.sectionSizeMargin = sectionSizeMargin;
		this.roomCount = roomCount;
		
		initCourseNames(courseCodeList);
		
		//Step 1: populate the studentList
		populateStudentList();
		

		
		//DEBUG
		//System.out.println("SectionSolution.init: #studentList = " + studentList.size() + " #student * 7 = " + studentList.size() * 7);
		
		//Step 2: populate curriculumMap
		populateCurriculumMap();
		
		//Step 3: populate courseList
		populateCourseList();
		
		//Step 4: populate sectionCountByCourse
		calculateSectionCountByCourse();
		
		for (Curriculum c : curriculumMap.values()) {
			c.setSectionSizeMargin(sectionSizeMargin);
			c.setSectionSizeMean(sectionSizeMean);
		}
		
		processPartialSections();
	}
	
	public void doit_v1() {
		processor2x = new Processing2X(this);
		processor2x.init_v1();
		processor2x.doit_v1();
	}

	public void doit_v3() { //基于clique求解，计算量太大，作废
		processor2xOptimized = new Processing2XOptimized(this);
		processor2xOptimized.doit();
	}

	public void doit_v2() {
		processor2x = new Processing2X(this);
		processor2x.init_v1();
		processor2x.doit_v2();
	}
	
	public void doit_v5() { 
		processor2x = new Processing2X(this);

		processor2x.doit_v5();
	}
	
	public boolean validateSectionList () {
		boolean isValid = false;
		
		
		
		return isValid;
	}
	
	public void printSectionList () {
		int totalStudentCount = sectionList.stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x + y);
		System.out.println("sectionList.size() = " + sectionList.size() + " totalStudent# = " + totalStudentCount);
		sectionList.stream().forEach(e -> System.out.println(e.getSectionCode() + ": " + e.getStudentCount()));
	}
	
	public void printSectionListSummary () {
		System.out.print("INPUT: studentCount = " + this.rawStudentList.size() + ", studentCount * 7 = " + this.rawStudentList.size() * 7);
		System.out.println("  sectionSizeMean = " + this.sectionSizeMean + ", roomCount = " + this.roomCount);
		System.out.println("  sectionSizeMargin = " + this.sectionSizeMargin + ", roomCount = " + this.roomCount);
		int totalStudentCount = sectionList.stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x + y);
		System.out.print("RESULT: sectionList.size() = " + sectionList.size() + " studentCount * 7 = " + totalStudentCount);
		IntSummaryStatistics theSummary = sectionList.stream().collect(Collectors.summarizingInt(e -> e.getStudentCount()));
		System.out.println("  (max, min, avg) = (" + theSummary.getMax() + ", " + theSummary.getMin() + ", " + theSummary.getAverage() + ")");
		
		//sectionList.stream().forEach(e -> System.out.println(e.getSectionCode() + ": " + e.getStudentCount()));
		int conflictCount = 0;
		for (int i = 0; i < sectionList.size(); i ++) {
			for (int j = i + 1; j < sectionList.size(); j ++) {
				if (sectionList.get(i).isConflictWith(sectionList.get(j))) {
					conflictCount ++;
				}
			}
		}
		System.out.println("conflictCount = " + conflictCount);
		System.out.println();
	}
	
	private void printAll() {
		curriculumMap.entrySet().stream().forEach(e -> System.out.println(e.getKey() + ": " + e.getValue().getStudentCount()));
		courseList.stream().forEach(e -> System.out.println(e.getCode() + ": " + e.getStudentCount()));
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

	public Map<String, Curriculum> getCurriculumMap() {
		return curriculumMap;
	}

	public void setCurriculumMap(Map<String, Curriculum> curriculumMap) {
		this.curriculumMap = curriculumMap;
	}

	public List<Course> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}

	public int getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}
	
	public List<CourseSection> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<CourseSection> sectionList) {
		this.sectionList = sectionList;
	}
	
	private void calculateSectionCountByCourse () {
		sectionCountByCourse = new HashMap<>();
		int sectionCount = 0;
		for (Course c : courseList) {
			sectionCount = CalculateSections.calculateSectioning(c.getStudentCount(), sectionSizeMean, sectionSizeMargin).size();
			sectionCountByCourse.put(c.getCode(), new Integer(sectionCount));
		}
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
	
	public int getSectionSizeMax () {
		return sectionSizeMean + sectionSizeMargin;
	}

	public List<CourseSection> getPartialSectionList() {
		return partialSectionList;
	}

	public void setPartialSectionList(List<CourseSection> partialSectionList) {
		this.partialSectionList = partialSectionList;
	}

	public List<CourseSection> getFullSectionList() {
		return fullSectionList;
	}

	public void setFullSectionList(List<CourseSection> fullSectionList) {
		this.fullSectionList = fullSectionList;
	}

	public void validate2X() {
		HashSet<Student> set = new HashSet<>(partialSectionStudentList);
		for (CourseSection section : sectionList) {
			long count = section.getStudentList().stream().filter(e->set.contains(e)).count();
			if(count > 0){
				System.out.println("组合班人数："+count+"班级人数："+section.getStudentList().size());
				if((int)count == section.getStudentList().size()){
					System.out.println("*******纯组合班*******"+section.getSectionCode());
				}
			}
		}
		
	}
	
	
	
}
