package net.zdsoft.evaluation.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;

public class EvaluateTableDto {
	
	private List<TeachEvaluateItem> evaluaList = new ArrayList<TeachEvaluateItem>();
	private List<TeachEvaluateItem> fillList = new ArrayList<TeachEvaluateItem>();
	private List<TeachEvaluateItem> answerList = new ArrayList<TeachEvaluateItem>();
	
	public List<TeachEvaluateItem> getEvaluaList() {
		return evaluaList;
	}
	public void setEvaluaList(List<TeachEvaluateItem> evaluaList) {
		this.evaluaList = evaluaList;
	}
	public List<TeachEvaluateItem> getFillList() {
		return fillList;
	}
	public void setFillList(List<TeachEvaluateItem> fillList) {
		this.fillList = fillList;
	}
	public List<TeachEvaluateItem> getAnswerList() {
		return answerList;
	}
	public void setAnswerList(List<TeachEvaluateItem> answerList) {
		this.answerList = answerList;
	}
	
	
}
