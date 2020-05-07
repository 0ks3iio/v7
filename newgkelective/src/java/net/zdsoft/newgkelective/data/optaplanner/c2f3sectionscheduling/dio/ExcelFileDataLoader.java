
//只适用于“蜀关中学单科分层”

package net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.dio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.common.CalculateSections;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.common.ExcelUtil;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.common.StudentVO1;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain.*;

public class ExcelFileDataLoader {
	private List<Student>   		studentList;
	private List<TakeCourse>   		    courseList;
	private Map<String, TakeCourse>     courseMap;
	private List<StudentCourse>     studentCourseList;
	private List<TimeSlot> 			timeSlotList;
	private Map<String, Integer>    courseSectionSizeMeanMap;
	private Map<String, Integer>    courseSectionSizeMarginMap;
	
	SectioningSolution 				solution;		//output
	
	public ExcelFileDataLoader(String excelFileName) {
		
		populateStudentList(excelFileName);
		
		//Debug
		System.out.println("Student# = " + studentList.size());
		//printStudentList();
		
		populateTimeSlotList();

		populateCourseSectionSize();
		
		linkStudentCourseToTimeSlot();
		
		//Debug
//		Set<String> courseNameSet = new HashSet<>();
//		studentList.stream().forEach(e -> {
//			courseNameSet.add(e.getChooseSubject1()); 
//			courseNameSet.add(e.getChooseSubject2()); 
//			courseNameSet.add(e.getChooseSubject3());
//		}); 
//		System.out.println("courseNameSet.size = " + courseNameSet.size());
		
	}
	
	public SectioningSolution buildSolution() {
		SectioningSolution solution = new SectioningSolution();
		solution.setId(1);
		solution.setAppName("蜀关中学单科分层");
		solution.setStudentList(studentList);
		solution.setCourseList(courseList);
		solution.setStudentCourseList(studentCourseList);
		solution.setTimeSlotList(timeSlotList);
		
		solution.setCourseSectionSizeMeanMap(courseSectionSizeMeanMap);
		solution.setCourseSectionSizeMarginMap(courseSectionSizeMarginMap);
		
		
		return solution;
	}
	
	private void populateCourseSectionSize() {
		courseSectionSizeMeanMap = new HashMap<String, Integer>();
		courseSectionSizeMarginMap = new HashMap<String, Integer>();
		
		courseSectionSizeMeanMap.put("政治A", new Integer(47));
		courseSectionSizeMeanMap.put("政治B", new Integer(48));
		courseSectionSizeMeanMap.put("历史A", new Integer(51));
		courseSectionSizeMeanMap.put("历史B", new Integer(53));
		courseSectionSizeMeanMap.put("地理A", new Integer(48));
		courseSectionSizeMeanMap.put("地理B", new Integer(54));
		courseSectionSizeMeanMap.put("物理A", new Integer(49));
		courseSectionSizeMeanMap.put("物理B", new Integer(48));
		courseSectionSizeMeanMap.put("物理C", new Integer(50));
		courseSectionSizeMeanMap.put("化学A", new Integer(51));
		courseSectionSizeMeanMap.put("化学B", new Integer(51));
		courseSectionSizeMeanMap.put("化学C", new Integer(50));
		courseSectionSizeMeanMap.put("生物A", new Integer(52));
		courseSectionSizeMeanMap.put("生物B", new Integer(50));
		courseSectionSizeMeanMap.put("生物C", new Integer(51));
		
		courseSectionSizeMarginMap.put("政治A", new Integer(5));
		courseSectionSizeMarginMap.put("政治B", new Integer(5));
		courseSectionSizeMarginMap.put("历史A", new Integer(5));
		courseSectionSizeMarginMap.put("历史B", new Integer(5));
		courseSectionSizeMarginMap.put("地理A", new Integer(5));
		courseSectionSizeMarginMap.put("地理B", new Integer(5));
		courseSectionSizeMarginMap.put("物理A", new Integer(5));
		courseSectionSizeMarginMap.put("物理B", new Integer(5));
		courseSectionSizeMarginMap.put("物理C", new Integer(5));
		courseSectionSizeMarginMap.put("化学A", new Integer(5));
		courseSectionSizeMarginMap.put("化学B", new Integer(5));
		courseSectionSizeMarginMap.put("化学C", new Integer(5));
		courseSectionSizeMarginMap.put("生物A", new Integer(5));
		courseSectionSizeMarginMap.put("生物B", new Integer(5));
		courseSectionSizeMarginMap.put("生物C", new Integer(5));
		
		courseList.stream().forEach(e -> {
			e.setSectionSizeMean(courseSectionSizeMeanMap.get(e.getCourseName())); 
			e.setSectionSizeMargin(courseSectionSizeMarginMap.get(e.getCourseName()));
			});
	}

