package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.domain1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.zdsoft.newgkelective.data.optaplanner.bestsectioncount.api.BestSectionCount;
import net.zdsoft.newgkelective.data.optaplanner.bestsectioncount.api.BestSectionCountInput;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.api.Sectioning2A1X;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.api.Sectioning2A1XInput;
import net.zdsoft.newgkelective.data.optaplanner.common.CalculateSections;

/**
 * 2+x 1.5版本
 *
 */
public class SectioningApp2 {
	/**
	 * s2a1xInput
	 * 		平均班级人数 sectionSizeMean
	 * 		平均班级人数的误差值 sectionSizeMargin
	 *		总的教室数量(开设行政班数量) maxRoomCount
	 *	group3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	 *  maxTeacherCountList，每门课的老师数量： {{<课名><老师数量供1X部分使用>}}
	 *  excludedGroup2AList，不允许出现的2A组合: {<选课1-2A><选课2-2A>}
	 *  pre1XList，手动2+X遗留下来的1X部分: {<选课1-3A><选课2-3A><选课3-3A><人数><选课1X>}
	 *  
	 * @param s2a1xInput
	 * @return <选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
	 * @throws Exception
	 */
    public  List<List<String>> makeResult(Sectioning2A1XInput s2a1xInput,int batch) throws Exception{

		//1、检验一下输入数据的有效性
		boolean isInputValid = validateInput(s2a1xInput);
		if (!isInputValid) {
			throw new Exception("输入数据有误，排除某个选课组合可去向的2科组合！");
		}
		//sectionSizeList: {<选课><每个教学班平均人数><误差大小><教学班数量>} 通过调用BestSectionCountInput算法
		List<List<String>> sectionSizeList=calculateSectionSize2(s2a1xInput, batch);
		s2a1xInput.setSectionSizeList(sectionSizeList);
		
		//2. Create the launcher
		Sectioning2A1X s2a1x = new Sectioning2A1X(s2a1xInput);
		
		//3. Do it!
		//resultGroup2AList: <选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
		List<List<String>> resultGroup2AList = s2a1x.calculateSectioning2A1X();
		

		//检验一下，结果是不是对的
		if (verifyResult(s2a1xInput.getGroup3AList(), resultGroup2AList) == false) {
			throw new Exception("ERROR: Something error in the result!");
		}
		return resultGroup2AList;
    }

