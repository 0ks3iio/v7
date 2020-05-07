package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.domain1;

import net.zdsoft.newgkelective.data.optaplanner.common.StudentVO3;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.newgkelective.data.optaplanner.bestsectioncount.api.BestSectionCount;
import net.zdsoft.newgkelective.data.optaplanner.bestsectioncount.api.BestSectionCountInput;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning3.api.Sectioning2A1X;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning3.api.Sectioning2A1XInput;
import net.zdsoft.newgkelective.data.optaplanner.common.CalculateSections;
import net.zdsoft.newgkelective.data.optaplanner.common.ExcelUtil;

/**
 * 2+x不完全开班 3.0版本
 */
public class SectioningApp3 {
	static final Logger LOG = LoggerFactory.getLogger(SectioningApp3.class);
	
	/**
	 * 
	 
	 * resultGroup2AList里每行的格式有三种情况:
	 * 1. 2A 开班情况： 		<选课1-2A> <选课2-2A> <开班数>   <总人数> [<选课1-3A><选课2-3A><选课3-3A><人数>]+
	 * 2. 3A 插班生情况：	<选课1-3A> <选课2-3A> <选课3-3A> <人数>---剩余人数
	 * 3. 3A独立开班的情况：	<选课1-3A> <选课2-3A> <选课3-3A> <开班数> <总人数>
	 * 可以通过判断List.size()来鉴别上述3种情形。
		 
	 * @param s2a1xInput
	 * @return
	 * @throws Exception
	 */
	 public  List<List<String>> makeResult(Sectioning2A1XInput s2a1xInput) throws Exception{
		
		//DEBUG: 检验一下输入数据的有效性
		boolean isInputValid = validateInput(s2a1xInput);
		if (!isInputValid) {
			throw new Exception("输入数据有误，排除某个选课组合可去向的2科组合！");
		}
		
		Sectioning2A1X s2a1x = new Sectioning2A1X(s2a1xInput);
		
		List<List<String>> resultGroup2AList = s2a1x.calculateSectioning2A1X();
		
//		//DEBUG，打印出来看看结果
//		printResult2A(resultGroup2AList);
//		
//		//DEBUG，可以参考一下，如何为1X部分的学生开班
//		printResult1X(s2a1xInput, resultGroup2AList); 

		//DEBUG，检验一下，结果是不是对的：数一下各门课的选课人数，输入输出是不是一致的
		if (verifyResult(s2a1xInput, resultGroup2AList) == false) {
			throw new Exception("ERROR: 算法结果中的选课人数，跟算法输入中的选课人数有出入！");
		}
		else {
			System.out.println("INFO: 算法结果中的选课人数，跟算法输入中的选课人数是一致的。");
		}
		return resultGroup2AList;
    }


