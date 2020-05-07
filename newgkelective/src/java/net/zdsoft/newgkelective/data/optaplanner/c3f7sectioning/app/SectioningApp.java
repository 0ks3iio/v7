package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.app;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.ExcelUtil;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.StudentVO;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain.CourseSection;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain.SectionSolution;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain.Student;

import org.apache.commons.lang3.StringUtils;

public class SectioningApp {

	public static void main(String[] args) throws IOException {
		List<SectionSolution> solutionList = searchBestSolutionArrayFromFile("牡丹江一中手动分班_1", 60, 0, 28);
		
		solutionList = solutionList.stream().sorted((x, y) -> x.getSectionList().size() - y.getSectionList().size()).collect(Collectors.toList());
		
		//INFO
		solutionList.stream().forEach(e -> e.printSectionListSummary());
		
	}
	/**
	 * 测试时调用
	 * @param xlsFileName
	 * @param sectionSizeMean
	 * @param sectionSizeMargin
	 * @param inputRoomCount 如果想用 从数据提取出来的行政班 人数 ，请传 -1
	 * @return
	 */
	private static List<SectionSolution> searchBestSolutionArrayFromFile (String xlsFileName, int sectionSizeMean, int sectionSizeMargin, 
			int inputRoomCount) {
		List<StudentVO> studentVOListAll = readStudentCourse(xlsFileName);
		List<String> courseCodeList = initCourseNames ();
		return searchBestSolutionArray( studentVOListAll, courseCodeList, sectionSizeMean, sectionSizeMargin, inputRoomCount);
	}
	/**
	 * 集成进 大项目时直接调用
	 * @param studentVOListAll
	 * @param courseCodeList
	 * @param sectionSizeMean
	 * @param sectionSizeMargin
	 * @param inputRoomCount 如果想用 从数据提取出来的行政班 人数 ，请传 -1
	 * @return
	 */
	public static List<SectionSolution> searchBestSolutionArray (List<StudentVO> studentVOListAll, List<String> courseCodeList,
			int sectionSizeMean, int sectionSizeMargin, int inputRoomCount) {
		List<StudentVO> studentVOListPartial = studentVOListAll.stream().filter(e -> e.getSectionID() != null).collect(Collectors.toList());
		//studentVOListPartial.stream().filter(e -> e.getSectionID() != null).forEach(e -> System.out.println(e));
		List<StudentVO> studentVOList = studentVOListAll.stream().filter(e -> e.getSectionID() == null).collect(Collectors.toList());
		//System.out.println("studentVOList.size = " + studentVOList.size() + ", studentVOListAll.size = " + studentVOListAll.size());
		if(inputRoomCount == -1){
			inputRoomCount = studentVOList.stream().collect(Collectors.groupingBy(StudentVO::getRawClassName)).size();
		}

		int maxSectionSize = sectionSizeMean + sectionSizeMargin;
		List<Integer> sectionSizeList = new ArrayList<>();
		sectionSizeList.add(new Integer(maxSectionSize));
		
//		for (int i = 50; i <= 60; i ++) {
//			sectionSizeList.add(new Integer(i));
//		}
		
		List<Integer> roomCountList = new ArrayList<>();
//		roomCountList.add(inputRoomCount);
		
		for (int i = inputRoomCount - 3; i <= inputRoomCount + 2; i ++ ) {
			roomCountList.add(new Integer(i));
		}
		
		//System.out.println("INFO: 求解遍数 = " + roomCountList.size() * sectionSizeList.size());
		List<SectionSolution> resultSolutionList = new ArrayList<>();
		for (Integer sectionSize : sectionSizeList) {
			for (Integer roomCount : roomCountList) {
				System.out.println("INFO: 开始求解，(maxSectionSize, roomCount) = (" + sectionSize.intValue() + ", " + roomCount.intValue() + ")");
				SectionSolution sectionSolution = new SectionSolution();

				try {
					sectionSolution.init("dev", studentVOListAll, courseCodeList, sectionSizeMean, sectionSizeMargin, roomCount.intValue());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//sectionSolution.doit_v1(); 
				sectionSolution.doit_v2();
				//sectionSolution.doit_v5();
				//sectionSolution.printSectionList();
				
				//DEBUG
//				int i3 =sectionSolution.getSectionList().stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x + y);
//				System.out.println("SectioningApp::searchBestSolutionArray(), totalStudentInResultSectionList = " + i3);
				
//				sectionSolution.validate2X();
				
				resultSolutionList.add(sectionSolution);
				//System.out.println("INFO: 求解完成，(maxSectionSize, roomCount) = (" + sectionSize.intValue() + ", " + roomCount.intValue() + ")");
			}
		}
		
		//System.out.println("分班完成");
		
		return resultSolutionList;
		
	}

	private static List<StudentVO> readStudentCourse(String xlsFileName) {
		List<StudentVO> studentVOList = null;
		try {
			String dataPath = System.getProperty("user.dir");
			FileInputStream fis = new FileInputStream(dataPath+"\\data\\" + xlsFileName + ".xls"); //输入1：学生选课情况
			ExcelUtil<StudentVO> util = new ExcelUtil<StudentVO>(StudentVO.class);	
			studentVOList = util.importExcel("学生信息", fis);	// 导入
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if (studentVOList == null) {
			System.out.println("ERROR: studentVOList == null !");
		}

		return studentVOList;
	}
	
	
	private static List<String> initCourseNames () {
		List<String> courseCodeList = new ArrayList<>();
		
		courseCodeList.add("物理A");
		courseCodeList.add("化学A");
		courseCodeList.add("生物A");
		courseCodeList.add("地理A");
		courseCodeList.add("政治A");
		courseCodeList.add("历史A");
		courseCodeList.add("技术A");

		courseCodeList.add("物理B");
		courseCodeList.add("化学B");
		courseCodeList.add("生物B");
		courseCodeList.add("地理B");
		courseCodeList.add("政治B");
		courseCodeList.add("历史B");
		courseCodeList.add("技术B");

		return courseCodeList;
	}

	
	private static void printList(List<CourseSection> sectionList, String filePath) throws IOException {
//		FileOutputStream os = new FileOutputStream("C:\\Users\\user\\Desktop\\" + fileName + "_ouput.txt");
		FileOutputStream os = new FileOutputStream(filePath);
		for (CourseSection courseSection : sectionList) {
			int sectionID = courseSection.getSectionID();
			String courseCode = courseSection.getCourse().getCode();
			StringBuilder stringBuilder = new StringBuilder();
			for (Student stu : courseSection.getStudentList()) {
				String studentID = stu.getStudentID();
				String studentName = stu.getStudentName();
				
				stringBuilder.append(studentName)
						.append(" ")//
						.append(studentID)
						.append(" ")//
						.append(courseCode)
						.append(" ")//
						.append(sectionID)
						.append("\n");
			}
			os.write(stringBuilder.toString().getBytes("UTF-8"));
		}
		os.flush();
		os.close();
	}
}
