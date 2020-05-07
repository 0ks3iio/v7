package net.zdsoft.qulity.data.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StuworkDataCountDto {
	public static final String STUWORK_LIST = "stuwork_list";
	public static final String FESTIVAL_LIST = "festival_list";
	public static final String GAME_LIST = "game_list";
	
	private Float[] countNumbers = new Float[3];
	
	private String[] acadyears = new String[4];
	
	private Map<String,List<String[]>> infoMap = new HashMap<String,List<String[]>>();

	public Float[] getCountNumbers() {
		return countNumbers;
	}

	public void setCountNumbers(Float[] countNumbers) {
		this.countNumbers = countNumbers;
	}

	public String[] getAcadyears() {
		return acadyears;
	}

	public void setAcadyears(String[] acadyears) {
		this.acadyears = acadyears;
	}

	public Map<String, List<String[]>> getInfoMap() {
		return infoMap;
	}

	public void setInfoMap(Map<String, List<String[]>> infoMap) {
		this.infoMap = infoMap;
	}
	
}