    /**
	 * 
	 * @param 学生选课列表，group3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	 * @return 每门课的教学班大小，sectionSizeList: {<选课><每个教学班平均人数><误差大小><教学班数量>}
	 * @throws IOException 
	 */
	private static List<List<String>> calculateSectionSize2(Sectioning2A1XInput s2a1xInput,int batch) throws IOException {
		
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
		solutionInput.setTimeSlotCount(batch); 									//算法参数2：3个时间点是常见的，如果是7选4的B课情况，这里应该是4
		solutionInput.setCourseStudentCountList(courseStudentCountList); 	//算法参数3：每门课的学生人数
		
		//2. 创建算法对象
		BestSectionCount launcher = new BestSectionCount(solutionInput);
		
		//3. Go Fly! 开拔，调用算法，获得结果
		//resultList: <课程名><开班数><学生数> 
		List<List<String>> resultList = launcher.calculateSectionCount();

		//DEBUG 把结果打印出来看看
//		printSectionSizeList(solutionInput, resultList);
		
		//整理成sectionSizeList要求的格式
		List<List<String>> sectionSizeList = new ArrayList<>();
		int allMarginSize=s2a1xInput.getSectionSizeMargin();
		int allMeanSize=s2a1xInput.getSectionSizeMean();
		int max=allMeanSize+allMarginSize;
		int min=allMeanSize+allMarginSize;
		for (List<String> line : resultList) {
			String courseName = line.get(0);
			int sectionCount = Integer.parseInt(line.get(1));
			int studentCount = Integer.parseInt(line.get(2));
			List<Integer> sizeList = CalculateSections.calculateSectionsByKnownCount(studentCount, sectionCount);
			
			List<String> oneCourseSizeInfo = new ArrayList<>();
			oneCourseSizeInfo.add(courseName);
			int sizeMean = sizeList.get(sizeList.size()/2);		//取中间的一个
			oneCourseSizeInfo.add("" + sizeMean); 				//sectionSizeMean;
			//oneCourseSizeInfo.add("" + (int)(sizeMean * 0.15)); //sectionSizeMargin;
			if(sizeMean>=min && sizeMean<=max) {
				if(sizeMean>allMeanSize) {
					oneCourseSizeInfo.add("" + (max-sizeMean));
				}else {
					oneCourseSizeInfo.add("" + (sizeMean-min));
				}
			}else {
				oneCourseSizeInfo.add("" + allMarginSize);
			}
			
			oneCourseSizeInfo.add("" + sectionCount);
			sectionSizeList.add(oneCourseSizeInfo);
		}

		return sectionSizeList;
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

		for (List<String> line : excludedGroup2AList) {
			Collections.sort(line);
			excludedGroup2A.add(line.get(0)+line.get(1));
		}
		
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
	 * 把返回的结果，打印出来看看
	 * @param resultGroup2AList: <选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
	 */
	private static void printResult(List<List<String>> resultGroup2AList) {
		resultGroup2AList.stream().forEach(e -> System.out.println(e));
		
		int sectionCount = 0;
		Map<String, List<List<String>>> group2AList = resultGroup2AList.stream().collect(Collectors.groupingBy(e -> e.get(0) + e.get(1)));
		for (Map.Entry<String, List<List<String>>> me : group2AList.entrySet()) {
			System.out.print(me.getKey() + ":");

			int studentCount = 0;
			for (int i = 0; i < me.getValue().size(); i++) {
				studentCount += Integer.parseInt(me.getValue().get(i).get(6));
			}
			int localSectionCount = Integer.parseInt(me.getValue().get(0).get(2));
			//List<Integer> sectionSizeList = CalculateSections.calculateSectioning(studentCount, sizeMean, sizeMargin);
			List<Integer> sectionSizeList = spreadEvenly(studentCount, localSectionCount);
			sectionCount += localSectionCount;
			System.out.println(sectionSizeList);
			
			//同一个3A来的Bundle，组合在一起
			Map<String, List<List<String>>> bundleList = me.getValue().stream().collect(Collectors.groupingBy(e -> e.get(3) + e.get(4) + e.get(5)));
			for (Map.Entry<String, List<List<String>>> me2 : bundleList.entrySet()) {
				int studentCountInBundle = 0;
				for (List<String> line : me2.getValue()) {
					studentCountInBundle += Integer.parseInt(line.get(6));
				}
				System.out.println("\t" + me2.getKey() + "(" + studentCountInBundle + ")");
			}
		}
		System.out.println("Total Section Count = " + sectionCount);
	}	

	//input3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	//result2AList: <选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
	private static boolean verifyResult(List<List<String>> input3AList, List<List<String>> result2AList) {
		boolean isCorrect = true;
		
		//原始输入中，每个3A组合的人数
		Map<String, Integer> input3ACounters = new HashMap<>();
		for (List<String> inputLine : input3AList) {
			List<String> code3AList = new ArrayList<>();
			code3AList.add(inputLine.get(0));
			code3AList.add(inputLine.get(1));
			code3AList.add(inputLine.get(2));
			Collections.sort(code3AList);
			input3ACounters.put(code3AList.get(0) + code3AList.get(1) + code3AList.get(2), new Integer(inputLine.get(3)));
		}
		
		//结果中的，3A组合的人数汇总一下，应该跟原始输入的总人数一致
		Map<String, Integer> output3ACounters = new HashMap<>();
		for (List<String> resultLine : result2AList) {
			List<String> code3AList = new ArrayList<>();
			code3AList.add(resultLine.get(3));
			code3AList.add(resultLine.get(4));
			code3AList.add(resultLine.get(5));
			Collections.sort(code3AList);
			
			//2A中的第一个，应该在3A中出现
			if (!code3AList.contains(resultLine.get(0))) {
				isCorrect = false;
				System.out.println("ERROR: " + resultLine);
				break;
			}
			
			//2A中的第二个，应该在3A中出现
			if (!code3AList.contains(resultLine.get(1))) {
				isCorrect = false;
				System.out.println("ERROR: " + resultLine);
				break;
			}
			
			//把分散在不同的2A中的3A组合人数，汇总起来
			Integer count = new Integer(resultLine.get(6));
			if (output3ACounters.containsKey(code3AList.get(0) + code3AList.get(1) + code3AList.get(2))) {
				Integer oldCount = output3ACounters.get(code3AList.get(0) + code3AList.get(1) + code3AList.get(2));
				oldCount += count;
				output3ACounters.put(code3AList.get(0) + code3AList.get(1) + code3AList.get(2), oldCount);
			}
			else {
				output3ACounters.put(code3AList.get(0) + code3AList.get(1) + code3AList.get(2), count);
			}
		}
		
		//把原始输入的每个3A组合弄成一个String，放到Set里
		Set<String> inputLineSet = new HashSet<>();
		for (Map.Entry<String, Integer> me1 : input3ACounters.entrySet()) {
			String s1 = me1.getKey() + me1.getValue();
			inputLineSet.add(s1);
		}
		
		//把结果数据中的每个3A组合也弄成一个String，放到Set里
		Set<String> outputLineSet = new HashSet<>();
		for (Map.Entry<String, Integer> me2 : output3ACounters.entrySet()) {
			String s2 = me2.getKey() + me2.getValue();
			outputLineSet.add(s2);
		}
		
		//这两个Set，应该是一样的才行
		if (!inputLineSet.containsAll(outputLineSet) || !outputLineSet.containsAll(inputLineSet)) {
			isCorrect = false;
			System.out.println("inputLineSet = ");
			inputLineSet.stream().forEach(e -> System.out.println(e));

			System.out.println("outputLineSet = ");
			outputLineSet.stream().forEach(e -> System.out.println(e));
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

	
	
	/**
	 * 
	 * @param 学生选课列表，group3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	 * @param 教学班的平均大小 sectionSizeMean
	 * @param 教学班的平均大小的浮动范围 sectionSizeMargin
	 * @return 每门课的教学班大小，sectionSizeList: {<选课><每个教学班平均人数><误差大小><教学班数量>}
	 */
	private static List<List<String>> calculateSectionSize(List<List<String>> group3AList, int sectionSizeMean, int sectionSizeMargin) {
		List<List<String>> sectionSizeList = new ArrayList<>();
		
		//统计一下每门课总共有多少人上
		Map<String, Integer> studentCountByCourse = new HashMap<>();
		for (List<String> line : group3AList) {
			for (int i = 0; i < 3; i ++) {
				studentCountByCourse.merge(line.get(i), Integer.parseInt(line.get(3)), Integer::sum);
			}
		}
		
		//按每个教学班的平均人数是sectionSizeMean，计算每门课的平均人数
		for(Map.Entry<String, Integer> me : studentCountByCourse.entrySet()) {
			List<Integer> sizeList = CalculateSections.calculateSectioning(me.getValue().intValue(), sectionSizeMean, sectionSizeMargin);
			List<String> onePair = new ArrayList<>();
			onePair.add(me.getKey());
			onePair.add("" + sizeList.get(0));                //sectionSizeMean
			onePair.add("" + (int) (sizeList.get(0) * 0.15)); //sectionSizeMargin = 15%
			onePair.add("" + sizeList.size());
			sectionSizeList.add(onePair);
		}
		
		return sectionSizeList;
	}
		

}
