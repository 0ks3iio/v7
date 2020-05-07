package net.zdsoft.newgkelective.data.optaplanner.shuff.func;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

import net.zdsoft.newgkelective.data.optaplanner.shuff.domain.GroupClass;
import net.zdsoft.newgkelective.data.optaplanner.shuff.domain.GroupSubject;
import net.zdsoft.newgkelective.data.optaplanner.shuff.domain.Subject;
import net.zdsoft.newgkelective.data.optaplanner.shuff.domain.TimeSlot;


public class ShuffFunction {

	/**
	 * 组合 相同科目必须一致
	 * @param groupSubjectList
	 * @return 没在一起的参数
	 */
	public static int getCalaSameBath(List<GroupSubject> groupSubjectList) {
		int score=0;
		Map<GroupClass,List<GroupSubject>> groupMap=new HashMap<GroupClass, List<GroupSubject>>();
		for(GroupSubject g:groupSubjectList) {
			GroupClass key = g.getGroup().getGroupClass();
			if(CollectionUtils.isNotEmpty(key.getSameSubjectId())) {
				if(!groupMap.containsKey(key)) {
					groupMap.put(key, new ArrayList<>());
				}
				groupMap.get(key).add(g);
			}
		}
		if(groupMap.size()>0) {
			for(Entry<GroupClass, List<GroupSubject>> item:groupMap.entrySet()) {
				List<String> sameSubList = item.getKey().getSameSubjectId();
				Map<Subject,Map<TimeSlot,Integer>> map1=new HashMap<>();
				for(GroupSubject g:item.getValue()) {
					Subject sub = g.getSubject();
					if(!sameSubList.contains(sub.getSubjectId())) {
						continue;
					}
					Map<TimeSlot, Integer> map2 = map1.get(sub);
					if(map2==null) {
						map2=new HashMap<>();
						map1.put(sub, map2);
					}
					Integer ii = map2.get(g.getTimeSlot());
					if(ii==null) {
						map2.put(g.getTimeSlot(), g.getStuNum());
					}else {
						map2.put(g.getTimeSlot(), map2.get(g.getTimeSlot())+g.getStuNum());
					}
				}
				for(Entry<Subject, Map<TimeSlot, Integer>> item2:map1.entrySet()) {
					if(item2.getValue().size()<=1) {
						continue;
					}
					Integer[] arr = item2.getValue().values().toArray(new Integer[] {});
					//arr[] 最大值
					int max=0;
					for(int r:arr) {
						if(r>max) {
							max=r;
						}
						score=score+r;
					}
					score=score-max;
				}
				
			}
		}
		
		return score;
	}
	/**
	 * 一个班级内同样科目尽量一个批次
	 * @param groupSubjectList
	 * @return
	 */
	public static int getCalaSameBathInGroup(List<GroupSubject> groupSubjectList) {
		int score=0;
		Map<GroupClass,List<GroupSubject>> groupMap=new HashMap<GroupClass, List<GroupSubject>>();
		for(GroupSubject g:groupSubjectList) {
			GroupClass key = g.getGroup().getGroupClass();
			if(!groupMap.containsKey(key)) {
				groupMap.put(key, new ArrayList<>());
			}
			groupMap.get(key).add(g);
		}
		if(groupMap.size()>0) {
			for(Entry<GroupClass, List<GroupSubject>> item:groupMap.entrySet()) {
				Map<Subject,Map<TimeSlot,Integer>> map1=new HashMap<>();
				for(GroupSubject g:item.getValue()) {
					Subject sub = g.getSubject();
					Map<TimeSlot, Integer> map2 = map1.get(sub);
					if(map2==null) {
						map2=new HashMap<>();
						map1.put(sub, map2);
					}
					Integer ii = map2.get(g.getTimeSlot());
					if(ii==null) {
						map2.put(g.getTimeSlot(), 1);
					}else {
						map2.put(g.getTimeSlot(), map2.get(g.getTimeSlot())+1);
					}
				}
				for(Entry<Subject, Map<TimeSlot, Integer>> item2:map1.entrySet()) {
					if(item2.getValue().size()<=1) {
						continue;
					}
					Integer[] arr = item2.getValue().values().toArray(new Integer[] {});
					//arr[] 最大值
					int max=0;
					for(int r:arr) {
						if(r>max) {
							max=r;
						}
						score=score+r;
					}
					score=score-max;
				}
				
			}
		}
		
		return score;
	}
	/**
	 * 单个批次的某个科目人数在班级平均人数一半
	 * @param groupSubjectList
	 * @return
	 */
	public static int getMinStudentInGroup(List<GroupSubject> groupSubjectList) {
		int score=0;
		Map<Subject,Map<TimeSlot,Integer>> map1=new HashMap<>();
		for(GroupSubject g:groupSubjectList) {
			Subject sub = g.getSubject();
			Map<TimeSlot, Integer> map2 = map1.get(sub);
			if(map2==null) {
				map2=new HashMap<>();
				map1.put(sub, map2);
			}
			Integer ii = map2.get(g.getTimeSlot());
			if(ii==null) {
				map2.put(g.getTimeSlot(), g.getStuNum());
			}else {
				map2.put(g.getTimeSlot(), map2.get(g.getTimeSlot())+g.getStuNum());
			}
		}
		
		for(Entry<Subject, Map<TimeSlot, Integer>> item2:map1.entrySet()) {
			Subject subK = item2.getKey();
			Map<TimeSlot, Integer> val = item2.getValue();
			int mean = subK.getSectionSizeMean();
			int mar=subK.getSectionSizeMargin();
			for(Entry<TimeSlot, Integer> item:val.entrySet()) {
				if(item.getValue()<mean/2) {
					score=score+100;
				}else if(item.getValue()<mean*3/4) {
					score=score+10;
				}else if(item.getValue()>mean+mar){
					int a=item.getValue()/mean;
					int b=item.getValue()%mean;
					int ll=item.getValue()-mean*a;
					if(ll/a-mar>mean*1/3 && ll/a-mar<mean*3/4) {
						score=score+1;
					}
				}
				
			}
		}
		return score;
	}
}
