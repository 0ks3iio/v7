package net.zdsoft.stuwork.data.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DyStuworkDataCountDto {
	
	public static final String STUWORK_LIST = "stuwork_list";
	public static final String FESTIVAL_LIST = "festival_list";
	public static final String GAME_LIST = "game_list";
	public static final String CXDD_MAX_NUMBER="CXDD.MAX.NUMBER";//操行等第
	public static final String ZZBX_MAX_NUMBER="ZZBX.MAX.NUMBER";//值周表现
	public static final String JX_MAX_NUMBER="JX.MAX.NUMBER";//军训
	public static final String XN_MAX_NUMBER="XN.MAX.NUMBER";//学农

	public static final String XSGB_MAX_NUMBER="XSGB.MAX.NUMBER";//学生干部
	public static final String STGG_MAX_NUMBER="STGG.MAX.NUMBER";//社团骨干
	public static final String PYXJ_MAX_NUMBER="PYXJ.MAX.NUMBER";//评优先进
	public static final String TCGX_MAX_NUMBER="TCGX.MAX.NUMBER";//突出贡献

	public static final String XKJS_MAX_NUMBER="XKJS.MAX.NUMBER";//学科竞赛

	public static final String QXXHD_MAX_NUMBER="QXXHD.MAX.NUMBER";//全校性活动
	public static final String FESTIVAL_MAX_NUMBER="5FESTIVAL.MAX.NUMBER";//5大节日
	

	private Float[] countNumbers = new Float[3];
	
	private String[] acadyears = new String[4];
	
	private Map<String,List<String[]>> infoMap = new HashMap<String,List<String[]>>();

//	public float getCountNumber() {
//		return countNumber;
//	}
//
//	public void setCountNumber(float countNumber) {
//		this.countNumber = countNumber;
//	}

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

	public Float[] getCountNumbers() {
		if(countNumbers == null){
			countNumbers = new Float[3];
		}
		return countNumbers;
	}

	public void setCountNumbers(Float[] countNumbers) {
		this.countNumbers = countNumbers;
	}


	
	
}
