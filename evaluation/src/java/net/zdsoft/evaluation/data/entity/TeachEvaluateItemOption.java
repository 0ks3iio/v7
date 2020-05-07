package net.zdsoft.evaluation.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="teach_evaluate_item_option")
public class TeachEvaluateItemOption extends BaseEntity<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String unitId;
	/**
	 * 指标id
	 */
	private String itemId;
	/**
	 * 选项名称
	 */
	private String optionName;
	/**
	 * 选项序号
	 */
	private int optionNo;
	/**
	 * 折分
	 */
	private float score;
	
	@Transient
	private boolean haveSelected;
	
	public static void copyOption(TeachEvaluateItemOption newOption,TeachEvaluateItemOption oldOption){
		if(newOption == null){
			newOption = new TeachEvaluateItemOption();
		}
		newOption.setUnitId(oldOption.getUnitId());
//		newOption.setItemId(oldOption.getItemId());
		newOption.setOptionName(oldOption.getOptionName());
		newOption.setOptionNo(oldOption.getOptionNo());
		newOption.setScore(oldOption.getScore());
	}

	public boolean isHaveSelected() {
		return haveSelected;
	}


	public void setHaveSelected(boolean haveSelected) {
		this.haveSelected = haveSelected;
	}


	public String getUnitId() {
		return unitId;
	}


	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}


	public String getItemId() {
		return itemId;
	}


	public void setItemId(String itemId) {
		this.itemId = itemId;
	}


	public String getOptionName() {
		return optionName;
	}


	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}


	public int getOptionNo() {
		return optionNo;
	}


	public void setOptionNo(int optionNo) {
		this.optionNo = optionNo;
	}


	public float getScore() {
		return score;
	}


	public void setScore(float score) {
		this.score = score;
	}


	@Override
	public String fetchCacheEntitName() {
		return "teachEvalutionItemOption";
	}
	
}
