package net.zdsoft.gkelective.data.action.optaplanner.func;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.framework.entity.Constant;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeCapacityRange;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeConstantInfo;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeStudent;

public class SelectionScheduleFunction {

	private static final int NO_CLASSROOM = -1;
	

	/**
	 * 教室容量有限,学生不能太多挤在一个批次
	 * @param constantInfo
	 * @param students
	 * @return
	 */
    public static int calcStudentTooManyInOneRoomFunc(ArrangeConstantInfo constantInfo, List<ArrangeStudent> students) {
    	Map<Integer, Map<String, Integer>> batchSubjectIdTypeRoomCountMap = constantInfo.getBatchSubjectIdTypeRoomCountMap();
    	Map<String, Integer> subjectIdTypeRoomCountMap = null;
    	int roomCount = 0;
    	int roomHoldStudentCount = 0;
    	// key:batchIndex  value:map(key:subjectIdType, value:studentCount)
    	Map<Integer, Map<String, Integer>> batchSubjectIdTypeStudentCountMap = new HashMap<Integer, Map<String, Integer>>();
    	Map<String, Integer> subjectIdTypeStudentCountMap = null;
    	String subjectIdType = null;
		List<String> allSubjectIdTypeList = null;
		Integer[] subjectBatchIndexs = null;
		Integer batchIndex = null;
		int studentCount = 0;
		// 批次科目人数获取
		for (ArrangeStudent student : students) {
			allSubjectIdTypeList = student.getAllSubjectIdTypeList();
			subjectBatchIndexs = student.getSubjectBatch().getSubjectIdIndexs();
			for (int i = 0; i < subjectBatchIndexs.length; i++) {
				subjectIdType = allSubjectIdTypeList.get(i);
				if (subjectIdType == null) {
                    continue;
                }
				batchIndex = subjectBatchIndexs[i];
				
				if (batchSubjectIdTypeStudentCountMap.containsKey(batchIndex)) {
					subjectIdTypeStudentCountMap = batchSubjectIdTypeStudentCountMap.get(batchIndex);
				} else {
					subjectIdTypeStudentCountMap = new HashMap<String, Integer>();
					batchSubjectIdTypeStudentCountMap.put(batchIndex, subjectIdTypeStudentCountMap);
				}
				
				if (subjectIdTypeStudentCountMap.containsKey(subjectIdType)) {
					studentCount = subjectIdTypeStudentCountMap.get(subjectIdType);
				} else {
					studentCount = 0;
				}
				subjectIdTypeStudentCountMap.put(subjectIdType, studentCount+1);
			}
		}
		ArrangeCapacityRange capacityRange = null;
		int score = 0;
		Map<String,ArrangeCapacityRange> subjectIdTypeCapacityRangeMap = constantInfo.getSubjectIdTypeCapacityRangeMap();
		for (Map.Entry<Integer, Map<String, Integer>> batchSubjectIdTypeStudentCountEntry : batchSubjectIdTypeStudentCountMap.entrySet()) {
			batchIndex = batchSubjectIdTypeStudentCountEntry.getKey();
			subjectIdTypeStudentCountMap = batchSubjectIdTypeStudentCountEntry.getValue();
			if (batchSubjectIdTypeRoomCountMap.containsKey(batchIndex)) {
				subjectIdTypeRoomCountMap = batchSubjectIdTypeRoomCountMap.get(batchIndex);
				for (Map.Entry<String, Integer> subjectIdTypeStudentCountEntry: subjectIdTypeStudentCountMap.entrySet()) {
					subjectIdType = subjectIdTypeStudentCountEntry.getKey();
					if (subjectIdType == null) {
                        continue;
                    }
					// 该批次该科目的学生人数
					studentCount = subjectIdTypeStudentCountEntry.getValue();
					if (subjectIdTypeRoomCountMap.containsKey(subjectIdType)) {
						roomCount = subjectIdTypeRoomCountMap.get(subjectIdType);
						capacityRange = subjectIdTypeCapacityRangeMap.get(subjectIdType);
						// 最大容纳人数
						roomHoldStudentCount = roomCount * capacityRange.getMaxCapacity();
						if (roomHoldStudentCount < studentCount) {
						    //超出人数
							score += NO_CLASSROOM * (studentCount - roomHoldStudentCount);
						}
					} else {
					    // 该批次不存在该科目
						score += NO_CLASSROOM * studentCount;
					}
				}
			} else {
			    // 不存在该批次
				for (Map.Entry<String, Integer> subjectIdTypeStudentCountEntry : subjectIdTypeStudentCountMap.entrySet()) {
					score += (NO_CLASSROOM * subjectIdTypeStudentCountEntry.getValue());
				}
			}
		}
//    	System.out.println("教室容量有限========：" + score);
    	return score;
    }
    
