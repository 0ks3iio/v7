package net.zdsoft.newgkelective.data.optaplanner.func;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.newgkelective.data.optaplanner.domain.batch.BatchEntity;
import net.zdsoft.newgkelective.data.optaplanner.domain.batch.BatchLecture;

public class BatchFunctions {
	
	public static int getWorkDayPlanScore(List<BatchLecture> lectureList, Integer workdayCount) {
		int score = 0;
		
		Map<Integer, List<BatchLecture>> dayLecMap = lectureList.stream().collect(Collectors.groupingBy(e->e.getPeriod().getDay()));
		int sum = (int) dayLecMap.keySet().stream().map(e->dayLecMap.get(e).size()).collect(Collectors.summarizingInt(e->e)).getSum();
		double avg = sum/workdayCount;
		
		Set<Integer> distinct = new HashSet<>();
		for(int i=0;i<workdayCount;i++) {
			distinct.add(dayLecMap.get(i)==null?0:dayLecMap.get(i).size());
		}
		if(distinct.size()==2) {
			Integer[] array = distinct.toArray(new Integer[0]);
			if(Math.abs(array[0]-array[1])==1)
				return 0;
		}else if(distinct.size()==1) {
			return 0;
		}
		
		score = dayLecMap.keySet().stream().map(e -> dayLecMap.get(e).size())
					.map(e -> Math.pow(avg - e, 2))
					.collect(Collectors.summingDouble(e -> e)).intValue();
		
		return score;
	}
	
	public static int getWorkBlanceScore(List<BatchLecture> lectureList) {
		int score = 0;
		
		Map<BatchEntity, List<BatchLecture>> dayLecMap = lectureList.stream().collect(Collectors.groupingBy(BatchLecture::getBatchEntity));
		for (BatchEntity en : dayLecMap.keySet()) {
			List<BatchLecture> list = dayLecMap.get(en);
			Set<Integer> days = list.stream().map(e->e.getPeriod().getDay()).collect(Collectors.toSet());
			
			if(days.size() < list.size() && days.size()<7) {
				score += 7- days.size();
			}else {
				// 分散一些
				if(list.size() ==2 ) {
					int abs = Math.abs(list.get(0).getPeriod().getDay()-list.get(1).getPeriod().getDay());
					if(abs <=1) {
						score += (2-abs);
					}
				}else if(list.size() ==3 ) {
					int abs = days.stream().max(Integer::compareTo).get() - days.stream().min(Integer::compareTo).get();
					if(abs <=2) {
						score += (3-abs);
					}
				}
			}
		}
		
		return score;
	}
	
}
