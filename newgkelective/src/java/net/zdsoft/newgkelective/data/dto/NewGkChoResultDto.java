package net.zdsoft.newgkelective.data.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.newgkelective.data.entity.NewGkChoResult;

public class NewGkChoResultDto {
	private List<NewGkChoResult> resultList;
	/* 选的3门科目 */
    private Set<String> chooseSubjectIds;
    
    private String studentId;
    private String choiceId;
    private int schNum;
    private int allStu;
    private int chooseStu;
    private int notChooseStu;
    private int maleNum;
    private int femaleNum;
    private JSONObject jsonStringData1;
    private String jsonStringDataStr1;
    private String eduJsonStringDataStr1;
    private JSONObject jsonStringData3;
    private String jsonStringDataStr3;
    private String eduJsonStringDataStr3;
    private Set<String> subNames;
    private Set<String> subName3;
    private Map<String, String> subId;
    private Map<String, String> subId3;
    private Map<String, Integer> subNums;
    private Map<String, Integer> subNums3;
    
	public List<NewGkChoResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<NewGkChoResult> resultList) {
		this.resultList = resultList;
	}

	public Set<String> getChooseSubjectIds() {
		return chooseSubjectIds;
	}

	public void setChooseSubjectIds(Set<String> chooseSubjectIds) {
		this.chooseSubjectIds = chooseSubjectIds;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public int getAllStu() {
		return allStu;
	}

	public void setAllStu(int allStu) {
		this.allStu = allStu;
	}

	public int getChooseStu() {
		return chooseStu;
	}

	public void setChooseStu(int chooseStu) {
		this.chooseStu = chooseStu;
	}

	public int getNotChooseStu() {
		return notChooseStu;
	}

	public void setNotChooseStu(int notChooseStu) {
		this.notChooseStu = notChooseStu;
	}

	public int getMaleNum() {
		return maleNum;
	}

	public void setMaleNum(int maleNum) {
		this.maleNum = maleNum;
	}

	public int getFemaleNum() {
		return femaleNum;
	}

	public void setFemaleNum(int femaleNum) {
		this.femaleNum = femaleNum;
	}

	public JSONObject getJsonStringData1() {
		return jsonStringData1;
	}

	public void setJsonStringData1(JSONObject jsonStringData1) {
		this.jsonStringData1 = jsonStringData1;
	}

	public JSONObject getJsonStringData3() {
		return jsonStringData3;
	}

	public void setJsonStringData3(JSONObject jsonStringData3) {
		this.jsonStringData3 = jsonStringData3;
	}

	public String getJsonStringDataStr1() {
		return jsonStringDataStr1;
	}

	public void setJsonStringDataStr1(String jsonStringDataStr1) {
		this.jsonStringDataStr1 = jsonStringDataStr1;
	}

	public String getJsonStringDataStr3() {
		return jsonStringDataStr3;
	}

	public void setJsonStringDataStr3(String jsonStringDataStr3) {
		this.jsonStringDataStr3 = jsonStringDataStr3;
	}

	public String getEduJsonStringDataStr1() {
		return eduJsonStringDataStr1;
	}

	public void setEduJsonStringDataStr1(String eduJsonStringDataStr1) {
		this.eduJsonStringDataStr1 = eduJsonStringDataStr1;
	}

	public String getEduJsonStringDataStr3() {
		return eduJsonStringDataStr3;
	}

	public void setEduJsonStringDataStr3(String eduJsonStringDataStr3) {
		this.eduJsonStringDataStr3 = eduJsonStringDataStr3;
	}

	public int getSchNum() {
		return schNum;
	}

	public void setSchNum(int schNum) {
		this.schNum = schNum;
	}

	public Set<String> getSubNames() {
		return subNames;
	}

	public void setSubNames(Set<String> subNames) {
		this.subNames = subNames;
	}

	public Set<String> getSubName3() {
		return subName3;
	}

	public void setSubName3(Set<String> subName3) {
		this.subName3 = subName3;
	}

	public Map<String, Integer> getSubNums() {
		return subNums;
	}

	public void setSubNums(Map<String, Integer> subNums) {
		this.subNums = subNums;
	}

	public Map<String, Integer> getSubNums3() {
		return subNums3;
	}

	public void setSubNums3(Map<String, Integer> subNums3) {
		this.subNums3 = subNums3;
	}

	public Map<String, String> getSubId() {
		return subId;
	}

	public void setSubId(Map<String, String> subId) {
		this.subId = subId;
	}

	public Map<String, String> getSubId3() {
		return subId3;
	}

	public void setSubId3(Map<String, String> subId3) {
		this.subId3 = subId3;
	}

	public String getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
	}

}