    public static int calAveScore(ArrangeConstantInfo constantInfo, List<ArrangeStudent> students) {
    	// key:batchIndex  value:map(key:subjectIdType, value:studentCount)
    	Map<Integer, Map<String, List<ArrangeStudent>>> batchSubjectIdTypeStudentCountMap = new HashMap<Integer, Map<String, List<ArrangeStudent>>>();
    	// key:batchIndex  value:map(key:subjectIdType)
    	Map<Integer, Map<String, Double>> batchSubjectIdTypeAvgMap = new HashMap<Integer, Map<String, Double>>();
    	Map<String, List<ArrangeStudent>> subjectIdTypeStudentCountMap = null;
    	// key:subjectIdType
    	Map<String, Double> subjectIdTypeAvgMap = null;
    	String subjectIdType = null;
		List<String> allSubjectIdTypeList = null;
		Integer[] subjectBatchIndexs = null;
		Integer batchIndex = null;
		List<ArrangeStudent> studentCount = null;
		Double avg = null;
		Double stuScore = null;
		// key:subjectIdType
		Map<String,List<Double>> aveDouMap = null;
		List<Double> aveList = null;
		// 批次科目
		for (ArrangeStudent student : students) {
			allSubjectIdTypeList = student.getAllSubjectIdTypeList();
			subjectBatchIndexs = student.getSubjectBatch().getSubjectIdIndexs();
			for (int i = 0; i < subjectBatchIndexs.length; i++) {
				subjectIdType = allSubjectIdTypeList.get(i);
				if (subjectIdType == null) {
                    continue;
                }
				batchIndex = subjectBatchIndexs[i];
				
				if (batchSubjectIdTypeStudentCountMap.containsKey(batchIndex)) {
					subjectIdTypeStudentCountMap = batchSubjectIdTypeStudentCountMap.get(batchIndex);
				} else {
					subjectIdTypeStudentCountMap = new HashMap<String, List<ArrangeStudent>>();
					batchSubjectIdTypeStudentCountMap.put(batchIndex, subjectIdTypeStudentCountMap);
				}
				
				if (batchSubjectIdTypeAvgMap.containsKey(batchIndex)) {
					subjectIdTypeAvgMap = batchSubjectIdTypeAvgMap.get(batchIndex);
				} else {
					subjectIdTypeAvgMap = new HashMap<String, Double>();
					batchSubjectIdTypeAvgMap.put(batchIndex, subjectIdTypeAvgMap);
				}
				
				if (subjectIdTypeStudentCountMap.containsKey(subjectIdType)) {
					studentCount = subjectIdTypeStudentCountMap.get(subjectIdType);
				} else {
					studentCount = new ArrayList<ArrangeStudent>();
					subjectIdTypeStudentCountMap.put(subjectIdType, studentCount);
				}
				studentCount.add(student);
				
				if(subjectIdTypeAvgMap.containsKey(subjectIdType)){
					avg = subjectIdTypeAvgMap.get(subjectIdType);
				}else{
					avg = 0.0;
				}
//				stuScore = student.getStudentSubjectDto().getScoreMap().get(subjectIdType.substring(0, subjectIdType.length()-1));
				
				stuScore = student.getStudentSubjectDto().getScoreMap().get(Constant.GUID_ONE);
				
				subjectIdTypeAvgMap.put(subjectIdType, avg + (stuScore == null?0.0:stuScore));
			}
		}
		aveDouMap = new HashMap<String,List<Double>>();
		for(Map.Entry<Integer, Map<String, Double>> entry : batchSubjectIdTypeAvgMap.entrySet()){
			subjectIdTypeAvgMap = entry.getValue();
			subjectIdTypeStudentCountMap = batchSubjectIdTypeStudentCountMap.get(entry.getKey());
			for (Map.Entry<String, List<ArrangeStudent>> subTypeEntry : subjectIdTypeStudentCountMap.entrySet()) {
				avg = subjectIdTypeAvgMap.get(subTypeEntry.getKey());
				if(aveDouMap.containsKey(subTypeEntry.getKey())){
					aveList = aveDouMap.get(subTypeEntry.getKey());
				}else{
					aveList = new ArrayList<Double>();
					aveDouMap.put(subTypeEntry.getKey(), aveList);
				}
				aveList.add(avg/subTypeEntry.getValue().size());
			}
		}
		int score = 0;
		for(Map.Entry<String, List<Double>> entry : aveDouMap.entrySet()){
			aveList = entry.getValue();
			for (int i=0 ; i < aveList.size();i++) {
				for (int j = i+1 ; j < aveList.size() ; j++) {
					score += (NO_CLASSROOM * Math.abs((int)((aveList.get(i)-aveList.get(j)))));
				}
			}
		}
//		System.out.println("班级平均分："+score);
    	return score;
    }
    
}
