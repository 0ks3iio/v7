package net.zdsoft.newgkelective.data.dto;

import java.util.HashMap;
import java.util.Map;

public class NewGkChoCategoryDto {
	
	private String categoryName; //类别名称	
	private Integer maxNum;//最多选
	private Integer minNum;//最少选
	//key:subjectIds value:subjectIds
	Map<String,String[]> choMap = new HashMap<String,String[]>();
	Map<String,String> choNameMap = new HashMap<String,String>();
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}
	public Integer getMinNum() {
		return minNum;
	}
	public void setMinNum(Integer minNum) {
		this.minNum = minNum;
	}
	public Map<String, String[]> getChoMap() {
		return choMap;
	}
	public void setChoMap(Map<String, String[]> choMap) {
		this.choMap = choMap;
	}
	public Map<String, String> getChoNameMap() {
		return choNameMap;
	}
	public void setChoNameMap(Map<String, String> choNameMap) {
		this.choNameMap = choNameMap;
	}
	
}
