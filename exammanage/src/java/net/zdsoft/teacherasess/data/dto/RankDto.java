package net.zdsoft.teacherasess.data.dto;

public class RankDto {
	
	private String name;
	private String asessRankId;
	private String lineType;//1单上线，2双上线
	private String slice;//分层
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAsessRankId() {
		return asessRankId;
	}
	public void setAsessRankId(String asessRankId) {
		this.asessRankId = asessRankId;
	}
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	public String getSlice() {
		return slice;
	}
	public void setSlice(String slice) {
		this.slice = slice;
	}
	
}
