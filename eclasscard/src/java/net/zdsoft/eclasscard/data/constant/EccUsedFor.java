package net.zdsoft.eclasscard.data.constant;

import java.util.HashMap;
import java.util.Map;


public enum EccUsedFor {
	ECC_BPYT_1(EccConstants.ECC_MCODE_BPYT_1, "行政班"), 
	ECC_BPYT_2(EccConstants.ECC_MCODE_BPYT_2, "非行政班"),
	ECC_BPYT_3(EccConstants.ECC_MCODE_BPYT_3, "寝室"),
	ECC_BPYT_4(EccConstants.ECC_MCODE_BPYT_4, "校门（进）"),
	ECC_BPYT_5(EccConstants.ECC_MCODE_BPYT_5, "校门（出）"),
	ECC_BPYT_6(EccConstants.ECC_MCODE_BPYT_6, "签到"), 
	ECC_BPYT_7(EccConstants.ECC_MCODE_BPYT_7, "大厅");

	
	private String thisId;
	private String content;

	private EccUsedFor(String thisId, String content) {
		this.thisId = thisId;
		this.content = content;
	}

	public static Map<String,String> getEccUsedForMap() {
		Map<String,String> map = new HashMap<>();
		for (EccUsedFor ent : EccUsedFor.values()) {
			map.put(ent.getThisId(), ent.getContent());
		}
		return map;
	}

	public String getThisId() {
		return thisId;
	}

	public void setThisId(String thisId) {
		this.thisId = thisId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
