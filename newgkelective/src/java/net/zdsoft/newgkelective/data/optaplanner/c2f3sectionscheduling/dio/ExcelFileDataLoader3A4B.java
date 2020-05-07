
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
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.common.StudentVO3;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain.*;

public class ExcelFileDataLoader3A4B {
	private List<Student>   		studentList;			//input, xls: 学生列表
	private List<TakeCourse>   		    courseList;				//input, xls: 作为输入文件的xls中导出，courseList 包含了6门A课（或者7门）的名字，没有后缀
	private Map<String, Integer>    courseSectionSizeMeanMap;	//input: 每门课的教学班平均人数
	private Map<String, Integer>    courseSectionSizeMarginMap; //input: 每门课的教学班人数，允许浮动的范围

	private Map<String, TakeCourse>     courseMap;				//生成：从courseList中生成，名字跟Course之间的对应
	private List<StudentCourse>     studentCourseList;      //生成：每个学生，有几门课就有几个这个对象
	private List<TimeSlot> 			timeSlotList;			//生成：根据totalTimeSlotCount 自动生成
	
	SectioningSolution 				solution;				//output

	public SectioningSolution buildSolution() {
		SectioningSolution solution = new SectioningSolution();
		solution.setId(1);
		solution.setAppName("分班排课");
		solution.setStudentList(studentList);
		solution.setCourseList(courseList);
		solution.setStudentCourseList(studentCourseList);
		solution.setTimeSlotList(timeSlotList);
		
		solution.setCourseSectionSizeMeanMap(courseSectionSizeMeanMap);
		solution.setCourseSectionSizeMarginMap(courseSectionSizeMarginMap);
		
		return solution;
	}
	
	public ExcelFileDataLoader3A4B(String excelFileName) {
		
		//studentList, courseList, studentCourseList, courseMap
		populateStudentList(excelFileName);
		
		//Debug
		System.out.println("Student# = " + studentList.size());
		//printStudentList();
		
		//timeSlotList
		populateTimeSlotList();

		//本来应该是界面上来的，这里暂时代替用户生成一下
		//courseSectionSizeMeanMap, courseSectionSizeMarginMap
		populateCourseSectionSize();
		
		//init，初始化
		linkStudentCourseToTimeSlot();
	
	}

	private void populateStudentList(String excelFileName) {
		//studentList
		studentList = new ArrayList<>();
		List<StudentVO3> rawStudentList = readExcelFile(excelFileName);
		Set<String> courseNameSet = new HashSet<>();
		rawStudentList.stream().forEach(e -> {
				courseNameSet.add(e.getChooseSubject1());
				courseNameSet.add(e.getChooseSubject2());
				courseNameSet.add(e.getChooseSubject3());
			});
			
		//courseList
		courseList = new ArrayList<>();
		courseNameSet.stream().forEach(e -> courseList.add(new TakeCourse(e+"A")));
		courseNameSet.stream().forEach(e -> courseList.add(new TakeCourse(e+"B")));
		
		//courseMap
		courseMap = new HashMap<>();
		courseList.stream().forEach(e -> courseMap.put(e.getCourseName(), e));
		
		//studentCourseList
		studentCourseList = new ArrayList<>();
		
		int i = 1; 
		for (StudentVO3 rawStudent : rawStudentList) {
			Student student = new Student();
			student.setId(i ++);
			student.setStudentName(rawStudent.getName());
			student.setStudentId(rawStudent.getStuId());
			studentList.add(student);
			
			Set<String> myACourseNameSet = new HashSet<>();
			myACourseNameSet.add(rawStudent.getChooseSubject1());
			myACourseNameSet.add(rawStudent.getChooseSubject2());
			myACourseNameSet.add(rawStudent.getChooseSubject3());
			Set<String> myBCourseNameSet = Sets.difference(courseNameSet, myACourseNameSet);
			
			//A courses
			for (String courseName : myACourseNameSet) {
				StudentCourse studentCourse = new StudentCourse();
				TakeCourse currentCourse = courseMap.get(courseName+"A");
				studentCourse.setCourse(currentCourse);
				studentCourse.setStudent(student);
				studentCourseList.add(studentCourse);
			}
			
			//B courses
			for (String courseName : myBCourseNameSet) {
				StudentCourse studentCourse = new StudentCourse();
				TakeCourse currentCourse = courseMap.get(courseName+"B");
				studentCourse.setCourse(currentCourse);
				studentCourse.setStudent(student);
				studentCourseList.add(studentCourse);
			}
			
		}
	}

