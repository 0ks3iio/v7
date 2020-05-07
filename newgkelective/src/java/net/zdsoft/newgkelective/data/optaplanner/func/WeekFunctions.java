package net.zdsoft.newgkelective.data.optaplanner.func;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.zdsoft.newgkelective.data.optaplanner.domain.biweekly.Section;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;

public class WeekFunctions {
	
	/**
	 * 保证 一个行政班内单双周均匀
	 * @param section
	 * @return
	 */
	public static int getSectionWeeklyScore(List<Section> section, Map<Integer,Integer> coupleWeekIdMap) {
		int score = 0;
		
		Map<String, List<Section>> collect = section.stream().collect(Collectors.groupingBy(Section::getOldId));
		for (List<Section> sections : collect.values()) {
			int count1 = (int)sections.stream().filter(e->CGCourse.WEEK_TYPE_EVEN == e.getIsBiweekly().intValue()).map(e->e.getIsBiweekly()).count();
			int count2 = (int)sections.stream().filter(e->CGCourse.WEEK_TYPE_ODD == e.getIsBiweekly().intValue()).map(e->e.getIsBiweekly()).count();
			
			int x = Math.abs(count1 - count2);
			if(x > 1) {
				score += square(x-1);
			}
		}
		
		// 符合 单双周配对要求
		Map<Integer, Integer> idWeekMap = section.stream().collect(Collectors.toMap(Section::getId, Section::getIsBiweekly));
		for (Integer id : coupleWeekIdMap.keySet()) {
			Integer cid = coupleWeekIdMap.get(id);
			
			Integer weekType1 = idWeekMap.get(id);
			Integer weekType2 = idWeekMap.get(cid);
			
			if(weekType1 == null) {
				System.out.println("找不到id="+id+"的单双周");
				continue;
			}
			if(weekType2 == null) {
				System.out.println("找不到id="+cid+"的单双周");
				continue;
			}
			
			if((weekType1 + weekType2) != 0) {
				score += 1;
			}
		}
		
		return score;
	}
	
	/**
	 * 保证一个老师带的单双周均匀
	 * @param section
	 * @return
	 */
	public static int getTeacherWeeklyScore(List<Section> section) {
		int score = 0;
		
		Map<String, List<Section>> collect = section.stream().filter(s->s.getTeacherCode() != null).collect(Collectors.groupingBy(Section::getTeacherCode));
		for (List<Section> sections : collect.values()) {
			int count1 = (int)sections.stream().filter(e->CGCourse.WEEK_TYPE_EVEN == e.getIsBiweekly().intValue()).map(e->e.getIsBiweekly()).count();
			int count2 = (int)sections.stream().filter(e->CGCourse.WEEK_TYPE_ODD == e.getIsBiweekly().intValue()).map(e->e.getIsBiweekly()).count();
			
			int x = Math.abs(count1 - count2);
			if(x > 1) {
				score += square(x-1);
			}
		}
		
		return score;
	}
	private static int square(int size) {
		return (int)Math.pow(size, 2);
	}
}
