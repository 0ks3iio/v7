package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.io.Serializable;

public class CGConstants implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 班级最大人数
	 */
	private Integer sectionSizeMean; 

	private Integer sectionSizeMargin;
	/** 上午课时数*/
	private Integer amCount;
	/** 下午课时数*/
	private Integer pmCount;
	
	private int maxTimeslotIndex;
	
	private int workDayCount;

	public Integer getSectionSizeMean() {
		return sectionSizeMean;
	}

	public void setSectionSizeMean(Integer sectionSizeMean) {
		this.sectionSizeMean = sectionSizeMean;
	}

	public Integer getSectionSizeMargin() {
		return sectionSizeMargin;
	}

	public void setSectionSizeMargin(Integer sectionSizeMargin) {
		this.sectionSizeMargin = sectionSizeMargin;
	}

	public Integer getAmCount() {
		return amCount;
	}

	public void setAmCount(Integer amCount) {
		this.amCount = amCount;
	}

	public Integer getPmCount() {
		return pmCount;
	}

	public void setPmCount(Integer pmCount) {
		this.pmCount = pmCount;
	}

	public int getMaxTimeslotIndex() {
		return maxTimeslotIndex;
	}

	public void setMaxTimeslotIndex(int maxTimeslotIndex) {
		this.maxTimeslotIndex = maxTimeslotIndex;
	}

	public int getWorkDayCount() {
		return workDayCount;
	}

	public void setWorkDayCount(int workDayCount) {
		this.workDayCount = workDayCount;
	}
}
