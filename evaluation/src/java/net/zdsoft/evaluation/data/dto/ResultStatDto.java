package net.zdsoft.evaluation.data.dto;

import java.util.HashMap;
import java.util.Map;

public class ResultStatDto {
	
	private String teaName;
	private String teaId;
	private String subName;
	private String subId;
	private String clsName;
	private String clsId;
	
	private String stuId;
	private String stuName;
	private String stuCode;
	
	private float countScore;
	private Map<String,Float> itemScoreMap = new HashMap<String, Float>();
	private Map<String,String> itemTxtMap = new HashMap<String, String>();
	
	private int rank;
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}


	public Map<String, String> getItemTxtMap() {
		return itemTxtMap;
	}

	public void setItemTxtMap(Map<String, String> itemTxtMap) {
		this.itemTxtMap = itemTxtMap;
	}

	public String getStuCode() {
		return stuCode;
	}

	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}

	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public float getCountScore() {
		return countScore;
	}

	public void setCountScore(float countScore) {
		this.countScore = countScore;
	}

	public String getTeaName() {
		return teaName;
	}

	public void setTeaName(String teaName) {
		this.teaName = teaName;
	}

	public String getTeaId() {
		return teaId;
	}

	public void setTeaId(String teaId) {
		this.teaId = teaId;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getClsName() {
		return clsName;
	}

	public void setClsName(String clsName) {
		this.clsName = clsName;
	}

	public String getClsId() {
		return clsId;
	}

	public void setClsId(String clsId) {
		this.clsId = clsId;
	}

	public Map<String, Float> getItemScoreMap() {
		return itemScoreMap;
	}

	public void setItemScoreMap(Map<String, Float> itemScoreMap) {
		this.itemScoreMap = itemScoreMap;
	}
	
	
	
}
