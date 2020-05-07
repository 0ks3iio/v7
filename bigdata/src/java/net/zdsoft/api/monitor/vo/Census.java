package net.zdsoft.api.monitor.vo;

public class Census{
	private String resultType;
	private String interfaceId;
	private int tSaveCount;
	private int tFindCount;
	private int iSaveCount;
	private int iFindCount;
	
	private int tSaveNum;
	private int tFindNum;
	private int iSaveNum;
	private int iFindNum;
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public String getInterfaceId() {
		return interfaceId;
	}
	public int gettSaveCount() {
		return tSaveCount;
	}
	public void settSaveCount(int tSaveCount) {
		this.tSaveCount = tSaveCount;
	}
	public int gettFindCount() {
		return tFindCount;
	}
	public void settFindCount(int tFindCount) {
		this.tFindCount = tFindCount;
	}
	public int getiSaveCount() {
		return iSaveCount;
	}
	public void setiSaveCount(int iSaveCount) {
		this.iSaveCount = iSaveCount;
	}
	public int getiFindCount() {
		return iFindCount;
	}
	public void setiFindCount(int iFindCount) {
		this.iFindCount = iFindCount;
	}
	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}
	public int gettSaveNum() {
		return tSaveNum;
	}
	public void settSaveNum(int tSaveNum) {
		this.tSaveNum = tSaveNum;
	}
	public int gettFindNum() {
		return tFindNum;
	}
	public void settFindNum(int tFindNum) {
		this.tFindNum = tFindNum;
	}
	public int getiSaveNum() {
		return iSaveNum;
	}
	public void setiSaveNum(int iSaveNum) {
		this.iSaveNum = iSaveNum;
	}
	public int getiFindNum() {
		return iFindNum;
	}
	public void setiFindNum(int iFindNum) {
		this.iFindNum = iFindNum;
	}
}