	private void populateTimeSlotList() {
		timeSlotList = new ArrayList<>();
		
		for (int id = 1; id <= courseList.size() / 2; id ++) {
			TimeSlot timeSlot = new TimeSlot();
			timeSlot.setId(id);
			timeSlot.setTimeSlotName("T" + id);
			timeSlotList.add(timeSlot);
		}
	}
	
	
	private void populateCourseSectionSize() {
		Map<TakeCourse, List<StudentCourse>> studentListByCourse = studentCourseList.stream().collect(Collectors.groupingBy(StudentCourse::getCourse));
		
		courseSectionSizeMeanMap = new HashMap<String, Integer>();
		courseSectionSizeMarginMap = new HashMap<String, Integer>();
		
		int commonSectionSizeMean = 50;
		int commonSectionSizeMargin = 6;
		int totalSectionCount = 0;
		
		for(TakeCourse course : courseList) {
			String courseName = course.getCourseName();

			int studentCount = studentListByCourse.get(courseMap.get(courseName)).size();
			List<Integer> sectionSizeList = CalculateSections.calculateSectioning(studentCount, commonSectionSizeMean, commonSectionSizeMargin);
			int sectionSize = sectionSizeList.get(0);
			totalSectionCount += sectionSizeList.size();
			courseSectionSizeMeanMap.put(courseName, new Integer(sectionSize));
			courseSectionSizeMarginMap.put(courseName, new Integer((int)(sectionSize * 0.1)));

			//DEBUG
			System.out.println(courseName + " sectionSize = " + sectionSize + ", " + sectionSizeList);
			
		}
		
		//DEBUG
		//courseSectionSizeMeanMap.put("化学", new Integer(60));
		//courseSectionSizeMarginMap.put("化学", new Integer(8));
		//courseSectionSizeMarginMap.put("历史", new Integer(8));
		
		//DEBUG
		System.out.println("Total Section # = " + totalSectionCount);
		
		courseList.stream().forEach(e -> {
			e.setSectionSizeMean(courseSectionSizeMeanMap.get(e.getCourseName())); 
			e.setSectionSizeMargin(courseSectionSizeMarginMap.get(e.getCourseName()));
			});
	}

	private void linkStudentCourseToTimeSlot(){
		Map<Student, List<StudentCourse>> studentCourseByStudent = studentCourseList.stream().collect(Collectors.groupingBy(StudentCourse::getStudent));
		
		for (Map.Entry<Student, List<StudentCourse>> me : studentCourseByStudent.entrySet()) {
			Collections.shuffle(timeSlotList);
			
			for (int i = 0; i < me.getValue().size(); i ++) {
				me.getValue().get(i).setTimeSlot(timeSlotList.get(i));
				me.getValue().get(i).setTimeSlotDomain(timeSlotList);
				timeSlotList.get(i).getStudentCourseList().add(me.getValue().get(i));
			}
		}
	}
	
	
	private void printStudentList() {
		studentList.stream().forEach(e -> System.out.println(e));
	}
	
	private List<StudentVO3> readExcelFile(String excelFileName) {
		List<StudentVO3> studentVOList = null;
		try {
			String dataPath = System.getProperty("user.dir");
			FileInputStream fis = new FileInputStream(dataPath+"\\src\\main\\resources\\data\\" + excelFileName);
			ExcelUtil<StudentVO3> util = new ExcelUtil<StudentVO3>(StudentVO3.class);	// 创建excel工具类
			studentVOList = util.importExcel("学生信息", fis);	// 导入
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if (studentVOList == null) {
			System.out.println("ERROR: studentVOList == null !");
		}
		
		System.out.println("StudentVO3# = " + studentVOList.size());
		return studentVOList;
	}
}
