package net.zdsoft.api.base.dto;

import net.zdsoft.api.base.entity.eis.ApiCensusCount;

/**
 * 每天统计接口的调用次数
 * @author yangsj
 *
 */
public class ApiCensusDto {
	private ApiCensusCount censusCount;
	private String   key;
	private String   value;
    private String[] typeArray;
    private Integer[] findCountArray;
    private Integer[] findNumArray;
    private Integer[] saveCountArray;
    private Integer[] saveNumArray;
    private String developerName;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String[] getTypeArray() {
		return typeArray;
	}
	public void setTypeArray(String[] typeArray) {
		this.typeArray = typeArray;
	}
	public Integer[] getFindCountArray() {
		return findCountArray;
	}
	public void setFindCountArray(Integer[] findCountArray) {
		this.findCountArray = findCountArray;
	}
	public Integer[] getFindNumArray() {
		return findNumArray;
	}
	public void setFindNumArray(Integer[] findNumArray) {
		this.findNumArray = findNumArray;
	}
	public Integer[] getSaveCountArray() {
		return saveCountArray;
	}
	public void setSaveCountArray(Integer[] saveCountArray) {
		this.saveCountArray = saveCountArray;
	}
	public Integer[] getSaveNumArray() {
		return saveNumArray;
	}
	public void setSaveNumArray(Integer[] saveNumArray) {
		this.saveNumArray = saveNumArray;
	}
	public ApiCensusCount getCensusCount() {
		return censusCount;
	}
	public void setCensusCount(ApiCensusCount censusCount) {
		this.censusCount = censusCount;
	}
	public String getDeveloperName() {
		return developerName;
	}
	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}
}
