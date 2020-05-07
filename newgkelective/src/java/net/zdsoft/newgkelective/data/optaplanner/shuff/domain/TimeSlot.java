package net.zdsoft.newgkelective.data.optaplanner.shuff.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

@PlanningEntity
public class TimeSlot extends AbstractPersistable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String timeSlotName;//T1,T2,T3

	List<GroupSubject> groupSubject = new ArrayList<>();

	@InverseRelationShadowVariable(sourceVariableName = "timeSlot")
	public List<GroupSubject> getGroupSubject() {
		return groupSubject;
	}

	public void setGroupSubject(List<GroupSubject> groupSubject) {
		this.groupSubject = groupSubject;
	}
	public String getTimeSlotName() {
		return this.timeSlotName;
	}

	public void setTimeSlotName(String timeSlotName) {
		this.timeSlotName = timeSlotName;
	}
	
	
	public int getCurrentPartialSectionCount() {
		Map<Subject,Integer> countBySubject=new HashMap<>();
		Map<Subject,List<Integer>> stuNumsBySubject=new HashMap<>();
		for(GroupSubject g:groupSubject) {
			Integer sub = countBySubject.get(g.getSubject());
			if(sub==null) {
				countBySubject.put(g.getSubject(), g.getStuNum());
				stuNumsBySubject.put(g.getSubject(), new ArrayList<>());
			}else {
				countBySubject.put(g.getSubject(),countBySubject.get(g.getSubject())+g.getStuNum());
			}
			stuNumsBySubject.get(g.getSubject()).add(g.getStuNum());
		}
		int score=0;
		//计算平均数
		int localSectionCount;
		for (Entry<Subject, Integer> item:countBySubject.entrySet()) {
			Subject sub = item.getKey();
			int sectionSizeMean = sub.getSectionSizeMean();
			int sectionSizeMargin = sub.getSectionSizeMargin();
			int studentCount = item.getValue();
			for (localSectionCount = 0; studentCount > sectionSizeMean
					+ sectionSizeMargin; ++localSectionCount) {
				studentCount -= sectionSizeMean;
			}

			if (studentCount < sectionSizeMean - sectionSizeMargin && studentCount > 0) {
				double d = Math.sqrt((double) studentCount * 1.0D / (double) (localSectionCount + 1)) * 100.0D;
				score += (int) d;
			}
		}
		if(true) {
		for (Entry<Subject, List<Integer>> item:stuNumsBySubject.entrySet()) {
			Subject sub = item.getKey();
			int studentCount = countBySubject.get(sub);
			int sectionSizeMean = sub.getSectionSizeMean();
			int sectionSizeMargin = sub.getSectionSizeMargin();
			//最大班级数量
			int classNum = (studentCount-1)/(sectionSizeMean+sectionSizeMargin)+1;
			if(classNum>1) {
				//不处理
				continue;
			}
			List<Integer> values = item.getValue();
			Collections.sort(values);
			//尽量组合不拆分分班classNum班级数量 不超过最大值sectionSizeMean+sectionSizeMargin
			int[] result = openJxb(classNum, studentCount, item.getValue(), sectionSizeMean+sectionSizeMargin);
			for(int ss:result) {
				if(Math.abs(ss-sectionSizeMean)>sectionSizeMargin) {
					double d = Math.sqrt((double) (Math.abs(ss-sectionSizeMean)-sectionSizeMargin) * 1.0D) * 100.0D;
					score += (int) d;
				}
			}
		}
		}
		return score;
	}
	
	//同班级在不超过最大人数时 不拆
	private  int[] openJxb(int openNum, int allSize,List<Integer> itemList,int maxStu) {
		int[] array = new int[openNum];
		sort(itemList);
		int avg=(allSize-1)/openNum+1;
		int j=0;
		//1、如果出现刚好<=maxStu--直接开班
		//2、直接出现超过maxStu 根据数量算出本来的平均班级人数 通过平均班级人数进行avg满员开班--可能出现剩余人数 重新排序itemList
		while(true) {
			if(j>=openNum) {
				break;
			}
			array[j] = 0;
			Integer cs = itemList.get(0);
			if(cs<=maxStu) {
				//小于最大值 默认直接开班
				array[j]=cs;
				itemList.remove(0);
				j++;
			}else {
				//大于最大值 --用平均值开班
				int kk=cs/avg;//可以开班数量
				if(maxStu*kk>=cs) {
					int avg1=(cs-1)/kk+1;
					//完全开班
					for(int k=0;k<kk;k++) {
						if(j>=openNum) {
							//一般不会出现
							break;
						}
						if(cs==0) {
							break;
						}
						int ss=0;
						if(cs>avg1) {
							ss=avg1;
						}else {
							ss=cs;
						}
						array[j]=array[j]+ss;
						j++;
						cs=cs-ss;
					}
				}else {
					for(int k=0;k<kk;k++) {
						if(j>=openNum) {
							//一般不会出现
							break;
						}
						int ss=avg;
						array[j]=array[j]+ss;
						j++;
						cs=cs-ss;
						itemList.set(0, cs);
					}
					sort(itemList);
				}
				if(cs==0) {
					itemList.remove(0);
				}
			}
		}
		//第二步 从最大值人数开始操作，且完全分完人员
		while(true) {
			if(CollectionUtils.isEmpty(itemList)) {
				break;
			}
			Integer tt = itemList.get(0);
			//取得最多班级的数据放到最小的里面
			Arrays.sort(array);
			int minIndex=0;
			//1、如果人数完全可以放在最小班级里面，只要人数少于maxStu
			if(array[minIndex]+tt<=maxStu) {
				//所有
				array[minIndex]=array[minIndex]+tt;
				tt=0;
				itemList.remove(0);
			}else {
				//2、如果不可以，从最小班级人数开始，放足人数在最小班级 使其到达avg
				if(array[minIndex]>avg) {
					//不可能出现 跳出循环
					break;
				}else {
					while(true) {
						if(tt==0) {
							break;
						}
						int gg=avg-array[minIndex];
						if(gg>=tt) {
							//完全分完
							array[minIndex]=array[minIndex]+tt;
							itemList.remove(0);
							tt=0;
							break;
						}else {
							array[minIndex]=array[minIndex]+gg;
							tt=tt-gg;
							itemList.set(0, tt);
							//找到最小
							Arrays.sort(array);
							minIndex=0;
						}
					}
				}
			}
		}
		return array;
		
	}
	//从大到小
	private  void sort(List<Integer> itemList) {
		if(CollectionUtils.isEmpty(itemList)) {
			return;
		}
		Collections.sort(itemList);
		Collections.reverse(itemList); 
	}
}
