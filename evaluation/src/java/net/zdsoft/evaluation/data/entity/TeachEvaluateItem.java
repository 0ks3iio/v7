package net.zdsoft.evaluation.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="teach_evaluate_item")
public class TeachEvaluateItem extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	/**
	 * 评价类型  微代码（教学调查10、班主任调查11、导师调查12、选修课调查13）
	 */
	private String evaluateType;
	/**
	 * 指标名称  
	 */
	private String itemName;
	/**
	 * 指标类型     微代码（“评教项目10”、“满意率调查11”、“文本输入12”）
	 */
	private String itemType;
	/**
	 * 指标序号
	 */
	private int itemNo;
	/**
	 * 单选或者多选
	 */
	private String showType;
	/**
	 * 备注
	 */
	private String remark;
	
	private String projectId;

	@Transient
	private List<TeachEvaluateItemOption> optionList = new ArrayList<TeachEvaluateItemOption>();
	@Transient
	private String result;
	
	public static void copyItem(TeachEvaluateItem newItem,TeachEvaluateItem oldItem){
		if(newItem == null){
			newItem = new TeachEvaluateItem();
		}
		newItem.setEvaluateType(oldItem.getEvaluateType());
		newItem.setUnitId(oldItem.getUnitId());
		newItem.setItemName(oldItem.getItemName());
		newItem.setItemType(oldItem.getItemType());
		newItem.setItemNo(oldItem.getItemNo());
		newItem.setShowType(oldItem.getShowType());
		newItem.setRemark(oldItem.getRemark());
	}
	
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<TeachEvaluateItemOption> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<TeachEvaluateItemOption> optionList) {
		this.optionList = optionList;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getEvaluateType() {
		return evaluateType;
	}

	public void setEvaluateType(String evaluateType) {
		this.evaluateType = evaluateType;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public int getItemNo() {
		return itemNo;
	}

	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String fetchCacheEntitName() {
		return "teachEvaluationItem";
	}

}
