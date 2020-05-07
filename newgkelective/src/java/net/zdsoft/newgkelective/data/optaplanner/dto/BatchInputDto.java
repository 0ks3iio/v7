package net.zdsoft.newgkelective.data.optaplanner.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BatchInputDto")
public class BatchInputDto  implements Serializable {

	private static final long serialVersionUID = 1L;

	private String arrayItemId;
	
	/**
	 * key:A-1/A-2   v:day-periodInterval-period
	 */
	private Map<String,List<String>> batchDomainPeriodMap;
	private Map<String,Integer> batchWorkTimeMap;
	
	private List<String> allPeriods;

	public Map<String, List<String>> getBatchDomainPeriodMap() {
		return batchDomainPeriodMap;
	}

	public void setBatchDomainPeriodMap(Map<String, List<String>> batchDomainPeriodMap) {
		this.batchDomainPeriodMap = batchDomainPeriodMap;
	}

	public List<String> getAllPeriods() {
		return allPeriods;
	}

	public void setAllPeriods(List<String> allPeriods) {
		this.allPeriods = allPeriods;
	}

	public String getArrayItemId() {
		return arrayItemId;
	}

	public void setArrayItemId(String arrayItemId) {
		this.arrayItemId = arrayItemId;
	}

	public Map<String, Integer> getBatchWorkTimeMap() {
		return batchWorkTimeMap;
	}

	public void setBatchWorkTimeMap(Map<String, Integer> batchWorkTimeMap) {
		this.batchWorkTimeMap = batchWorkTimeMap;
	}
}