	/**
	 * 
	 * @param 学生选课列表，group3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	 * @return 每门课的教学班大小，sectionSizeList: {<选课><每个教学班平均人数><误差大小><教学班数量>}
	 * @throws IOException 
	 */
	public  List<List<String>> calculateSectionSize(Sectioning2A1XInput s2a1xInput,int bath) throws IOException {
		
		//studentCountByCourse：统计一下每门课总共有多少人上
		Map<String, Integer> studentCountByCourse = new HashMap<>();
		
		//先取3A部分的学生人数
		for (List<String> line : s2a1xInput.getGroup3AList()) {
			for (int i = 0; i < 3; i ++) {
				studentCountByCourse.merge(line.get(i).trim(), Integer.parseInt(line.get(3)), Integer::sum);
			}
		}
		
		//再取Pre1X部分的学生人数
		for(List<String> line : s2a1xInput.getPre1XList()) {
			String courseName =  line.get(0);
			int studentCount = Integer.parseInt(line.get(1));
			studentCountByCourse.merge(courseName.trim(), studentCount, Integer::sum);
		}
		
		//现在，studentCountByCourse里是全体的学生数了
		
		//把studentCountByCourse整理成 BestSectionCount 算法的输入格式
		List<List<String>> courseStudentCountList = new ArrayList<>();
		for (Map.Entry<String, Integer> me : studentCountByCourse.entrySet()) {
			List<String> line = new ArrayList<>();
			line.add(me.getKey());
			line.add("" + me.getValue().intValue());
			courseStudentCountList.add(line);
		}
		
		//1. 准备输入数据
		BestSectionCountInput solutionInput = new BestSectionCountInput();
		solutionInput.setMaxRoomCount(s2a1xInput.getMaxRoomCount());		//算法参数1：总的教室数量
		solutionInput.setTimeSlotCount(bath); 									//算法参数2：3个时间点是常见的，如果是7选4的B课情况，这里应该是4
		solutionInput.setCourseStudentCountList(courseStudentCountList); 	//算法参数3：每门课的学生人数
		
		//2. 创建算法对象
		BestSectionCount launcher = new BestSectionCount(solutionInput);
		
		//3. Go Fly! 调用算法，获得结果
		//resultList: <课程名><开班数><学生数> 
		List<List<String>> resultList = launcher.calculateSectionCount();

		//DEBUG 把结果打印出来看看
		if(Evn.isDevModel()) {
			printSectionSizeList(solutionInput, resultList);
		}
		
		//整理成sectionSizeList要求的格式
		List<List<String>> sectionSizeList = new ArrayList<>();
		for (List<String> line : resultList) {
			String courseName = line.get(0);
			int sectionCount = Integer.parseInt(line.get(1));
			int studentCount = Integer.parseInt(line.get(2));
			List<Integer> sizeList = CalculateSections.calculateSectionsByKnownCount(studentCount, sectionCount);
			
			List<String> oneCourseSizeInfo = new ArrayList<>();
			oneCourseSizeInfo.add(courseName);
			int sizeMean = sizeList.get(sizeList.size()/2);		//取中间的一个
			oneCourseSizeInfo.add("" + sizeMean); 				//sectionSizeMean;
			oneCourseSizeInfo.add("" + (int)(sizeMean * 0.15)); //sectionSizeMargin;
			oneCourseSizeInfo.add("" + sectionCount);
			sectionSizeList.add(oneCourseSizeInfo);
		}

		return sectionSizeList;
	}	
	
	//resultList: <课程名><开班数><学生数>
	private static void printSectionSizeList(BestSectionCountInput solutionInput, List<List<String>> resultList) {
		
		int totalSectionCount = 0;
		for (List<String> line : resultList) {
			String courseName = line.get(0);
			int sectionCount = Integer.parseInt(line.get(1));
			int studentCount = Integer.parseInt(line.get(2));
			List<Integer> sectionSizeList = CalculateSections.calculateSectionsByKnownCount(studentCount, sectionCount);
			System.out.println(courseName + ", 共" + sectionCount + "个教学班: " + sectionSizeList);
			
			totalSectionCount += sectionCount;
		}
		
		System.out.println("时空点共有：" + solutionInput.getMaxRoomCount() + " * " + solutionInput.getTimeSlotCount() + " = " + (solutionInput.getMaxRoomCount() * solutionInput.getTimeSlotCount()));
		System.out.println("实际开班总数为：" + totalSectionCount);
	}	
	/**
	 * 检验一下，数量数据是否有明显的错误
	 * @param s2a1xInput
	 * @return
	 */
	private static boolean validateInput(Sectioning2A1XInput s2a1xInput) {
		boolean isTooMany = isExcludingTooMany2A(s2a1xInput.getGroup3AList(), s2a1xInput.getExcludedGroup2AList());
		
		boolean isInputValid = true;
		if (isTooMany) {
			isInputValid = false;
		}
		
		return isInputValid;
	}
	
