package net.zdsoft.newgkelective.data.optaplanner.domain.batch;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import net.zdsoft.newgkelective.data.optaplanner.util.AbstractPersistable;

@XStreamAlias("BatchEntity")
public class BatchEntity extends AbstractPersistable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// A/B 选/学考
	private String subjectType; 
	/**
	 * 7选3 ：1/2/3...;虚拟课程 xuniCid1/xuniCid2/...
	 */
	private String batch;
	// 此批次点的课有几节
	private int lectureCount;
	/**
	 * 此批次点可以安排的 时间
	 */
	private List<BatchPeriod> periodList;
	
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	
	public List<BatchPeriod> getPeriodList() {
		return periodList;
	}
	public void setPeriodList(List<BatchPeriod> periodList) {
		this.periodList = periodList;
	}
	public int getLectureCount() {
		return lectureCount;
	}
	public void setLectureCount(int lectureCount) {
		this.lectureCount = lectureCount;
	} 
	public String getBatchStr() {
		return subjectType+"-"+batch;
	}
}
