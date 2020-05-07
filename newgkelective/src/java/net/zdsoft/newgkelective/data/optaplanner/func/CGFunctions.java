package net.zdsoft.newgkelective.data.optaplanner.func;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGConstants;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseSection;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGDay;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGPeriod;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CGFunctions {
	private static Logger logger = LoggerFactory.getLogger(CGFunctions.class);

	/**
	 * 合班 同时排课 计分
	 * @param lectureList
	 * @return
	 */
	public static int getMeawhileScore(List<CGSectionLecture> lectureList) {
		int score = 0;
		
		Map<CGCourseSection, List<CGSectionLecture>> sectionLectureMap = lectureList.stream().collect(Collectors.groupingBy(CGSectionLecture::getCourseSection));
		
		List<List<CGSectionLecture>> values = new ArrayList<>(sectionLectureMap.values());
		List<CGSectionLecture> list = null;
		List<CGSectionLecture> list2 = null;
		Set<CGPeriod> collect = null;
		Set<CGPeriod> collect2 = null;
		int min = 0;
		int sec = 0;
		for (int j=0;j<values.size();j++) {
			list = values.get(j);
			for (int k=j++;k<values.size();k++) {
				list2 = values.get(k);
				
				collect = list.stream().map(e->e.getPeriod()).collect(Collectors.toSet());
				collect2 = list2.stream().map(e->e.getPeriod()).collect(Collectors.toSet());
				min = Math.min(collect.size(), collect2.size());
				sec = Sets.intersection(collect, collect2).size();
				if(sec < min)
					score += square(min - sec);
				
			}
		}
		
		
		return score;
	}

	/**
	 * 计算 不同时排课 的分数；部分班级不能同时 排课
	 * @param lectureList
	 * @return
	 */
	public static int getNoMeawhileScore(List<CGSectionLecture> lectureList) {
		int score = 0;
		
		Map<String, List<CGSectionLecture>> oldIdLecMap = lectureList.stream().collect(Collectors.groupingBy(e->e.getCourseSection().getOldId()));
		List<List<CGSectionLecture>> arrayList = new ArrayList<>(oldIdLecMap.values());
		for(int i=0;i<arrayList.size();i++) {
			List<CGSectionLecture> list = arrayList.get(i);
			for(int j=i+1;j<arrayList.size();j++) {
				List<CGSectionLecture> list2 = arrayList.get(j);
				for (CGSectionLecture le1 : list) {
					for (CGSectionLecture le2 : list2) {
						if(le1.getPeriod().equals(le2.getPeriod())) {
							score++;
						}
					}
				}
			}
		}
		
		return score;
	}
	
	/**
	 * 检验同一个行政班 单双周配对 是否满足 
	 * @param lectureList
	 * @return
	 */
	public static int getWeekCoupleScore(List<CGSectionLecture> lectureList) {
		int score =0;
		
		Function<CGCourse,String> cFun = c->c.getCode();
		Map<String, CGSectionLecture> courseCodeLectureMap = lectureList.stream().filter(e->e.getIsBiWeekly() != CGCourse.WEEK_TYPE_NORMAL)
				.collect(Collectors.toMap(e->cFun.apply(e.getCourse()), Function.identity()));
		for (CGSectionLecture l : lectureList) {
			if(StringUtils.isNotBlank(l.getCourse().getIsBiweeklyCourse())) {
				CGSectionLecture l2 = courseCodeLectureMap.get(l.getCourse().getIsBiweeklyCourse());
				if(l2 != null && !l2.getPeriod().equals(l.getPeriod())) {
					score += 1;
				}else if(l2 == null){
					throw new RuntimeException("找不到"+l.getCourse().getIsBiweeklyCourse()+"对应的课程");
				}
			}
		}

		return score;
	}
	
	public static int getCoupleAndMinworkDayScore(List<CGSectionLecture> lectureList,CGConstants constants) {
		int score = 0;
		
		score += getCoupleScore(lectureList,constants);
		score += getMinWorkDayDiff(lectureList);
		
		return score;
	}
	
	public static int getCoupleScore(List<CGSectionLecture> lectureList, CGConstants constants) {
		int score = 0;
		
		int count = (int)lectureList.stream().filter(l->l.getTimeSlotCount()!=1).count();
		if(count < 1)
			return score;
		
		int workDayCount = constants.getWorkDayCount();
		int min = count/workDayCount;
		int max = (count-1)/workDayCount + 1;
		
		Map<CGDay, List<CGSectionLecture>> collect = lectureList.stream().collect(Collectors.groupingBy(CGSectionLecture::getDay));
		for (List<CGSectionLecture> ls : collect.values()) {
			Collections.sort(ls, (x,y)->x.getTimeSlotIndex()-y.getTimeSlotIndex());
			
			int cc = 0;
			for(int i=0;i<ls.size();i++) {
				if(ls.get(i).getTimeSlotCount() == 2) {  // 指定连排的课程
					if(i+1 >= ls.size()) {  // 这是今天最后一节课程 不可能连排
						score += 2;
					}else if(ls.get(i+1).getTimeSlotCount() == 2) {  // 也是连排课程，两节设置为连排的课程不能放在一起
						score += 2;
					}else if(ls.get(i+1).getTimeSlotIndex() - ls.get(i).getTimeSlotIndex() != 1) { // 不联排 中间有时间空隙
						score += 2;
					} else if (!ls.get(i + 1).getPeriod().getTimeslot().getPeriodInterval()
							.equals(ls.get(i).getPeriod().getTimeslot().getPeriodInterval())) {  // 连排但 不是同一个时间段 例如：一个上午最后一节 一个 下午第一节
						score += 2;
					}
					
					cc++;
				}
			}
			
			if(cc < min) {
				score += 10;
			}
			if(cc > max) {
				score += 10;
			}
		}
		
		return score;
	}
	
	public static int[] getTeacherAllScore(List<CGSectionLecture> lectureList,int workDayCount) {
		int[] softScores = new int[] {0,0,0}; 
		
		softScores[1] += calcTeacherScheduleScore(lectureList);
		softScores[2] += calcTeacherArrangeScore(lectureList, workDayCount);
		
		return softScores;
	}
	
	/**
	 * 教师教学进度一致
	 * @return
	 */
	public static int calcTeacherScheduleScore(List<CGSectionLecture> lectureList) {
		int score = 0;
		Map<CGCourseSection, List<CGSectionLecture>> sectionLeMap = lectureList.stream().filter(l -> l.getSectionLectureList().size() >= 3)
				.collect(Collectors.groupingBy(CGSectionLecture::getCourseSection));
		if(sectionLeMap.size() < 2)
			return score;
		

		List<List<CGSectionLecture>> sectionLectures = sectionLeMap.values().stream()
//				.map(e->e.stream().sorted((x,y)->x.getDay().getDayIndex()-y.getDay().getDayIndex()).collect(Collectors.toList()))
				.collect(Collectors.toList());
		List<CGSectionLecture> list = null;
		List<CGSectionLecture> list2 = null;
		Set<CGDay> collect = null;
		Set<CGDay> collect2 = null;
		int min = 0;
		int size = 0;
		for (int j=0;j<sectionLectures.size();j++) {
			for (int k=j+1;k<sectionLectures.size();k++) {
				list = sectionLectures.get(j);
				list2 = sectionLectures.get(k);
				if(!list.get(0).getCourse().equals(list2.get(0).getCourse())) // 不是同一种科目 无需教学进度一致
					continue;
				
				collect = list.stream().map(e->e.getPeriod().getDay()).collect(Collectors.toSet());
				collect2 = list2.stream().map(e->e.getPeriod().getDay()).collect(Collectors.toSet());
				min = Math.min(collect.size(), collect2.size());
				size = Sets.intersection(collect, collect2).size();
				if(size < min)
					score += square(min-size);
				else {
					//TODO 使每天的课时数相等
				}
			}
		}
		
		
		score = getCoupleDistributeScore(score, sectionLectures);
		
		return score;
	}
	
	/**
	 * 连排的情况下 对老师的教学进度 计分
	 */
	private static int getCoupleDistributeScore(int score, List<List<CGSectionLecture>> sectionLectures) {
		for (int j=0;j<sectionLectures.size();j++) {
			for (int k=j+1;k<sectionLectures.size();k++) {
				List<CGSectionLecture> l1 = sectionLectures.get(j).stream().filter(e->e.getTimeSlotCount()>1).collect(Collectors.toList());
				List<CGSectionLecture> l2 = sectionLectures.get(k).stream().filter(e->e.getTimeSlotCount()>1).collect(Collectors.toList());
				
				int max = Math.max(l1.size(), l2.size());
				for(int i=0;i<max;i++) {
					if(l1.size() <= i || l2.size() <= i || !l1.get(i).getCourse().equals(l2.get(i).getCourse()))
						continue;
					score += square(l1.get(i).getDay().getDayIndex() - l2.get(i).getDay().getDayIndex());
				}
				
			}
		}
		return score;
	}
	
	public static int getDayIntervalScore(List<CGSectionLecture> lectureList) {
		int score = 0;
		
		Map<Integer, List<Integer>> timeslotDayMap = lectureList.stream().filter(e->e.getPeriod().getTimeslot().getTimeslotIndex()<3)
			.collect(Collectors.groupingBy(e->e.getPeriod().getTimeslot().getTimeslotIndex(), 
					Collectors.mapping(e->e.getPeriod().getDay().getDayIndex(),Collectors.toList())));
		for (List<Integer> days : timeslotDayMap.values()) {
			if(days.size()>3) {
				score += (days.size()-3) * 10;
			}else if(days.size() == 3 && isContinuous(days) == 0) {
				// 上午 1 2节课 有三天 并且连续 
				score += 10;
			}
		}
		
		return score;
	}
	
	public static int getTeacherScheduleBlanceScore(List<CGSectionLecture> ls) {
		int score = 0;
		Map<CGDay, List<CGSectionLecture>> dayLectureMap = ls.stream().collect(Collectors.groupingBy(e->e.getPeriod().getDay()));
		Set<CGCourseSection> allSections = ls.stream().map(CGSectionLecture::getCourseSection).collect(Collectors.toSet());
		Map<CGCourse, List<CGCourseSection>> courseSctionMap = allSections.stream().collect(Collectors.groupingBy(CGCourseSection::getCourse));
		
		Map<CGCourseSection,Integer> historyCountMap = new HashMap<>();
		for (CGDay d : dayLectureMap.keySet().stream().sorted(CGDay::compareTo).collect(Collectors.toList())) {
			List<CGSectionLecture> list = dayLectureMap.get(d);
			Map<CGCourse, List<CGSectionLecture>> clM = list.stream().collect(Collectors.groupingBy(e->e.getCourse()));
			for (CGCourse course : clM.keySet()) {
				List<CGSectionLecture> list2 = clM.get(course);
				Map<CGCourseSection, List<CGSectionLecture>> collect = list2.stream().collect(Collectors.groupingBy(e->e.getCourseSection()));
				List<CGCourseSection> scL = courseSctionMap.get(course);
				for(int i=0;i<scL.size();i++) {
					int size1 = Optional.ofNullable(collect.get(scL.get(i))).orElse(new ArrayList<>()).size();
					if(!historyCountMap.containsKey(scL.get(i))) {
						historyCountMap.put(scL.get(i), 0);
					}
					historyCountMap.put(scL.get(i), size1 + historyCountMap.get(scL.get(i)));
				}
				for(int i=0;i<scL.size();i++) {
					int size1 = historyCountMap.get(scL.get(i));
					for(int j=i+1;j<scL.size();j++) {
						int size2 = historyCountMap.get(scL.get(j));
						
						score += (size1 - size2)*(size1 - size2);
					}
				}
				
			}
			
			
		}
		
		return score;
	}
	
	/**
	 * 评估一个老师在一天中 上的课的连续性
	 * @param lectureList
	 * @return
	 */
	public static int evaluateTeacherLectures(List<CGSectionLecture> lectureList) {
		Map<CGDay, List<CGSectionLecture>> dayLectureMap = lectureList.stream().collect(Collectors.groupingBy(l->l.getPeriod().getDay()));
		int score = 0;
		for (List<CGSectionLecture> ls : dayLectureMap.values()) {
			List<Integer> slotIndexs = ls.stream().filter(e->e.getPeriod().getTimeslot().getPeriodInterval().equals("2")).map(e->e.getPeriod()
						.getTimeslot().getTimeslotIndex())
					.sorted()
					.collect(Collectors.toList());
			score += isContinuous(slotIndexs);
			List<Integer> slotIndexs2 = ls.stream().filter(e->e.getPeriod().getTimeslot().getPeriodInterval().equals("3")).map(e->e.getPeriod()
					.getTimeslot().getTimeslotIndex())
					.sorted()
					.collect(Collectors.toList());
			score += isContinuous(slotIndexs2);
			
		}
		
		return score;
	}
	
	private static int isContinuous(List<Integer> slotIndexs) {
		Collections.sort(slotIndexs);
		Iterator<Integer> iterator = slotIndexs.iterator();
		int score = 0;
		int prev = -1;
		int now = -1;
		while(iterator.hasNext()) {
			now = iterator.next();
			if(prev != -1) {
				score += square(now - prev -1);
			}
			prev = now;
		}
		
		return score;
	}
	public static void main(String[] args) {
		int continuous = isContinuous(Arrays.asList(1,2,3,4,5,8));
		System.out.println(continuous);
	}
	public static int calcLectureConflict(CGPeriod p1, CGPeriod p2, int constraint) {
		int score = 0;
		switch(constraint) {
			case -1:  // 不能在同一时间点
				if(p1.getPeriodCode().equals(p2.getPeriodCode())) {
					score = 1;
				}
				break;
			case 1:  // 必须在同一天 或者隔一天
				int dayInterval = Math.abs(p1.getDay().getDayIndex() - p2.getDay().getDayIndex());
				if(dayInterval > 1) {
					score = dayInterval;
				}
				break;
			case 2:  // 必须同一时间点
				if(!p1.getPeriodCode().equals(p2.getPeriodCode())) {
					score = 1;
				}
				break;
			default :
		}
		return score;
	}
	
	/**
	 * 课程在半天和 一天之内课时分配
	 * @param list 一个section的所有lecture 比如 1班语文 的所有 lecture
	 * @param maxTimeslotIndex
	 * @return
	 */
	public static int calcLectureArrangeScore(List<CGSectionLecture> list, CGConstants constant) {
		Integer amCount = constant.getAmCount();
		Integer pmCount = constant.getPmCount();
		
		int score = 0;
		
		if(list.size() <= 0) {
			return score;
		}
		String arrangeDay = list.get(0).getCourse().getArrangeDay();
		String arrangeHalfDay = list.get(0).getCourse().getArrangeDay();
		if(arrangeDay == null || arrangeHalfDay == null) {
			logger.error("course arrange error:课程"+list.get(0).getCourse().getCode()+" 课时分配为null");
			return score;
		}
		
		Map<CGDay, List<CGSectionLecture>> collect = list.stream().collect(Collectors.groupingBy(l->l.getPeriod().getDay()));
		
		for (List<CGSectionLecture> dl : collect.values()) {
			List<CGSectionLecture> amLs = dl.stream().filter(l->l.getPeriod().getTimeslot().getPeriodInterval().equals("2")).collect(Collectors.toList());
			List<CGSectionLecture> pmLs = dl.stream().filter(l->l.getPeriod().getTimeslot().getPeriodInterval().equals("3")).collect(Collectors.toList());
			
			switch(arrangeDay) {
			case "02":  score += square(pmLs.size()); break; // 上午多
			case "03":	score += square(amLs.size()); break; // 下午多
			case "04":	score += square(amLs.size() - pmLs.size());// 均衡
				break;
			default:
			}
			
			// 半天内分配
			score += getHalfDayArrange(amCount,amCount, arrangeHalfDay, amLs);
			score += getHalfDayArrange(pmCount,pmCount + amCount, arrangeHalfDay, pmLs);
		}
		
		return score;
	}
	
	private static int square(int size) {
		return (int)Math.pow(size, 2);
	}

	private static int getHalfDayArrange(int intervalCount, int timeslotIndex, String arrangeHalfDay,
			List<CGSectionLecture> amLs) {
		int score = 0;
		if(amLs.size() <= 1)
			return score;
		
		switch(arrangeHalfDay) {
		case "02": score += amLs.stream().map(e->square(e.getPeriod().getTimeslot().getTimeslotIndex())).reduce((x,y)->x+y).orElse(0); //靠前
			break;
		case "03": score += amLs.stream().map(e->square(timeslotIndex - e.getPeriod().getTimeslot().getTimeslotIndex())).reduce((x,y)->x+y).orElse(0); //靠后
			break;
		case "04": 
			int maxGap = (intervalCount - amLs.size() - 1)/(amLs.size()-1)+1  +1;
			int minGap = (intervalCount - amLs.size())/(amLs.size()-1)  +1;
			List<Integer> collect = amLs.stream().map(e->e.getPeriod().getTimeslot().getTimeslotIndex()).sorted().collect(Collectors.toList());
			int pre = -1;
			for (Integer timesIndex : collect) {
				if(pre != -1) {
					int gap = Math.abs(timesIndex-pre);
					if(gap > maxGap || gap < minGap) {
						score += square(maxGap-gap);
					}
				}
				pre = timesIndex;
			}
			break;
		}
		return score;
	}
	
	/**
	 * 教师的课时分布
	 * @param list
	 * @param workDayCount
	 * @return
	 */
	public static int calcTeacherArrangeScore(List<CGSectionLecture> list,int workDayCount) {
		int score = 0;
		String weekTimeArrange = list.get(0).getCourseSection().getTeacher().getWeekTimeArrange();
		String dayTimeArrange = list.get(0).getCourseSection().getTeacher().getDayTimeArrange();
		
		if(weekTimeArrange == null || dayTimeArrange == null) {
			logger.error("teacher lecture arrange error:教师"+list.get(0).getTeacherCode()+" 课时分配为null");
			return score;
		}
		
		score += getSameHalfDayScore(list,score);
//		score += 1;
		score += evaluateTeacherLectures(list);
		
		if(true)
			return score;
//		int maxCountPerDay = (list.size()-1)/workDayCount + 1;
//		int minCountPerDay = list.size()/workDayCount;
//		switch(weekTimeArrange) {
//		case "1":  // 均衡
//			Map<Integer, List<CGSectionLecture>> collect = list.stream().collect(Collectors.groupingBy(l->l.getPeriod().getDay().getDayIndex()));
//			Integer ts = collect.values().stream().filter(e->e.size()>maxCountPerDay || e.size()<minCountPerDay)
//					.map(e->square(e.size()-maxCountPerDay))
//					.reduce(Integer::sum).orElse(0);
//			score += ts;
//			break;
//		case "2":  // 集中
//			score += getVariance(list.stream().map(e->e.getDay().getDayIndex()).collect(Collectors.toList()));
//			break;
//		}
		
		Map<CGDay, List<CGSectionLecture>> dl = list.stream().collect(Collectors.groupingBy(e->e.getPeriod().getDay()));
		Set<String> amt = new HashSet(Arrays.asList("1","2"));
		for (List<CGSectionLecture> ls : dl.values()) {
			List<CGSectionLecture> aml = ls.stream().filter(e->amt.contains(e.getPeriod().getTimeslot().getPeriodInterval())).collect(Collectors.toList());
			List<CGSectionLecture> pml = ls.stream().filter(e->!aml.contains(e)).collect(Collectors.toList());
			
			int min = Math.min(aml.size(), pml.size());
			List<CGSectionLecture> minLs;
			List<CGSectionLecture> maxLs;
			if(min == aml.size()) {
				minLs = aml;
				maxLs = pml;
			}else {
				minLs = pml;
				maxLs = aml;
			}
			
			switch(dayTimeArrange) {
			case "2":  // 同半天
				score += min;
				break;
			case "3":  // 同半天连续
				score += min;
				
				score += isContinuous(maxLs.stream().map(e->e.getPeriod().getTimeslot().getTimeslotIndex()).collect(Collectors.toList()));
				
				break;
			case "4":  // 同半天间隔
				score += min;
				
				score += getVariance(maxLs.stream().map(e->e.getPeriod().getTimeslot().getTimeslotIndex()).collect(Collectors.toList()));
				break;
			}
		}
		
		return score;
	}
	
	public static int getSameHalfDayScore(List<CGSectionLecture> list, int score) {
		for(int i=0;i<list.size();i++) {
			for(int j=i+1;j<list.size();j++) {
				CGPeriod period = list.get(i).getPeriod();
				CGPeriod period2 = list.get(j).getPeriod();
				if(period.getDay().equals(period2.getDay()) 
						&& !period.getTimeslot().getPeriodInterval().equals(period2.getTimeslot().getPeriodInterval())) {
					score += 10;
				}
			}
		}
		return score;
	}
	
	private static int getVariance(List<Integer> list) {
		Double avg = list.stream().collect(Collectors.averagingInt(e->e));
		return (int)Math.ceil(list.stream().map(e->Math.pow(avg-e, 2)).reduce((x,y)->x+y).orElse(0.0d));
	}
	
	public static int calcLectureIntervalConstraintScore(CGSectionLecture left, CGSectionLecture right, float interval) {
		int score = 0;
		CGPeriod period = left.getPeriod();
		CGPeriod period2 = right.getPeriod();
		int ai = period.getDay().getDayIndex() - period2.getDay().getDayIndex();
		switch(interval+"") {
		case "0.1": 
			if(period.isContinus(period2)) {
				score = 2;
			}
			break;
		case "0.5": 
			if(period.getDay().equals(period2.getDay()) 
					&& period.getTimeslot().getPeriodInterval().equals(period2.getTimeslot().getPeriodInterval())) {
				score = 2;
			}
			break;
		case "1.0": 
			if(Math.abs(ai) < 1) {
				score = 2;
			}
			break;
		case "2.0": 
			if(Math.abs(ai) < 2) {
				score = 2;
			}
			break;
		case "3.0": 
			if(Math.abs(ai) < 3) {
				score = 2;
			}
			break;
		}
		
		return score;
	}
	
	public static int subPeriod(CGPeriod leftPeriod, CGPeriod rightPeriod) {
		if(leftPeriod.getDay().getDayIndex() > rightPeriod.getDay().getDayIndex()) {
			return 0;
		}else if(leftPeriod.getDay().getDayIndex() < rightPeriod.getDay().getDayIndex()) {
			return 1;
		}else if(leftPeriod.getTimeslot().getTimeslotIndex() > rightPeriod.getTimeslot().getTimeslotIndex()) {
			return 0;
		}else {
			return 1;
		}
		
	}
	
	
	public static int getMinWorkDayDiff(List<CGSectionLecture> lectureList) {
		if(lectureList.stream().allMatch(e->e.getPeriodList().size()==lectureList.size())) {
			return 0;
		}
		
		Set<CGDay> daySet = new HashSet<CGDay>();
		for(CGSectionLecture lectureItem : lectureList){
			daySet.add(lectureItem.getPeriod().getDay());
		}
		
		int minWorkDay = lectureList.get(0).getMinWorkingDaySize(); 
		if (daySet.size() >= minWorkDay)
			return 0;
		else
			return minWorkDay - daySet.size();		
	}
}
