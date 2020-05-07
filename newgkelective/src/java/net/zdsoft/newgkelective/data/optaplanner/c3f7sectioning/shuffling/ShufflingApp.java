package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.shuffling;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.ExcelUtil;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.shuffling.api.SectioningShuffle;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.shuffling.api.SectioningShuffleInput;
import net.zdsoft.newgkelective.data.optaplanner.common.StudentVO3;
import net.zdsoft.newgkelective.data.optaplanner.util.CalculateSections;


public class ShufflingApp {
	
	public static void main(String[] args) throws IOException {
		

	}
	
	
	public List<List<String>> solve(ShufflingAppDto shufflingAppDto) throws IOException{
		
		SectioningShuffleInput shuffleInput = new SectioningShuffleInput();
		
		//算法参数1：给定几个时间点
		shuffleInput.setTimeSlotCount(shufflingAppDto.getTimeSlotCount());

		//算法参数2：教室数量
		//【注意】当 maxRoomCount == 0 时，忽略本参数
		shuffleInput.setMaxRoomCount(shufflingAppDto.getMaxRoomCount());

		// 算法输入1：准备算法数据，正常情况下应该是由Client端从数据库里获取
		//     这里作为测试，从XLS文件中读取学生选课信息
		//     studentCourseSelectionList: {<studentID><选课1><选课2><选课3>}
		shuffleInput.setStudentCourseSelectionList(shufflingAppDto.getStudentCourseSelectionList());

		
		// 算法输入3：手动2+X遗留下来的1X部分
		//    pre1XList: {<studentID><选课><时间点({"T1", "T2", "T3"}，三者选一)>}
		shuffleInput.setPre1XList(shufflingAppDto.getPre1XList());

		// 算法输入4：已经排好的教学班，以便混排的时候可以少量溢出一点到这些已经排好的教学班里--暂时没有
		//    preSectionList:  {<选课名称> <已有教学班数> <时间点({"T1", "T2", "T3"}，三者选一)>}
		List<List<String>> preSectionList = new ArrayList<>(); 		
		shuffleInput.setPreSectionList(preSectionList);
		
		//============ isKeepTeacher ============ 算法输入5~7
		
		
		// 算法输入2放到后面一点有好处，在isKeepTeacher==true的情况下，通过新老师的教学计划来计算每门课的sectionSizeMean和sectionSizeMargin，更加准确。
		// 算法输入2： 准备算法数据，正常情况下应该是由Client端从界面上获取
		//     这里作为测试，自动计算了。每门课的平均大小，应该是界面上给定，这里只是个例子
		//     sectionSizeList: {<选课><每个教学班平均人数><误差大小>}
		shuffleInput.setSectionSizeList(shufflingAppDto.getSectionSizeList());
		
		
		
		//2. Create the launcher
		SectioningShuffle sectioningShuffle = new SectioningShuffle(shuffleInput);
		
		//3. Do it!
		// 返回结果   resultStudentList: {<studentID> <选课> <教学班ID> <时间点> 【<旧老师> <新老师>】}
		List<List<String>> resultStudentList = sectioningShuffle.shuffleSections();
		return resultStudentList;
	}
	
	

	/**
	 * @param resultList: {<studentID> <选课> <教学班ID> <时间点>}
	 */
	private static void printResult(List<List<String>> resultList) {
		
		//分班，按时间点+教学班ID
		Map<String, List<List<String>>> jxbList = resultList.stream().collect(Collectors.groupingBy(e -> e.get(3) + "-" + e.get(2)));
		
		//临时用来排序用，按时间点排序，输出的时候好看一点
		List<String> printoutList = new ArrayList<>();
		jxbList.entrySet().stream().forEach(e -> printoutList.add(e.getKey() + ": " + e.getValue().size()));
		Collections.sort(printoutList);
		
		//打印输出
		printoutList.stream().forEach(e -> System.out.println(e));
		
		//再数一下每个时间点有几个教学班，以便查看教室数量是否够用
		Map<String, List<List<String>>> jxbListByTimeSlot = resultList.stream().collect(Collectors.groupingBy(e -> e.get(3)));
		for (Map.Entry<String, List<List<String>>> me : jxbListByTimeSlot.entrySet()) {
			Map<String, List<List<String>>> jxbListInTimeSlot = me.getValue().stream().collect(Collectors.groupingBy(e -> e.get(2)));
			System.out.println(me.getKey() + ": " + jxbListInTimeSlot.entrySet().size());
		}
	}
	
	/**
	 * 
	 * @param studentCourseSelectionList：{<studentID><选课1><选课2><选课3>...}
	 * @param timeSlotCount: int
	 * @return 1: 时间点比选课数多；-1：时间点比选课数少；0：时间点和选课数相等
	 */
	private static int validate(List<List<String>> studentCourseSelectionList, int timeSlotCount) {
		int minCounter = 100, maxCounter = 0;
		for (List<String> line : studentCourseSelectionList) {
			if (line.size() - 1 > maxCounter) {
				maxCounter = line.size() - 1;
			}
			
			if (line.size() - 1 < minCounter) {
				minCounter = line.size() - 1;
			}
		}
		
		if (minCounter == maxCounter) {
			if (minCounter > timeSlotCount) {
				return -1;
			}
			else if (minCounter < timeSlotCount) {
				return 1;
			}
			else {
				return 0;
			}
		}
		else if (maxCounter > timeSlotCount) {
			return -1;
		}
		else {
			return 1;
		}
	}	
}
