package net.zdsoft.newgkelective.data.optaplanner2.domain;

import java.util.Map;

public class ArrangeConstantInfo {

	// batch , subjectidtype 教室数
	private Map<Integer, Map<String, Integer>> batchSubjectIdTypeRoomCountMap;
	private Map<String, ArrangeCapacityRange> subjectIdTypeCapacityRangeMap;
	
	/**
	 * 获取batchSubjectIdTypeRoomCountMap
	 * @return batchSubjectIdTypeRoomCountMap
	 */
	public Map<Integer,Map<String,Integer>> getBatchSubjectIdTypeRoomCountMap() {
	    return batchSubjectIdTypeRoomCountMap;
	}

	/**
	 * 设置batchSubjectIdTypeRoomCountMap
	 * @param batchSubjectIdTypeRoomCountMap batchSubjectIdTypeRoomCountMap
	 */
	public void setBatchSubjectIdTypeRoomCountMap(Map<Integer,Map<String,Integer>> batchSubjectIdTypeRoomCountMap) {
	    this.batchSubjectIdTypeRoomCountMap = batchSubjectIdTypeRoomCountMap;
	}

    /**
     * 获取subjectIdTypeCapacityRangeMap
     * @return subjectIdTypeCapacityRangeMap
     */
    public Map<String,ArrangeCapacityRange> getSubjectIdTypeCapacityRangeMap() {
        return subjectIdTypeCapacityRangeMap;
    }

    /**
     * 设置subjectIdTypeCapacityRangeMap
     * @param subjectIdTypeCapacityRangeMap subjectIdTypeCapacityRangeMap
     */
    public void setSubjectIdTypeCapacityRangeMap(Map<String,ArrangeCapacityRange> subjectIdTypeCapacityRangeMap) {
        this.subjectIdTypeCapacityRangeMap = subjectIdTypeCapacityRangeMap;
    }
	
	
}
