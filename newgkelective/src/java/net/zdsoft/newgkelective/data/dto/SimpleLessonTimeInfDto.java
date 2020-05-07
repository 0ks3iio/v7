package net.zdsoft.newgkelective.data.dto;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleLessonTimeInfDto {
	private String array_item_id;
	private Date creation_time;
	private String item_name;
	private Map<String,Integer> freeLessonMap = new LinkedHashMap<String,Integer>();
	
	public String getArray_item_id() {
		return array_item_id;
	}
	public void setArray_item_id(String array_item_id) {
		this.array_item_id = array_item_id;
	}
	public Date getCreation_time() {
		return creation_time;
	}
	public void setCreation_time(Date creation_time) {
		this.creation_time = creation_time;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public Map<String, Integer> getFreeLessonMap() {
		return freeLessonMap;
	}
	public void setFreeLessonMap(Map<String, Integer> freeLessonMap) {
		this.freeLessonMap = freeLessonMap;
	}
	
}
