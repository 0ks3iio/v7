package net.zdsoft.studevelop.data.dto;

public class StuSubjectAchiDto {
	
	private String subname;
	private String subid;
	private String qmachi;
	private String psachi;
	private String xxtd;
	private String coomunication;
	private String discovery;
	private String schPlace;
	/**
	 * 平时总分
	 */
	private String psFullMark;
	/**
	 * 期末总分
	 */
	private String qmFullMark;
	/**
	 * 平时成绩/总分
	 */
	private float psPercent;
	/**
	 * 期末成绩/总分
	 */
	private float qmPercent;

	
	public float getPsPercent() {
		return psPercent;
	}
	public void setPsPercent(float psPercent) {
		this.psPercent = psPercent;
	}
	public float getQmPercent() {
		return qmPercent;
	}
	public void setQmPercent(float qmPercent) {
		this.qmPercent = qmPercent;
	}
	public String getSubname() {
		return subname;
	}
	public void setSubname(String subname) {
		this.subname = subname;
	}
	public String getSubid() {
		return subid;
	}
	public void setSubid(String subid) {
		this.subid = subid;
	}
	public String getQmachi() {
		return qmachi;
	}
	public void setQmachi(String qmachi) {
		this.qmachi = qmachi;
	}
	public String getPsachi() {
		return psachi;
	}
	public void setPsachi(String psachi) {
		this.psachi = psachi;
	}
	public String getXxtd() {
		return xxtd;
	}
	public void setXxtd(String xxtd) {
		this.xxtd = xxtd;
	}
	public String getCoomunication() {
		return coomunication;
	}
	public void setCoomunication(String coomunication) {
		this.coomunication = coomunication;
	}
	public String getDiscovery() {
		return discovery;
	}
	public void setDiscovery(String discovery) {
		this.discovery = discovery;
	}
	public String getSchPlace() {
		return schPlace;
	}
	public void setSchPlace(String schPlace) {
		this.schPlace = schPlace;
	}

	public String getPsFullMark() {
		return psFullMark;
	}

	public void setPsFullMark(String psFullMark) {
		this.psFullMark = psFullMark;
	}

	public String getQmFullMark() {
		return qmFullMark;
	}

	public void setQmFullMark(String qmFullMark) {
		this.qmFullMark = qmFullMark;
	}
}