	private void linkStudentCourseToTimeSlot(){
		Map<Student, List<StudentCourse>> studentCourseByStudent = studentCourseList.stream().collect(Collectors.groupingBy(StudentCourse::getStudent));
		
		for (Map.Entry<Student, List<StudentCourse>> me : studentCourseByStudent.entrySet()) {
			Collections.shuffle(timeSlotList);
			
//			System.out.println("List<StudentCourse> size = " + me.getValue().size());
//			System.out.println("timeSlotList size = " + timeSlotList.size());
			
			me.getValue().get(0).setTimeSlot(timeSlotList.get(0));
			me.getValue().get(0).setTimeSlotDomain(timeSlotList);
			timeSlotList.get(0).getStudentCourseList().add(me.getValue().get(0));

			me.getValue().get(1).setTimeSlot(timeSlotList.get(1));
			me.getValue().get(1).setTimeSlotDomain(timeSlotList);
			timeSlotList.get(1).getStudentCourseList().add(me.getValue().get(1));

			me.getValue().get(2).setTimeSlot(timeSlotList.get(2));
			me.getValue().get(2).setTimeSlotDomain(timeSlotList);
			timeSlotList.get(2).getStudentCourseList().add(me.getValue().get(2));
			
		}
	
	}
	
	private void populateTimeSlotList() {
		timeSlotList = new ArrayList<>();
		
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setId(1);
		timeSlot.setTimeSlotName("T1");
		timeSlotList.add(timeSlot);
		
		timeSlot = new TimeSlot();
		timeSlot.setId(2);
		timeSlot.setTimeSlotName("T2");
		timeSlotList.add(timeSlot);
		
		timeSlot = new TimeSlot();
		timeSlot.setId(3);
		timeSlot.setTimeSlotName("T3");
		timeSlotList.add(timeSlot);
	}
	
	private void populateStudentList(String excelFileName) {
		studentList = new ArrayList<>();
		List<StudentVO1> rawStudentList = readExcelFile(excelFileName);
		Set<String> courseNameSet = new HashSet<>();
		rawStudentList.stream().forEach(e -> courseNameSet.add(e.getChooseSubject1()));
		courseList = new ArrayList<>();
		courseNameSet.stream().forEach(e -> courseList.add(new TakeCourse(e)));
		courseMap = new HashMap<>();
		courseList.stream().forEach(e -> courseMap.put(e.getCourseName(), e));
		studentCourseList = new ArrayList<>();
		
		Map<String, List<StudentVO1>> studentSelectList = rawStudentList.stream().collect(Collectors.groupingBy(StudentVO1::getStuId));
		int i = 1; 
		for (Map.Entry<String, List<StudentVO1>> me : studentSelectList.entrySet()) {
			//System.out.println(me.getKey() + ": " + me.getValue().size());
			Student student = new Student();
			student.setId(i ++);
			student.setStudentName(me.getValue().get(0).getName());
			student.setStudentId(me.getValue().get(0).getStuId());
			
			StudentCourse studentCourse = new StudentCourse();
			TakeCourse currentCourse = courseMap.get(me.getValue().get(0).getChooseSubject1());
			studentCourse.setCourse(currentCourse);
			studentCourse.setStudent(student);
			studentCourseList.add(studentCourse);
			
			studentCourse = new StudentCourse();
			currentCourse = courseMap.get(me.getValue().get(1).getChooseSubject1());
			studentCourse.setCourse(currentCourse);
			studentCourse.setStudent(student);
			studentCourseList.add(studentCourse);

			studentCourse = new StudentCourse();
			currentCourse = courseMap.get(me.getValue().get(2).getChooseSubject1());
			studentCourse.setCourse(currentCourse);
			studentCourse.setStudent(student);
			studentCourseList.add(studentCourse);
			
			studentList.add(student);
		}
	}
	
	private void printStudentList() {
		studentList.stream().forEach(e -> System.out.println(e));
	}
	
	private List<StudentVO1> readExcelFile(String excelFileName) {
		List<StudentVO1> studentVOList = null;
		try {
			String dataPath = System.getProperty("user.dir");
			FileInputStream fis = new FileInputStream(dataPath+"\\src\\main\\resources\\data\\" + excelFileName);
			ExcelUtil<StudentVO1> util = new ExcelUtil<StudentVO1>(StudentVO1.class);	// 创建excel工具类
			studentVOList = util.importExcel("学生信息", fis);	// 导入
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if (studentVOList == null) {
			System.out.println("ERROR: studentVOList == null !");
		}
		
		System.out.println("StudentVO1# = " + studentVOList.size());
		return studentVOList;
	}
}