	/**
	 * 检测一下，会不会有3A组合，被要排除的2A组合覆盖了
	 * @param group3AList
	 * @param excludedGroup2AList
	 * @return
	 */
	private static boolean isExcludingTooMany2A(List<List<String>> group3AList, List<List<String>> excludedGroup2AList) {
		boolean isTooMany = false;

		//excludedGroup2A
		List<String> excludedGroup2A = new ArrayList<>();

		//for (List<String> line : CollectionUtils.emptyIfNull(excludedGroup2AList)) {
		for (List<String> line : excludedGroup2AList) {
			Collections.sort(line);
			excludedGroup2A.add(line.get(0)+line.get(1));
		}
		
		//for(List<String> line : CollectionUtils.emptyIfNull(group3AList)) {
		for(List<String> line : group3AList) {
			List<String> newLine = new ArrayList<>(); //拷贝出来，自己排序，不要破坏原来的数据
			newLine.add(line.get(0));
			newLine.add(line.get(1));
			newLine.add(line.get(2));
			Collections.sort(newLine);
			if (excludedGroup2A.contains(newLine.get(0).trim() + line.get(1).trim()) &&
					excludedGroup2A.contains(newLine.get(0).trim() + line.get(2).trim()) &&
					excludedGroup2A.contains(newLine.get(1).trim() + line.get(2).trim())) {
				System.out.println("3A 组合【" + newLine.get(0).trim() + newLine.get(1).trim() + newLine.get(2).trim() + "】被完全排除掉了！");
				isTooMany = true; //找到一个
			}
		}
		
		return isTooMany;
	}	
	
	/**
	 * 造点数据，手动遗留下来的1X部分
	 * @return pre1XList: {<选课> <人数>}
	 */
	private static List<List<String>> makeupPre1XList() {
		List<List<String>> pre1XList = new ArrayList<>();
		
		return pre1XList;
	}
			
	/**
	 * 哪些 2A组合不喜欢的，可以禁止掉。
	 * @return excludedGroup2AList： {<选课1-2A><选课2-2A>}
	 */
	private static List<List<String>> makeupExcludedGroup2AList() {
		List<List<String>> excludedGroup2AList = new ArrayList<>();
		
//		List<String> line = new ArrayList<>();
//		line.add("地理");
//		line.add("政治");
//		excludedGroup2AList.add(line);
//
//		line = new ArrayList<>();
//		line.add("历史");
//		line.add("政治");
//		excludedGroup2AList.add(line);
		
//		line = new ArrayList<>();
//		line.add("历史");
//		line.add("地理");
//		excludedGroup2AList.add(line);
		
		
		return excludedGroup2AList;
	}
	
	/**
	 * 【正常应用情况下，每门课最多老师数量应该从界面上来】，这个参数主要是用来限制1X部分的老师数量。2A部分还没有排时间点呢，所以就不要紧一些。
	 * 
	 * @param group3AList: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	 * @param sectionSizeMean: int
	 * @param sectionSizeMargin: int
	 * @return maxTeacherCountList: {<课名><总共有几个老师>}
	 */
	private static List<List<String>> calculateMaxTeacherCountList(List<List<String>> group3AList, int sectionSizeMean, int sectionSizeMargin) {
		List<List<String>> maxTeacherCountList = new ArrayList<>();
		Map<String, Integer> studentCounter = new HashMap<>();
		
		for(List<String> line : group3AList) {
			int studentCount = Integer.parseInt(line.get(3));
			studentCounter.merge(line.get(0).trim(), studentCount, Integer::sum);
			studentCounter.merge(line.get(1).trim(), studentCount, Integer::sum);
			studentCounter.merge(line.get(2).trim(), studentCount, Integer::sum);
		}

		for (Map.Entry<String, Integer> me : studentCounter.entrySet()) {
			List<String> oline = new ArrayList<>();
			int sectionCount = CalculateSections.calculateSectioning(me.getValue().intValue(), sectionSizeMean, sectionSizeMargin).size();
			oline.add(me.getKey());
			oline.add("" + (sectionCount / 3 + 1));
			maxTeacherCountList.add(oline);
		}
		
		//System.out.println("maxTeacherCountList: ");
		//maxTeacherCountList.stream().forEach(e -> System.out.println(e));
		
		return maxTeacherCountList;
	}
	
