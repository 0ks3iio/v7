package net.zdsoft.newgkelective.data.optaplanner.common;

import java.util.ArrayList;
import java.util.List;

public class CalculateSections {

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i ++) {
			List<Integer> r = calculateSectioning2(i, 41, 20);
			System.out.println(i + ":" + r.size() + ":" + r);
		}
	}
	
	//【注意】：sectionCount是0的时候，返回一个空的sizeList
	public static List<Integer> calculateSectionsByKnownCount(int total, int sectionCount) {
		List<Integer> sizeList = new ArrayList<>();
		
		if (sectionCount == 0) {
			return sizeList;
		}
		
		int currentSectionSize = total / sectionCount;
		
		//先算整数部分
		for (int i = 0; i < sectionCount; i ++) {
			sizeList.add(new Integer(currentSectionSize));
		}
		
		//把多余的几个分摊出去
		int remainderCount = total % sectionCount;
//		System.out.println("total = " + total);
//		System.out.println("sectionCount = " + sectionCount);
//		System.out.println("currentSectionSize = " + currentSectionSize);
//		System.out.println("remainderCount = " + remainderCount);
//		System.out.println("sizeList = " + sizeList);
		if (remainderCount > 0) {
			for (int j = 0; j < remainderCount; j ++) {
				Integer count = sizeList.get(j);
				count += 1;
				sizeList.set(j, count);
			}
		}

		return sizeList;

	}
	
	public static List<Integer> calculateSectioning2(int total, int max, int margin) {
		assert max > 0;
		assert max > margin;
		assert total >= 0;
		
		List<Integer> sectionSizeList = new ArrayList<Integer>();
		
		int sectionCount = total / (max + margin);
		if (sectionCount * (max + margin) < total) {
			sectionCount ++;
		}
			
		if (sectionCount == 0) return sectionSizeList;
		
		int sectionSize = total / sectionCount;
		int remainStudentCount = total;
		while(remainStudentCount >= sectionSize) {
			sectionSizeList.add(new Integer(sectionSize));
			remainStudentCount -= sectionSize;
		}
		
		assert remainStudentCount < sectionSizeList.size();
		
		//clean up the rest few
		if (remainStudentCount > 0) {
			 for (int i = 0; i < remainStudentCount; i ++)
				 sectionSizeList.set(i, sectionSizeList.get(i) + 1);
		}

		return sectionSizeList;
	}
	
	public static List<Integer> calculateSectioning(int total, int max, int margin) {
		assert max > 0;
		assert max > margin;
		assert total >= 0;
		
		List<Integer> sectionSizeList = new ArrayList<Integer>();
		
		int sectionCount = total / max;
		if (sectionCount * margin < total % max) {
			sectionCount ++;
		}

		if (sectionCount == 0) return sectionSizeList;
		
		int sectionSize = total / sectionCount;
		int remainStudentCount = total;
		while(remainStudentCount >= sectionSize) {
			sectionSizeList.add(new Integer(sectionSize));
			remainStudentCount -= sectionSize;
		}
		
		assert remainStudentCount < sectionSizeList.size();
		
		//clean up the rest few
		if (remainStudentCount > 0) {
			 for (int i = 0; i < remainStudentCount; i ++)
				 sectionSizeList.set(i, sectionSizeList.get(i) + 1);
		}

		return sectionSizeList;
	}


	//人数太少就不会开班，用于把人数很多的3A组合中移除部分学生进行开班
	public static List<Integer> calculateSectioning3A(int studentCount, int maxSectionSize, int margin) {
		
		List<Integer> sectionSizeList = new ArrayList<Integer>();
		if (studentCount < maxSectionSize - margin) {
			return sectionSizeList;
		}
		
		int sectionCount = studentCount / maxSectionSize;
		int sectionSize = maxSectionSize;
		if (sectionCount * margin >= studentCount % maxSectionSize) {
			sectionSize += (studentCount % maxSectionSize) / sectionCount;
		}
		else if (sectionCount * margin + studentCount % maxSectionSize >=  maxSectionSize - margin) {
			sectionCount ++;
			sectionSize = studentCount / sectionCount;
		}
		
		if (sectionCount == 0) return sectionSizeList;
		
		int remainStudentCount = studentCount;
		while(remainStudentCount >= sectionSize) {
			sectionSizeList.add(new Integer(sectionSize));
			remainStudentCount -= sectionSize;
		}
		
		//clean up the rest few
		if (remainStudentCount > 0 && remainStudentCount < sectionSizeList.size()) {
			 for (int i = 0; i < remainStudentCount; i ++)
				 sectionSizeList.set(i, new Integer(sectionSize+1));
		}

		return sectionSizeList;
	}
	
	
}