	/**
	 * 把选课数据，从XLS文件中读取出来
	 * @param excelFileName: XLS 文件名
	 * @return List<List<String>> group3AList: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	 */
	private static List<List<String>> readExcelFile(String excelFileName) {
		List<StudentVO3> studentVOList = null;
		try {
			String dataPath = System.getProperty("user.dir");
			FileInputStream fis = new FileInputStream(dataPath+"\\src\\main\\resources\\data\\" + excelFileName);
			ExcelUtil<StudentVO3> util = new ExcelUtil<StudentVO3>(StudentVO3.class);	// 创建excel工具类
			studentVOList = util.importExcel("学生信息", fis);	// 导入
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if (studentVOList == null) { //没有数据，不玩了
			System.out.println("ERROR: studentVOList == null !");
			System.exit(-1); 
		}

		Map<String, List<StudentVO3>> rawCurriculumList = studentVOList.stream().collect(Collectors.groupingBy(StudentVO3::getCurriculumCode));
		
		List<List<String>> inputList = new ArrayList<>();
		for (Map.Entry<String, List<StudentVO3>> me : rawCurriculumList.entrySet()) {
			List<String> oneLine = new ArrayList<>(4);
			oneLine.add(me.getValue().get(0).getChooseSubject1().trim());
			oneLine.add(me.getValue().get(0).getChooseSubject2().trim());
			oneLine.add(me.getValue().get(0).getChooseSubject3().trim());
			Collections.sort(oneLine);
			oneLine.add("" + me.getValue().size());
			inputList.add(oneLine);
		}
		
		return inputList;
	}
	
	//resultGroup2AList: <选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
	private static void printResult1X(Sectioning2A1XInput s2a1xInput, List<List<String>> resultGroup2AList) {
		System.out.println("\n=====BEGIN SectioningApp3::printResult1X(...)");
		
		//{ <选课1-3A> <选课2-3A> <选课3-3A> <人数> } 2+X主体部分学生的选课情况（不包括Pre1X部分）
		List<List<String>> group3AList = s2a1xInput.getGroup3AList();
		
		//courseSectionCount1X: {<Course><SectionCount>} 1X部分的计划开班数量（包括Pre1X部分）
		Map<String, Integer> courseSectionCount1X = new HashMap<>();
		//先初始化成全部的开班数量
		s2a1xInput.getSectionSizeList().stream().forEach(e -> courseSectionCount1X.put(e.get(0), new Integer(Integer.parseInt(e.get(3)))));

		//再减去2A部分的开班数量，剩下的就是1X部分（包括Pre1X）的计划开班数量
		for (List<String> oneLine : resultGroup2AList) {
			if (oneLine.size() == 5) {//3A独立开班部分
				courseSectionCount1X.merge(oneLine.get(0), -1 * Integer.parseInt(oneLine.get(3)), Integer::sum); 
				courseSectionCount1X.merge(oneLine.get(1), -1 * Integer.parseInt(oneLine.get(3)), Integer::sum); 
				courseSectionCount1X.merge(oneLine.get(2), -1 * Integer.parseInt(oneLine.get(3)), Integer::sum); 
			}
			else if (oneLine.size() > 5) {//2A开班部分
				courseSectionCount1X.merge(oneLine.get(0), -1 * Integer.parseInt(oneLine.get(2)), Integer::sum); 
				courseSectionCount1X.merge(oneLine.get(1), -1 * Integer.parseInt(oneLine.get(2)), Integer::sum); 				
			}
		}
		
		//现在，courseSectionCount1X中就是每门课在1X(包括了Pre1X)应该开的教学班数量
		
		//上面算了教学班数量，下面来算一下学生数量
		
		//studentCountByCourseName3A: {<CourseName> <StudentCount>}， 3A部分的学生数量
		//studentCountByCourseName3A是每门课的总体学生数
		Map<String, Integer> studentCountByCourseName3A = new HashMap<>();
		for (List<String> oneLine : group3AList) {
			Integer localStudentCount = Integer.parseInt(oneLine.get(3));
			studentCountByCourseName3A.merge(oneLine.get(0), localStudentCount, Integer::sum);
			studentCountByCourseName3A.merge(oneLine.get(1), localStudentCount, Integer::sum);
			studentCountByCourseName3A.merge(oneLine.get(2), localStudentCount, Integer::sum);
		}
		
		//从studentCountByCourseName3A里减去3A独立开班的学生人数
		for (List<String> oneLine : resultGroup2AList) {
			if (oneLine.size() == 5) {//3A独立开班部分
				Integer localStudentCount = Integer.parseInt(oneLine.get(4));
				studentCountByCourseName3A.merge(oneLine.get(0), -1 * localStudentCount, Integer::sum);
				studentCountByCourseName3A.merge(oneLine.get(1), -1 * localStudentCount, Integer::sum);
				studentCountByCourseName3A.merge(oneLine.get(2), -1 * localStudentCount, Integer::sum);
			}
			else if (oneLine.size() > 5) { //2A开班部分
				Integer localStudentCount = Integer.parseInt(oneLine.get(3));
				studentCountByCourseName3A.merge(oneLine.get(0), -1 * localStudentCount, Integer::sum);		
				studentCountByCourseName3A.merge(oneLine.get(1), -1 * localStudentCount, Integer::sum);		
			}
		}		

		//studentCountByCourseNamePre1X: {<CourseName> <StudentCount>}，Pre1X部分的学生数量
		Map<String, Integer> studentCountByCourseNamePre1X = new HashMap<>();
		for (List<String> oneLine : s2a1xInput.getPre1XList()) { //pre1XList: {<课程名><人数>}
			Integer localStudentCount = Integer.parseInt(oneLine.get(1));
			studentCountByCourseNamePre1X.merge(oneLine.get(0), localStudentCount, Integer::sum);
		}
		
		//计算 studentCountByCourseName1X = studentCountByCourseName3A - studentCountByCourseName2A + studentCountByCourseNamePre1X，咔嚓就算出1X部分的学生数量了
		Map<String, Integer> studentCountByCourseName1X = new HashMap<>();
		for (Map.Entry<String, Integer> c : studentCountByCourseName3A.entrySet()) {
			Integer studentCount1X = c.getValue();
			Integer studentCountPre1X = studentCountByCourseNamePre1X.getOrDefault(c.getKey(), new Integer(0));
			studentCount1X += studentCountPre1X;
			studentCountByCourseName1X.put(c.getKey(), studentCount1X);
		}
		
		//现在，studentCountByCourseName1X就是1X部分的学生数量了
		//之前，courseSectionCount1X 是1X部分的教学班数量
		
		//然后就可以开班了
		System.out.println("\n1X部分的开班情况：");
		for (Map.Entry<String, Integer> c : studentCountByCourseName1X.entrySet()) {
			String courseName = c.getKey();
			int studentCount = c.getValue().intValue();
			int sectionCount = courseSectionCount1X.get(courseName).intValue();
			
			List<Integer> sizeList = CalculateSections.calculateSectionsByKnownCount(studentCount, sectionCount);
			if (sizeList.size() == 0) {
				System.out.println("\t" + courseName + ": 没有开班计划！ 剩余学生数：" + studentCount);
			}
			else {
				System.out.println("\t" + courseName + ": " + sizeList);
			}
		}
		
		System.out.println("各课的总体理想开班情况：{<选课><每个教学班平均人数><误差大小><计划开班数量>}");
		
		//sectionSizeList: {<选课><每个教学班平均人数><误差大小><计划开班数量>}
		s2a1xInput.getSectionSizeList().stream().forEach(e -> System.out.println("\t" + e));
		
		//比较一下实际开班数量和理想开班数量之间的关系
		int totalPlanedSectionCount = 0;
		for (List<String> planLine : s2a1xInput.getSectionSizeList()) {
			totalPlanedSectionCount += Integer.parseInt(planLine.get(3));
		}
		
		System.out.println("各课的总体理想开班总数：" + totalPlanedSectionCount);
		System.out.println("时空点总数：" + (s2a1xInput.getMaxRoomCount() * 3));
		
		System.out.println("=====END SectioningApp3::printResult1X(...)");
	}
	
	/**
	 * 把返回的结果，打印出来看看
	 * @param resultGroup2AList: <选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
	 */
	private static void printResult2A(List<List<String>> resultGroup2AList) {
		System.out.println("\n=====BEGIN SectioningApp3::printResult2A(...)");
		System.out.println("算法返回的结果数据：");
		
		for (List<String> oneLine : resultGroup2AList) {
			
			//插班生的情况
			if (oneLine.size() == 4) { 
				System.out.println("\t" + oneLine);
				continue;
			}
			
			//3A独立开班的情况
			if (oneLine.size() == 5) { 
				System.out.println("\t" + oneLine);
				continue;
			}
			
			
			//2A 开班的情况
			System.out.print("\t" + oneLine.get(0) + ", " + oneLine.get(1) + ", " + oneLine.get(2) + "个班：");
			int sectionCount = Integer.parseInt(oneLine.get(2));
			int studentCount = Integer.parseInt(oneLine.get(3));
			List<Integer> sizeList = CalculateSections.calculateSectionsByKnownCount(studentCount, sectionCount);
			System.out.println(sizeList);
			for (int i = 4; i < oneLine.size(); i += 4) {
				System.out.println("\t\t" + oneLine.get(i) + ", " + oneLine.get(i + 1) + ", " + oneLine.get(i + 2) + ", " + oneLine.get(i + 3));
			}
		}
	}	

	//input3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	//result2AList: <选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
	private static boolean verifyResult(Sectioning2A1XInput s2a1xInput, List<List<String>> resultGroup2AList) {
		boolean isCorrect = true;
		
		//{ <选课1-3A> <选课2-3A> <选课3-3A> <人数> } 2+X主体部分学生的选课情况（不包括Pre1X部分）
		List<List<String>> group3AList = s2a1xInput.getGroup3AList();
		

		//上面算了教学班数量，下面来算一下学生数量
		
		//studentCountByCourseName_before: {<CourseName> <StudentCount>}， 3A部分的学生数量
		//studentCountByCourseName3A是每门课的总体学生数
		Map<String, Integer> studentCountByCourseName_input = new HashMap<>();
		for (List<String> oneLine : group3AList) {
			Integer localStudentCount = Integer.parseInt(oneLine.get(3));
			studentCountByCourseName_input.merge(oneLine.get(0), localStudentCount, Integer::sum);
			studentCountByCourseName_input.merge(oneLine.get(1), localStudentCount, Integer::sum);
			studentCountByCourseName_input.merge(oneLine.get(2), localStudentCount, Integer::sum);
		}
		
		for (List<String> oneLine : resultGroup2AList) {
			if (oneLine.size() == 5) {//3A独立开班部分
				Integer localStudentCount = Integer.parseInt(oneLine.get(4));
				studentCountByCourseName_input.merge(oneLine.get(0), -1 * localStudentCount, Integer::sum);
				studentCountByCourseName_input.merge(oneLine.get(1), -1 * localStudentCount, Integer::sum);
				studentCountByCourseName_input.merge(oneLine.get(2), -1 * localStudentCount, Integer::sum);
			}
			else if (oneLine.size() > 5) { //2A开班部分
				for (int i = 4; i < oneLine.size(); i += 4) {
					Integer localStudentCount = Integer.parseInt(oneLine.get(i + 3));
					studentCountByCourseName_input.merge(oneLine.get(i), -1 * localStudentCount, Integer::sum);
					studentCountByCourseName_input.merge(oneLine.get(i + 1), -1 * localStudentCount, Integer::sum);
					studentCountByCourseName_input.merge(oneLine.get(i + 2), -1 * localStudentCount, Integer::sum);					
				}
			}
			else {//插班生部分
				Integer localStudentCount = Integer.parseInt(oneLine.get(3));
				studentCountByCourseName_input.merge(oneLine.get(0), -1 * localStudentCount, Integer::sum);
				studentCountByCourseName_input.merge(oneLine.get(1), -1 * localStudentCount, Integer::sum);
				studentCountByCourseName_input.merge(oneLine.get(2), -1 * localStudentCount, Integer::sum);
			}
		}		

		//现在，studentCountByCourseName_input中每门课的学生人数都应该是0
		
		for(Integer i : studentCountByCourseName_input.values()) {
			if (i.intValue() != 0) {
				isCorrect = false;
				break;
			}
		}
		
		return isCorrect;
	}
	
	private static List<Integer> spreadEvenly(int totalStudentCount, int sectionCount) {
		List<Integer> sizeList = new ArrayList<>();
		for (int i = 0; i < sectionCount; i ++) {
			sizeList.add(new Integer(0));
		}
		
		for (int j = 0; j < totalStudentCount; j ++) {
			Integer count = sizeList.get(j % sectionCount);
			count ++;
			sizeList.set(j % sectionCount, count);
		}
		
		return sizeList;
	}

	
	//邹平一中， 364， 46x8
	private static List<List<String>> makeupGroup3AList1() {
		//group3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
		List<List<String>> group3AList = new ArrayList<>();
		
		//3A组合1
		List<String> oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("历史");
		oneLine.add("地理");
		oneLine.add("43");
		group3AList.add(oneLine);

		//3A组合2
		oneLine = new ArrayList<>();
		oneLine.add("地理");
		oneLine.add("物理");
		oneLine.add("化学");
		oneLine.add("30");
		group3AList.add(oneLine);
		
		//3A组合3
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("地理");
		oneLine.add("物理");
		oneLine.add("25");
		group3AList.add(oneLine);

		//3A组合4
		oneLine = new ArrayList<>();
		oneLine.add("地理");
		oneLine.add("物理");
		oneLine.add("技术");
		oneLine.add("26");
		group3AList.add(oneLine);

		//3A组合5
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("历史");
		oneLine.add("化学");
		oneLine.add("25");
		group3AList.add(oneLine);

		//3A组合6
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("化学");
		oneLine.add("生物");
		oneLine.add("24");
		group3AList.add(oneLine);

		//3A组合7
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("物理");
		oneLine.add("技术");
		oneLine.add("21");
		group3AList.add(oneLine);

		//3A组合8
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("物理");
		oneLine.add("化学");
		oneLine.add("19");
		group3AList.add(oneLine);

		//3A组合9
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("物理");
		oneLine.add("化学");
		oneLine.add("17");
		group3AList.add(oneLine);

		//3A组合10
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("历史");
		oneLine.add("生物");
		oneLine.add("16");
		group3AList.add(oneLine);

		//3A组合11
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("地理");
		oneLine.add("化学");
		oneLine.add("15");
		group3AList.add(oneLine);

		//3A组合12
		oneLine = new ArrayList<>();
		oneLine.add("物理");
		oneLine.add("化学");
		oneLine.add("技术");
		oneLine.add("15");
		group3AList.add(oneLine);

		//3A组合13
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("化学");
		oneLine.add("生物");
		oneLine.add("17");
		group3AList.add(oneLine);

		//3A组合14
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("地理");
		oneLine.add("物理");
		oneLine.add("13");
		group3AList.add(oneLine);

		//3A组合15
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("地理");
		oneLine.add("化学");
		oneLine.add("13");
		group3AList.add(oneLine);

		//3A组合16
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("物理");
		oneLine.add("技术");
		oneLine.add("8");
		group3AList.add(oneLine);

		//3A组合17
		oneLine = new ArrayList<>();
		oneLine.add("地理");
		oneLine.add("化学");
		oneLine.add("生物");
		oneLine.add("9");
		group3AList.add(oneLine);

		//3A组合18
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("历史");
		oneLine.add("技术");
		oneLine.add("6");
		group3AList.add(oneLine);

		//3A组合19
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("物理");
		oneLine.add("生物");
		oneLine.add("5");
		group3AList.add(oneLine);

		//3A组合20
		oneLine = new ArrayList<>();
		oneLine.add("物理");
		oneLine.add("化学");
		oneLine.add("生物");
		oneLine.add("4");
		group3AList.add(oneLine);

		//3A组合21
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("地理");
		oneLine.add("生物");
		oneLine.add("3");
		group3AList.add(oneLine);

		//3A组合22
		oneLine = new ArrayList<>();
		oneLine.add("化学");
		oneLine.add("生物");
		oneLine.add("技术");
		oneLine.add("3");
		group3AList.add(oneLine);

		//3A组合23
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("地理");
		oneLine.add("生物");
		oneLine.add("1");
		group3AList.add(oneLine);

		//3A组合24
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("化学");
		oneLine.add("技术");
		oneLine.add("1");
		group3AList.add(oneLine);

		//3A组合25
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("生物");
		oneLine.add("技术");
		oneLine.add("1");
		group3AList.add(oneLine);

		//3A组合26
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("物理");
		oneLine.add("生物");
		oneLine.add("1");
		group3AList.add(oneLine);

		//3A组合27
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("化学");
		oneLine.add("技术");
		oneLine.add("1");
		group3AList.add(oneLine);

		//3A组合28
		oneLine = new ArrayList<>();
		oneLine.add("地理");
		oneLine.add("化学");
		oneLine.add("技术");
		oneLine.add("1");
		group3AList.add(oneLine);

		//3A组合29
		oneLine = new ArrayList<>();
		oneLine.add("地理");
		oneLine.add("生物");
		oneLine.add("技术");
		oneLine.add("1");
		group3AList.add(oneLine);

		return group3AList;
	}
		

	//周村试验， 310， 40x8
	private static List<List<String>> makeupGroup3AList2() {
		//group3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
		List<List<String>> group3AList = new ArrayList<>();
		
		//3A组合1
		List<String> oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("地理");
		oneLine.add("生物");
		oneLine.add("127");
		group3AList.add(oneLine);

		//3A组合2
		oneLine = new ArrayList<>();
		oneLine.add("地理");
		oneLine.add("化学");
		oneLine.add("生物");
		oneLine.add("34");
		group3AList.add(oneLine);
		
		//3A组合3
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("地理");
		oneLine.add("化学");
		oneLine.add("23");
		group3AList.add(oneLine);

		//3A组合4
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("历史");
		oneLine.add("地理");
		oneLine.add("21");
		group3AList.add(oneLine);

		//3A组合5
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("化学");
		oneLine.add("生物");
		oneLine.add("19");
		group3AList.add(oneLine);

		//3A组合6
		oneLine = new ArrayList<>();
		oneLine.add("地理");
		oneLine.add("物理");
		oneLine.add("生物");
		oneLine.add("16");
		group3AList.add(oneLine);

		//3A组合7
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("地理");
		oneLine.add("物理");
		oneLine.add("15");
		group3AList.add(oneLine);

		//3A组合8
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("历史");
		oneLine.add("生物");
		oneLine.add("9");
		group3AList.add(oneLine);

		//3A组合9
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("地理");
		oneLine.add("生物");
		oneLine.add("9");
		group3AList.add(oneLine);

		//3A组合10
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("物理");
		oneLine.add("生物");
		oneLine.add("9");
		group3AList.add(oneLine);

		//3A组合11
		oneLine = new ArrayList<>();
		oneLine.add("地理");
		oneLine.add("物理");
		oneLine.add("化学");
		oneLine.add("8");
		group3AList.add(oneLine);

		//3A组合12
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("历史");
		oneLine.add("化学");
		oneLine.add("7");
		group3AList.add(oneLine);

		//3A组合13
		oneLine = new ArrayList<>();
		oneLine.add("物理");
		oneLine.add("化学");
		oneLine.add("生物");
		oneLine.add("5");
		group3AList.add(oneLine);

		//3A组合14
		oneLine = new ArrayList<>();
		oneLine.add("历史");
		oneLine.add("物理");
		oneLine.add("化学");
		oneLine.add("4");
		group3AList.add(oneLine);

		//3A组合15
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("历史");
		oneLine.add("物理");
		oneLine.add("1");
		group3AList.add(oneLine);

		//3A组合16
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("物理");
		oneLine.add("化学");
		oneLine.add("1");
		group3AList.add(oneLine);

		//3A组合17
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("物理");
		oneLine.add("生物");
		oneLine.add("1");
		group3AList.add(oneLine);

		//3A组合18
		oneLine = new ArrayList<>();
		oneLine.add("政治");
		oneLine.add("化学");
		oneLine.add("生物");
		oneLine.add("1");
		group3AList.add(oneLine);

		return group3AList;
	}
	
}
